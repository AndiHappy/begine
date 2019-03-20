package begine.load;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import begine.util.LoadCondition;
import begine.util.LoadConditionPoolUtil;

/**
 * @author guizhai
 *
 */
public abstract class BasePage implements LoadCondition {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	public static String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) " + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
	//每一个页面对应的URL
	private volatile String url;
	//每一个网页对应的Doc，这个内存占用可能需要优化！！
	private volatile Document doc;
	
	private volatile String host;
	
	private volatile boolean ini;
	
	public BasePage(String url) {
		ini(url);
	}

	protected void ini(String url) {
		setUrl(url);
		//默认加载页面
		loadURL();
	}
	
	private void loadURL() {
		LoadConditionPoolUtil.submit(new Runnable() {
		      @Override
		      public void run() {
		        if (getDoc() == null && !isIni()) {
		          try {
		            Connection connection = Jsoup.connect(url).timeout(10000).validateTLSCertificates(false);
		            connection.timeout(LoadConditionPoolUtil.waitTimeMils);
		            connection.userAgent(userAgent);
		            connection.followRedirects(true);
		            Document docValue = connection.get();
		            setDoc(docValue);
		            String baseurl = docValue.baseUri();
		            String host = new URL(baseurl).getHost();
		            setHost(host);
		            setIni(true);
//		            log.info("load: {} ",url);
		          } catch (IOException e) {
		            log.error("{},初始化出现错误：{}", url, e);
		          }
		        }
		      }
		    });

		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isIni() {
		return ini;
	}

	public void setIni(boolean ini) {
		this.ini = ini;
	}
	
	@Override
	public boolean meetCondition() {
		return ini;
	}


}
