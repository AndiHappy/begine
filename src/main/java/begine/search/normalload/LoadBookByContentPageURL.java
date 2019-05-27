package begine.search.normalload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import begine.util.ConfigUtil;
import begine.util.FileUtil;
import begine.util.Util;


/**
 * load book from content page URL
 * 
 * */

public class LoadBookByContentPageURL {

	private static Logger log = LoggerFactory.getLogger(LoadBookByContentPageURL.class);
	
	private ContentPage page = null;
	
	//文件名称
	private String fileName = null;

	public LoadBookByContentPageURL(String contenturl) {
		setPage(new ContentPage(contenturl));
	}

	public boolean loadBookToFile() throws IOException {
		if (page == null || page.getPages() == null || page.getPages().isEmpty()) {
			throw new IllegalAccessError("NONE PAGES FOUND");
		}

		String title = this.getPage().getBookTitle();

		if (StringUtils.isAllBlank(title)) {
			title = Math.abs(new Random().nextLong()) + "";
		}
		String pin = PinyinHelper.convertToPinyinString(title, ",", PinyinFormat.WITHOUT_TONE);
		String fileName = ConfigUtil.fileStorePath+pin.replaceAll(",", "") + ".txt";
		Util.ensureSpace();
		File file = new File(fileName);
		setFileName(fileName);
		if (file.exists()) {
			file.delete();
		}

		file.createNewFile();
		
		for (Page p : page.getPages()) {
			String titlestring = p.getTitle() + "\n";
			log.info(" \n 保存:{}, {} ",p.getUrl(), titlestring);
			FileUtil.instanct().saveValueToFile(file, titlestring, true);
			String content1 = p.getContentText() + " ";
			if(StringUtils.isNotEmpty(content1)) {
				FileUtil.instanct().saveValueToFile(file, content1, true);
			}else {
				log.error(" \n 错误没有内容。 :{}, {} \n\n",p.getUrl(), titlestring);

			}
		}
		return true;
	}

	public static void main(String[] args) {
		String utl = "http://www.shuquge.com/txt/89290/index.html";
		LoadBookByContentPageURL test = new LoadBookByContentPageURL(utl);
//		List<String> pages = test.page.getPagesLinks();
//		for (String string : pages) {
//			log.info(string);
//		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			try {
				String value = reader.readLine();
				int page = Integer.parseInt(value);
				ContentPage pages = test.getPage();
				List<Page> pagecontents = pages.getPages();
				if(page < pagecontents.size()) {
					Page pagecontent = pagecontents.get(page);
					if(pagecontent.ini) {
						String content = pagecontent.getContentText();
//						int contentlength = content.length();
						System.out.println(content);
//						int i = 0;
//						while(true) {
//							value = reader.readLine();
//							if(StringUtils.isNoneBlank(value)) {
//								if((i+1)* 200 < contentlength) {
//									System.out.println(content.substring(i*200 , (i+1)*200));
//									i++;
//								}else {
//									System.out.println(content.substring(i*200 , contentlength));
//									break;
//								}
//						}
//						}
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
//		try {
//			test.loadBookToFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		System.exit(0);
	}

	public ContentPage getPage() {
		return page;
	}

	public void setPage(ContentPage page) {
		this.page = page;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
