package begine.controller.collection;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import begine.util.BResult;

/**
 * @author zhailz
 *
 * @version 2018年8月14日 下午5:07:13
 */

//TODO 
// 功能： 收集网络页面
// 说明：类似网络的收藏页面，不过是可以打标签，可以写记录
@RestController
public class CollectionController {

	private static Logger logger = LoggerFactory.getLogger(CollectionController.class);
	
	@RequestMapping("/collect")
	public BResult test(HttpServletRequest request,HttpServletResponse response) {
		//允许跨域名进行访问
				response.addHeader("Access-Control-Allow-Origin", "*");
				logger.info("enter APIAuthorityInterceptor perHandle method...");
				// web请求从cookies取登录用户信息 端请求按照端的方式取登录用户信息
				String requestURI = request.getRequestURI();
				logger.info("Visitor URI[" + requestURI + "]");
				Map<String, String[]> paras = request.getParameterMap();
				for (String parakey : paras.keySet()) {
					logger.info("httplog para:{},value:{}",parakey,paras.get(parakey)[0]);
				}
				
				Enumeration<String> ite = request.getHeaderNames();
			    while(ite.hasMoreElements()){
			    	String header = ite.nextElement();
			    	logger.info("httplog header:{},value:{}",header,request.getHeader(header));
			    }
			    
		return new BResult(0,"参数", paras);
	}

}
