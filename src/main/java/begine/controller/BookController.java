package begine.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.WeakHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import begine.search.normalload.LoadBookByContentPageURL;
import begine.server.search.GoogleSearch;
import begine.util.BResult;
import begine.util.TimeFormat;

/**
 * @author zhailz
 *
 * @version 2018年8月14日 下午5:07:13
 */

@RestController
public class BookController {

	private static Logger logger = LoggerFactory.getLogger(BookController.class);

	private WeakHashMap<String, String> cache = new WeakHashMap<String, String>(200); 
	
	@Autowired
	private GoogleSearch googleSearch;
	
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
		ModelAndView view = new ModelAndView("index");
		view.addObject("searchName", "searchValue" + Math.random() + " " + name);
		return view;
	}

	/***
	 * cache url
	 * */
	@RequestMapping("/loadbyurl")
	public BResult search(HttpServletRequest req, @RequestParam(value = "url", required = true) String url) throws Exception {
		logger.info("visit:{},{},loadbyurl:{}",req.getRequestURL().toString(), req.getRequestURI(), url);
		String time = TimeFormat.getFormatDate(new Date(), TimeFormat.TIME_FORMAT_D);
		if(cache.get(url+time) != null) {
			return new BResult(0, cache.get(url), "success");
		}
		
		String fullURL = req.getRequestURL().toString();
		URL geturl = new URL(fullURL);
		logger.info("host:{},pro:{},port:{},path:{}",geturl.getHost(),geturl.getProtocol(),geturl.getPort(),geturl.getPath());
		LoadBookByContentPageURL load = new LoadBookByContentPageURL(url);
		try {
			load.loadBookToFile();
		} catch (IOException e) {
			return new BResult(-1, load.getFileName(), e.getMessage());
		}
		String loadURL = geturl.getProtocol()+"://"+geturl.getHost()+":"+geturl.getPort()+"/down?fName="+load.getFileName();
		cache.put(url+time, loadURL);
		return new BResult(0, loadURL, "success");
	}
	
	
	/***
	 * newest chapter
	 * google search
	 * */
	@RequestMapping("/newchapter")
	public BResult newchapter(HttpServletRequest req, 
			@RequestParam(value = "name", required = true) String name) throws Exception {
		logger.info("name:{} , requestURL:{} ",name, req.getRequestURI());
		googleSearch.search(name);
		return new BResult(0, "", "success");
	}
}
