package begine.search.mouse;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentPage {

	private String title;

	private String Content;

	private static Logger logger = LoggerFactory.getLogger(ContentPage.class);

	public ContentPage(Document doc) {
		// filter content
		List<Element> nodes = doc.select("div[id=\"content\"],div[id=\"contents\"]");
		if (nodes.isEmpty()) {
			logger.error("doc:{} can not found content!", doc.baseUri());
		}

		String content = Util.filter(nodes.get(0).html());
		this.setContent(content);
	}

	public ContentPage(String title) {
		this.setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
