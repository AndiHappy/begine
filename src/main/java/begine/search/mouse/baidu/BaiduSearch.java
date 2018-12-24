package begine.search.mouse.baidu;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import begine.search.mouse.ALLConetentPage;
import begine.search.mouse.DPage;
import begine.search.mouse.DirectoryPage;
import begine.search.mouse.FileUtil;
import begine.search.mouse.LoadThreadPoolUtil;
import begine.util.BResult;

/**
 * @author zhailz
 *
 * @version 2018年8月23日 下午10:17:40
 */
public class BaiduSearch {

	public static final Logger log = LoggerFactory.getLogger(BaiduSearch.class);

	private static String afterSearchWorld = " 目录";

	private String keyWorld;

	private static final String searchURL = "https://www.baidu.com/s?wd=";

	private static final int searchPage = 5;
	
	public BaiduSearch(String keyworld) {
		setKeyWorld(keyworld);
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static BResult search(String value,String cache) throws IOException {
		String pin = PinyinHelper.convertToPinyinString(value, ",", PinyinFormat.WITHOUT_TONE);
		String fileName = pin.replaceAll(",", "")+".txt";
		BaiduSearch search = new BaiduSearch(value + afterSearchWorld);

		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}
		String surl = search.getSearchURL();
//		String surl = search.getALLSearchURL()[1];
		BaiduPage page = new BaiduPage(surl);
		List<String> directoryLinks = page.getDirectoryLink();
//		// 通过目录页，迅速的找到每一个章节对应的目录
		DirectoryPage content = new DirectoryPage(directoryLinks,true);
//		
//		DirectoryPage content = new DirectoryPage(page.getContents());

		
		ALLConetentPage cs = content.caculateEveryChapterPage();
		List<DPage> pages = cs.getPs();
		if (!pages.isEmpty()) {
			for (DPage p : pages) {
				if (LoadThreadPoolUtil.waitLoadDoc(p, 10)) {
					String title = p.getTitle() + "\n";
					log.info("保存:{},url:{}", title, p.getDoc().baseUri());
					FileUtil.instanct().saveValueToFile(file, title, true);
					String content1 = p.getTextContent() + "\n";
					FileUtil.instanct().saveValueToFile(file, content1, true);
				}
			}
		}
		
		return new BResult(0,"success",fileName);
	}

	public String[] getALLSearchURL() {
		String[] urls = new String[searchPage];
		for (int i = 0; i < searchPage; i++) {
			urls[i] =  searchURL + keyWorld + "&pn="+10*i;
		}
		return urls;
	}
	
	public String getSearchURL() {
		return searchURL + keyWorld;
	}

	public String getKeyWorld() {
		return keyWorld;
	}

	public void setKeyWorld(String keyWorld) {
		this.keyWorld = keyWorld;
	}
	
	public static void main(String[] args) throws IOException{
		String value = "诡秘之主";
		BaiduSearch.search(value,"cache");
	}
}
