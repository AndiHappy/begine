package begine.search.mouse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhailz
 *
 * @version 2018年8月27日 下午3:49:44
 */
public class Util {
	private static Logger log = LoggerFactory.getLogger(Util.class);
	private static final HashMap<Character, Integer> array = new HashMap<Character, Integer>();
	private PatternUtil pt = PatternUtil.instanct();
	

	private Util() {
		ini();
	}

	private static final class UtilHolder {
		private static final Util instance = new Util();
	}

	public static Util getInstance() {
		return UtilHolder.instance;
	}
	
	public static String filter(String html) {
		return html.replaceAll("高速文字首发 ", "").replaceAll("手机同步阅读", "").replaceAll("&nbsp;;", "").replaceAll("&nbsp;", "").replaceAll("readx();", "").replaceAll("ahref=", "").replaceAll("^\"http:.*;", "").replaceAll("read3();", "")
				.replaceAll("bdshare();", "").replaceAll("www.x4399.com", "").replaceAll("wap.x4399.com", "").replaceAll("uservote();←→addbookcase();read4();", "").replaceAll("<[^>]+>", "");

	}

	/**
	 * 根据具体的站来进行判断，比较的low，但是也算是一个比较笨的方法
	 */
	public boolean judgeIsRealDirectoryPage(DPage dPage) {
		if (LoadThreadPoolUtil.waitLoadDoc(dPage, 1)) {
			String value = dPage.getDoc().baseUri();
			if (value.startsWith("https://m.biquge.info") || value.startsWith("https://baike.baidu.com") || value.startsWith("http://www.tadu.com") || value.startsWith("http://www.sodu.cc") || value.startsWith("https://m.qidian.com")
					|| value.startsWith("http://www.shuyue.cc") || value.startsWith("http://www.lkong.net/")) {
				return false;
			}
			try {
				URL url = new URL(value);
				String host = url.getHost();
				if (pt.hasHost(host)) {
					return true;
				} else {
					log.error("\n\n\n \t!!! 处理域名:{} !!!\t\n\n\n", value);
					return false;
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			log.info("baseURL:{}", value);
		}
		return false;
	}
	
	public static int getElementNumber(Element o1) {
		String value1 = o1.text();
		List<Integer> res = indexofAll(value1, "第");
		List<Integer> res3 = indexofAll(value1, "节");
		List<Integer> res4 = indexofAll(value1, "章");
		if((res3.size() >= 1 || res4.size() >= 1) && res.size() > 0){
			int end = res3.size() >= 1?res3.get(0):res4.get(0);
			int begine = getFirstIndex(res,end);
			String compare1 = value1.substring(begine+1, end);
			if(canConvert(compare1)){
				return convertChineseToNum(compare1);
			}
			
			if(isNumChacter(compare1)){
				return Integer.parseInt(compare1);
			}
		}
		return 0;
	}
	
	private static int getFirstIndex(List<Integer> res, Integer integer) {
		int value = res.size()-1;
		while(res.get(value) > integer) value--;
		return res.get(value);
	}
	public static List<Integer> indexofAll(String des,String s){
		List<Integer> value = new ArrayList<Integer>();
		if(StringUtils.isAnyBlank(des,s) || des.indexOf(s) == -1){
			return value;
		}
		
		int begine = des.indexOf(s);
		while(begine != -1){
			value.add(begine);
			begine = des.indexOf(s, begine+1);
		}
		
		return value;
		
	}

	private void ini() {
		array.put('一', 1);
		array.put('二', 2);
		array.put('两', 2);
		array.put('三', 3);
		array.put('四', 4);
		array.put('五', 5);
		array.put('六', 6);
		array.put('七', 7);
		array.put('八', 8);
		array.put('九', 9);
		array.put('十', 10);
		array.put('事', 10);
		array.put('百', 100);
		array.put('千', 1000);
		array.put('万', 10000);
		array.put('零', 0);
	}

	public static int convertChineseToNum(String chinese) {
		chinese = chinese.trim();
		char[] v = chinese.toCharArray();
		int res = 0;
		int i = 0;
		int emp = 0;
		for (; i < v.length; i++) {
			int tmpv = array.get(v[i]);
			if (i > 0) {
				int pre = array.get(v[i - 1]);
				if (tmpv > pre && tmpv % 10 == 0) {
					res = pre * tmpv + emp;
					emp = emp + pre * tmpv;
				} else {
					res = res + tmpv;
				}
			} else {
				res = tmpv;
			}
		}
		return res;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Util.convertChineseToNum("二"));
		System.out.println(Util.convertChineseToNum("十"));
		System.out.println(Util.convertChineseToNum("十二"));
		System.out.println(Util.convertChineseToNum("二十二"));
		System.out.println(Util.convertChineseToNum("三十"));
		System.out.println(Util.convertChineseToNum("二百零二"));
		System.out.println(Util.convertChineseToNum("二百二十二"));
		System.out.println(Util.convertChineseToNum("五百三十二"));
		System.out.println(Util.convertChineseToNum("一千五百三十二"));
		System.out.println(Util.convertChineseToNum("一千零二"));
		System.out.println(Util.convertChineseToNum("一万一千零二十二"));
		System.out.println(Util.convertChineseToNum("一万零一十二"));

	}

	public static boolean canConvert(String value) {
		if (StringUtil.isNotBlank(value)) {
			for (char ch : value.toCharArray()) {
				if (array.get(ch) == null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean isChineseNum(char ch) {
		if (array.get(ch) != null) {
			return true;
		}
		return false;
	}

	public int getNumFromChinesesString(String china) {
		int begine = china.indexOf("第");
		int select = china.indexOf("章");
		int select1 = china.indexOf("节");
		select= select<select1?select:select1;

		if (begine != -1 && select != -1) {
			return convertChineseToNum(china.substring(begine + 1, select));
		} else {
			String chineseCanConvert = getChineseCanConvert(china);
			return convertChineseToNum(chineseCanConvert);
		}

	}

	private String getChineseCanConvert(String china) {
		StringBuilder builder = new StringBuilder();
		char[] va = china.toCharArray();
		for (int i = 0; i < va.length; i++) {
			if (isChineseNum(va[i])) {
				builder.append(va[i]);
			} else if (!isChineseNum(va[i]) && builder.length() == 0) {
				continue;
			} else {
				break;
			}
		}
		return builder.toString();
	}

	public static boolean isNumChacter(String value){
		return StringUtils.isNumeric(value);
	}

	public static void comapre(String textContent, String textContent2) {
		int i = 0,j=0,flag1=0,containerrorright=0,containerrorlength=0;
		boolean choose = false;
		for (; i < textContent.length(); ) {
			if(textContent.charAt(i) == textContent2.charAt(j)){
				i++;j++;
				if(choose){
					containerrorright++;
					containerrorlength++;
				}
			}else{
				choose=true;
				containerrorlength++;
				i++;j++;
				flag1++;
				if(flag1>15)break;
			}
		}
		
		if(containerrorright < 50){
			i = i - containerrorlength;
			j = j - containerrorlength;
		}
		
		
		choose = false;
		int ii = textContent.length() - 1 ,jj=textContent2.length() -1,flag2=0,containerror2=0,containerrorlength2=0;
		for (; ii > 0 && jj >0; ) {
			if(textContent.charAt(ii) == textContent2.charAt(jj)){
				ii--;jj--;
				if(choose){
					containerror2++;
					containerrorlength2++;
				}
			}else{
				choose=true;
				ii--;jj--;
				flag2++;
				containerrorlength2++;
				if(flag2>15)break;
			}
		}
		
		if(containerror2 < 50){
			ii = ii + containerrorlength2;
			jj = jj + containerrorlength2;
		}
		System.out.println(textContent.substring(i, ii));
		System.out.println(textContent2.substring(j, jj));
	}
}
