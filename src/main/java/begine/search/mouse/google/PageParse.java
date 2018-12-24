package begine.search.mouse.google;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhailz
 *
 */
public class PageParse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static Map<String, Object> analysisWebDirectoryPage(List<GooglePage> pages) {
		 Map<String, Object> findResult = new HashMap<>();
		 for (int i = 0; i < pages.size(); i++) {
			 GooglePage page = pages.get(i);
//			 page.getResult()
		}
		 return findResult;
		
	}

}
