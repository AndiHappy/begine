package chromedriver;

import begine.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.Arrays;

/**
 * 另外的思路
 * */
public class Chrome {
	
	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "begine/src/main/resource/chromedriver");
		Chrome rome = new Chrome();
		rome.testGoogleSearch();
	}
	
	public void testGoogleSearch() throws Exception {
		String fileName = "2.txt";
		File file = new File(fileName);
		if(file.exists()) {
			file.delete();
		}

		file.createNewFile();


		// Optional, if not specified, WebDriver will search your path for chromedriver.
		ChromeOptions options = new ChromeOptions();
//		options.setExperimentalOption("excludeSwitches", Arrays.asList("disable-popup-blocking"));
		options.addArguments("--headless");
//		options.addArguments("--no-sandbox");//  # 取消沙盒模式
		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

		ChromeDriver driver = new ChromeDriver(options);

		driver.get("https://www.yanqbook.com/5/5030/1507269.html");

		// 首先是拿到content
		WebElement elenet = driver.findElementById("_17mb_content");

		System.out.println(elenet.toString());
		System.out.println(elenet.getText());
		String links = driver.findElementByLinkText("下一页").getAttribute("href");
		if(StringUtils.isNotBlank(links)){
			while(true){
				boolean contine = false;
				try{
					WebElement value = driver.findElementByLinkText("下一页");
					System.out.println(value.isEnabled());
					driver.findElementByLinkText("下一页").click();
					boolean flag = true;
					while(flag){
						elenet = driver.findElementById("_17mb_content");
						String text = elenet.getText();
						if(text.startsWith("正在加载")){
							Thread.sleep(50);
						}else {
							flag=false;
							FileUtil.instanct().saveValueToFile(file, text, true);
						}
					}

					System.out.println("加载了："+ driver.getTitle());
				}catch (Exception e){
					contine = true;
				}

				if(contine){
					try{
						WebElement value = driver.findElementByLinkText("下一章");
						System.out.println(value.isEnabled());
						driver.findElementByLinkText("下一章").click();
						elenet = driver.findElementById("_17mb_content");
						boolean flag = true;
						while(flag){
							elenet = driver.findElementById("_17mb_content");
							String text = elenet.getText();
							if(text.startsWith("正在加载")){
								Thread.sleep(50);
							}else {
								flag=false;
								FileUtil.instanct().saveValueToFile(file, text, true);
							}
						}

						System.out.println("加载了："+ driver.getTitle());
					}catch (Exception e){
						//
					}
				}
			}
		}


		driver.quit();
	}

}
