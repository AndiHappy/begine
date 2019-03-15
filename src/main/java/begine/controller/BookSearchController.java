package begine.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import begine.load.LoadBookByContentPageURL;
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
		return new BResult(0, name, "error");
	}

	@RequestMapping("/searchDirectory")
	public BResult searchDirectory(HttpServletRequest req, @RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "searchEngine", required = true, defaultValue = "baidu") String searchEngine,
			@RequestParam(value = "cache", required = true, defaultValue = "Over") String Cache) {
		logger.info("visit:{},search:{}", req.getRequestURI(), name);
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
		List<ChapterLink> users = new ArrayList<>();
		view.addObject("users", users);
		return view;
	}

	@RequestMapping("/read")
	public ModelAndView read(HttpServletRequest req, @RequestParam(value = "url", required = true) String url)
			throws JSONException {
		ModelAndView view = new ModelAndView("read");
		logger.info("read,访问路径:{},目录页地址:{}", req.getRequestURI(), url);
		view.addObject("content", true);
		view.addObject("content1", "qwer");
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
