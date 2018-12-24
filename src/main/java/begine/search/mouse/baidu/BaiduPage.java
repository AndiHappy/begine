package begine.search.mouse.baidu;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import begine.search.mouse.Page;
import begine.search.mouse.DPage;
import begine.search.mouse.LoadThreadPoolUtil;

/**
 * @author zhailz
 *
 * @version 2018年8月23日 下午10:14:47
 */
public class BaiduPage extends Page {

	public static final Logger log = LoggerFactory.getLogger(BaiduPage.class);

	private List<DPage> contents = new ArrayList<DPage>();
	
	//正则匹配过滤掉不需要的网站
    public static final Pattern extendhost = Pattern.compile(".*(http://www.baidu.com/link?url=).*");

	public BaiduPage(String url) {
		super(url);
	}
	
	
	/**
	 * 获得搜索的目录页link
	 * @throws UnsupportedEncodingException 
	 * */
	public List<String> getDirectoryLink() throws UnsupportedEncodingException {
		//google 最长等待10秒
		LoadThreadPoolUtil.waitLoadDoc(this, 10);
		if(isIni() && getDoc() != null){
			 List<String> linksvalue = new ArrayList<String>();
			Elements links = doc.select("div[class=\"result c-container \"]");
			if (links != null && !links.isEmpty()) {
				for (int i = 0; i < links.size(); i++) {
					Element link = links.get(i);
					Elements abshrefElement = link.select("a[data-click]");
					String abshref =  abshrefElement.get(0).attr("abs:href");
					//非资源，非图片，非源站
					if(abshref.startsWith("http://www.baidu.com/link?url=")){
						linksvalue.add(abshref);
						getContents().add(new DPage(abshref, null));
					}
					
				}
				return linksvalue;
			}
		}
		return null;
	}


	public List<DPage> getContents() {
		return contents;
	}


	public void setContents(List<DPage> contents) {
		this.contents = contents;
	}

}