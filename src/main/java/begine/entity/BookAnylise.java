package begine.entity;

import java.util.TreeMap;

import begine.search.mouse.ContentPage;
import begine.search.mouse.ThreadPoolUtil;

/**
 * @author zhailz
 *
 */
public class BookAnylise {
	
	
	private String newestChapterName;
	
	private String newestChapterLink;
	
	private String newestChapterContent;
	
	private long newestTime;
	
	private TreeMap<String,ContentPage> allChapterLinks;
	
	private boolean load = false;
 
	public BookAnylise(String newestChapterLink, String title,TreeMap<String,ContentPage> allChapterLinks) {
		this.setAllChapterLinks(allChapterLinks);
		this.setNewestChapterLink(newestChapterLink);
		this.setNewestChapterName(title);
		ThreadPoolUtil.load(this,newestChapterLink);
	}

	public String getNewestChapterName() {
		return newestChapterName;
	}

	public void setNewestChapterName(String newestChapterName) {
		this.newestChapterName = newestChapterName;
	}

	public String getNewestChapterLink() {
		return newestChapterLink;
	}

	public void setNewestChapterLink(String newestChapterLink) {
		this.newestChapterLink = newestChapterLink;
	}

	public String getNewestChapterContent() {
		return newestChapterContent;
	}

	public void setNewestChapterContent(String newestChapterContent) {
		this.newestChapterContent = newestChapterContent;
	}

	public long getNewestTime() {
		return newestTime;
	}

	public void setNewestTime(long newestTime) {
		this.newestTime = newestTime;
	}

	public TreeMap<String,ContentPage> getAllChapterLinks() {
		return allChapterLinks;
	}

	public void setAllChapterLinks(TreeMap<String,ContentPage> allChapterLinks) {
		this.allChapterLinks = allChapterLinks;
	}

	public boolean isLoad() {
		return load;
	}

	public void setLoad(boolean load) {
		this.load = load;
	}
	
	

}
