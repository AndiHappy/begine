package begine.search.normalload;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import begine.util.LoadConditionPoolUtil;
import begine.util.Util;

/**
 * @author guizhai
 *
 */
public class ContentPage extends BasePage {

	private static Logger log = LoggerFactory.getLogger(ContentPage.class);

	private List<Page> pages = null;

	private String bookTitle = null;

	private List<String> pagesLinks = null;

	public ContentPage(String url) {
		super(url);
		iniBookTitle();
		calculateEveryPage();
		iniPagesAndLoad();

	}

	public void iniBookTitle() {
		if (LoadConditionPoolUtil.waitLoadDoc(this, 500)) {
			Document doc = this.getDoc();
			Elements titles = doc.select("h1");
			if (titles != null && !titles.isEmpty()) {
				setBookTitle(titles.text());
			} else {
				if(titles.isEmpty()) {
					setBookTitle((long)Math.random()+"-00001");
				}
//				throw new IllegalAccessError("NONE " + getUrl() + " BOOKTITLE !");
			}
		} else {
			throw new IllegalAccessError("load " + getUrl() + " timeout");
		}
	}

	public void iniPagesAndLoad() {
//		if (getPages() == null && getPagesLinks() != null) {
			List<Page> pages = new ArrayList<Page>();
			for (String pageLink : getPagesLinks()) {
				Page page = new Page(pageLink);
				pages.add(page);
			}
			setPages(pages);
			loadAndCauPages(pages);
//				});
//			}

//		}
	}

	private void loadAndCauPages(List<Page> pages) throws IllegalAccessError {
		for (Page page : pages) {
//				LoadConditionPoolUtil.submit(new Runnable() {
//					@Override
//					public void run() {
					if (LoadConditionPoolUtil.waitLoadDoc(page, 20) && !page.isHasinipage()) {
						Document doc = page.getDoc();
						String content = Util.getInstance().getContentFromDoc(doc,this.getHost());
						if(content != null &&  content.length() > 200) {
							page.setContentText(content);
						}else {
							Node contentDiv = Util.getInstance().getContentDivHtmlElement(doc,this.getHost());
							if (contentDiv != null) {
								String divFilterString = Util.getInstance().filter(contentDiv.toString());
								page.setContentText(divFilterString);
							} else {
								throw new IllegalAccessError("NONE content div " + getUrl() + " !");
							}
						}

						String titleFromSetting =  Util.getInstance().getTitleFromSetting(doc,this.host);
						if(titleFromSetting != null && !titleFromSetting.isEmpty()) {
							page.setTitle(titleFromSetting);
						}else {
							Element titleDiv = Util.getInstance().getTitleDiv(doc);
							if (titleDiv != null) {
								String titleString = Util.getInstance().filterTitle(titleDiv.text());
								page.setTitle(titleString);
							} else {
								throw new IllegalAccessError("NONE title Div " + getUrl() + " !");
							}
						}

						page.setHasinipage(true);

					} else {
						throw new IllegalAccessError("load " + getUrl() + " timeout");
					}
				}
	}

	public List<String> calculateEveryPage() {

		if (getPagesLinks() == null) {
			if (LoadConditionPoolUtil.waitLoadDoc(this, 20)) {
				Document doc = this.getDoc();
				List<String> pagelinks = Util.getInstance().getChapterLinks(doc);
//				log.info("calculate chapter num: {}", pagelinks.size());
				setPagesLinks(pagelinks);
			} else {
				throw new IllegalAccessError("load " + getUrl() + " timeout");
			}
		}

		return getPagesLinks();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public List<String> getPagesLinks() {
		return pagesLinks;
	}

	public void setPagesLinks(List<String> pagesLinks) {
		this.pagesLinks = pagesLinks;
	}

}
