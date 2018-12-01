package test_funzionali;

import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.youtubeException.YoutubeException;
import com.example.youtubeconnector.UpdateVideo;
import com.example.youtubeconnector.YoutubeAnswer;
import com.example.youtubeconnector.YoutubeAppApplication;
import com.example.youtubeconnector.YoutubeChannel;
import com.example.youtubeconnector.YoutubeChannelRepository;
import com.example.youtubeconnector.YoutubeComment;
import com.example.youtubeconnector.YoutubeCommentRepository;
import com.example.youtubeconnector.YoutubeConnector;
import com.example.youtubeconnector.YoutubeVideo;
import com.example.youtubeconnector.YoutubeVideoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = YoutubeAppApplication.class)
@DataMongoTest
public class ChooseVideoTest {
	private static ChromeDriverService service;
	  private WebDriver driver;
	  private boolean acceptNextAlert = true;
	  @Autowired
		public MongoTemplate mongoTemplate;
	  @Autowired
		public YoutubeVideoRepository youtubeVideoRepository;
		@Autowired
		public YoutubeChannelRepository youtubeChannelRepository;
		@Autowired
		public YoutubeCommentRepository youtubeCommentRepository;

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
	  
	  @Before
	  public void createDatabase() throws YoutubeException, InterruptedException {
		  mongoTemplate.dropCollection(YoutubeVideo.class);
		  mongoTemplate.dropCollection(YoutubeChannel.class);
		  mongoTemplate.dropCollection(YoutubeComment.class);
		  
		  String json = YoutubeConnector.jsonGetRequest("http://localhost:8080/createDb/Video1.json", "");
		  YoutubeVideo video1 = new YoutubeVideo(json);
		  YoutubeChannel channel1 = new YoutubeChannel(json);
		  UpdateVideo update1 = new UpdateVideo(json);
		  video1.addUpdate(update1);
		  channel1.addVideo(video1.getVideoId());
		  json = YoutubeConnector.jsonGetRequest("http://localhost:8080/createDb/Channel1.json", "");
		  channel1.setSubscribers(json);
		  json = YoutubeConnector.jsonGetRequest("http://localhost:8080/createDb/Comment1.json", "");
		  ArrayList<YoutubeComment> comments1 = YoutubeComment.commentsParser(json, video1.getVideoId());
		  json = YoutubeConnector.jsonGetRequest("http://localhost:8080/createDb/Answer1.json", "");
		  ArrayList<YoutubeAnswer> answers1 = YoutubeAnswer.answersParser(json, "UgyC2Nozv0K5m8SN4LN4AaABAg");
		  json = YoutubeConnector.jsonGetRequest("http://localhost:8080/createDb/Answer1.json", "");
		  ArrayList<YoutubeAnswer> answers2 = YoutubeAnswer.answersParser(json, "Ugy1IwqTLYC8DVOIc114AaABAg");
		  json = YoutubeConnector.jsonGetRequest("http://localhost:8080/createDb/Answer1.json", "");
		  ArrayList<YoutubeAnswer> answers3 = YoutubeAnswer.answersParser(json, "UgxkklaKVLoDEX7jq5p4AaABAg");
		  for(int i=0; i<comments1.size(); i++) {
			  video1.addComment(comments1.get(i).getCommentId());
		  }
		  youtubeVideoRepository.save(video1);
		  youtubeChannelRepository.save(channel1);
		  youtubeCommentRepository.saveAll(comments1);
		  Optional<YoutubeComment> optionalComment1 = youtubeCommentRepository.findById("UgyC2Nozv0K5m8SN4LN4AaABAg");
		  if(optionalComment1.isPresent()) {
			  YoutubeComment comment1 = optionalComment1.get();
			  comment1.setAnswer(answers1);
			  youtubeCommentRepository.save(comment1);
		  }
		  Optional<YoutubeComment> optionalComment2 = youtubeCommentRepository.findById("Ugy1IwqTLYC8DVOIc114AaABAg");
		  if(optionalComment2.isPresent()) {
			  YoutubeComment comment2 = optionalComment2.get();
			  comment2.setAnswer(answers2);
			  youtubeCommentRepository.save(comment2);
		  }
		  Optional<YoutubeComment> optionalComment3 = youtubeCommentRepository.findById("UgxkklaKVLoDEX7jq5p4AaABAg");
		  if(optionalComment3.isPresent()) {
			  YoutubeComment comment3 = optionalComment3.get();
			  comment3.setAnswer(answers3);
			  youtubeCommentRepository.save(comment3);
		  }
		  
		  
		  
		  json = YoutubeConnector.jsonGetRequest("http://localhost:8080/createDb/Video2.json", "");
		  YoutubeVideo video2 = new YoutubeVideo(json);
		  YoutubeChannel channel2 = new YoutubeChannel(json);
		  UpdateVideo update2 = new UpdateVideo(json);
		  video2.addUpdate(update2);
		  channel2.addVideo(video2.getVideoId());
		  json = YoutubeConnector.jsonGetRequest("http://localhost:8080/createDb/Channel2.json", "");
		  channel2.setSubscribers(json);
		  json = YoutubeConnector.jsonGetRequest("http://localhost:8080/createDb/Comment2.json", "");
		  ArrayList<YoutubeComment> comments2 = YoutubeComment.commentsParser(json, video1.getVideoId());
		  for(int i=0; i<comments2.size(); i++) {
			  video2.addComment(comments2.get(i).getCommentId());
		  }
		  youtubeVideoRepository.save(video2);
		  youtubeChannelRepository.save(channel2);
		  youtubeCommentRepository.saveAll(comments2);
		  
	  }

	  @After
	  public void quitDriver() {
	    driver.quit();
	  }


	  @Test
	  public void testChooseVideo() throws Exception {
	    driver.get("http://localhost:8080/pages/index.html");
	    Thread.sleep(1000);
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='TORTA FREDDA MENTA e CIOCCOLAT...'])[1]/following::img[1]")).click();
	    Thread.sleep(1000);
	    assertEquals("TORTA FREDDA MENTA e CIOCCOLATO, Ricetta Facile Senza Cottura", driver.findElement(By.id("titleVideo")).getText());
	    assertEquals("uccia3000", driver.findElement(By.id("titleChannel")).getText());
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
