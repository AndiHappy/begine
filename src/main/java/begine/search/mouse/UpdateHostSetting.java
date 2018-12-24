package begine.search.mouse;

import java.util.ArrayList;
import java.util.List;

public class UpdateHostSetting extends Thread {

	private List<DPage> pages;
	
	public UpdateHostSetting(List<DPage> pages2) {
		List<DPage> pages = new ArrayList<>();
		for (DPage dPage : pages2) {
			if(Util.getInstance().judgeIsRealDirectoryPage(dPage)){
				pages.add(dPage);
			}
		}
		
		setPages(pages);
		
	}
	
	
	@Override
	public void run() {
		//TODO 根据取到的目录数，更新设置Host的设置
		
	}
	public List<DPage> getPages() {
		return pages;
	}

	public void setPages(List<DPage> pages) {
		this.pages = pages;
	}

}
