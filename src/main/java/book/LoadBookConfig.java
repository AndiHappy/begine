package book;

import begine.util.FileUtil;
import begine.util.LoadConditionPoolUtil;
import begine.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

/**
 * 另外的一种方式
 * */
public class LoadBookConfig {

    private Logger log = LoggerFactory.getLogger(getClass());

    private String begineLink;

    private HTMLPage beginePage;

    private TreeMap<String,HTMLPage> chatptersLinsHTMLPagesCache = new TreeMap<>();


    public LoadBookConfig(String begineLink) {
        setBegineLink(begineLink);
    }

    //加载页面的章节的链接
    public void loadContentChapters() {
        HTMLPage headPage = new HTMLPage(getBegineLink());
        headPage.loadURL();
        if (LoadConditionPoolUtil.waitLoadDoc(headPage, 20000)) {
            Document doc = headPage.getDoc();
            String pageants = Util.getInstance().findNextLink(doc);

            if(pageants == null){
                throw new IllegalAccessError("load " + getBegineLink() + " timeout");
            }

            headPage.setNextLink(pageants);
            setBeginePage(headPage);

            chatptersLinsHTMLPagesCache.put(begineLink,beginePage);

        } else {
            throw new IllegalAccessError("load " + getBegineLink() + " timeout");
        }
    }

    public void loadAndSaveToFile(File file) {
        if(beginePage != null){
            HTMLPage current = beginePage;
            while(current != null){
                if (LoadConditionPoolUtil.waitLoadDoc(current, 20000)) {
                    String titlestring = Util.getInstance().getHTMLTitle(current) + "\n";
                    log.info(" \n 保存:{}", titlestring);
                    FileUtil.instanct().saveValueToFile(file, titlestring, true);
                    String content = Util.getInstance().getHTMLContent(current) + "\n";
                    if (StringUtils.isNotEmpty(content)) {
                        FileUtil.instanct().saveValueToFile(file, content, true);
                    } else {
                        log.error(" \n 错误没有内容。 :{}, {} \n\n", current.getUrl(), titlestring);
                    }
                    current.loadNextHTML();
                    current = current.getNextHTML();
                }
            }
        }
    }

//    //加载每一个链接
//    public void iniEveryCharpterLinks() {
//        for (int i = 0; i < getChatptersLins().size() ; i++) {
//            String chapterLink = getChatptersLins().get(i);
//            HTMLPage page = new HTMLPage(chapterLink);
//            page.loadURL();
//            chatptersLinsHTMLPagesCache.put(chapterLink,page);
//        }
//
//        for (int i = 0; i < getChatptersLins().size() ; i++) {
//            String chapterLink = getChatptersLins().get(i);
//            HTMLPage page = chatptersLinsHTMLPagesCache.get(chapterLink);
//            if (LoadConditionPoolUtil.waitLoadDoc(page, 20000)) {
//                Document doc = page.getDoc();
//                List<String> pagelinks = Util.getInstance().getChapterLinks(doc);
//                if(pagelinks == null || pagelinks.isEmpty() || pagelinks.size() < 3){
//                    throw new IllegalAccessError("load " + getBegineLink() + " timeout");
//                }
//                log.info("calculate chapter num: {}", pagelinks.size());
//                setChatptersLins(pagelinks);
//            } else {
//                throw new IllegalAccessError("load " + getBegineLink() + " timeout");
//            }
//        }
//
//
//    }


    public String getBegineLink() {
        return begineLink;
    }

    public void setBegineLink(String begineLink) {
        this.begineLink = begineLink;
    }


    public HTMLPage getBeginePage() {
        return beginePage;
    }

    public void setBeginePage(HTMLPage beginePage) {
        this.beginePage = beginePage;
    }


}
