package begine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import begine.Application;
import begine.search.mouse.baidu.BaiduSearch;
import begine.search.mouse.google.GooglePage;
import begine.search.mouse.google.GoogleSearch;
import begine.search.mouse.google.PageParse;
import begine.util.BResult;

/**
 * @author zhailz
 *
 * @version 2018年8月14日 下午5:07:13
 */

@RestController
public class BookSearchController {

	private static Logger logger = LoggerFactory.getLogger(BookSearchController.class);
	
	private static String afterSearchWorld = " 目录";
	
	@RequestMapping("/state")
	public BResult test(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new BResult(0,name, name);
	}

	@RequestMapping("/hello")
	public ModelAndView greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		ModelAndView view = new ModelAndView("hello");
		view.addObject("searchName", "searchValue" + Math.random() + " " + name);
		return view;
	}

	@RequestMapping("/search")
	public BResult search(HttpServletRequest req, 
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "searchEngine", required = true, defaultValue = "baidu") String searchEngine,
			@RequestParam(value = "cache", required = true, defaultValue = "Over") String Cache
			) {
		logger.info("visit:{},search:{}", req.getRequestURI(), name);
		try {
			if ("baidu".equalsIgnoreCase(searchEngine)) {
				BResult result =  BaiduSearch.search(name,Cache);
				//单机准备
				String loadURL = "http://45.78.3.205:"+Application.port+"/down?fName="+result.getValue().toString();
				result.setValue(loadURL);
				return result;
			}
//			if ("google".equalsIgnoreCase(searchEngine)) {
//				return GoogleSearch.search(name);
//			}
		} catch (IOException e) {
			logger.error("搜索出现错误:{}",e);
		}
		return new BResult(0, name, "error");
	}
	
	@RequestMapping("/searchDirectory")
	public BResult searchDirectory(HttpServletRequest req, 
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "searchEngine", required = true, defaultValue = "baidu") String searchEngine,
			@RequestParam(value = "cache", required = true, defaultValue = "Over") String Cache
			) {
		logger.info("visit:{},search:{}", req.getRequestURI(), name);
		
		 Map<String, Boolean> directory = GoogleSearch.searchDirestory(name, Cache);
		 for (String link : directory.keySet()) {
			 logger.info(link);
		}
		 //探索统一的破解方式
		 Map<String, Object> findResult = new HashMap<>(directory.size());
		 List<GooglePage> pages = new ArrayList<GooglePage>();
		 if(directory != null && directory.size() > 0){
			 for (String link : directory.keySet()) {
				 logger.info("link: {} ",link);
				 GooglePage page;
				try {
					page = new GooglePage(link);
					pages.add(page);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		 }

		 
		 
		return new BResult(0, name, pages);
	}

}
