package begine.controller.bs;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import begine.server.log.BussinessLog;
import begine.util.BResult;

/**
 * @author zhailz
 *
 * @version 2018年8月14日 下午5:07:13
 */

@RestController
@RequestMapping("/passport")
public class CollectionController {

	private static Logger logger = LoggerFactory.getLogger(CollectionController.class);
	
    @BussinessLog("[{1}] log in system")
    @RequestMapping(value = "/signin")
    @ResponseBody
	public BResult submitLogin(HttpServletRequest request,HttpServletResponse response) {
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
