package begine.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhailz
 *
 * @version 2018年8月30日 下午2:47:39
 */
public class ReqestUtil {

	/**
	 * 从request中获取参数信息
	 * 
	 * @param request
	 * @return key=value 组成的字符串
	 */
	public static String getParams(final HttpServletRequest request) {
		Enumeration<?> e = request.getParameterNames();
		StringBuilder builder = new StringBuilder();
		builder.append("Parameters: ");
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();

			String value = request.getParameter(name);
			builder.append(name).append("=").append(value).append(" ");
		}
		return builder.toString();
	}
}
