package begine.load;

import java.io.File;
import java.io.IOException;
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
			FileUtil.instanct().saveValueToFile(file, content1, true);
		}
		return true;
	}

	public static void main(String[] args) {
		String utl = "https://www.ptwxz.com/html/9/9932/";
		LoadBookByContentPageURL test = new LoadBookByContentPageURL(utl);
		List<String> pages = test.page.getPagesLinks();
		for (String string : pages) {
			log.info(string);
		}
		try {
			test.loadBookToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
