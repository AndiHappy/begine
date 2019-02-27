package begine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import begine.Application;
import begine.load.LoadBookByContentPageURL;
import begine.search.mouse.ContentPage;
import begine.search.mouse.LoadThreadPoolUtil;
import begine.search.mouse.Page;
import begine.search.mouse.baidu.BaiduSearch;
import begine.search.mouse.google.GoogleSearch;
import begine.util.BResult;

/**
 * @author zhailz
 *
 * @version 2018年8月14日 下午5:07:13
 */

@RestController
public class BookSearchController {

	private static Logger logger = LoggerFactory.getLogger(BookSearchController.class);

	public static final Pattern chapterLink = Pattern.compile("第.*章");

	private static String afterSearchWorld = " 目录";

	@RequestMapping("/state")
	public BResult test(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new BResult(0, name, name);
	}
	
	@RequestMapping("/")
	public ModelAndView index(@RequestParam(value = "name", defaultValue = "World") String name) {
		ModelAndView view = new ModelAndView("index");
		view.addObject("searchName", "searchValue" + Math.random() + " " + name);
		return view;
	}

	@RequestMapping("/hello")
	public ModelAndView greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		ModelAndView view = new ModelAndView("hello");
		view.addObject("searchName", "searchValue" + Math.random() + " " + name);
		return view;
	}

	@RequestMapping("/search")
	public BResult search(HttpServletRequest req, @RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "searchEngine", required = true, defaultValue = "baidu") String searchEngine,
			@RequestParam(value = "cache", required = true, defaultValue = "Over") String Cache) {
		logger.info("visit:{},search:{}", req.getRequestURI(), name);
		try {
			if ("baidu".equalsIgnoreCase(searchEngine)) {
				BResult result = BaiduSearch.search(name, Cache);
				// 单机准备
				String loadURL = "http://45.78.3.205:" + Application.port + "/down?fName="
						+ result.getValue().toString();
				result.setValue(loadURL);
				return result;
			}
		} catch (IOException e) {
			logger.error("搜索出现错误:{}", e);
		}
		return new BResult(0, name, "error");
	}

	@RequestMapping("/searchDirectory")
	public BResult searchDirectory(HttpServletRequest req, @RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "searchEngine", required = true, defaultValue = "baidu") String searchEngine,
			@RequestParam(value = "cache", required = true, defaultValue = "Over") String Cache) {
		logger.info("visit:{},search:{}", req.getRequestURI(), name);

		// add queue
		List<GooglePageBrief> result = GoogleSearch.searchDirectory(name, Cache);
		for (GooglePageBrief googlePage : result) {
			logger.info("google link:{} result:{}", googlePage.getUrl(), googlePage.getNewestChapterName());
		}

		return new BResult(0, name, "");
	}

	@RequestMapping("/loadbook")
	public BResult loadbook(HttpServletRequest req,
			@RequestParam(value = "contenturl", required = true) String contenturl) {
		logger.info("loadbook,访问路径:{},目录页地址:{}", req.getRequestURI(), contenturl);

		LoadBookByContentPageURL page = new LoadBookByContentPageURL(contenturl);

		return new BResult(0, "success", "");
	}

	@RequestMapping("/content")
	public ModelAndView content(HttpServletRequest req,
			@RequestParam(value = "contenturl", required = true) String contenturl) throws JSONException {
		ModelAndView view = new ModelAndView("content");
		logger.info("loadbook,访问路径:{},目录页地址:{}", req.getRequestURI(), contenturl);
		Page page = new Page(contenturl);
		LoadThreadPoolUtil.waitLoadDoc(page, 30);
		List<ChapterLink> users = new ArrayList<>();
		// 首先分析的是link的情况
		TreeMap<String, ContentPage> chaptersLink = new TreeMap<String, ContentPage>();
		Elements elements = page.getDoc().select("a");
		for (Element chatptera : elements) {
			String aText = chatptera.text();
			String[] aTextArray = aText.split(" ");
			if (aTextArray != null) {
				// chatpter match
				if (chapterLink.matcher(aTextArray[0]).find()) {
					ChapterLink chapter = new ChapterLink();
					chapter.name = chatptera.text();
					chapter.link = chatptera.absUrl("href");
					users.add(chapter);
					chaptersLink.put(chatptera.absUrl("href"), new ContentPage(chatptera.text()));
				}
			}
		}
		view.addObject("users", users);
		return view;
	}

	@RequestMapping("/read")
	public ModelAndView read(HttpServletRequest req, @RequestParam(value = "url", required = true) String url)
			throws JSONException {
		ModelAndView view = new ModelAndView("read");
		logger.info("read,访问路径:{},目录页地址:{}", req.getRequestURI(), url);
		Page page = new Page(url);
		LoadThreadPoolUtil.waitLoadDoc(page, 30);
		Elements value = page.getDoc().select("div[id=\"contentbox\"]");
		Elements ad = page.getDoc().select("div[class=\"ad_content\"]");
		for (int i = 0; i < ad.size(); i++) {
			ad.get(i).remove();
		}
		String content = value.get(0).html();
		view.addObject("content", true);
		view.addObject("content1", content);
		return view;
	}
}

class ChapterLink {
	public String id = UUID.randomUUID().toString();
	public String name;
	public String link;
	public String edit;
	public String delete;

}
