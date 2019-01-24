package begine.search.mouse.google;

import java.util.TreeMap;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import begine.entity.BookAnylise;
import begine.search.mouse.ContentPage;
import begine.search.mouse.LoadThreadPoolUtil;
import begine.search.mouse.Page;

/**
 * @author zhailz
 *
 * @version 2018年8月23日 下午3:21:29
 */
public class GooglePage extends Page {

	private static Logger log = LoggerFactory.getLogger(GooglePage.class);
	
	//正则匹配过滤掉不需要的网站
    public static final Pattern extendhost = Pattern.compile(".*tw.hjwzw.com.*|.*google.*|.*qidian.*|.*baike.*|.*douban.*|.*blogger.*|.*youtube.*");
    //正则匹配过滤掉不需要的网站
    public static final Pattern chapterLink = Pattern.compile("第.*章");   	
   	//分析的完成以后，是否需要这个介面
   	private boolean isNeed;
   	
   	private BookAnylise result = null;

	public GooglePage(String url) {
		super(url);
//		anylise();
	}

	private void anylise() {
		LoadThreadPoolUtil.waitLoadDoc(this,60);
		Document document = this.getDoc();
		Pattern extendhost = Pattern.compile(".*tw.hjwzw.com.*|.*google.*|.*qidian.*|.*baike.*|.*douban.*|.*blogger.*|.*youtube.*");
		boolean exclude = extendhost.matcher(this.getHost()).find();
		if(exclude){
			log.info("exclude :{} ",this.getUrl());
			setNeed(false);
			return;
		}
		

		//第二步需要到目录页，判断目录页的方法
		
		//首先分析的是link的情况
		TreeMap<String,ContentPage> chaptersLink = new  TreeMap<String,ContentPage>();
		Elements elements = document.select("a");
		int totlaLinks = elements.size();
		log.info("total link size:{}",totlaLinks);
		for (Element chatptera : elements) {
			String aText = chatptera.text();
			String[] aTextArray = aText.split(" ");
			if(aTextArray != null){
				// chatpter match
				if(chapterLink.matcher(aTextArray[0]).find()){
					chaptersLink.put(chatptera.absUrl("href"),new ContentPage(chatptera.text()));
				}
			}
		}
		
		// anylise error 
		if(chaptersLink.isEmpty()){
			log.error("Error link:{},please anylise!",this.getUrl());
			throw new IllegalAccessError("please anylise "+ this.getUrl());
		}
		
//		updateMsg
		BookAnylise reult = new BookAnylise(chaptersLink.lastKey(),chaptersLink.lastEntry().getValue().getTitle(),chaptersLink);
		setResult(reult);
	}

	public boolean isNeed() {
		return isNeed;
	}

	public void setNeed(boolean isNeed) {
		this.isNeed = isNeed;
	}
	
	public static void main(String[] args){
		System.out.println("http://www.16xz.com/html/9/9852/");
		GooglePage page = new GooglePage("http://www.16xz.com/html/9/9852/");
		System.out.println(page.getResult().getNewestChapterName());
	}

	public BookAnylise getResult() {
		return result;
	}

	public void setResult(BookAnylise result) {
		this.result = result;
	}

}
