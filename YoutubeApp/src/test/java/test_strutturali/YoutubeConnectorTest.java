package test_strutturali;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;
import com.example.youtubeconnector.RequestType;
import com.example.youtubeconnector.YoutubeAnswer;
import com.example.youtubeconnector.YoutubeAppApplication;
import com.example.youtubeconnector.YoutubeChannel;
import com.example.youtubeconnector.YoutubeChannelRepository;
import com.example.youtubeconnector.YoutubeComment;
import com.example.youtubeconnector.YoutubeCommentRepository;
import com.example.youtubeconnector.YoutubeConnector;
import com.example.youtubeconnector.YoutubeConnectorConfig;
import com.example.youtubeconnector.YoutubeConnectorController;
import com.example.youtubeconnector.YoutubeVideo;
import com.example.youtubeconnector.YoutubeVideoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = YoutubeAppApplication.class)
@DataMongoTest
public class YoutubeConnectorTest {
	
	private YoutubeConnector ytConnector;
	@Autowired
	public MongoTemplate mongoTemplate;
	@Autowired
	public YoutubeVideoRepository youtubeVideoRepository;
	@Autowired
	public YoutubeChannelRepository youtubeChannelRepository;
	@Autowired
	public YoutubeCommentRepository youtubeCommentRepository;
	
	public YoutubeConnectorConfig youtubeConnectorConfig;
	public List<String> listVideoId;
	
	@Before
	public void setUpBeforeClass() throws YoutubeException, JSONException {
		mongoTemplate.dropCollection(YoutubeVideo.class);
		mongoTemplate.dropCollection(YoutubeChannel.class);
		mongoTemplate.dropCollection(YoutubeComment.class);
		
		String json = "";
		List<YoutubeVideo> videos = new ArrayList<YoutubeVideo>();
		json = YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Video.json", "");
		YoutubeVideo video1 = new YoutubeVideo(json);
		videos.add(video1);
		youtubeVideoRepository.saveAll(videos);
		
		List<YoutubeChannel> channels = new ArrayList<YoutubeChannel>();
		json = YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Video.json", "");
		YoutubeChannel channel1 = new YoutubeChannel(json);
		channels.add(channel1);
		youtubeChannelRepository.saveAll(channels);
		
		List<YoutubeComment> comments = new ArrayList<YoutubeComment>();
		json = YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Comment.json", "");
		YoutubeComment comment1 = new YoutubeComment(json, 0);
		comments.add(comment1);
		youtubeCommentRepository.saveAll(comments);
		
		youtubeConnectorConfig = new YoutubeConnectorConfig();
		String[] urls = {"https://www.youtube.com/watch?v=9JYPTJ4dSYQ"};
		youtubeConnectorConfig.setUrl(urls);
		Calendar cal = Calendar.getInstance(); 
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		String hour = hours + ":" + minutes;
		youtubeConnectorConfig.setTime(hour);
		YoutubeConnectorConfig.sentimentKey = "MTgzMTIzNTctMDdERi00NzRDLThFNzEtNDFCQzMzNTlFNDc1";
		YoutubeConnectorConfig.youtubeKey = "AIzaSyDIT4EW90s26Kd2X8EDbNm6pTmNU05EYJo";
		
		this.ytConnector = new YoutubeConnector(youtubeChannelRepository, youtubeVideoRepository, youtubeCommentRepository, youtubeConnectorConfig);
		this.listVideoId = new ArrayList<String>();
		listVideoId.add("9JYPTJ4dSYQ");
		this.ytConnector.setListVideoId(listVideoId);
		this.ytConnector.setTestMode(true);
	}
	
	@Test
	public void youtubeConnectorTest(){
		assertEquals(ytConnector.getYoutubeChannelRepository(), youtubeChannelRepository);
		assertEquals(ytConnector.getYoutubeVideoRepository(), youtubeVideoRepository);
		assertEquals(ytConnector.getYoutubeCommentRepository(), youtubeCommentRepository);
		assertEquals(ytConnector.getListVideoId(), listVideoId);
		assertEquals(ytConnector.isTestMode(), true);
		assertEquals(ytConnector.getYoutubeConnectorConfig(), youtubeConnectorConfig);
	}
	
	@Test
	public void getterTest(){
		assertEquals(ytConnector.getYoutubeChannelRepository(), youtubeChannelRepository);
		assertEquals(ytConnector.getYoutubeVideoRepository(), youtubeVideoRepository);
		assertEquals(ytConnector.getYoutubeCommentRepository(), youtubeCommentRepository);
		assertEquals(ytConnector.getYoutubeConnectorConfig(), youtubeConnectorConfig);
		assertEquals(ytConnector.getListVideoId(), listVideoId);
		assertEquals(ytConnector.isTestMode(), true);
		
	}
	
	@Test
	public void jsonGetRequestTest() {
		try {
			YoutubeConnector.jsonGetRequest("http://localhost:8080/test/PageNotFound.json", "body");
		} catch (YoutubeException e) {
			assertEquals(e.getMessage(), "Connection failed: http://localhost:8080/test/PageNotFound.json");
			assertEquals(e.getErrorCode(), ErrorCode.ConnectionError);
		}
	}
	
	@Test
	public void createRequestTest() {
		YoutubeConnectorConfig.sentimentKey = "MTgzMTIzNTctMDdERi00NzRDLThFNzEtNDFCQzMzNTlFNDc1";
		YoutubeConnectorConfig.youtubeKey = "AIzaSyDIT4EW90s26Kd2X8EDbNm6pTmNU05EYJo";
		String requestVideo = YoutubeConnector.createRequest("idVideo", RequestType.Video, "");
		assertEquals(requestVideo, "https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&id=idVideo&key=AIzaSyDIT4EW90s26Kd2X8EDbNm6pTmNU05EYJo");
		String requestChannel = YoutubeConnector.createRequest("idChannel", RequestType.Channel, "");
		assertEquals(requestChannel, "https://www.googleapis.com/youtube/v3/channels?part=snippet,contentDetails,statistics&id=idChannel&key=AIzaSyDIT4EW90s26Kd2X8EDbNm6pTmNU05EYJo");
		String requestComment = YoutubeConnector.createRequest("idVideo", RequestType.Comments, "");
		assertEquals(requestComment, "https://www.googleapis.com/youtube/v3/commentThreads?part=snippet&videoId=idVideo&order=time&key=AIzaSyDIT4EW90s26Kd2X8EDbNm6pTmNU05EYJo");
		String requestAnswer = YoutubeConnector.createRequest("idComment", RequestType.Answers, "");
		assertEquals(requestAnswer, "https://www.googleapis.com/youtube/v3/comments?part=snippet&parentId=idComment&key=AIzaSyDIT4EW90s26Kd2X8EDbNm6pTmNU05EYJo");
		String requestSentiment = YoutubeConnector.createRequest("", RequestType.SentimentEngine, "testo del commento");
		assertEquals(requestSentiment, "https://webapp.app2check.com/api/analyze?apiKey=MTgzMTIzNTctMDdERi00NzRDLThFNzEtNDFCQzMzNTlFNDc1&language=it&split=false");
		String requestDefault = YoutubeConnector.createRequest("", RequestType.Default, "");
		assertEquals(requestDefault, null);
	}
	
	@Test
	public void sleepTest() {
		long start = System.currentTimeMillis();
		assertEquals(ytConnector.sleep(1), true);
		long stop = System.currentTimeMillis();
		assertEquals(stop-start > 59900, true);
		assertEquals(stop-start < 60100, true);
	}
	
	@Test
	public void loadVideoId() {
		String[] urls = {"https://www.youtube.com/watch?v=ID-VIDEO-1", "https://www.youtube.com/watch?v=ID-VIDEO-2&t=6s", "https://www.youtube.com/watch?v=ID-VIDEO-3"};
		List<String> ids = new ArrayList<String>();
		ids.add("ID-VIDEO-1");
		ids.add("ID-VIDEO-2");
		ids.add("ID-VIDEO-3");
		this.ytConnector.loadVideoId(urls);
		List<String> videoIds = this.ytConnector.getListVideoId();
		assertEquals(videoIds, ids);
	}
	
	@Test
	public void setDataTest() {
		String[] data = YoutubeConnector.setData("aaaa-MM-ggThh:mm:ss.000Z");
		assertArrayEquals(data, null);
	}
	
	@Test
	public void setDataTest1() {
		String[] data = YoutubeConnector.setData("2018-8-30T18:55:43.000Z");
		String[] realData = {"30/8/2018", "20:55:43", "1535655343000"};
		assertArrayEquals(data, realData);
	}
	
	@Test
	public void timeConverterTest() {
		Time time = YoutubeConnector.timeConverter("PatternSbagliato");
		assertEquals(time, null);
	}
	
	@Test
	public void timeConverterTest1() {
		Time time = YoutubeConnector.timeConverter("PT4M6S");
		Time realTime = new Time(0, 4, 6);
		assertEquals(time, realTime);
	}
	
	@Test
	public void timeConverterTest2() {
		Time time = YoutubeConnector.timeConverter("PT1H4M6S");
		Time realTime = new Time(1, 4, 6);
		assertEquals(time, realTime);
	}
	
	@Test
	public void timeConverterTest3() {
		Time time = YoutubeConnector.timeConverter("PT1H6S");
		Time realTime = new Time(1, 0, 6);
		assertEquals(time, realTime);
	}
	
	@Test
	public void timeConverterTest4() {
		Time time = YoutubeConnector.timeConverter("PT1H5M");
		Time realTime = new Time(1, 5, 0);
		assertEquals(time, realTime);
	}
	
	@Test
	public void inTimeTest1() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 45);
		assertTrue(ytConnector.inTime(cal, "0:30"));
	}
	
	@Test
	public void inTimeTest2() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 45);
		assertFalse(ytConnector.inTime(cal, "0:20"));
	}
	
	@Test
	public void inTimeTest3() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 10);
		assertFalse(ytConnector.inTime(cal, "0:20"));
	}
	
	@Test
	public void saveCommentTestValidInput() {
		YoutubeComment ytComment = new YoutubeComment("commentId", "author", "authorId", "bellissimo video", "publishedAtDate",
				"publishedAtTime", 1534674000L, 1534674000L, 10, -1, -1);
		YoutubeAnswer ytAnswer = new YoutubeAnswer("answerId", "author", "authorId", "bellissimo video", "publishedAtDate",
				"publishedAtTime", 1534674000L, 1534674000L, 10, -1, -1);
		ytComment.addAnswer(ytAnswer);
		youtubeCommentRepository.save(ytComment);
		ytConnector.saveRatingForComment();
		Optional<YoutubeComment> optionalComment = youtubeCommentRepository.findById("commentId");
		if(optionalComment.isPresent()) {
			YoutubeComment comment = optionalComment.get();
			YoutubeAnswer answer = comment.getAnswer().get(0);
			assertEquals(comment.getRating(), 5);
			assertEquals(comment.getConfidence(), 1, 0.01);
			assertEquals(answer.getRating(), 5);
			assertEquals(answer.getConfidence(), 1, 0.01);
		}
	}
	
	@Test
	public void saveCommentTestValidInputRatingSet() {
		YoutubeComment ytComment = new YoutubeComment("commentId", "author", "authorId", "non so se mi piace o meno", "publishedAtDate",
				"publishedAtTime", 1534674000L, 1534674000L, 10, 2, 0.9);
		YoutubeAnswer ytAnswer = new YoutubeAnswer("answerId", "author", "authorId", "non so se mi piace o meno", "publishedAtDate",
				"publishedAtTime", 1534674000L, 1534674000L, 10, 2, 0.9);
		ytComment.addAnswer(ytAnswer);
		youtubeCommentRepository.save(ytComment);
		ytConnector.saveRatingForComment();
		Optional<YoutubeComment> optionalComment = youtubeCommentRepository.findById("commentId");
		if(optionalComment.isPresent()) {
			YoutubeComment comment = optionalComment.get();
			YoutubeAnswer answer = comment.getAnswer().get(0);
			assertEquals(comment.getRating(), 2);
			assertEquals(comment.getConfidence(), 0.9, 0.01);
			assertEquals(answer.getRating(), 2);
			assertEquals(answer.getConfidence(), 0.9, 0.01);
		}
	}
	
	@Test
	public void saveCommentTestInvalidInput() {
		YoutubeComment ytComment = new YoutubeComment("commentId", "author", "authorId", "text\\invalid", "publishedAtDate",
				"publishedAtTime", 1534674000L, 1534674000L, 10, -1, -1);
		YoutubeAnswer ytAnswer = new YoutubeAnswer("answerId", "author", "authorId", "text\\invalid", "publishedAtDate",
				"publishedAtTime", 1534674000L, 1534674000L, 10, -1, -1);
		ytComment.addAnswer(ytAnswer);
		youtubeCommentRepository.save(ytComment);
		ytConnector.saveRatingForComment();
		Optional<YoutubeComment> optionalComment = youtubeCommentRepository.findById("commentId");
		if(optionalComment.isPresent()) {
			YoutubeComment comment = optionalComment.get();
			YoutubeAnswer answer = comment.getAnswer().get(0);
			assertEquals(comment.getRating(), -1);
			assertEquals(comment.getConfidence(), -1, 0.01);
			assertEquals(answer.getRating(), -1);
			assertEquals(answer.getConfidence(), -1, 0.01);
		}
	}
	
	@Test
	public void saveVideoTestNewVideoId() throws YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/VideoNew.json", "");
		YoutubeVideo ytVideo = ytConnector.saveVideo("new video id", json);
		assertEquals(ytVideo.getVideoId(), "new video id");
		assertEquals(ytVideo.getTitle(), "new video title");
		assertEquals(ytVideo.getChannelId(), "new channel id");
		assertEquals(ytVideo.getPublishedAtDate(), "8/6/2018");
		assertEquals(ytVideo.getPublishedAtTime(), "23:2:4");
		assertEquals(ytVideo.getTimestamp(), 1528491724000L);
		assertEquals(ytVideo.getDuration(), "00:04:06");
		assertEquals(ytVideo.getThumbnails(), "");
		assertEquals(ytVideo.getDescription(), "");
	}
	
	@Test
	public void saveVideoTestOldVideoId() throws YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Video.json", "");
		YoutubeVideo ytVideo = ytConnector.saveVideo(youtubeVideoRepository.findAll().get(0).getVideoId(), json);
		assertEquals(ytVideo.getVideoId(), "9JYPTJ4dSYQ");
		assertEquals(ytVideo.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(ytVideo.getChannelId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytVideo.getPublishedAtDate(), "8/6/2018");
		assertEquals(ytVideo.getPublishedAtTime(), "23:2:4");
		assertEquals(ytVideo.getTimestamp(), 1528491724000L);
		assertEquals(ytVideo.getDuration(), "00:04:06");
		assertEquals(ytVideo.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(ytVideo.getDescription(), "");
	}
	
	@Test
	public void saveVideoTestOldChannelId() throws YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/VideoNewOldChannel.json", "");
		YoutubeVideo ytVideo = ytConnector.saveVideo("new video id", json);
		assertEquals(ytVideo.getVideoId(), "new video id");
		assertEquals(ytVideo.getTitle(), "new video title");
		assertEquals(ytVideo.getChannelId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytVideo.getPublishedAtDate(), "8/6/2018");
		assertEquals(ytVideo.getPublishedAtTime(), "23:2:4");
		assertEquals(ytVideo.getTimestamp(), 1528491724000L);
		assertEquals(ytVideo.getDuration(), "00:04:06");
		assertEquals(ytVideo.getThumbnails(), "");
		assertEquals(ytVideo.getDescription(), "");
	}
	
	@Test
	public void saveCommentTest() throws YoutubeException, JSONException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Comment.json", "");
		List<YoutubeComment> ytComments = ytConnector.saveComment("9JYPTJ4dSYQ", json);
		YoutubeComment ytComment = ytComments.get(0);
		assertEquals(ytComment.getCommentId(), "UgzDUbfqaEILi43pdHJ4AaABAg");
		assertEquals(ytComment.getAuthor(), "Annalisa Bovone");
		assertEquals(ytComment.getAuthorId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytComment.getText(), "commento 1");
		assertEquals(ytComment.getPublishedAtDate(), "30/8/2018");
		assertEquals(ytComment.getPublishedAtTime(), "10:53:35");
		assertEquals(ytComment.getTimestamp(), 1535619215000L);
		assertEquals(ytComment.getUpdatedAt(), 1535619215000L);
		assertEquals(ytComment.getLike(), 1);
		assertEquals(ytComment.getRating(), -1);
		assertEquals(ytComment.getConfidence(), -1, 0.01);
		assertEquals(ytComment.getAnswer(), new ArrayList<YoutubeAnswer>());
	}
	
	@Test
	public void saveCommentTestNoVideoFound() throws YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Comment.json", "");
		List<YoutubeComment> ytComments = ytConnector.saveComment("video id not existing", json);
		assertEquals(ytComments, null);
	}
	
	@Test
	public void saveCommentTestNewComment() throws YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/CommentNew.json", "");
		List<YoutubeComment> ytComments = ytConnector.saveComment("9JYPTJ4dSYQ", json);
		YoutubeComment ytComment = ytComments.get(0);
		assertEquals(ytComment.getCommentId(), "new comment id");
		assertEquals(ytComment.getAuthor(), "Annalisa Bovone");
		assertEquals(ytComment.getAuthorId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytComment.getText(), "commento new");
		assertEquals(ytComment.getPublishedAtDate(), "30/8/2018");
		assertEquals(ytComment.getPublishedAtTime(), "10:53:35");
		assertEquals(ytComment.getTimestamp(), 1535619215000L);
		assertEquals(ytComment.getUpdatedAt(), 1535619215000L);
		assertEquals(ytComment.getLike(), 1);
		assertEquals(ytComment.getRating(), -1);
		assertEquals(ytComment.getConfidence(), -1, 0.01);
		assertEquals(ytComment.getAnswer(), new ArrayList<YoutubeAnswer>());
	}
	
	@Test
	public void saveCommentTestUpdateComment() throws YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/CommentUpdate.json", "");
		List<YoutubeComment> ytComments = ytConnector.saveComment("9JYPTJ4dSYQ", json);
		YoutubeComment ytComment = ytComments.get(0);
		assertEquals(ytComment.getCommentId(), "UgzDUbfqaEILi43pdHJ4AaABAg");
		assertEquals(ytComment.getAuthor(), "Annalisa Bovone");
		assertEquals(ytComment.getAuthorId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytComment.getText(), "commento update");
		assertEquals(ytComment.getPublishedAtDate(), "30/8/2018");
		assertEquals(ytComment.getPublishedAtTime(), "10:53:35");
		assertEquals(ytComment.getTimestamp(), 1535619215000L);
		assertEquals(ytComment.getUpdatedAt(), 1535705615000L);
		assertEquals(ytComment.getLike(), 1);
		assertEquals(ytComment.getRating(), -1);
		assertEquals(ytComment.getConfidence(), -1, 0.01);
		assertEquals(ytComment.getAnswer(), new ArrayList<YoutubeAnswer>());
	}
	
	@Test
	public void saveAnswerTest() throws YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Answer.json", "");
		List<YoutubeAnswer> ytAnswers = ytConnector.saveAnswer("UgzDUbfqaEILi43pdHJ4AaABAg", json);
		YoutubeAnswer ytAnswer = ytAnswers.get(0);
		assertEquals(ytAnswer.getCommentId(), "UgzDUbfqaEILi43pdHJ4AaABAg.8kZiJlHGVEK8kZiLdl4-u7");
		assertEquals(ytAnswer.getAuthor(), "Annalisa Bovone");
		assertEquals(ytAnswer.getAuthorId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytAnswer.getText(), "risposta 2");
		assertEquals(ytAnswer.getPublishedAtDate(), "30/8/2018");
		assertEquals(ytAnswer.getPublishedAtTime(), "10:53:50");
		assertEquals(ytAnswer.getTimestamp(), 1535619230000L);
		assertEquals(ytAnswer.getUpdatedAt(), 1535619230000L);
		assertEquals(ytAnswer.getLike(), 0);
		assertEquals(ytAnswer.getRating(), -1);
		assertEquals(ytAnswer.getConfidence(), -1, 0.01);
	}
	
	@Test
	public void saveAnswerTestAnswerNoUpdate() throws YoutubeException {
		Optional<YoutubeComment> optionalComment = youtubeCommentRepository.findById("UgzDUbfqaEILi43pdHJ4AaABAg");
		if(optionalComment.isPresent()) {
			YoutubeComment comment = optionalComment.get();
			YoutubeAnswer ytAnswer = new YoutubeAnswer("UgzDUbfqaEILi43pdHJ4AaABAg.8kZiJlHGVEK8kZiKkMYHvJ", "Annalisa Bovone","UCnlgdfqucEal83r8MvcBXtQ", "risposta 1", "30/8/2018", "10:53:43", 1535619223000L, 1535619223000L, 1, -1, -1);
			comment.addAnswer(ytAnswer);
			youtubeCommentRepository.save(comment);
		}
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Answer.json", "");
		List<YoutubeAnswer> ytAnswers = ytConnector.saveAnswer("UgzDUbfqaEILi43pdHJ4AaABAg", json);
		YoutubeAnswer ytAnswer = ytAnswers.get(1);
		assertEquals(ytAnswer.getCommentId(), "UgzDUbfqaEILi43pdHJ4AaABAg.8kZiJlHGVEK8kZiKkMYHvJ");
		assertEquals(ytAnswer.getAuthor(), "Annalisa Bovone");
		assertEquals(ytAnswer.getAuthorId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytAnswer.getText(), "risposta 1");
		assertEquals(ytAnswer.getPublishedAtDate(), "30/8/2018");
		assertEquals(ytAnswer.getPublishedAtTime(), "10:53:43");
		assertEquals(ytAnswer.getTimestamp(), 1535619223000L);
		assertEquals(ytAnswer.getUpdatedAt(), 1535619223000L);
		assertEquals(ytAnswer.getLike(), 1);
		assertEquals(ytAnswer.getRating(), -1);
		assertEquals(ytAnswer.getConfidence(), -1, 0.01);
	}
	
	@Test
	public void saveAnswerTestAnswerUpdate() throws YoutubeException {
		Optional<YoutubeComment> optionalComment = youtubeCommentRepository.findById("UgzDUbfqaEILi43pdHJ4AaABAg");
		if(optionalComment.isPresent()) {
			YoutubeComment comment = optionalComment.get();
			YoutubeAnswer ytAnswer = new YoutubeAnswer("UgzDUbfqaEILi43pdHJ4AaABAg.8kZiJlHGVEK8kZiKkMYHvJ", "Annalisa Bovone","UCnlgdfqucEal83r8MvcBXtQ", "risposta 1", "30/8/2018", "10:53:43", 1535619221000L, 1535619221000L, 1, -1, -1);
			comment.addAnswer(ytAnswer);
			youtubeCommentRepository.save(comment);
		}
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Answer.json", "");
		List<YoutubeAnswer> ytAnswers = ytConnector.saveAnswer("UgzDUbfqaEILi43pdHJ4AaABAg", json);
		YoutubeAnswer ytAnswer = ytAnswers.get(1);
		assertEquals(ytAnswer.getCommentId(), "UgzDUbfqaEILi43pdHJ4AaABAg.8kZiJlHGVEK8kZiKkMYHvJ");
		assertEquals(ytAnswer.getAuthor(), "Annalisa Bovone");
		assertEquals(ytAnswer.getAuthorId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytAnswer.getText(), "risposta 1");
		assertEquals(ytAnswer.getPublishedAtDate(), "30/8/2018");
		assertEquals(ytAnswer.getPublishedAtTime(), "10:53:43");
		assertEquals(ytAnswer.getTimestamp(), 1535619223000L);
		assertEquals(ytAnswer.getUpdatedAt(), 1535619223000L);
		assertEquals(ytAnswer.getLike(), 1);
		assertEquals(ytAnswer.getRating(), -1);
		assertEquals(ytAnswer.getConfidence(), -1, 0.01);
	}
	
	@Test
	public void runTest() {
		this.youtubeConnectorConfig.setTime("hh:mm");
		this.ytConnector = new YoutubeConnector(youtubeChannelRepository, youtubeVideoRepository, youtubeCommentRepository, youtubeConnectorConfig);
		this.ytConnector.setTestMode(true);
		this.ytConnector.run();
		assertEquals(this.ytConnector.getState(), "missing properties");		
	}
}
