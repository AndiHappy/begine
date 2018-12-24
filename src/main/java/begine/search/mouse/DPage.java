package begine.search.mouse;


public class DPage extends Page {


	private DirectoryPage parent = null;
	
	private volatile String textContent = null;

	private volatile String title = null;
	
	public DPage(String url,DirectoryPage parent) {
		super(url);
		setParent(parent);
	}

	public String text() {
		LoadThreadPoolUtil.waitLoadDoc(this,60);
		return this.doc.text();
	}

	public DirectoryPage getParent() {
		return parent;
	}

	public void setParent(DirectoryPage parent) {
		this.parent = parent;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "DPage [url=" + url + "]";
	}
	
}