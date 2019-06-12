package begine.search.monitorhostweb;

import org.apache.commons.codec.digest.Md5Crypt;
import org.jsoup.Jsoup;

import begine.util.HCUtil;

public class MonitorWebPage {
	
	
	
	public static void main(String[] args) {
		MonitorWebPage monitor = new MonitorWebPage();
		monitor.monitor("https://www.biquge.com.cn/book/31822/");
	}

	private void monitor(String monitorurl) {
		for (int i = 0; i < 100; i++) {
			String value = HCUtil.getInstance().getResponse(monitorurl);
			System.out.println(value.hashCode() + " "+ Md5Crypt.md5Crypt(value.getBytes()));
			Jsoup.parse(value);
		}
		
	}

}
