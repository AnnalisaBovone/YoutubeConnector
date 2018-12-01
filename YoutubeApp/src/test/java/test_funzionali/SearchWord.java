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
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SearchWord {
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
	  public void testSearchWord() throws Exception {
	    driver.get("http://localhost:8080/pages/infoVideo.html?ID=jWYPs_rIKaQ");
	    driver.findElement(By.id("research-word")).click();
	    Thread.sleep(1000);
	    driver.findElement(By.id("input-search")).click();
	    driver.findElement(By.id("input-search")).clear();
	    driver.findElement(By.id("input-search")).sendKeys("buona");
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Commenti'])[1]/following::img[1]")).click();
	    Thread.sleep(1000);
	    assertEquals("buona", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Commenti'])[1]/following::span[1]")).getText());
	    assertEquals("Buona brava😇😇😇", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='buona'])[1]/following::p[4]")).getText());
	    assertEquals("Bellissima idea. Buona e veloce. Grazie", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Buona'])[1]/following::p[4]")).getText());
	    assertEquals("WOW CHE MERAVIGLIA! ADORO CIOCCOLATO E MENTA! COME NON FARE QUESTA DELIZIA! CARISSIMA UCCIA,SEI SEMPRE FANTASTICA! GRAZIE MILLE PER LE TUE SPLENDIDE RICETTE! BUONA GIORNATA E A PRESTO! UN FORTISSIMO ABBRACCIO E BACIONI CIAO! 🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🌹🏆😘😘😘😘😘💌💞💖💓❤💗😉😃😃😃😃", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Buona'])[2]/following::p[4]")).getText());
	  }
	
	@Test
	  public void testSearchWordNoComment() throws Exception {
	    driver.get("http://localhost:8080/pages/infoVideo.html?ID=jWYPs_rIKaQ");
	    driver.findElement(By.id("research-word")).click();
	    driver.findElement(By.id("input-search")).click();
	    driver.findElement(By.id("input-search")).clear();
	    driver.findElement(By.id("input-search")).sendKeys("equazioni differenziali");
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
