package test_funzionali;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SearchIntervalTime {
	private static ChromeDriverService service;
	  private WebDriver driver;
	  private boolean acceptNextAlert = true;
	  
	  @BeforeClass
	  public static void createAndStartService() throws IOException {
	    service = new ChromeDriverService.Builder()
	        .usingDriverExecutable(new File("lib/windows/chromedriver.exe"))
	        .usingAnyFreePort()
	        .build();
	    service.start();
	  }

	  @AfterClass
	  public static void createAndStopService() {
	    service.stop();
	  }

	  @Before
	  public void createDriver() {
	    driver = new RemoteWebDriver(service.getUrl(),
	        DesiredCapabilities.chrome());
	  }
	  
	  @After
	  public void quitDriver() {
	    driver.quit();
	  }


	  @Test
	  public void testSearchIntervalTime() throws Exception {
	    driver.get("http://localhost:8080/pages/infoVideo.html?ID=jWYPs_rIKaQ");
	    driver.findElement(By.id("research-date")).click();
	    Thread.sleep(1000);
	    driver.findElement(By.id("input-from")).click();
	    Thread.sleep(2000);
	    
	    JavascriptExecutor js = null;
	    if (driver instanceof JavascriptExecutor) {
	        js = (JavascriptExecutor)driver;
	    } 
	    
	    js.executeScript("document.getElementById('input-from').value = '2018-09-04T00:00'");
	    js.executeScript("document.getElementById('input-to').value = '2018-09-04T23:59'");

	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Commenti'])[1]/following::img[1]")).click();
	    Thread.sleep(1000);
	    assertEquals("data pubblicazione: 4/9/2018 ora: 22:59:43", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Commenti'])[1]/following::p[4]")).getText());
	    assertEquals("data pubblicazione: 4/9/2018 ora: 16:50:30", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Visualizza risposta'])[1]/following::p[3]")).getText());
	    assertEquals("data pubblicazione: 4/9/2018 ora: 14:38:44", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Visualizza risposta'])[1]/following::p[7]")).getText());
	    assertEquals("data pubblicazione: 4/9/2018 ora: 8:32:39", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Visualizza risposta'])[1]/following::p[11]")).getText());
	  }
	  
	  @Test
	  public void testSearchIntervalTimeNoComment() throws Exception {
	    driver.get("http://localhost:8080/pages/infoVideo.html?ID=jWYPs_rIKaQ");
	    driver.findElement(By.id("research-date")).click();
	    Thread.sleep(1000);
	    driver.findElement(By.id("input-from")).click();
	    Thread.sleep(2000);
	    
	    JavascriptExecutor js = null;
	    if (driver instanceof JavascriptExecutor) {
	        js = (JavascriptExecutor)driver;
	    } 
	    
	    js.executeScript("document.getElementById('input-from').value = '2018-08-20T00:00'");
	    js.executeScript("document.getElementById('input-to').value = '2018-08-20T23:59'");

	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Commenti'])[1]/following::img[1]")).click();
	    Thread.sleep(1000);
	    
	    assertEquals("Non ci sono commenti", driver.findElement(By.id("nocomment")).getText());
	  }
	
	private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }

	  private boolean isAlertPresent() {
	    try {
	      driver.switchTo().alert();
	      return true;
	    } catch (NoAlertPresentException e) {
	      return false;
	    }
	  }

	  private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      String alertText = alert.getText();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alertText;
	    } finally {
	      acceptNextAlert = true;
	    }
	  }

}
