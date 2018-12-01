package test_strutturali;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;
import com.example.youtubeconnector.UpdateVideo;
import com.example.youtubeconnector.YoutubeConnector;
import com.example.youtubeconnector.YoutubeVideo;

public class UpdateVideoTest {
	private String json;
	private UpdateVideo update;
	
	@Before
	public void setUpBeforeClass() throws YoutubeException {
		this.json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Video.json", "");
		this.update = new UpdateVideo(json);
	}
	
	@Test
	public void updateVideoTest() throws YoutubeException {
		UpdateVideo update = new UpdateVideo(this.json);
		String actualDate = Calendar.getInstance().get(Calendar.DATE) + "/" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
		assertEquals(update.getDate(), actualDate);
		assertEquals(update.getLike(), 1);
		assertEquals(update.getDislike(), 0);
		assertEquals(update.getViews(), 6);
	}
	
	@Test
	public void updateVideoParsingError() {
		
	}
	
	@Test
	public void updateVideoTestNullParameter() {
		UpdateVideo update = null;
		try {
			update = new UpdateVideo(null);
		} catch (YoutubeException e) {
			assertEquals(e.getMessage(), "string json for updatevideo is null");
			assertEquals(e.getErrorCode(),ErrorCode.ParsingUpdateVideoError);
		}
		assertEquals(update, null);
	}
	
	@Test
	public void updateVideoTestNoParameters() {
		UpdateVideo update = new UpdateVideo();
		assertEquals(update.getDate(), null);
		assertEquals(update.getLike(), 0);
		assertEquals(update.getDislike(), 0);
		assertEquals(update.getViews(), 0);
	}
	
	@Test
	public void updateVideoTestParsingError() throws YoutubeException {
		UpdateVideo update = null;
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/VideoParsingError.json", "");
		try {
			update = new UpdateVideo(json);
		}catch (YoutubeException e) {
			assertEquals(e.getMessage(), "Error parsing json update video: No value for pageInfo");
			assertEquals(e.getErrorCode(),ErrorCode.ParsingUpdateVideoError);
		}
		assertEquals(update, null);
	}
	
	@Test
	public void updateVideoTestParsingError2() throws YoutubeException {
		UpdateVideo update = null;
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/UpdateVideoParsingError.json", "");
		try {
			update = new UpdateVideo(json);
		}catch (YoutubeException e) {
			assertEquals(e.getMessage(), "Error parsing json update video: No value for viewCount");
			assertEquals(e.getErrorCode(),ErrorCode.ParsingUpdateVideoError);
		}
		assertEquals(update, null);
	}
	
	@Test
	public void youtubeVideoTestNoLike() throws YoutubeException {
		UpdateVideo update = null;
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/UpdateVideoNoLike.json", "");
		update = new UpdateVideo(json);
		String actualDate = Calendar.getInstance().get(Calendar.DATE) + "/" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
		assertEquals(update.getDate(), actualDate);
		assertEquals(update.getLike(), -1);
		assertEquals(update.getDislike(), 0);
		assertEquals(update.getViews(), 6);
	}
	
	@Test
	public void youtubeVideoTestNoDislike() throws YoutubeException {
		UpdateVideo update = null;
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/UpdateVideoNoDislike.json", "");
		update = new UpdateVideo(json);
		String actualDate = Calendar.getInstance().get(Calendar.DATE) + "/" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
		assertEquals(update.getDate(), actualDate);
		assertEquals(update.getLike(), 1);
		assertEquals(update.getDislike(), -1);
		assertEquals(update.getViews(), 6);
	}
	
	@Test
	public void updateVideoTestTotalResult() throws YoutubeException {
		UpdateVideo update = null;
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/VideoTotalResult.json", "");
		try {
			update = new UpdateVideo(json);
		}catch (YoutubeException e) {
			assertEquals(e.getMessage(), "video not found");
			assertEquals(e.getErrorCode(),ErrorCode.VideoNotFound);
		}
		assertEquals(update, null);
	}
	
	@Test
	public void getterTest() throws YoutubeException {
		UpdateVideo update = new UpdateVideo(this.json);
		String actualDate = Calendar.getInstance().get(Calendar.DATE) + "/" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
		assertEquals(update.getDate(), actualDate);
		assertEquals(update.getLike(), 1);
		assertEquals(update.getDislike(), 0);
		assertEquals(update.getViews(), 6);
	}
	
	@Test
	public void setterTest() throws YoutubeException {
		UpdateVideo update = new UpdateVideo(this.json);
		String date = "nuova data";
		update.setDate(date);
		assertEquals(update.getDate(), date);
		update.setLike(10);
		assertEquals(update.getLike(), 10);
		update.setDislike(5);
		assertEquals(update.getDislike(), 5);
		update.setViews(20);
		assertEquals(update.getViews(), 20);
	}
}
