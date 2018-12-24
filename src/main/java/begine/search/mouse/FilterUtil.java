package begine.search.mouse;

import java.util.regex.Pattern;

/**
 * @author zhailz
 *
 * @version 2018年8月23日 下午3:21:29
 * 
 * 过滤网址
 */
public class FilterUtil {

    public static final Pattern resourceLinkFilters = Pattern.compile(
            ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
            "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    
    public static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");
    
    public static final Pattern googlePatterns =  Pattern.compile("https://www.google.com/{1}+[^\\s]*");
    
    
    

}
