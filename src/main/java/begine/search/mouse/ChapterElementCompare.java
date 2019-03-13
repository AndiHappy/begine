package begine.search.mouse;

import java.util.Comparator;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChapterElementCompare implements Comparator<Element> {

	public static final Logger log = LoggerFactory.getLogger(ChapterElementCompare.class);

	//比较大小
	@Override
	public int compare(Element o1, Element o2) {
		if (o1 != null && o2 != null) {
			int int1 = Util.getElementNumber(o1);
			int int2 = Util.getElementNumber(o2);
			String optional1 = o1.absUrl("href");
			String optional2 = o2.absUrl("href");

			if (Math.abs((int2 - int1)) > 1) {
				return compareBaseUrl(optional1, optional2);
			}
			if (int1 > int2) {
				return 1;
			} else if (int1 == int2) {
				return compareBaseUrl(optional1, optional2);
			} else {
				return -1;
			}
		}
		return 0;
	}

	/**
	 * @param o1
	 * @param o2
	 * @param value1
	 * @param value2
	 * @return
	 */
	private int compareBaseUrl(String urlindex1, String urlindex2) {
		int urlindexb = urlindex1.lastIndexOf("/");
		int urlindexe = urlindex1.lastIndexOf(".");
		urlindex1 = urlindex1.substring(urlindexb + 1, urlindexe);

		urlindexb = urlindex2.lastIndexOf("/");
		urlindexe = urlindex2.lastIndexOf(".");
		urlindex2 = urlindex2.substring(urlindexb + 1, urlindexe);

		int int11 = Integer.parseInt(urlindex1);
		int int21 = Integer.parseInt(urlindex2);
		if (int11 > int21) {
			return 1;
		} else if (int11 < int21) {
			return -1;
		} else {
			return 0;
		}
	}
}
