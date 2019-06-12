package begine.search.monitorhostweb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import begine.search.normalload.BasePage;

/**
 * @author guizhai
 *
 */
public class GoogleSearchPage extends BasePage {

	public static final String  baseurl = "https://www.google.com.hk/search?q=keyword&btnG=Search&safe=active&gbv=1";
	public GoogleSearchPage(String keyword) throws UnsupportedEncodingException {
		super( baseurl.replace("keyword", URLEncoder.encode(keyword,"UTF-8")));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

}
