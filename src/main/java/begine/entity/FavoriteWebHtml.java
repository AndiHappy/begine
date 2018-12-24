package begine.entity;

import java.util.List;

public class FavoriteWebHtml {
	
	private String url;
	private String title;
	private long createTime;
	private long delTime;
	private List<String> tags;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getDelTime() {
		return delTime;
	}
	public void setDelTime(long delTime) {
		this.delTime = delTime;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
