package test_strutturali;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.example.youtubeconnector.YoutubeConnectorConfig;

public class YoutubeConfigTest {
	private YoutubeConnectorConfig ytConfig;

	@Before
	public void setUpBeforeClass() {
		ytConfig = new YoutubeConnectorConfig();
	}
	
	@Test
	public void getterTest() {
		assertArrayEquals(ytConfig.getUrl(), null);
		assertEquals(ytConfig.getTime(), null);
		assertEquals(ytConfig.getSentimentKey(), "MTgzMTIzNTctMDdERi00NzRDLThFNzEtNDFCQzMzNTlFNDc1");
		assertEquals(ytConfig.getYoutubeKey(), "AIzaSyDIT4EW90s26Kd2X8EDbNm6pTmNU05EYJo");
	}
	
	@Test
	public void setterTest() {
		String [] url = {"video-id1", "video-id2"};
		ytConfig.setUrl(url);
		assertArrayEquals(ytConfig.getUrl(), url);
		ytConfig.setTime("10:30");
		assertEquals(ytConfig.getTime(), "10:30");
		ytConfig.setSentimentKey("chiave sentiment");
		assertEquals(ytConfig.getSentimentKey(), "chiave sentiment");
		ytConfig.setYoutubeKey("chiave youtube");
		assertEquals(ytConfig.getYoutubeKey(), "chiave youtube");
	}
	
	@Test
	public void checkTest1() {
		String [] url = {"video-id1", "video-id2"};
		ytConfig.setUrl(url);
		ytConfig.setTime("hh:mm");
		ytConfig.setSentimentKey("chiave sentiment");
		ytConfig.setYoutubeKey("chiave youtube");
		assertFalse(ytConfig.check());
	}
	
	@Test
	public void checkTest2() {
		String [] url = {"video-id1", "video-id2"};
		ytConfig.setUrl(url);
		ytConfig.setTime("10:30");
		ytConfig.setSentimentKey(null);
		ytConfig.setYoutubeKey("chiave youtube");
		assertFalse(ytConfig.check());
	}
	
	@Test
	public void checkTest3() {
		String [] url = {"video-id1", "video-id2"};
		ytConfig.setUrl(url);
		ytConfig.setTime("10:30");
		ytConfig.setSentimentKey("chiave sentiment");
		ytConfig.setYoutubeKey(null);
		assertFalse(ytConfig.check());
	}
	
	@Test
	public void checkTest4() {
		String [] url = {"video-id1", "video-id2"};
		ytConfig.setUrl(url);
		ytConfig.setTime("10:30");
		ytConfig.setSentimentKey("chiave sentiment");
		ytConfig.setYoutubeKey("chiave youtube");
		assertTrue(ytConfig.check());
	}
	
	@Test
	public void checkTest5() {
		ytConfig.setUrl(null);
		ytConfig.setTime("10:30");
		ytConfig.setSentimentKey("chiave sentiment");
		ytConfig.setYoutubeKey("chiave youtube");
		assertFalse(ytConfig.check());
	}
}
