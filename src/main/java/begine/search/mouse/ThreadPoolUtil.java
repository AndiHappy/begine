package begine.search.mouse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import begine.entity.BookAnylise;

public class ThreadPoolUtil {

	private ThreadPoolUtil(){}
	
	private static ExecutorService pool = Executors.newFixedThreadPool(8);
	
	private static class ThreadPoolUtilHolder{
		private static ThreadPoolUtil instance = new ThreadPoolUtil();
	}
	
	public static ThreadPoolUtil getIns(){
		return ThreadPoolUtilHolder.instance;
	}
	
	public static void load(final BookAnylise bookAnylise, final String newestChapterLink) {
		
//		pool.submit(new Runnable() {
			
//			@Override
//			public void run() {
			if(bookAnylise.getAllChapterLinks().get(newestChapterLink) != null){
				ContentPage contentpage = bookAnylise.getAllChapterLinks().get(newestChapterLink);
				bookAnylise.setNewestChapterContent(contentpage.getContent());
				bookAnylise.setLoad(true);
			}
				
//			}
//		});
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



	

}
