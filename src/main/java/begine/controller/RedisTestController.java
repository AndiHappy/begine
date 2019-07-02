package begine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import begine.springredis.RedisServer;
import begine.util.BResult;

@RestController
public class RedisTestController {

	@Autowired
	private RedisServer redisServer;
	
	@RequestMapping("/redis")
	public BResult test(@RequestParam(value = "name", defaultValue = "World",required = false) String userId,
			@RequestParam(value = "name", defaultValue = "Http://127.0.0.1:8080/",required = false) String url
			) {
		return redisServer.addLink(userId, url);
	}
}
