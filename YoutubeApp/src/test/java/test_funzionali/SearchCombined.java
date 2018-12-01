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
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SearchCombined {

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
	  public void testSearchCombined() throws Exception {
	    driver.get("http://localhost:8080/pages/infoVideo.html?ID=jWYPs_rIKaQ");
	    
	    driver.findElement(By.id("research-word")).click();
	    Thread.sleep(1000);
	    driver.findElement(By.id("input-search")).click();
	    driver.findElement(By.id("input-search")).clear();
	    driver.findElement(By.id("input-search")).sendKeys("ciao");
	    
	    driver.findElement(By.id("research-date")).click();
	    Thread.sleep(1000);
	    driver.findElement(By.id("input-from")).click();
	    Thread.sleep(2000);
	    
	    JavascriptExecutor js = null;
	    if (driver instanceof JavascriptExecutor) {
	        js = (JavascriptExecutor)driver;
	    } 
	    
	    js.executeScript("document.getElementById('input-from').value = '2018-09-03T00:00'");
	    js.executeScript("document.getElementById('input-to').value = '2018-09-03T23:59'");

	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Commenti'])[1]/following::img[1]")).click();
	    Thread.sleep(1000);
	    assertEquals("Ricetta vista e stravista, niente di nuovo, anzi direi piuttosto scopiazzata qua e là 🤫🤫🤫", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Commenti'])[1]/following::p[3]")).getText());
	    assertEquals("data pubblicazione: 3/9/2018 ora: 19:53:53", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Commenti'])[1]/following::p[4]")).getText());
	    assertEquals("Ciao favolosa ucciaaaaa😘", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Visualizza risposta'])[1]/following::p[2]")).getText());
	    assertEquals("data pubblicazione: 3/9/2018 ora: 18:39:43", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Ciao'])[1]/following::p[1]")).getText());
	    assertTrue(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Ciao'])[1]/following::p[4]")).getText().matches("^Bravissima Uccia♡ , si puo' sostituire lo sciroppo di menta con quello di amarena o mandorla [\\s\\S]$"));
	    assertEquals("data pubblicazione: 3/9/2018 ora: 17:41:1", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Ciao'])[1]/following::p[5]")).getText());
	    assertEquals("Questa torta e ' buonissima !!! Facile da fare e bella da vedere! !! Complimenti Uccia sei bravissima!!! Ciao un abbraccio!!!♡♥♡♥", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Visualizza risposta'])[2]/following::p[2]")).getText());
	    assertEquals("data pubblicazione: 3/9/2018 ora: 15:45:52", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Ciao'])[2]/following::p[1]")).getText());
	    assertEquals("WOW CHE MERAVIGLIA! ADORO CIOCCOLATO E MENTA! COME NON FARE QUESTA DELIZIA! CARISSIMA UCCIA,SEI SEMPRE FANTASTICA! GRAZIE MILLE PER LE TUE SPLENDIDE RICETTE! BUONA GIORNATA E A PRESTO! UN FORTISSIMO ABBRACCIO E BACIONI CIAO! 🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🏆😘😘😘😘😘💌💞💖💓❤💗😉😃😃😃😃", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Ciao'])[2]/following::p[4]")).getText());
	    assertEquals("data pubblicazione: 3/9/2018 ora: 15:7:19", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='CIAO'])[1]/following::p[1]")).getText());
	 
	  }
	  
	  @Test
	  public void testSearchCombinedNoComment() throws InterruptedException {
		  driver.get("http://localhost:8080/pages/infoVideo.html?ID=jWYPs_rIKaQ");
		    
		    driver.findElement(By.id("research-word")).click();
		    Thread.sleep(1000);
		    driver.findElement(By.id("input-search")).click();
		    driver.findElement(By.id("input-search")).clear();
		    driver.findElement(By.id("input-search")).sendKeys("equazioni");
		    
		    driver.findElement(By.id("research-date")).click();
		    Thread.sleep(1000);
		    driver.findElement(By.id("input-from")).click();
		    Thread.sleep(2000);
		    
		    JavascriptExecutor js = null;
		    if (driver instanceof JavascriptExecutor) {
		        js = (JavascriptExecutor)driver;
		    } 
		    
		    js.executeScript("document.getElementById('input-from').value = '2018-09-01T00:00'");
		    js.executeScript("document.getElementById('input-to').value = '2018-09-01T23:59'");

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
