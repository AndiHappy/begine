package begine.load;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import begine.search.mouse.FileUtil;

/**
 * load book from content page URL
 * 
 * */

public class LoadBookByContentPageURL {

	private static Logger log = LoggerFactory.getLogger(LoadBookByContentPageURL.class);
	
	private ContentPage page = null;

	public LoadBookByContentPageURL(String contenturl) {
		setPage(new ContentPage(contenturl));
	}

	public void loadBookToFile() throws IOException {
		if (page == null || page.getPages() == null || page.getPages().isEmpty()) {
			throw new IllegalAccessError("NONE PAGES FOUND");
		}

		String title = this.getPage().getBookTitle();

		if (StringUtils.isAllBlank(title)) {
			title = Math.abs(new Random().nextLong()) + "";
		}
		String pin = PinyinHelper.convertToPinyinString(title, ",", PinyinFormat.WITHOUT_TONE);
		String fileName = pin.replaceAll(",", "") + ".txt";
		File file = new File(fileName);
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
	}

	public static void main(String[] args) {
		String utl = "http://www.pfwx.com/wozhenbushishenxian/";
		LoadBookByContentPageURL test = new LoadBookByContentPageURL(utl);
		List<String> pages = test.page.getPagesLinks();
		for (String string : pages) {
			log.info(string);
		}
		try {
			test.loadBookToFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ContentPage getPage() {
		return page;
	}

	public void setPage(ContentPage page) {
		this.page = page;
	}

}
