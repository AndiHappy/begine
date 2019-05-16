package begine.search.mouse;

/**
 * @author zhailz
 *
 * @version 2018年8月15日 上午10:17:52
 */

public class HostSetting {

	private String host;
	private String chooseLinkPattern;
	private String chooseTitlePattern;
	private String chooseContentPattern;
	private int priority;
	private int startIndex;
	private int endIndex;

	public HostSetting(String host, String setting) {
		this.host = host;
		String[] sp = setting.split(",");
		if (sp.length > 0) {
			this.chooseLinkPattern = sp[0];
		}
		if(sp.length >1){
			this.chooseContentPattern = sp[1];
		}
		if(sp.length > 2){
			this.priority = Integer.parseInt(sp[2]);
		}
		
		if(sp.length > 3){
			this.chooseTitlePattern = sp[3];
		}
		
		if(sp.length > 4){
			this.startIndex =Integer.parseInt( sp[4]);
		}
		
		if(sp.length > 5){
			this.endIndex = Integer.parseInt(sp[5]);
		}

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getChooseLinkPattern() {
		return chooseLinkPattern;
	}

	public void setChooseLinkPattern(String chooseLinkPattern) {
		this.chooseLinkPattern = chooseLinkPattern;
	}

	public String getChooseContentPattern() {
		return chooseContentPattern;
	}

	public void setChooseContentPattern(String chooseContentPattern) {
		this.chooseContentPattern = chooseContentPattern;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getChooseTitlePattern() {
		return chooseTitlePattern;
	}

	public void setChooseTitlePattern(String chooseTitlePattern) {
		this.chooseTitlePattern = chooseTitlePattern;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

}
