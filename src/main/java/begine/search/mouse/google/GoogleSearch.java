package begine.search.mouse.google;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import begine.controller.GooglePageBrief;
import begine.search.mouse.LoadThreadPoolUtil;
import begine.search.mouse.Page;
import begine.search.mouse.PatternUtil;

public class GoogleSearch {

	private static String searchURL = "https://www.google.com/search?q=";

	private static int page = 10;
	
	private static PatternUtil util =  PatternUtil.instanct();

	public static List<GooglePageBrief> searchDirectory(String name, String cache) {
		searchURL = searchURL + name;
		List<GooglePageBrief> result = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			searchURL = searchURL + "&start=" + i * page;
			Page page = new Page(searchURL);
			LoadThreadPoolUtil.waitLoadDoc(page, 60);
			Document document = page.getDoc();
			if (document != null) {
				org.jsoup.select.Elements elemets = document.select("div[class=\"r\"]");
				if (elemets.size() > 0) {
					try {
						for (Element element : elemets) {
							if(result.size() > 2) break;
							String link = element.select("a[ping]").get(0).absUrl("href");
							URL url = new URL(link);
							boolean value = util.hasHost(url.getHost());
							if(value){
								result.add(new GooglePageBrief(link));
							}
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}

			}

		}
		return result;
	}

	public static void main(String[] args) {
		GoogleSearch.searchDirectory("诡秘之主", "");
	}

}
