package begine.springredis;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import begine.springredis.client.RateLimiterClient;
import begine.util.BResult;

@Component
public class RedisServer {

	// inject the actual template
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	// inject the template as ListOperations
	// can also inject as Value, Set, ZSet, and HashOperations
	@Resource(name = "redisTemplate")
	private ListOperations<String, String> listOps;
	
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOps;


	@Autowired
	private RateLimiterClient rateLimiterClient;
			
	
	public BResult addLink(String userId, String urls) {
		try {
			rateLimiterClient.acquire("aaaa", "kkkk");
			
			URL url = new URL(urls);
			valueOps.set("key", userId);
			String oldValue = valueOps.getAndSet("urls", urls);
			System.out.println("old value: " + oldValue);
			
			listOps.leftPush(userId, url.toExternalForm());
			// or use template directly
			redisTemplate.boundListOps(userId).leftPush(url.toExternalForm());
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}