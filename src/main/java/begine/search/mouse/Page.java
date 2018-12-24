package begine.search.mouse;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author zhailz
 *
 * @version 2018年8月23日 下午3:21:29
 */
public class Page {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) " + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
	//每一个页面对应的URL
	protected volatile String url;
	//每一个网页对应的Doc，这个内存占用可能需要优化！！
	protected volatile Document doc;
	
	private volatile String host;
	
	private volatile int priority;
	
	private PatternUtil p = PatternUtil.instanct();
	
	//是否已经加载了
	protected volatile boolean ini = false;

	public Page(String url) {
		// 每一个页面对应的都有一个URL
		setUrl(url);
		//默认加载页面
		loadURL();
	}

	private void loadURL() {
		LoadThreadPoolUtil.submit(new Runnable() {
		      @Override
		      public void run() {
		        if (doc == null && !ini) {
		          try {
		            Connection connection = Jsoup.connect(url).timeout(10000).validateTLSCertificates(false);
		            connection.timeout(LoadThreadPoolUtil.waitTimeMils);
		            connection.userAgent(userAgent);
		            connection.followRedirects(true);
		            Document docValue = connection.get();
		            setDoc(docValue);
		            String baseurl = docValue.baseUri();
		            String host = new URL(baseurl).getHost();
		            setHost(host);
		            setIni(true);
		            int priority = p.getHostPriority(host);
		            setPriority(priority);
		          } catch (IOException e) {
		            log.error("{},初始化出现错误：{}", url, e);
		          }
		        }
		      }
		    });

		
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

	public boolean isIni() {
		return ini;
	}

	public void setIni(boolean ini) {
		this.ini = ini;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
