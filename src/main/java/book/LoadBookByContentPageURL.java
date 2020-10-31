package book;

import begine.load.ContentPage;
import begine.load.Page;
import begine.util.ConfigUtil;
import begine.util.FileUtil;
import begine.util.Util;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;


/**
 * load book from content page URL
 */

public class LoadBookByContentPageURL {

    private static Logger log = LoggerFactory.getLogger(LoadBookByContentPageURL.class);

    private ContentPage page = null;

    //文件名称
    private String fileName = null;

    private String contenturl = null;

    public LoadBookByContentPageURL(String contenturl) {
        this.contenturl = contenturl;
        load();
    }

    public void load() {
        ContentPage page = new ContentPage(contenturl);
        setPage(page);
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
        String fileName = ConfigUtil.fileStorePath + pin.replaceAll(",", "") + ".txt";
        Util.ensureSpace();
        File file = new File(fileName);
        setFileName(fileName);
        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        for (Page p : page.getPages()) {

        }
        return true;
    }

    public static void main(String[] args) throws IOException {
//		if (args != null && args.length > 0){
        String begineLink = "https://www.yanqbook.com/4/4817/1437883.html";//args[0];//"http://www.6lk.net/book/40868.html";
        LoadBookConfig book = new LoadBookConfig(begineLink);
        book.loadContentChapters();
        String fileName = "1.txt";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        book.loadAndSaveToFile(file);

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
