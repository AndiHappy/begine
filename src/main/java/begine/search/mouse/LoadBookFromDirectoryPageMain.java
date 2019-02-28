package begine.search.mouse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * @author zhailz
 *
 * @version 2018年8月23日 下午9:01:01
 */
public class LoadBookFromDirectoryPageMain  {

	private static final Logger log = LoggerFactory.getLogger(LoadBookFromDirectoryPageMain.class);

	private List<DPage> pages = null;
	
	private PatternUtil pt = PatternUtil.instanct();
	
	private ExecutorService pool =  Executors.newFixedThreadPool(5);
	
	public LoadBookFromDirectoryPageMain(List<String> directoryLinks,boolean load) {
		if (directoryLinks != null && !directoryLinks.isEmpty()) {
			List<DPage> pages = new ArrayList<>();
			for (String dPagelinks : directoryLinks) {
				pages.add(new DPage(dPagelinks));
			}
			setPages(pages);
		}
	}

	public LoadBookFromDirectoryPageMain(List<DPage> contents) {
		List<DPage> pages = new ArrayList<>();
		if(contents != null && contents.isEmpty()){
			for (DPage dPage : contents) {
				if (LoadThreadPoolUtil.waitLoadDoc(dPage, 3)) {
					if (Util.getInstance().judgeIsRealDirectoryPage(dPage)) {
						pages.add(dPage);
					}
				}
			}
		}
		setPages(pages);
	}

	public LoadBookFromDirectoryPageMain(String url) {
		List<DPage> pages = new ArrayList<>();
		DPage pageContent = new DPage(url);
		pages.add(pageContent);
		setPages(pages);
	}

	public ALLConetentPage caculateEveryChapterPage() {
		if (pages != null && pages.size() > 0) {
			DPage[] value = sort(pages);
			for (DPage dPage : value) {
				if (LoadThreadPoolUtil.waitLoadDoc(dPage, 3)) {
					if (Util.getInstance().judgeIsRealDirectoryPage(dPage)) {
						String host = dPage.getHost();
						String chooseLinkPattern = null;
						String chooseContentPattern = null;
						String chooseTitlePattern = null;
						HostSetting setting = null;
						if (!pt.hasHost(host)) {
							log.error("待处理域名:{}", host);
						} else {
							setting = pt.getHostSetting(host);
							chooseLinkPattern = setting.getChooseLinkPattern();
							chooseContentPattern = setting.getChooseContentPattern();
							chooseTitlePattern = setting.getChooseTitlePattern();
							log.info("host:{},chooseLinkPattern:{},chooseContentPattern:{}", host, chooseLinkPattern, chooseContentPattern);
							return findAndIniContent(dPage, chooseLinkPattern, chooseContentPattern,chooseTitlePattern);

						}
					}
				}
			}

		}
		return null;
	}

	private DPage[] sort(List<DPage> pages2) {
		DPage[] a = new DPage[pages2.size()];
		pages2.toArray(a);
		Comparator<DPage> c = new Comparator<DPage>() {
			@Override
			public int compare(DPage o1, DPage o2) {
				return o1.getPriority() > o2.getPriority()? 1:-1;
			}
		};
		Arrays.sort(a, c);
		//启动额外的线程，保证结果回填，根据得到的章节数，章节目录值之类的
		UpdateHostSetting setting = new UpdateHostSetting(pages2);
		pool.execute(setting);
		return a;
	}

	/**
	 * @param dPage
	 * @param chooseLinkPattern
	 * @param chooseContentPattern
	 * @param chooseTitlePattern 
	 * @return
	 */
	private ALLConetentPage findAndIniContent(DPage dPage, String chooseLinkPattern, String chooseContentPattern, String chooseTitlePattern) {
		Document doc = dPage.getDoc();
		Elements titles = doc.select(chooseTitlePattern);
		dPage.setTitle(titles.text());
		List<String> everyPageLinks = findEveryPageTdClassL(doc, chooseLinkPattern);
		ALLConetentPage content = new ALLConetentPage(everyPageLinks);
		content.iniContent(chooseContentPattern);
		return content;
	}

	private List<String> findEveryPageTdClassL(Document doc, String pattern) {
		List<String> pages = new ArrayList<String>();
		Elements elements = doc.select(pattern);
		if (!elements.isEmpty()) {
			Element[] elementsort = new Element[elements.size()] ;
			for (int i = 0; i < elements.size(); i++) {
				Element element = elements.get(i);
				Elements j = element.select("a");
				if (!j.isEmpty() && j.size() == 1) {
					Element a = j.first();
					elementsort[i] = a;
				}
			}
			
			boolean compare = false;
			try {
				Arrays.sort(elementsort, new ChapterElementCompare());
				compare= true;
			} catch (Exception e) {
				log.info("对比章节目录出现错误:{}",e);
			}
			if(compare){
				ArrayList<Element> chect = judgeChapterIsSequence(elementsort);
				log.error("顺序检查:{}", chect);
			}
			
			for (Element element : elementsort) {
				if(element != null){
					log.info("{}",element.text());
					pages.add(element.absUrl("href"));
				}
			}
		}
		return pages;
	}

	
	private ArrayList<Element> judgeChapterIsSequence(Element[] elementsort) {
		ArrayList<Element> value = new ArrayList<>();
		for (int i = 1; i < elementsort.length; i++) {
			if(elementsort[i-1] != null && elementsort[i] != null){
				int ind1 = Util.getElementNumber(elementsort[i-1]);
				int ind2 =Util.getElementNumber(elementsort[i]);
				if(ind1 + 1 != ind2){
					String urlindex1 = elementsort[i-1].absUrl("href");
					int urlindexb = urlindex1.lastIndexOf("/");
					int urlindexe = urlindex1.lastIndexOf(".");
					urlindex1 = urlindex1.substring(urlindexb + 1, urlindexe);
					
					String urlindex2 = elementsort[i].absUrl("href");
					urlindexb = urlindex2.lastIndexOf("/");
					urlindexe = urlindex2.lastIndexOf(".");
					urlindex2 = urlindex2.substring(urlindexb + 1, urlindexe);
					
					int int11 = Integer.parseInt(urlindex1);
					int int21 = Integer.parseInt(urlindex2);
					if(int11 + 1 != int21){
						value.add(elementsort[i-1]);
						value.add(elementsort[i]);
					}
				}
			}
		}
		return value;
		
	}

	public List<DPage> getPages() {
		return pages;
	}

	public void setPages(List<DPage> pages) {
		this.pages = pages;
	}

	public static void main(String[] args) throws IOException{
		LoadBookFromDirectoryPageMain content = new LoadBookFromDirectoryPageMain("https://www.sqsxs.com/book/114/114131/");
		ALLConetentPage cs = content.caculateEveryChapterPage();
		String title = content.getPages().get(0).getTitle();
		if(StringUtils.isAllBlank(title)){
			title = new Random().nextLong()+"";
		}
		String pin = PinyinHelper.convertToPinyinString(title, ",", PinyinFormat.WITHOUT_TONE);
		String fileName = pin.replaceAll(",", "")+".txt";
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}
		List<DPage> pages = cs.getPs();
		if (!pages.isEmpty()) {
			for (DPage p : pages) {
				if (LoadThreadPoolUtil.waitLoadDoc(p, 10)) {
					 title = p.getTitle() + "\n";
					log.info("保存:{},url:{}", title, p.getDoc().baseUri());
					FileUtil.instanct().saveValueToFile(file, title, true);
					String content1 = p.getTextContent() + "\n";
					FileUtil.instanct().saveValueToFile(file, content1, true);
				}
			}
		}
	}

}
