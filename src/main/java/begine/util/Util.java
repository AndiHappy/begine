package begine.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import begine.search.mouse.HostSetting;
import begine.search.mouse.PatternUtil;

/**
 * @author zhailz
 *
 * @version 2018年8月27日 下午3:49:44
 */
public class Util {
	private static Logger log = LoggerFactory.getLogger(Util.class);
	private static final HashMap<Character, Integer> array = new HashMap<Character, Integer>();
	private static final PatternUtil pt = PatternUtil.instanct();

	private Util() {
		ini();
	}

	private static final class UtilHolder {
		private static final Util instance = new Util();
	}

	public static Util getInstance() {
		return UtilHolder.instance;
	}

	public String filterTitle(String titleDiv) {
//		return titleDiv.replaceAll("_.*", "").replaceAll("-.*", "")
		//				.replaceAll("&nbsp;", "")
		return titleDiv;
	}

	/**
	 * 处理html 过滤的东西，比较的麻烦一步一步的进行
	 * */
	public String filter(String html) {

		/** 删除普通标签  */
		String nohtml = html.replaceAll("<(S*?)[^>]*>.*?|<.*? />", "");
		/** 删除转义字符 */
		String content = nohtml.replaceAll("&.{2,6}?;", "");

		/** 删除google 广告产生的遗留 */
		String nogooglead = content.replaceAll("(\\(.*);", "");

		/** 删除添加的广告*/
		String v = nogooglead.replaceAll("https:\\/\\/.*|请记住本书.*;", "");

		return v;

	}

	public int getElementNumber(Element o1) {
		String value1 = o1.text();
		List<Integer> res = indexofAll(value1, "第");
		List<Integer> res3 = indexofAll(value1, "节");
		List<Integer> res4 = indexofAll(value1, "章");
		if ((res3.size() >= 1 || res4.size() >= 1) && res.size() > 0) {
			int end = res3.size() >= 1 ? res3.get(0) : res4.get(0);
			int begine = getFirstIndex(res, end);
			String compare1 = value1.substring(begine + 1, end);
			if (canConvert(compare1)) {
				return convertChineseToNum(compare1);
			}

			if (isNumChacter(compare1)) {
				return Integer.parseInt(compare1);
			}
		}
		return 0;
	}

	private int getFirstIndex(List<Integer> res, Integer integer) {
		int value = res.size() - 1;
		while (res.get(value) > integer)
			value--;
		return res.get(value);
	}

	public List<Integer> indexofAll(String des, String s) {
		List<Integer> value = new ArrayList<Integer>();
		if (StringUtils.isAnyBlank(des, s) || des.indexOf(s) == -1) {
			return value;
		}

		int begine = des.indexOf(s);
		while (begine != -1) {
			value.add(begine);
			begine = des.indexOf(s, begine + 1);
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
		select = select < select1 ? select : select1;

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

	public static boolean isNumChacter(String value) {
		return StringUtils.isNumeric(value);
	}

	public static void comapre(String textContent, String textContent2) {
		int i = 0, j = 0, flag1 = 0, containerrorright = 0, containerrorlength = 0;
		boolean choose = false;
		for (; i < textContent.length();) {
			if (textContent.charAt(i) == textContent2.charAt(j)) {
				i++;
				j++;
				if (choose) {
					containerrorright++;
					containerrorlength++;
				}
			} else {
				choose = true;
				containerrorlength++;
				i++;
				j++;
				flag1++;
				if (flag1 > 15)
					break;
			}
		}

		if (containerrorright < 50) {
			i = i - containerrorlength;
			j = j - containerrorlength;
		}

		choose = false;
		int ii = textContent.length() - 1, jj = textContent2.length() - 1, flag2 = 0, containerror2 = 0,
				containerrorlength2 = 0;
		for (; ii > 0 && jj > 0;) {
			if (textContent.charAt(ii) == textContent2.charAt(jj)) {
				ii--;
				jj--;
				if (choose) {
					containerror2++;
					containerrorlength2++;
				}
			} else {
				choose = true;
				ii--;
				jj--;
				flag2++;
				containerrorlength2++;
				if (flag2 > 15)
					break;
			}
		}

		if (containerror2 < 50) {
			ii = ii + containerrorlength2;
			jj = jj + containerrorlength2;
		}
		System.out.println(textContent.substring(i, ii));
		System.out.println(textContent2.substring(j, jj));
	}

	/**
	 * according to Document find Chapter Link by a tag
	 */
	public List<String> getChapterLinks(Document doc) {

		List<String> tmp = new ArrayList<String>();
		Elements alinks = doc.select("a");
		if (alinks != null && !alinks.isEmpty()) {
			for (int i = 0; i < alinks.size(); i++) {
				Element element = alinks.get(i);
				if (i == alinks.size() - 1) {
					System.out.println();
				}
				String linkText = element.text();
				if (ConfigUtil.chapterLinkPattern.matcher(linkText).find()) {
					
					System.out.println(linkText);
//					Matcher matcher = ConfigUtil.chapterLinkPattern.matcher(linkText);
//					if(matcher.find()) {
//						String pattern = matcher.group(0);
//						if(pattern != null && !pattern.isEmpty()) {
//							int pageNum = Util.convertChineseToNum(linkText);
//							if(pageNum > 0) {
//								
//							}
//						}
//					}
					tmp.add(element.absUrl("href"));
				}
			}
		}
		Collections.sort(tmp);
		return tmp;
	}


	public String getContentFromDoc(Document doc, String host) {
		Elements content = null;
		StringBuilder res = new StringBuilder();
		HostSetting hostSetting = pt.getHostSetting(host);
		if (hostSetting != null) {
			String pattern = hostSetting.getChooseContentPattern();
			content = doc.select(pattern);
			if (content != null && !content.isEmpty()) {
				List<Node> listnodes = content.get(0).childNodes();
				for (int i = 0;i< listnodes.size() - 1; i++) {
					 Node result = listnodes.get(i);
					 if(result instanceof TextNode  ) {
						 String texttmp = ((TextNode)result).toString().replace("&nbsp;", "");
						 res.append(texttmp);
						 res.append("\n");
					 }
					 if(result instanceof  Element && ((Element) result).tagName().equals("p") ) {
						 String texttmp = ((Element)result).toString().replace("&nbsp;", "");
						 res.append(texttmp);
						 res.append("\n");
					 }
				}
			}else {
				log.warn(host +" Pattern setting no content elements {}",doc.baseUri());

			}
		}else {
			throw new IllegalAccessError("No Pattern setting for: "+ host);
		}
		return res.toString();
	}


	/**
	 * according to Document find Content Div
	 * @param host 
	 * 
	 * @return
	 */
	public Node getContentDivHtmlElement(Document doc, String host) {
		Elements content = null;
		HostSetting hostSetting = pt.getHostSetting(host);
		if (hostSetting != null) {
			String pattern = hostSetting.getChooseContentPattern();
			content = doc.select(pattern);
			if (content != null && !content.isEmpty()) {
				//				filterResult(content.get(0),"div");
				return content.get(0);
			}
		}

		if (content == null || content.isEmpty()) {
			content = doc.select("div[id=\"content\"]");
		}

		if (content == null || content.isEmpty()) {
			content = doc.select("div[id=\"booktext\"]");
		}

		// 情况： https://www.x23us.com/html/72/72784/32647450.html
		if (content == null || content.isEmpty()) {
			content = doc.select("dd[id=contents]");
		}

		// 情况：https://www.mkxs8.com/267/267529/57141436.html
		if (content == null || content.isEmpty()) {
			content = doc.select("div[id=\"text_area\"]");
		}

		// 情况：https://www.mkxs8.com/267/267529/57141436.html
		if (content == null || content.isEmpty()) {
			content = doc.select("div[id=\"view_content_txt\"]");
		}

		// 情况：https://www.mkxs8.com/267/267529/57141436.html
		if (content == null || content.isEmpty()) {
			content = doc.select("div[id=\"contentbox\"]");
		}

		if (content != null && !content.isEmpty()) {
			return content.get(0);
		}

		// 情况：https://www.ptwxz.com/html/6/6035/3314738.html
		if (content == null || content.isEmpty()) {
			content = doc.select("body");
			StringBuilder builder = new StringBuilder();
			for (Node element : content.get(0).childNodes()) {
				if (element instanceof TextNode && StringUtils.isNoneBlank(((TextNode) element).text())) {
					builder.append(((TextNode) element).text() + "\n\n");
				}
			}

			// https://www.danmei.la/book/11/11764/1280527.html
			if (builder.toString() == null || builder.toString().equals("")) {
				content = doc.select("p[class=\"Text\"]");
				if (!content.isEmpty()) {
					for (Node element : content.get(0).childNodes()) {
						if (element instanceof TextNode && StringUtils.isNoneBlank(((TextNode) element).text())) {
							builder.append(((TextNode) element).text() + "\n\n");
						}
					}
				}
			}
			return new TextNode(builder.toString(), doc.baseUri());
		}

		return null;
	}


	/**
	 * calculate the content pattern
	 */
	public void comparePattern(Elements html1, Elements html2) {
		for (int i = 0; i < html1.size(); i++) {
			Element index = html1.get(i);
			Element index2 = html2.get(i);
			System.out.println(index);
			System.out.println(index2);
		}

	}

	public Element getTitleDiv(Document doc) {
		Elements title = doc.select("title");
		if (title != null && !title.isEmpty()) {
			return title.get(0);
		}
		return null;
	}
	
	public String getTitleFromSetting(Document doc, String host) {
		Elements titleElement = null;
		HostSetting hostSetting = pt.getHostSetting(host);
		if (hostSetting != null) {
			String pattern = hostSetting.getChooseTitlePattern();
			titleElement = doc.select(pattern);
			if (titleElement != null && !titleElement.isEmpty()) {
				return titleElement.get(0).ownText();
			}
		}
		return null;
	}

	/**
	 * 文件的大小
	 */
	public static long getDirSize(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				long size = 0;
				for (File f : children)
					size += getDirSize(f);
				return size;
			} else {// 如果是文件则直接返回其大小,以“兆”为单位
				return file.length();
			}
		} else {
			log.info("文件或者文件夹:{} 不存在，请检查路径是否正确！", file.getAbsolutePath());
			return 0L;
		}
	}

	public static void ensureSpace() {
		File file = new File(ConfigUtil.fileStorePath);
		long tmpsize = getDirSize(file);
		if (tmpsize >= ConfigUtil.fileStoreMaxSizePath) {
			File[] children = file.listFiles();
			Arrays.parallelSort(children, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return (int) (o1.lastModified() - o2.lastModified());
				}
			});

			// ensure length
			if (children.length > 0) {
				children[0].delete();
			}

			ensureSpace();
		}
		return;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//		Util.getInstance().ensureSpace();
		//		System.out.println(Util.convertChineseToNum("二"));
		//		System.out.println(Util.convertChineseToNum("十"));
		//		System.out.println(Util.convertChineseToNum("十二"));
		//		System.out.println(Util.convertChineseToNum("二十二"));
		//		System.out.println(Util.convertChineseToNum("三十"));
		//		System.out.println(Util.convertChineseToNum("二百零二"));
		//		System.out.println(Util.convertChineseToNum("二百二十二"));
		//		System.out.println(Util.convertChineseToNum("五百三十二"));
		//		System.out.println(Util.convertChineseToNum("一千五百三十二"));
		//		System.out.println(Util.convertChineseToNum("一千零二"));
		//		System.out.println(Util.convertChineseToNum("一万一千零二十二"));
		//		System.out.println(Util.convertChineseToNum("一万零一十二"));

		String value = "<!--over-->\n" + " <br>\n" + " <br>★★\n" + " <a href=\"http://www.pfwx.com/\">平凡文学</a>★★ 如果觉得\n"
				+ "手机用户请到\n" + " https://m.33zw.com/xiaoshuo/154621/阅读最新章节\n" + "\n"
				+ " https://www.33zw.com/xiaoshuo/154621/，谢谢您的支持！！\n" + ""
				+ " <a href=\"http://www.pfwx.com/wozhenbushishenxian/\">我真不是神仙</a>好看，请把本站网址推荐给您的朋友吧！ \n";
		System.out.println(Util.getInstance().filter(value));

	}
}
