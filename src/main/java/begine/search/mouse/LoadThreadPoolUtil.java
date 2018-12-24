package begine.search.mouse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhailz
 *
 * @version 2018年8月15日 上午10:17:52
 */
public class LoadThreadPoolUtil {

	// 等待时间
	public static int waitTimeMils = 500000000;

	private static Logger log = LoggerFactory.getLogger(LoadThreadPoolUtil.class);

	protected static ExecutorService executePool = new ThreadPoolExecutor(24, 24, 60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(5000));

	public static void submit(Runnable task) {
		executePool.execute(task);
	}
	
	/**
	 * 等待一定的时间，放弃
	 */
	public static boolean waitLoadDoc(Page page,int waitTimeSeconds) {
		int waitTime = 0;
		try {
			// 乐观锁的用法
			for (;;) {
				if (page.getDoc() != null || waitTime > waitTimeSeconds*1000) {
					return page.getDoc() != null;
				} else {
					// 等待，直到初始化完成
//					log.info("{},没有加载完成，需要等待...", page.getUrl());
					int waitTimetmp = 500;
					Thread.sleep(waitTimetmp);
					waitTime = waitTime + waitTimetmp;
				}
			}
		} catch (InterruptedException e) {
			log.error("初始化目录出现错误：{}", e);
			return false;
		}
	}

}
