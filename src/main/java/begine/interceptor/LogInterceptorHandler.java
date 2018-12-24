package begine.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import begine.util.ReqestUtil;

/**
 * REST拦截，校验方法签名
 * */
public class LogInterceptorHandler extends WebContentInterceptor{
	
	public static Logger log  = LoggerFactory.getLogger(LogInterceptorHandler.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		String lookupPath = request.getRequestURL().toString();
		log.info("visit: {}",lookupPath);
		log.info("handlter:{}",handler);
		if(handler instanceof HandlerMethod){
			HandlerMethod method = (HandlerMethod) handler;
			String paras = ReqestUtil.getParams(request);
			log.info("Method:{},para:{},returnValue:{},waste:{}",method.getMethod().getName(),paras);
		}
		return true;
	}
	

}
