package begine.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import begine.search.mouse.LoadThreadPoolUtil;
import begine.search.mouse.Page;
import begine.search.mouse.PatternUtil;
import begine.util.Constants;

public class GooglePageBrief extends Page {

	private static PatternUtil util =  PatternUtil.instanct();
	private static Logger log = LoggerFactory.getLogger(GooglePageBrief.class);
	private String newestChapterName ;
	private String newestChapterLink ;
	
	public GooglePageBrief(String link) {
		super(link);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				constructBrief();
			}
		}).start();
		
	}

	private void constructBrief() {
		LoadThreadPoolUtil.waitLoadDoc(this, 30);
		Document document = this.getDoc();
		boolean value = util.hasHost(this.getHost());
		if(value){
			// 首先分析的是link的情况
			List<Element> chaptersLink = new ArrayList<>();
			if(document != null){
				Elements elements = document.select("a");
				if(!elements.isEmpty()){
					for (Element chatptera : elements) {
						String aText = chatptera.text();
						String[] aTextArray = aText.split(" ");
						if (aTextArray != null) {
							// chatpter match
							if (Constants.chapterLink.matcher(aTextArray[0]).find()) {
								chaptersLink.add(chatptera);
							}
						}
					}
				}
			}
			
			Collections.sort(chaptersLink, new Comparator<Element>() {

				@Override
				public int compare(Element o1, Element o2) {
					int indexo1 = o1.attr("href").lastIndexOf("/");
					int indexo11 = o1.attr("href").lastIndexOf(".");
					int indexo2 = o2.attr("href").lastIndexOf("/");
					int indexo22 = o2.attr("href").lastIndexOf(".");
					String o1c = o1.attr("href").substring(indexo1+1,indexo11);
					String o2c = o2.attr("href").substring(indexo2+1,indexo22);
					int value = Integer.parseInt(o1c)-Integer.parseInt(o2c);
					return value;
				}
			});
			
//			for (Element element : chaptersLink) {
//				System.out.println(element.absUrl("href") + " "+ element.text());
//			}
			
			if(!chaptersLink.isEmpty()){
				this.setNewestChapterLink(chaptersLink.get(chaptersLink.size()-1).absUrl("href"));
				this.setNewestChapterName(chaptersLink.get(chaptersLink.size()-1).text());
			}
		}
		
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
	
	public static void main(String[] args){
		GooglePageBrief bref = new GooglePageBrief("https://www.uukanshu.com/b/74534/");
		System.out.println(bref.getNewestChapterLink());
	}

}
