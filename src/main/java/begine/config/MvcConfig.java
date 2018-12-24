package begine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import begine.interceptor.LogInterceptorHandler;

/**
 * @author zhailz
 *
 * @version 2017年12月5日 上午11:27:17
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	/**
	 * 设置静态资源
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 默认的情况下，配置的参数是：
		// # spring.mvc.static-path-pattern=/**  
        // # spring.resources.static-locations=/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/ 
		//
        // registry.addResourceHandler("/**").addResourceLocations("classpath:/staticresource/");
	}

	/**
	 * 设置欢迎页的
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// index.html页面在 templates 文件夹下
		registry.addViewController("/").setViewName("index");
		// 请求转移的设置
		// registry.addViewController( "/" ).setViewName(
		// "forward:/yourpage.html" );
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	/**
	 * 拦截器的设置
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns 用于添加拦截规则,excludePathPatterns 用户排除拦截
		HandlerInterceptor interceptor = new LogInterceptorHandler();
		registry.addInterceptor(interceptor).addPathPatterns("/up").addPathPatterns("/down");
	}

}