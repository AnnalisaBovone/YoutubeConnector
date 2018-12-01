package test_strutturali;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;
import com.example.youtubeconnector.RequestType;
import com.example.youtubeconnector.UpdateVideo;
import com.example.youtubeconnector.YoutubeConnector;
import com.example.youtubeconnector.YoutubeVideo;

public class YoutubeVideoTest {

	private String json;
	private YoutubeVideo ytVideo;
	
	@Before
	public void setUpBeforeClass() throws YoutubeException {
		this.json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Video.json", "");
		this.ytVideo = new YoutubeVideo(json);
	}
	
	@Test
	public void youtubeVideoTest() throws YoutubeException {
		YoutubeVideo ytVideo = new YoutubeVideo(this.json);
		assertEquals(ytVideo.getVideoId(), "9JYPTJ4dSYQ");
		assertEquals(ytVideo.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(ytVideo.getChannelId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytVideo.getPublishedAtDate(), "8/6/2018");
		assertEquals(ytVideo.getPublishedAtTime(), "23:2:4");
		assertEquals(ytVideo.getTimestamp(), 1528491724000L);
		assertEquals(ytVideo.getDuration(), "00:04:06");
		assertEquals(ytVideo.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(ytVideo.getDescription(), "");
		assertEquals(ytVideo.getComments(), new ArrayList<String>());
		assertEquals(ytVideo.getUpdates(), new ArrayList<UpdateVideo>());
	}
	
	@Test
	public void youtubeVideoTestParsingError() throws YoutubeException {
		YoutubeVideo ytVideo = null;
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/VideoParsingError.json", "");
		try {
			ytVideo = new YoutubeVideo(json);
		}catch (YoutubeException e) {
			assertEquals(e.getMessage(), "Error parsing json video: No value for pageInfo");
			assertEquals(e.getErrorCode(),ErrorCode.ParsingVideoError);
		}
		assertEquals(ytVideo, null);
	}
	
	@Test
	public void youtubeVideoTestParsingError2() throws YoutubeException {
		YoutubeVideo ytVideo = null;
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/VideoParsingError2.json", "");
		try {
			ytVideo = new YoutubeVideo(json);
		}catch (YoutubeException e) {
			assertEquals(e.getMessage(), "Error parsing json video: No value for title");
			assertEquals(e.getErrorCode(),ErrorCode.ParsingVideoError);
		}
		assertEquals(ytVideo, null);
	}
	
	@Test
	public void youtubeVideoTestTotalResult() throws YoutubeException {
		YoutubeVideo ytVideo = null;
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/VideoTotalResult.json", "");
		try {
			ytVideo = new YoutubeVideo(json);
		}catch (YoutubeException e) {
			assertEquals(e.getMessage(), "video not found");
			assertEquals(e.getErrorCode(),ErrorCode.VideoNotFound);
		}
		assertEquals(ytVideo, null);
	}
	
	@Test
	public void youtubeVideoTestNullParameter() {
		YoutubeVideo ytVideo = null;
		try {
			ytVideo = new YoutubeVideo(null);
		} catch (YoutubeException e) {
			assertEquals(e.getMessage(), "json string for video is null");
			assertEquals(e.getErrorCode(),ErrorCode.ParsingVideoError);
		}
		assertEquals(ytVideo, null);
	}
	
	@Test
	public void youtubeVideoTestNoParameters() {
		YoutubeVideo ytVideo = new YoutubeVideo();
		assertEquals(ytVideo.getVideoId(), null);
		assertEquals(ytVideo.getTitle(), null);
		assertEquals(ytVideo.getChannelId(), null);
		assertEquals(ytVideo.getPublishedAtDate(), null);
		assertEquals(ytVideo.getPublishedAtTime(), null);
		assertEquals(ytVideo.getTimestamp(), 0);
		assertEquals(ytVideo.getDuration(), null);
		assertEquals(ytVideo.getThumbnails(), null);
		assertEquals(ytVideo.getDescription(), null);
		assertEquals(ytVideo.getUpdates(), null);
		assertEquals(ytVideo.getComments(), null);
	}
	
	@Test
	public void getterTest() {
		assertEquals(ytVideo.getVideoId(), "9JYPTJ4dSYQ");
		assertEquals(ytVideo.getTitle(), "English Prototype \"Aimed Tour\"");
		assertEquals(ytVideo.getChannelId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytVideo.getPublishedAtDate(), "8/6/2018");
		assertEquals(ytVideo.getPublishedAtTime(), "23:2:4");
		assertEquals(ytVideo.getTimestamp(), 1528491724000L);
		assertEquals(ytVideo.getDuration(), "00:04:06");
		assertEquals(ytVideo.getThumbnails(), "https://i.ytimg.com/vi/9JYPTJ4dSYQ/sddefault.jpg");
		assertEquals(ytVideo.getDescription(), "");
		assertEquals(ytVideo.getUpdates(), new ArrayList<UpdateVideo>());
		assertEquals(ytVideo.getComments(), new ArrayList<String>());
	}

	@Test
	public void setterTest() throws YoutubeException {
		YoutubeVideo ytVideo = new YoutubeVideo(this.json);
		ytVideo.setVideoId("nuovo id video");
		assertEquals(ytVideo.getVideoId(), "nuovo id video");
		ytVideo.setTitle("nuovo titolo video");
		assertEquals(ytVideo.getTitle(), "nuovo titolo video");
		ytVideo.setChannelId("nuovo id channel");
		assertEquals(ytVideo.getChannelId(), "nuovo id channel");
		ytVideo.setPublishedAtDate("nuova data");
		assertEquals(ytVideo.getPublishedAtDate(), "nuova data");
		ytVideo.setPublishedAtTime("nuova ora");
		assertEquals(ytVideo.getPublishedAtTime(), "nuova ora");
		ytVideo.setTimestamp(1528491733000L);
		assertEquals(ytVideo.getTimestamp(), 1528491733000L);
		ytVideo.setDuration("nuova durata");
		assertEquals(ytVideo.getDuration(), "nuova durata");
		ytVideo.setThumbnails("nuova immagine anteprima");
		assertEquals(ytVideo.getThumbnails(), "nuova immagine anteprima");
		ytVideo.setDescription("nuova descrizione");
		assertEquals(ytVideo.getDescription(), "nuova descrizione");
		List<UpdateVideo> updates = new ArrayList<UpdateVideo>();
		UpdateVideo update = new UpdateVideo("nuova data", 10, 5, 20);
		updates.add(update);
		ytVideo.setUpdates(updates);
		assertEquals(ytVideo.getUpdates(), updates);
		List<String> comments = new ArrayList<String>();
		String commentId = "nuovo id commento";
		comments.add(commentId);
		ytVideo.setComments(comments);
		assertEquals(ytVideo.getComments(), comments);
	}
	
	@Test
	public void addUpdateTest() throws YoutubeException {
		YoutubeVideo ytVideo = new YoutubeVideo(this.json);
		List<UpdateVideo> updates = ytVideo.getUpdates();
		UpdateVideo update = new UpdateVideo("nuova data", 10, 5, 20);
		updates.add(update);
		ytVideo.addUpdate(update);
		assertEquals(ytVideo.getUpdates(), updates);
	}
	
	@Test
	public void addUpdateTest2() throws YoutubeException {
		YoutubeVideo ytVideo = new YoutubeVideo(this.json);
		List<UpdateVideo> updates = ytVideo.getUpdates();
		UpdateVideo update = new UpdateVideo("nuova data", 10, 5, 20);
		ytVideo.addUpdate(update);
		assertEquals(ytVideo.getUpdates(), updates);
	}
	
	@Test
	public void addUpdateTest3() throws YoutubeException {
		YoutubeVideo ytVideo = new YoutubeVideo(this.json);
		UpdateVideo update = new UpdateVideo("oggi", 20, 10, 40);
		UpdateVideo update2 = new UpdateVideo("ieri", 18, 9, 35);
		List<UpdateVideo> updates = new ArrayList<UpdateVideo>();
		updates.add(update);
		updates.add(update2);
		ytVideo.setUpdates(updates);
		UpdateVideo update3 = new UpdateVideo("domani", 22, 20, 50);
		ytVideo.addUpdate(update3);
		updates.add(update3);
		assertEquals(ytVideo.getUpdates(), updates);
	}
	
	
	@Test
	public void addComment() throws YoutubeException {
		YoutubeVideo ytVideo = new YoutubeVideo(this.json);
		List<String> commentIds = ytVideo.getComments();
		String commentId = "nuovo id commento";
		commentIds.add(commentId);
		ytVideo.addComment(commentId);
		assertEquals(ytVideo.getComments(), commentIds);
	}
}
