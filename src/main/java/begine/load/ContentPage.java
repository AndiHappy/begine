package begine.load;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import begine.util.LoadConditionPoolUtil;
import begine.util.Util;

/**
 * @author guizhai
 *
 */
public class ContentPage extends BasePage {

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
		if (LoadConditionPoolUtil.waitLoadDoc(this, 20)) {
			Document doc = this.getDoc();
			Elements titles = doc.select("h1");
			if (titles != null && !titles.isEmpty()) {
				setBookTitle(titles.text());
			} else {
				throw new IllegalAccessError("NONE " + getUrl() + " BOOKTITLE !");
			}
		} else {
			throw new IllegalAccessError("load " + getUrl() + " timeout");
		}
	}

	public void iniPagesAndLoad() {
		if (getPages() == null && getPagesLinks() != null) {
			List<Page> pages = new ArrayList<Page>();
			for (String pageLink : getPagesLinks()) {
				Page page = new Page(pageLink);
				pages.add(page);
			}
			setPages(pages);

			for (Page page : pages) {
				LoadConditionPoolUtil.submit(new Runnable() {
					@Override
					public void run() {
						if (LoadConditionPoolUtil.waitLoadDoc(page, 20)) {
							Document doc = page.getDoc();
							Node contentDiv = Util.getInstance().getContentDivHtmlElement(doc);
							if (contentDiv != null) {
								String divFilterString = Util.getInstance().filter(contentDiv.toString());
								page.setContentText(divFilterString);
							} else {
								throw new IllegalAccessError("NONE content div " + getUrl() + " !");
							}

							Element titleDiv = Util.getInstance().getTitleDiv(doc);
							if (titleDiv != null) {
								String titleString = Util.getInstance().filterTitle(titleDiv.text());
								page.setTitle(titleString);
							} else {
								throw new IllegalAccessError("NONE title Div " + getUrl() + " !");
							}

							page.setHasinipage(true);

						} else {
							throw new IllegalAccessError("load " + getUrl() + " timeout");
						}
					}
				});
			}

		}
	}

	public List<String> calculateEveryPage() {

		if (getPagesLinks() == null) {
			if (LoadConditionPoolUtil.waitLoadDoc(this, 20)) {
				Document doc = this.getDoc();
				setPagesLinks(Util.getInstance().getChapterLinks(doc));
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
