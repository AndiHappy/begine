package begine.search.mouse.google;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import begine.search.mouse.LoadThreadPoolUtil;
import begine.search.mouse.Page;

public class GoogleSearch {

	private static String searchURL = "https://www.google.com/search?q=";

	private static int page = 10;

	public static Map<String, Boolean> searchDirestory(String name, String cache) {
		Map<String, Boolean> result = null;
		searchURL = searchURL + name;
		for (int i = 0; i < 10; i++) {
			searchURL = searchURL + "&start=" + i * page;
			Page page = new Page(searchURL);
			LoadThreadPoolUtil.waitLoadDoc(page, 100);
			Document document = page.getDoc();
			if (document != null) {
				org.jsoup.select.Elements elemets = document.select("div[class=\"r\"]");
				if (elemets.size() > 0) {
					if (result == null)
						result = new HashMap<>();
					for (Element element : elemets) {
						String link = element.select("a[ping]").get(0).absUrl("href");
						result.put(link, false);
					}
				}

			}

		}

		return result;
	}

	public static void main(String[] args) {
		GoogleSearch.searchDirestory("", "");
	}

}
