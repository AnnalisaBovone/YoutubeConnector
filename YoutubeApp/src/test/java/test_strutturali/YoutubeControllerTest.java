package test_strutturali;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.example.youtubeconnector.YoutubeConnectorConfig;
import com.example.youtubeconnector.YoutubeConnectorController;
import com.example.youtubeconnector.YoutubeVideo;
import com.example.youtubeconnector.YoutubeVideoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = YoutubeAppApplication.class)
@DataMongoTest
public class YoutubeControllerTest {
	private YoutubeConnectorController ytController;
	@Autowired
	public MongoTemplate mongoTemplate;
	@Autowired
	public YoutubeVideoRepository youtubeVideoRepository;
	@Autowired
	public YoutubeChannelRepository youtubeChannelRepository;
	@Autowired
	public YoutubeCommentRepository youtubeCommentRepository;
	
	public YoutubeConnectorConfig youtubeConnectorConfig;
	
	@Before
	public void setUpBeforeClass() throws YoutubeException, JSONException {
		mongoTemplate.dropCollection(YoutubeVideo.class);
		mongoTemplate.dropCollection(YoutubeChannel.class);
		mongoTemplate.dropCollection(YoutubeComment.class);
		
		String json="";
		json = YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Video.json", "");
		YoutubeVideo video1 = new YoutubeVideo(json);
		video1.setVideoId("videoId1");
		video1.setChannelId("channel1");
		YoutubeChannel channel1 = new YoutubeChannel(json);
		channel1.setChannelId("channel1");
		channel1.addVideo(video1.getVideoId());
		UpdateVideo update = new UpdateVideo("oggi", 10, 5, 20);
		video1.addUpdate(update);
		youtubeVideoRepository.save(video1);
		youtubeChannelRepository.save(channel1);
		
		YoutubeVideo video2 = new YoutubeVideo(json);
		video2.setVideoId("videoId2");
		video2.setChannelId("channelId2");
		video2.addComment("comment5");
		video2.addComment("comment2");
		video2.addComment("comment6");
		video2.addComment("comment7");
		video2.addComment("comment8");
		YoutubeComment comment5 = new YoutubeComment("comment5","authorName","authorId", "parola", "date", "time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		YoutubeComment comment6 = new YoutubeComment("comment6","authorName","authorId", "parola", "date", "time", 1535788800000L, 1535788800000L, 1, 3, 0.5);
		YoutubeComment comment7 = new YoutubeComment("comment7","authorName","authorId", "text", "date", "time", 1535507000000L, 1535507000000L, 1, 3, 0.5);
		YoutubeAnswer answer3 = new YoutubeAnswer("answer3","authorName","authorId", "parola", "date", "time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		YoutubeAnswer answer4 = new YoutubeAnswer("answer4","authorName","authorId", "text", "date", "time", 1535507000000L, 1535507000000L, 1, 3, 0.5);
		YoutubeAnswer answer5 = new YoutubeAnswer("answer5","authorName","authorId", "text", "date", "time", 1535788800000L, 1535788800000L, 1, 3, 0.5);
		YoutubeComment comment8 = new YoutubeComment("comment8","authorName","authorId", "text", "date", "time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		comment7.addAnswer(answer3);
		comment7.addAnswer(answer4);
		comment7.addAnswer(answer5);
		youtubeCommentRepository.save(comment5);
		youtubeCommentRepository.save(comment6);
		youtubeCommentRepository.save(comment7);
		youtubeCommentRepository.save(comment8);
		youtubeVideoRepository.save(video2);
		
		YoutubeVideo video3 = new YoutubeVideo(json);
		video3.setVideoId("videoId3");
		video3.setChannelId("channel2");
		UpdateVideo update2 = new UpdateVideo("oggi", 10, 5, 20);
		video3.addUpdate(update2);
		YoutubeChannel channel2 = new YoutubeChannel(json);
		channel2.setChannelId("channel2");
		channel2.addVideo(video3.getVideoId());
		channel2.addVideo("video not in db");
		youtubeVideoRepository.save(video3);
		youtubeChannelRepository.save(channel2);
		
		YoutubeVideo video4 = new YoutubeVideo(json);
		video4.setVideoId("videoId4");
		video4.setChannelId("channel3");
		YoutubeChannel channel3 = new YoutubeChannel(json);
		channel3.setChannelId("channel3");
		channel3.addVideo(video4.getVideoId());
		UpdateVideo update1 = new UpdateVideo("oggi", -1, 5, 20);
		video4.addUpdate(update1);
		video4.addComment("comment1");
		video4.addComment("comment2");
		YoutubeComment comment1 = new YoutubeComment("comment1","authorName","authorId", "parola", "date", "time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		video4.addComment("comment3");
		YoutubeComment comment3 = new YoutubeComment("comment3","authorName","authorId", "text", "date", "time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		YoutubeAnswer answer1 = new YoutubeAnswer("answer1","authorName","authorId", "spiccioli", "date", "time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		YoutubeAnswer answer2 = new YoutubeAnswer("answer2","authorName","authorId", "parola", "date", "time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		comment3.addAnswer(answer1);
		comment3.addAnswer(answer2);
		video4.addComment("comment4");
		YoutubeComment comment4 = new YoutubeComment("comment4","authorName","authorId", "text", "date", "time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		youtubeCommentRepository.save(comment4);
		youtubeCommentRepository.save(comment3);
		youtubeCommentRepository.save(comment1);
		youtubeVideoRepository.save(video4);
		youtubeChannelRepository.save(channel3);
		
		youtubeConnectorConfig = new YoutubeConnectorConfig();
		String[] urls = {"https://www.youtube.com/watch?v=9JYPTJ4dSYQ"};
		youtubeConnectorConfig.setUrl(urls);
		youtubeConnectorConfig.setTime("hh:mm");
		YoutubeConnectorConfig.sentimentKey = "MTgzMTIzNTctMDdERi00NzRDLThFNzEtNDFCQzMzNTlFNDc1";
		YoutubeConnectorConfig.youtubeKey = "AIzaSyDIT4EW90s26Kd2X8EDbNm6pTmNU05EYJo";
		
		this.ytController = new YoutubeConnectorController(youtubeChannelRepository, youtubeVideoRepository, youtubeCommentRepository, youtubeConnectorConfig);
	}
	
	@Test
	public void youtubeControllerTest() {
		assertEquals(ytController.getYoutubeChannelRepository(), youtubeChannelRepository);
		assertEquals(ytController.getYoutubeVideoRepository(), youtubeVideoRepository);
		assertEquals(ytController.getYoutubeCommentRepository(), youtubeCommentRepository);
		assertEquals(ytController.getYoutubeConnectorConfig(), youtubeConnectorConfig);
	}
	
	@Test
	public void setterTest() {
		assertEquals(ytController.getYoutubeChannelRepository(), youtubeChannelRepository);
		assertEquals(ytController.getYoutubeVideoRepository(), youtubeVideoRepository);
		assertEquals(ytController.getYoutubeCommentRepository(), youtubeCommentRepository);
		assertEquals(ytController.getYoutubeConnectorConfig(), youtubeConnectorConfig);
	}
	
	@Test
	public void getAllVideoTest() throws YoutubeException {
		List<YoutubeVideo> expecteds = ytController.getAllVideo();
		assertEquals(expecteds.size(), 4);
		YoutubeVideo expected0 = expecteds.get(0);
		assertEquals(expected0.getVideoId(), "videoId1");
		assertEquals(expected0.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(expected0.getChannelId(), "channel1");
		assertEquals(expected0.getPublishedAtDate(), "8/6/2018");
		assertEquals(expected0.getPublishedAtTime(), "23:2:4");
		assertEquals(expected0.getTimestamp(), 1528491724000L);
		assertEquals(expected0.getDuration(), "00:04:06");
		assertEquals(expected0.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(expected0.getDescription(), "");
		assertEquals(expected0.getUpdates().size(), 1);
		assertEquals(expected0.getUpdates().get(0).getDate(), "oggi");
		assertEquals(expected0.getUpdates().get(0).getLike(), 10);
		assertEquals(expected0.getUpdates().get(0).getDislike(), 5);
		assertEquals(expected0.getUpdates().get(0).getViews(), 20);
		assertEquals(expected0.getComments().size(), 0);
		
		YoutubeVideo expected1 = expecteds.get(1);
		assertEquals(expected1.getVideoId(), "videoId2");
		assertEquals(expected1.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(expected1.getChannelId(), "channelId2");
		assertEquals(expected1.getPublishedAtDate(), "8/6/2018");
		assertEquals(expected1.getPublishedAtTime(), "23:2:4");
		assertEquals(expected1.getTimestamp(), 1528491724000L);
		assertEquals(expected1.getDuration(), "00:04:06");
		assertEquals(expected1.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(expected1.getDescription(), "");
		assertEquals(expected1.getUpdates().size(), 0);
		assertEquals(expected1.getComments().size(), 5);
		assertEquals(expected1.getComments().get(0), "comment5");
		assertEquals(expected1.getComments().get(1), "comment2");
		assertEquals(expected1.getComments().get(2), "comment6");
		assertEquals(expected1.getComments().get(3), "comment7");
		assertEquals(expected1.getComments().get(4), "comment8");
	
		YoutubeVideo expected2 = expecteds.get(2);
		assertEquals(expected2.getVideoId(), "videoId3");
		assertEquals(expected2.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(expected2.getChannelId(), "channel2");
		assertEquals(expected2.getPublishedAtDate(), "8/6/2018");
		assertEquals(expected2.getPublishedAtTime(), "23:2:4");
		assertEquals(expected2.getTimestamp(), 1528491724000L);
		assertEquals(expected2.getDuration(), "00:04:06");
		assertEquals(expected2.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(expected2.getDescription(), "");
		assertEquals(expected2.getUpdates().size(), 1);
		assertEquals(expected2.getUpdates().get(0).getDate(), "oggi");
		assertEquals(expected2.getUpdates().get(0).getLike(), 10);
		assertEquals(expected2.getUpdates().get(0).getDislike(), 5);
		assertEquals(expected2.getUpdates().get(0).getViews(), 20);
		assertEquals(expected2.getComments().size(), 0);
		
		
		YoutubeVideo expected3 = expecteds.get(3);
		assertEquals(expected3.getVideoId(), "videoId4");
		assertEquals(expected3.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(expected3.getChannelId(), "channel3");
		assertEquals(expected3.getPublishedAtDate(), "8/6/2018");
		assertEquals(expected3.getPublishedAtTime(), "23:2:4");
		assertEquals(expected3.getTimestamp(), 1528491724000L);
		assertEquals(expected3.getDuration(), "00:04:06");
		assertEquals(expected3.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(expected3.getDescription(), "");
		assertEquals(expected3.getUpdates().size(), 1);
		assertEquals(expected3.getUpdates().get(0).getDate(), "oggi");
		assertEquals(expected3.getUpdates().get(0).getLike(), -1);
		assertEquals(expected3.getUpdates().get(0).getDislike(), 5);
		assertEquals(expected3.getUpdates().get(0).getViews(), 20);
		assertEquals(expected3.getComments().size(), 4);
		assertEquals(expected3.getComments().get(0), "comment1");
		assertEquals(expected3.getComments().get(1), "comment2");
		assertEquals(expected3.getComments().get(2), "comment3");
		assertEquals(expected3.getComments().get(3), "comment4");
	}
	
	@Test
	public void getInfoVideoTest() throws YoutubeException {
		HashMap<String, Object> infoVideo = ytController.getInfoVideo("videoId1");
		YoutubeVideo v = (YoutubeVideo) infoVideo.get("video");
		assertEquals(v.getVideoId(), "videoId1");
		assertEquals(v.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(v.getChannelId(), "channel1");
		assertEquals(v.getPublishedAtDate(), "8/6/2018");
		assertEquals(v.getPublishedAtTime(), "23:2:4");
		assertEquals(v.getTimestamp(), 1528491724000L);
		assertEquals(v.getDuration(), "00:04:06");
		assertEquals(v.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(v.getDescription(), "");
		assertEquals(v.getUpdates().size(), 1);
		assertEquals(v.getUpdates().get(0).getDate(), "oggi");
		assertEquals(v.getUpdates().get(0).getLike(), 10);
		assertEquals(v.getUpdates().get(0).getDislike(), 5);
		assertEquals(v.getUpdates().get(0).getViews(), 20);
		assertEquals(v.getComments().size(), 0);
		YoutubeChannel c = (YoutubeChannel) infoVideo.get("channel");
		assertEquals(c.getChannelId(), "channel1");
		assertEquals(c.getChannelTitle(), "Annalisa Bovone");
		assertEquals(c.getSubscribers(), -1);
		assertEquals(c.getVideoIds().size(), 1);
		assertEquals(c.getVideoIds().get(0), "videoId1");
		int totalViews = (int) infoVideo.get("totalViews");
		assertEquals(totalViews, 20);
		int totalLike = (int) infoVideo.get("totalLike");
		assertEquals(totalLike, 10);
		List<YoutubeComment> comments = (List<YoutubeComment>) infoVideo.get("comment");
		List<YoutubeAnswer> messages = (List<YoutubeAnswer>) infoVideo.get("allComment");
		assertEquals(comments.size(), 0);
		assertEquals(messages.size(), 0);
	}
	
	@Test
	public void getInfoVideoTestVideoNotExisting() throws YoutubeException {
		HashMap<String, Object> infoVideo = ytController.getInfoVideo("video id not existing");
		assertEquals(infoVideo, null);
	}
	
	@Test
	public void getInfoVideoTestChannelNotExisting() throws YoutubeException {
		HashMap<String, Object> infoVideo = ytController.getInfoVideo("videoId2");
		YoutubeVideo v = (YoutubeVideo) infoVideo.get("video");
		assertEquals(v.getVideoId(), "videoId2");
		assertEquals(v.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(v.getChannelId(), "channelId2");
		assertEquals(v.getPublishedAtDate(), "8/6/2018");
		assertEquals(v.getPublishedAtTime(), "23:2:4");
		assertEquals(v.getTimestamp(), 1528491724000L);
		assertEquals(v.getDuration(), "00:04:06");
		assertEquals(v.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(v.getDescription(), "");
		assertEquals(v.getUpdates().size(), 0);
		assertEquals(v.getComments().size(), 5);
		assertEquals(v.getComments().get(0), "comment5");
		assertEquals(v.getComments().get(1), "comment2");
		assertEquals(v.getComments().get(2), "comment6");
		assertEquals(v.getComments().get(3), "comment7");
		assertEquals(v.getComments().get(4), "comment8");
		YoutubeChannel c = (YoutubeChannel) infoVideo.get("channel");
		assertEquals(c, null);
		List<YoutubeComment> comments = (List<YoutubeComment>) infoVideo.get("comment");
		List<YoutubeAnswer> messages = (List<YoutubeAnswer>) infoVideo.get("allComment");
		assertEquals(comments.size(), 4);
		YoutubeComment comment0 = comments.get(0);
		assertEquals(comment0.getCommentId(), "comment7");
		assertEquals(comment0.getAuthor(), "authorName");
		assertEquals(comment0.getAuthorId(), "authorId");
		assertEquals(comment0.getText(), "text");
		assertEquals(comment0.getPublishedAtDate(), "date");
		assertEquals(comment0.getPublishedAtTime(), "time");
		assertEquals(comment0.getTimestamp(), 1535507000000L);
		assertEquals(comment0.getUpdatedAt(), 1535507000000L);
		assertEquals(comment0.getLike(), 1);
		assertEquals(comment0.getRating(), 3);
		assertEquals(comment0.getConfidence(), 0.5, 0.01);
		assertEquals(comment0.getAnswer().size(), 3);
		assertEquals(messages.size(), 7);
	}
	
	@Test
	public void getInfoVideoTestNoIdInVideoIds() throws YoutubeException {
		HashMap<String, Object> infoVideo = ytController.getInfoVideo("videoId3");
		YoutubeVideo v = (YoutubeVideo) infoVideo.get("video");
		assertEquals(v.getVideoId(), "videoId3");
		assertEquals(v.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(v.getChannelId(), "channel2");
		assertEquals(v.getPublishedAtDate(), "8/6/2018");
		assertEquals(v.getPublishedAtTime(), "23:2:4");
		assertEquals(v.getTimestamp(), 1528491724000L);
		assertEquals(v.getDuration(), "00:04:06");
		assertEquals(v.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(v.getDescription(), "");
		assertEquals(v.getUpdates().size(), 1);
		assertEquals(v.getComments().size(), 0);
		assertEquals(v.getUpdates().get(0).getDate(), "oggi");
		assertEquals(v.getUpdates().get(0).getLike(), 10);
		assertEquals(v.getUpdates().get(0).getDislike(), 5);
		assertEquals(v.getUpdates().get(0).getViews(), 20);
		int totalViews = (int) infoVideo.get("totalViews");
		assertEquals(totalViews, 20);
		int totalLike = (int) infoVideo.get("totalLike");
		assertEquals(totalLike, 10);
		YoutubeChannel c = (YoutubeChannel) infoVideo.get("channel");
		assertEquals(c.getChannelId(), "channel2");
		assertEquals(c.getChannelTitle(), "Annalisa Bovone");
		assertEquals(c.getSubscribers(), -1);
		assertEquals(c.getVideoIds().size(), 2);
		assertEquals(c.getVideoIds().get(0), "videoId3");
		assertEquals(c.getVideoIds().get(1), "video not in db");
		List<YoutubeComment> comments = (List<YoutubeComment>) infoVideo.get("comment");
		List<YoutubeAnswer> messages = (List<YoutubeAnswer>) infoVideo.get("allComment");
		assertEquals(comments.size(), 0);
		assertEquals(messages.size(), 0);
	}
	
	@Test
	public void getInfoVideoTestNoLikeCount() throws YoutubeException {
		HashMap<String, Object> infoVideo = ytController.getInfoVideo("videoId4");
		YoutubeVideo v = (YoutubeVideo) infoVideo.get("video");
		assertEquals(v.getVideoId(), "videoId4");
		assertEquals(v.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(v.getChannelId(), "channel3");
		assertEquals(v.getPublishedAtDate(), "8/6/2018");
		assertEquals(v.getPublishedAtTime(), "23:2:4");
		assertEquals(v.getTimestamp(), 1528491724000L);
		assertEquals(v.getDuration(), "00:04:06");
		assertEquals(v.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(v.getDescription(), "");
		assertEquals(v.getUpdates().size(), 1);
		assertEquals(v.getComments().size(), 4);
		assertEquals(v.getUpdates().get(0).getDate(), "oggi");
		assertEquals(v.getUpdates().get(0).getLike(), -1);
		assertEquals(v.getUpdates().get(0).getDislike(), 5);
		assertEquals(v.getUpdates().get(0).getViews(), 20);
		int totalViews = (int) infoVideo.get("totalViews");
		assertEquals(totalViews, 20);
		int totalLike = (int) infoVideo.get("totalLike");
		assertEquals(totalLike, 0);
		YoutubeChannel c = (YoutubeChannel) infoVideo.get("channel");
		assertEquals(c.getChannelId(), "channel3");
		assertEquals(c.getChannelTitle(), "Annalisa Bovone");
		assertEquals(c.getSubscribers(), -1);
		assertEquals(c.getVideoIds().size(), 1);
		assertEquals(c.getVideoIds().get(0), "videoId4");
		List<YoutubeComment> comments = (List<YoutubeComment>) infoVideo.get("comment");
		List<YoutubeAnswer> messages = (List<YoutubeAnswer>) infoVideo.get("allComment");
		assertEquals(comments.size(), 3);
		assertEquals(messages.size(), 5);
	}
	
	@Test
	public void getChannelFromIdTest() {
		YoutubeChannel c = ytController.getChannelFromId("channel1");
		assertEquals(c.getChannelId(), "channel1");
		assertEquals(c.getChannelTitle(), "Annalisa Bovone");
		assertEquals(c.getSubscribers(), -1);
		assertEquals(c.getVideoIds().size(), 1);
		assertEquals(c.getVideoIds().get(0), "videoId1");
	}
	
	@Test
	public void getChannelFromIdTestNoChannelExisting() {
		YoutubeChannel c = ytController.getChannelFromId("channel8");
		assertEquals(c, null);
	}
	
	@Test
	public void getCommentFromWordTest() {
		List<YoutubeComment> comments = ytController.getCommentFromWord("parola", "videoId4");
		assertEquals(comments.size(), 2);
		YoutubeComment comment0 = comments.get(0);
		assertEquals(comment0.getCommentId(), "comment1");
		assertEquals(comment0.getAuthor(), "authorName");
		assertEquals(comment0.getAuthorId(), "authorId");
		assertEquals(comment0.getText(), "<span style=background-color:yellow>parola</span>");
		assertEquals(comment0.getPublishedAtDate(), "date");
		assertEquals(comment0.getPublishedAtTime(), "time");
		assertEquals(comment0.getTimestamp(), 1535619215000L);
		assertEquals(comment0.getUpdatedAt(), 1535619215000L);
		assertEquals(comment0.getLike(), 1);
		assertEquals(comment0.getRating(), 3);
		assertEquals(comment0.getConfidence(), 0.5, 0.01);
		assertEquals(comment0.getAnswer().size(), 0);
		
		YoutubeComment comment1 = comments.get(1);
		assertEquals(comment1.getCommentId(), "comment3");
		assertEquals(comment1.getAuthor(), "authorName");
		assertEquals(comment1.getAuthorId(), "authorId");
		assertEquals(comment1.getText(), "text");
		assertEquals(comment1.getPublishedAtDate(), "date");
		assertEquals(comment1.getPublishedAtTime(), "time");
		assertEquals(comment1.getTimestamp(), 1535619215000L);
		assertEquals(comment1.getUpdatedAt(), 1535619215000L);
		assertEquals(comment1.getLike(), 1);
		assertEquals(comment1.getRating(), 3);
		assertEquals(comment1.getConfidence(), 0.5, 0.01);
		assertEquals(comment1.getAnswer().size(), 2);
		
		YoutubeAnswer answer1 = comment1.getAnswer().get(0);
		assertEquals(answer1.getCommentId(), "answer1");
		assertEquals(answer1.getAuthor(), "authorName");
		assertEquals(answer1.getAuthorId(), "authorId");
		assertEquals(answer1.getText(), "spiccioli");
		assertEquals(answer1.getPublishedAtDate(), "date");
		assertEquals(answer1.getPublishedAtTime(), "time");
		assertEquals(answer1.getTimestamp(), 1535619215000L);
		assertEquals(answer1.getUpdatedAt(), 1535619215000L);
		assertEquals(answer1.getLike(), 1);
		assertEquals(answer1.getRating(), 3);
		assertEquals(answer1.getConfidence(), 0.5, 0.01);
		
		YoutubeAnswer answer2 = comment1.getAnswer().get(1);
		assertEquals(answer2.getCommentId(), "answer2");
		assertEquals(answer2.getAuthor(), "authorName");
		assertEquals(answer2.getAuthorId(), "authorId");
		assertEquals(answer2.getText(), "<span style=background-color:yellow>parola</span>");
		assertEquals(answer2.getPublishedAtDate(), "date");
		assertEquals(answer2.getPublishedAtTime(), "time");
		assertEquals(answer2.getTimestamp(), 1535619215000L);
		assertEquals(answer2.getUpdatedAt(), 1535619215000L);
		assertEquals(answer2.getLike(), 1);
		assertEquals(answer2.getRating(), 3);
		assertEquals(answer2.getConfidence(), 0.5, 0.01);
	}
	
	@Test
	public void getCommentFromWordTestNoVideoExisting() {
		List<YoutubeComment> comments = ytController.getCommentFromWord("parola", "video8");
		assertEquals(comments, null);
	}
	
	@Test
	public void getCommentFromTemporalIntervalTest() {
		List<YoutubeComment> comments = ytController.getCommentFromTemporalInterval(1535616000000L, 1535619600000L, "videoId2");
		assertEquals(comments.size(), 3);
		YoutubeComment comment0 = comments.get(0);
		assertEquals(comment0.getCommentId(), "comment5");
		assertEquals(comment0.getAuthor(), "authorName");
		assertEquals(comment0.getAuthorId(), "authorId");
		assertEquals(comment0.getText(), "parola");
		assertEquals(comment0.getPublishedAtDate(), "date");
		assertEquals(comment0.getPublishedAtTime(), "time");
		assertEquals(comment0.getTimestamp(), 1535619215000L);
		assertEquals(comment0.getUpdatedAt(), 1535619215000L);
		assertEquals(comment0.getLike(), 1);
		assertEquals(comment0.getRating(), 3);
		assertEquals(comment0.getConfidence(), 0.5, 0.01);
		assertEquals(comment0.getAnswer().size(), 0);
		
		YoutubeComment comment1 = comments.get(2);
		assertEquals(comment1.getCommentId(), "comment8");
		assertEquals(comment1.getAuthor(), "authorName");
		assertEquals(comment1.getAuthorId(), "authorId");
		assertEquals(comment1.getText(), "text");
		assertEquals(comment1.getPublishedAtDate(), "date");
		assertEquals(comment1.getPublishedAtTime(), "time");
		assertEquals(comment1.getTimestamp(), 1535619215000L);
		assertEquals(comment1.getUpdatedAt(), 1535619215000L);
		assertEquals(comment1.getLike(), 1);
		assertEquals(comment1.getRating(), 3);
		assertEquals(comment1.getConfidence(), 0.5, 0.01);
		assertEquals(comment1.getAnswer().size(), 0);
		
		YoutubeComment comment2 = comments.get(1);
		assertEquals(comment2.getCommentId(), "comment7");
		assertEquals(comment2.getAuthor(), "authorName");
		assertEquals(comment2.getAuthorId(), "authorId");
		assertEquals(comment2.getText(), "text");
		assertEquals(comment2.getPublishedAtDate(), "date");
		assertEquals(comment2.getPublishedAtTime(), "time");
		assertEquals(comment2.getTimestamp(), 1535507000000L);
		assertEquals(comment2.getUpdatedAt(), 1535507000000L);
		assertEquals(comment2.getLike(), 1);
		assertEquals(comment2.getRating(), 3);
		assertEquals(comment2.getConfidence(), 0.5, 0.01);
		assertEquals(comment2.getAnswer().size(), 3);
		
		YoutubeAnswer answer1 = comment2.getAnswer().get(0);
		assertEquals(answer1.getCommentId(), "answer3");
		assertEquals(answer1.getAuthor(), "authorName");
		assertEquals(answer1.getAuthorId(), "authorId");
		assertEquals(answer1.getText(), "parola");
		assertEquals(answer1.getPublishedAtDate(), "date");
		assertEquals(answer1.getPublishedAtTime(), "time");
		assertEquals(answer1.getTimestamp(), 1535619215000L);
		assertEquals(answer1.getUpdatedAt(), 1535619215000L);
		assertEquals(answer1.getLike(), 1);
		assertEquals(answer1.getRating(), 3);
		assertEquals(answer1.getConfidence(), 0.5, 0.01);
	}
	
	@Test
	public void getCommentFromTemporalIntervalTestNoExistingVideo() {
		List<YoutubeComment> comments = ytController.getCommentFromTemporalInterval(1535616000000L, 1535619600000L, "video8");
		assertEquals(comments, null);
	}
	
	@Test
	public void getCommentFromCombineFilter() {
		List<YoutubeComment> comments = ytController.getCommentFromCombineFilter("parola", 1535616000000L, 1535619600000L, "videoId2");
		assertEquals(comments.size(), 2);
		YoutubeComment comment0 = comments.get(0);
		assertEquals(comment0.getCommentId(), "comment5");
		assertEquals(comment0.getAuthor(), "authorName");
		assertEquals(comment0.getAuthorId(), "authorId");
		assertEquals(comment0.getText(), "<span style=background-color:yellow>parola</span>");
		assertEquals(comment0.getPublishedAtDate(), "date");
		assertEquals(comment0.getPublishedAtTime(), "time");
		assertEquals(comment0.getTimestamp(), 1535619215000L);
		assertEquals(comment0.getUpdatedAt(), 1535619215000L);
		assertEquals(comment0.getLike(), 1);
		assertEquals(comment0.getRating(), 3);
		assertEquals(comment0.getConfidence(), 0.5, 0.01);
		assertEquals(comment0.getAnswer().size(), 0);
		
		YoutubeComment comment2 = comments.get(1);
		assertEquals(comment2.getCommentId(), "comment7");
		assertEquals(comment2.getAuthor(), "authorName");
		assertEquals(comment2.getAuthorId(), "authorId");
		assertEquals(comment2.getText(), "text");
		assertEquals(comment2.getPublishedAtDate(), "date");
		assertEquals(comment2.getPublishedAtTime(), "time");
		assertEquals(comment2.getTimestamp(), 1535507000000L);
		assertEquals(comment2.getUpdatedAt(), 1535507000000L);
		assertEquals(comment2.getLike(), 1);
		assertEquals(comment2.getRating(), 3);
		assertEquals(comment2.getConfidence(), 0.5, 0.01);
		assertEquals(comment2.getAnswer().size(), 3);
		
		YoutubeAnswer answer1 = comment2.getAnswer().get(0);
		assertEquals(answer1.getCommentId(), "answer3");
		assertEquals(answer1.getAuthor(), "authorName");
		assertEquals(answer1.getAuthorId(), "authorId");
		assertEquals(answer1.getText(), "<span style=background-color:yellow>parola</span>");
		assertEquals(answer1.getPublishedAtDate(), "date");
		assertEquals(answer1.getPublishedAtTime(), "time");
		assertEquals(answer1.getTimestamp(), 1535619215000L);
		assertEquals(answer1.getUpdatedAt(), 1535619215000L);
		assertEquals(answer1.getLike(), 1);
		assertEquals(answer1.getRating(), 3);
		assertEquals(answer1.getConfidence(), 0.5, 0.01);
	}
	
}
