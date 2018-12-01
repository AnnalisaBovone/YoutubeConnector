package test_strutturali;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;
import com.example.youtubeconnector.YoutubeAnswer;
import com.example.youtubeconnector.YoutubeComment;
import com.example.youtubeconnector.YoutubeConnector;
import com.example.youtubeconnector.YoutubeConnectorConfig;

public class YoutubeAnswerTest {
	private String json;
	private List<YoutubeAnswer> ytAnswer;
	
	@Before
	public void setUpBeforeClass() throws YoutubeException {
		this.json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Answer.json", "");
	}
	
	@Test
	public void youtubeAnswerTestNoParameters() {
		YoutubeAnswer ytAnswer = new YoutubeAnswer();
		assertEquals(ytAnswer.getCommentId(), null);
		assertEquals(ytAnswer.getAuthor(), null);
		assertEquals(ytAnswer.getAuthorId(), null);
		assertEquals(ytAnswer.getText(), null);
		assertEquals(ytAnswer.getPublishedAtDate(), null);
		assertEquals(ytAnswer.getPublishedAtTime(), null);
		assertEquals(ytAnswer.getTimestamp(), 0L);
		assertEquals(ytAnswer.getUpdatedAt(), 0L);
		assertEquals(ytAnswer.getLike(), 0);
		assertEquals(ytAnswer.getRating(), 0);
		assertEquals(ytAnswer.getConfidence(), 0.0, 0.01);
	}
	
	@Test
	public void youtubeAnswerTest() throws JSONException {
		YoutubeAnswer ytAnswer = new YoutubeAnswer(this.json, 0);
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
	public void youtubeAnswerTestNoLike() throws JSONException, YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/AnswerNoLike.json", "");
		YoutubeAnswer ytAnswer = new YoutubeAnswer(json, 0);
		assertEquals(ytAnswer.getCommentId(), "UgzDUbfqaEILi43pdHJ4AaABAg.8kZiJlHGVEK8kZiLdl4-u7");
		assertEquals(ytAnswer.getAuthor(), "Annalisa Bovone");
		assertEquals(ytAnswer.getAuthorId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytAnswer.getText(), "risposta 2");
		assertEquals(ytAnswer.getPublishedAtDate(), "30/8/2018");
		assertEquals(ytAnswer.getPublishedAtTime(), "10:53:50");
		assertEquals(ytAnswer.getTimestamp(), 1535619230000L);
		assertEquals(ytAnswer.getUpdatedAt(), 1535619230000L);
		assertEquals(ytAnswer.getLike(), -1);
		assertEquals(ytAnswer.getRating(), -1);
		assertEquals(ytAnswer.getConfidence(), -1, 0.01);
	}
	
	@Test
	public void getterTest() throws JSONException {
		YoutubeAnswer ytAnswer = new YoutubeAnswer(json, 0);
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
	public void setterTest() throws JSONException {
		YoutubeAnswer ytAnswer = new YoutubeAnswer(json, 0);
		ytAnswer.setCommentId("nuovo commentId");
		assertEquals(ytAnswer.getCommentId(), "nuovo commentId");
		ytAnswer.setAuthor("nuovo nome autore");
		assertEquals(ytAnswer.getAuthor(), "nuovo nome autore");
		ytAnswer.setAuthorId("nuovo id autore");
		assertEquals(ytAnswer.getAuthorId(), "nuovo id autore");
		ytAnswer.setText("nuovo testo commento");
		assertEquals(ytAnswer.getText(), "nuovo testo commento");
		ytAnswer.setPublishedAtDate("nuova data");
		assertEquals(ytAnswer.getPublishedAtDate(), "nuova data");
		ytAnswer.setPublishedAtTime("nuovo orario");
		assertEquals(ytAnswer.getPublishedAtTime(), "nuovo orario");
		ytAnswer.setTimestamp(1535620440000L);
		assertEquals(ytAnswer.getTimestamp(), 1535620440000L);
		ytAnswer.setUpdatedAt(1535620440000L);
		assertEquals(ytAnswer.getUpdatedAt(), 1535620440000L);
		ytAnswer.setLike(10);
		assertEquals(ytAnswer.getLike(), 10);
		ytAnswer.setRating(4);
		assertEquals(ytAnswer.getRating(), 4);
		ytAnswer.setConfidence(0.7);
		assertEquals(ytAnswer.getConfidence(), 0.7, 0.01);
	}
	
	@Test
	public void setSentimentEngineTestVoto5() throws YoutubeException, JSONException {
		YoutubeAnswer ytAnswer = new YoutubeAnswer(this.json, 0);
		ytAnswer.setText("Bellissimo video");
		String json = YoutubeConnector.jsonGetRequest("http://localhost:8080/test/SentimentVoto5.json", "");;
		ytAnswer.setSentimentEngine(json);
		assertEquals(ytAnswer.getRating(), 5);
		assertEquals(ytAnswer.getConfidence(), 1, 0.01);
	}
	
	@Test
	public void setSentimentEngineTestVoto0() throws YoutubeException, JSONException {
		YoutubeAnswer ytAnswer = new YoutubeAnswer(this.json, 0);
		ytAnswer.setText("Hai assaggiato la bevanda Rivella?");
		String json = YoutubeConnector.jsonGetRequest("http://localhost:8080/test/SentimentVoto0.json", "");;
		ytAnswer.setSentimentEngine(json);
		assertEquals(ytAnswer.getRating(), 0);
		assertEquals(ytAnswer.getConfidence(), 0.0, 0.01);
	}
	
	@Test
	public void setSentimentEngineTestVoto6() throws YoutubeException, JSONException {
		YoutubeAnswer ytAnswer = new YoutubeAnswer(this.json, 0);
		ytAnswer.setText("Ciao");
		String json = YoutubeConnector.jsonGetRequest("http://localhost:8080/test/SentimentVoto6.json", "");;
		ytAnswer.setSentimentEngine(json);
		assertEquals(ytAnswer.getRating(), 6);
		assertEquals(ytAnswer.getConfidence(), 0.0, 0.01);
	}
	
	@Test
	public void setSentimentEngineTestNull() throws YoutubeException, JSONException {
		YoutubeAnswer ytAnswer = new YoutubeAnswer(this.json, 0);
		ytAnswer.setText("Bellissimo video");
		ytAnswer.setSentimentEngine(null);
		assertEquals(ytAnswer.getRating(), -1);
		assertEquals(ytAnswer.getConfidence(), -1, 0.01);
	}
	
	@Test
	public void answerParserTest() throws YoutubeException {
		List<YoutubeAnswer> answers = YoutubeAnswer.answersParser(json, "UgzDUbfqaEILi43pdHJ4AaABAg");
		YoutubeAnswer ytAnswer = answers.get(0);
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
	public void answerParserTestNoItems() throws YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/AnswerNoItems.json", "");
		List<YoutubeAnswer> answers = null;
		try{
			YoutubeAnswer.answersParser(json, "UgzDUbfqaEILi43pdHJ4AaABAg");
		}catch(YoutubeException e) {
			assertEquals(e.getMessage(), "Error parsing json answer: No value for items");
			assertEquals(e.getErrorCode(),ErrorCode.ParsingAnswerError);
		}
		assertEquals(answers, null);
	}
	
	@Test
	public void answersParserTestWithNextPageToken() throws YoutubeException {
		YoutubeConnectorConfig.youtubeKey = "AIzaSyDIT4EW90s26Kd2X8EDbNm6pTmNU05EYJo";
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/AnswerWithNextPageToken.json", "");
		List<YoutubeAnswer> answers = YoutubeAnswer.answersParser(json, "UgwWOdZ3yRmvCLF6VaB4AaABAg");
		YoutubeAnswer ytAnswer = answers.get(0);
		assertEquals(ytAnswer.getCommentId(), "UgwWOdZ3yRmvCLF6VaB4AaABAg.8iM0cUfd_ka8jeYs57uR5P");
		assertEquals(ytAnswer.getAuthor(), "Marcello Morelli");
		assertEquals(ytAnswer.getAuthorId(), "UC0iuKsLULQWd7AlzkPVj2FQ");
		assertEquals(ytAnswer.getText(), "ma e il vostro lavoro e siete pure pagati cosa ti lamenti");
		assertEquals(ytAnswer.getPublishedAtDate(), "7/8/2018");
		assertEquals(ytAnswer.getPublishedAtTime(), "20:46:20");
		assertEquals(ytAnswer.getTimestamp(), 1533667580000L);
		assertEquals(ytAnswer.getUpdatedAt(), 1533667580000L);
		assertEquals(ytAnswer.getLike(), 0);
		assertEquals(ytAnswer.getRating(), -1);
		assertEquals(ytAnswer.getConfidence(), -1, 0.01);
	}
	
}
