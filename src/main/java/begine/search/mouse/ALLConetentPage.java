package begine.search.mouse;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhailz
 *
 * @version 2018年8月27日 下午10:40:04
 */
public class ALLConetentPage {

	private static final Logger log = LoggerFactory.getLogger(ALLConetentPage.class);
	private List<DPage> ps;
	private PatternUtil p = PatternUtil.instanct();
	public static String yingppattern = "yingp";
	public static Random random = new  Random();
	private String ronghyu = null;

	public ALLConetentPage(List<String> pages) {
		List<DPage> ps = new ArrayList<DPage>();
		for (String string : pages) {
			ps.add(new DPage(string, null));
		}
		setPs(ps);
	}

	public ALLConetentPage(Set<String> pageslinks) {
		List<DPage> ps = new ArrayList<DPage>();
		for (String pagelink : pageslinks) {
			ps.add(new DPage(pagelink, null));
		}
		setPs(ps);
	}

	public List<DPage> getPs() {
		return ps;
	}

	public void setPs(List<DPage> ps) {
		this.ps = ps;
	}

	public static String percent(double p1, double p2) {
		String str;
		double p3 = p1 / p2;
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		str = nf.format(p3);
		return str;
	}
	

	public void iniContent(String pattern) {
		if (ps != null && !ps.isEmpty()) {
			for (int i = 0; i < ps.size(); i++) {
				DPage dPage = ps.get(i);
				log.info("load complete: {},load:{}", percent(i, ps.size()), dPage.url);
				if (dPage.getTextContent() == null) {
					//					pool.submit((new Runnable() {
					//						@Override
					//						public void run() {
					try {
						if (LoadThreadPoolUtil.waitLoadDoc(dPage, 10)) {
							Document dp = dPage.getDoc();
							String url = dp.baseUri();
							URL u = new URL(url);
							if(p.getHostSetting(u.getHost()) != null) {
								String pattern1 = p.getHostSetting(u.getHost()).getChooseTitlePattern();
								Element title = dp.select(pattern1).first();
								String titleText = title.text();
								log.info("title:{}", titleText);
								dPage.setTitle(titleText);
							}else {
								Elements title = dp.select("title");
								String titleText = title.text();
								log.info("title:{}", titleText);
								dPage.setTitle(titleText);
							}
							
							String html = null;
							if(StringUtils.isNotBlank(pattern)) {
								Elements content = dp.select(pattern);
								html = content.toString();
							}else {
								Elements html1 = dp.select("body");
								Element body = html1.first();
								html = body.toString();
								html = deleteAllHTMLTag(html);
								html = deleteRongYuText(html);
							}
							
							String firstCellHzDec = filter(html);
							dPage.setTextContent(firstCellHzDec);
//							System.out.println(firstCellHzDec);
						}
					} catch (Exception e) {
						log.error("拉去error:{}", e);
					}
				}
			}
		}
	}

	private String deleteRongYuText(String html) {
		if(getRonghyu() == null) {
			this.setRonghyu(html);
		}
		
		char[] va = getRonghyu().toCharArray();
		char[] ba = html.toCharArray();
		for (int i = 0; i < va.length; i++) {
			if(va[i] != ba[i]) {
				System.out.println(va[i]);
			}
		}
		
		return null;
	}

	private void searchAllTextNodes(Node body, ArrayList<Node> textNodes) {
		List<Node> nodes = body.childNodes();
		if(nodes != null && nodes.size() > 0)
		for (Node node : nodes) {
			if(node instanceof TextNode){
				textNodes.add(node);
				if(node.toString().contains("恺撒")){
					System.out.println(node.toString());
				}
			}
			
			if(node.childNodes() != null && node.childNodes().size() > 0){
				searchAllTextNodes(node, textNodes);
			}
		}   
	}

	public static String deleteAllHTMLTag(String source) {
		  if(source == null) {
		       return "";
		  }
		  String s = source;
		  /** 删除普通标签  */
		  s = s.replaceAll("<(S*?)[^>]*>.*?|<.*? />", "");
		  /** 删除转义字符 */
		  s = s.replaceAll("&.{2,6}?;", "");
		  return s;
		}
	private String filter(String html) {
		return html.replaceAll("高速文字首发 ", "").replace("(adsbygoogle = window.adsbygoogle || []).push({});", "").replaceAll("手机同步阅读", "").replaceAll("&nbsp;;", "").replaceAll("&nbsp;", "").replaceAll("readx();", "").replaceAll("ahref=", "").replaceAll("^\"http:.*;", "").replaceAll("read3();", "")
				.replaceAll("bdshare();", "").replaceAll("www.x4399.com", "").replaceAll("wap.x4399.com", "").replaceAll("uservote();←→addbookcase();read4();", "").replaceAll("<[^>]+>", "");

	}

	public String getRonghyu() {
		return ronghyu;
	}

	public void setRonghyu(String ronghyu) {
		this.ronghyu = ronghyu;
	}

	

}
