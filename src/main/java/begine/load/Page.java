package begine.load;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import begine.util.LoadCondition;
import begine.util.LoadConditionPoolUtil;
import begine.util.Util;

/**
 * @author guizhai
 *
 */
public class Page extends BasePage {

	private String contentText = null;

	private String title = null;

	private volatile boolean hasinipage;

	public Page(String url) {
		super(url);
	}

	public void iniPageContent() {
		if (LoadConditionPoolUtil.waitLoadDoc(this, 20)) {
			Document doc = this.getDoc();
			Node contentDiv = Util.getInstance().getContentDivHtmlElement(doc);
			if (contentDiv != null) {
				String divFilterString = Util.getInstance().filter(contentDiv.toString());
				setContentText(divFilterString);
				
			} else {
				throw new IllegalAccessError("NONE content div " + getUrl() + " BOOKTITLE !");
			}
		} else {
			throw new IllegalAccessError("load " + getUrl() + " timeout");
		}
		
		setHasinipage(true);
	}

	public void iniPageTitle() {
		if (LoadConditionPoolUtil.waitLoadDoc(this, 20)) {
			Document doc = this.getDoc();
			Elements title = doc.select("title");
			String titleText = title.text();
			setTitle(titleText);
		}

	}

	private class PageHasLoad implements LoadCondition {
		Page page;

		public PageHasLoad(Page page) {
			this.page = page;
		}

		@Override
		public boolean meetCondition() {
			return this.page.contentText != null;
		}

	}

	public String getContentText() {
		LoadConditionPoolUtil.waitLoadDoc(new PageHasLoad(this), 20);
		return contentText;

	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public boolean isHasinipage() {
		return hasinipage;
	}

	public void setHasinipage(boolean hasinipage) {
		this.hasinipage = hasinipage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public static void main(String[] args) {
		// select no div content
		/*
		Page pa = new Page("https://www.ptwxz.com/html/6/6035/3314738.html");
		pa.iniPageTitle();
		pa.iniPageContent();
		System.out.println(pa.getTitle());
		System.out.println(pa.getContentText());
		*/
		
		Page pa = new Page("https://www.x23us.com/html/72/72784/32647450.html");
		pa.iniPageTitle();
		pa.iniPageContent();
		System.out.println(pa.getTitle());
		System.out.println(pa.getContentText());
	}

}
