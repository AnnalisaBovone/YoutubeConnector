package test_strutturali;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;
import com.example.youtubeconnector.UpdateVideo;
import com.example.youtubeconnector.YoutubeAnswer;
import com.example.youtubeconnector.YoutubeComment;
import com.example.youtubeconnector.YoutubeConnector;
import com.example.youtubeconnector.YoutubeConnectorConfig;
import com.example.youtubeconnector.YoutubeVideo;

public class YoutubeCommentTest {
	private String json;
	private List<YoutubeComment> ytComments;
	
	@Before
	public void setUpBeforeClass() throws YoutubeException {
		this.json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Comment.json", "");
	}
	
	@Test
	public void youtubeCommentTestNoParameters() {
		YoutubeComment ytComment = new YoutubeComment();
		assertEquals(ytComment.getCommentId(), null);
		assertEquals(ytComment.getAuthor(), null);
		assertEquals(ytComment.getAuthorId(), null);
		assertEquals(ytComment.getText(), null);
		assertEquals(ytComment.getPublishedAtDate(), null);
		assertEquals(ytComment.getPublishedAtTime(), null);
		assertEquals(ytComment.getTimestamp(), 0L);
		assertEquals(ytComment.getUpdatedAt(), 0L);
		assertEquals(ytComment.getLike(), 0);
		assertEquals(ytComment.getRating(), 0);
		assertEquals(ytComment.getConfidence(), 0.0, 0.01);
		assertEquals(ytComment.getAnswer(), null);
	}
	
	@Test
	public void youtubeCommentTest() throws JSONException {
		YoutubeComment ytComment = new YoutubeComment(this.json, 0);
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
	public void youtubeCommentTestNoLike() throws JSONException, YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/CommentNoLike.json", "");
		YoutubeComment ytComment = new YoutubeComment(json, 0);
		assertEquals(ytComment.getCommentId(), "UgzDUbfqaEILi43pdHJ4AaABAg");
		assertEquals(ytComment.getAuthor(), "Annalisa Bovone");
		assertEquals(ytComment.getAuthorId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytComment.getText(), "commento 1");
		assertEquals(ytComment.getPublishedAtDate(), "30/8/2018");
		assertEquals(ytComment.getPublishedAtTime(), "10:53:35");
		assertEquals(ytComment.getTimestamp(), 1535619215000L);
		assertEquals(ytComment.getUpdatedAt(), 1535619215000L);
		assertEquals(ytComment.getLike(), -1);
		assertEquals(ytComment.getRating(), -1);
		assertEquals(ytComment.getConfidence(), -1, 0.01);
		assertEquals(ytComment.getAnswer(), new ArrayList<YoutubeAnswer>());
	}
	
	@Test
	public void getterTest() throws JSONException {
		YoutubeComment ytComment = new YoutubeComment(this.json, 0);
		assertEquals(ytComment.getAnswer(), new ArrayList<YoutubeAnswer>());
	}
	
	@Test
	public void setterTest() throws JSONException {
		YoutubeComment ytComment = new YoutubeComment(this.json, 0);
		ArrayList<YoutubeAnswer> answers = new ArrayList<YoutubeAnswer>();
		YoutubeAnswer answer = new YoutubeAnswer("answerId","authorName","authorId", "text", "date",
				"time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		answers.add(answer);
		ytComment.setAnswer(answers);
		assertEquals(ytComment.getAnswer(), answers);
	}
	
	@Test
	public void commentsParserTest() throws YoutubeException {
		List<YoutubeComment> comments = YoutubeComment.commentsParser(json, "9JYPTJ4dSYQ");
		YoutubeComment ytComment = comments.get(0);
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
	public void commentsParserTestWithNextPageToken() throws YoutubeException {
		YoutubeConnectorConfig.youtubeKey = "AIzaSyDIT4EW90s26Kd2X8EDbNm6pTmNU05EYJo";
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/CommentWithNextPageToken.json", "");
		List<YoutubeComment> comments = YoutubeComment.commentsParser(json, "jP4sAj9O8Ps");
		YoutubeComment ytComment = comments.get(0);
		assertEquals(ytComment.getCommentId(), "Ugy6lEw3mlxRx1QaksJ4AaABAg");
		assertEquals(ytComment.getAuthor(), "iosif gheraescu");
		assertEquals(ytComment.getAuthorId(), "UCaKWgnfqQ97uGOsBYE1o8zg");
		assertEquals(ytComment.getText(), "Auguri");
		assertEquals(ytComment.getPublishedAtDate(), "31/8/2018");
		assertEquals(ytComment.getPublishedAtTime(), "10:1:36");
		assertEquals(ytComment.getTimestamp(), 1535702496000L);
		assertEquals(ytComment.getUpdatedAt(), 1535702496000L);
		assertEquals(ytComment.getLike(), 0);
		assertEquals(ytComment.getRating(), -1);
		assertEquals(ytComment.getConfidence(), -1, 0.01);
		assertEquals(ytComment.getAnswer(), new ArrayList<YoutubeAnswer>());
	}
	
	@Test
	public void commentsParserTestNoTotalResult() throws YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/CommentNoTotalResult.json", "");
		List<YoutubeComment> comments = null;
		try {
			YoutubeComment.commentsParser(json, "9JYPTJ4dSYQ");
		}catch(YoutubeException e) {
			assertEquals(e.getMessage(), "Error parsing json comment: No value for totalResults");
			assertEquals(e.getErrorCode(),ErrorCode.ParsingCommentError);
		}
		assertEquals(comments, null);
	}
	
	@Test
	public void addAnswerTest() throws YoutubeException, JSONException {
		YoutubeComment ytComment = new YoutubeComment(this.json, 0);
		List<YoutubeAnswer> answers = ytComment.getAnswer();
		YoutubeAnswer answer = new YoutubeAnswer("answerId","authorName","authorId", "text", "date",
				"time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		ytComment.addAnswer(answer);
		assertEquals(ytComment.getAnswer(), answers);
	}
	
	@Test
	public void addAnswerTest2() throws YoutubeException, JSONException {
		YoutubeComment ytComment = new YoutubeComment(this.json, 0);
		List<YoutubeAnswer> answers = ytComment.getAnswer();
		YoutubeAnswer answer = new YoutubeAnswer("answerId","authorName","authorId", "text", "date",
				"time", 1535619215000L, 1535619215000L, 1, 3, 0.5);
		answers.add(answer);
		ytComment.addAnswer(answer);
		assertEquals(ytComment.getAnswer(), answers);
	}
	
}
