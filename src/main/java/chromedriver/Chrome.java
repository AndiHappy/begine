package chromedriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * 另外的思路
 * */
public class Chrome {
	
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "/Users/guizhai/Documents/chromedriver/chromedriver");
		Chrome rome = new Chrome();
		rome.testGoogleSearch();
	}
	
	public void testGoogleSearch() throws InterruptedException {
		// Optional, if not specified, WebDriver will search your path for chromedriver.
		ChromeDriver driver = new ChromeDriver();
		driver.get("https://www.fpzw.com/xiaoshuo/105/105098/");
//		Thread.sleep(5000); // Let the user actually see something!
		WebElement searchBox = driver.findElementByTagName("dd");
		searchBox.sendKeys("ChromeDriver");
		searchBox.submit();
		Thread.sleep(5000); // Let the user actually see something!
		driver.quit();
	}

}
