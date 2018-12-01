package test_strutturali;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;
import com.example.youtubeconnector.YoutubeChannel;
import com.example.youtubeconnector.YoutubeConnector;
import com.example.youtubeconnector.YoutubeVideo;

public class YoutubeChannelTest {
	private String json;
	private YoutubeChannel ytChannel;
	
	@Before
	public void setUpBeforeClass() throws YoutubeException {
		this.json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Video.json", "");
		this.ytChannel = new YoutubeChannel(json);
	}
	
	@Test
	public void youtubeChannelTest() throws YoutubeException {
		YoutubeChannel ytChannel = new YoutubeChannel(this.json);
		assertEquals(ytChannel.getChannelId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytChannel.getChannelTitle(), "Annalisa Bovone");
		assertEquals(ytChannel.getSubscribers(), -1);
		assertEquals(ytChannel.getVideoIds(), new ArrayList<String>());
	}
	
	@Test
	public void youtubeChannelTest1() throws YoutubeException {
		YoutubeChannel ytChannel = new YoutubeChannel("UCnlgdfqucEal83r8MvcBXtQ", "Annalisa Bovone");
		assertEquals(ytChannel.getChannelId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytChannel.getChannelTitle(), "Annalisa Bovone");
		assertEquals(ytChannel.getSubscribers(), -1);
		assertEquals(ytChannel.getVideoIds(), new ArrayList<String>());
	}
	
	@Test
	public void youtubeChannelTestParsingError() throws YoutubeException {
		YoutubeChannel ytChannel = null;
		String json = YoutubeConnector.jsonGetRequest("http://localhost:8080/test/ChannelParsingError.json", "");
		try{
			ytChannel = new YoutubeChannel(json);
		}catch(YoutubeException e) {
			assertEquals(e.getMessage(), "Error parsing json channel: No value for channelId");
			assertEquals(e.getErrorCode(),ErrorCode.ParsingChannelError);
		}
		assertEquals(ytChannel, null);
	}
	
	@Test
	public void youtubeChannelTestNoParameter() {
		YoutubeChannel ytChannel = new YoutubeChannel();
		assertEquals(ytChannel.getChannelId(), null);
		assertEquals(ytChannel.getChannelTitle(), null);
		assertEquals(ytChannel.getSubscribers(), 0);
		assertEquals(ytChannel.getVideoIds(), null);
	}
	
	@Test
	public void getterTest() throws YoutubeException {
		YoutubeChannel ytChannel = new YoutubeChannel(this.json);
		assertEquals(ytChannel.getChannelId(), "UCnlgdfqucEal83r8MvcBXtQ");
		assertEquals(ytChannel.getChannelTitle(), "Annalisa Bovone");
		assertEquals(ytChannel.getSubscribers(), -1);
		assertEquals(ytChannel.getVideoIds(), new ArrayList<String>());
	}
	
	@Test
	public void setterTest() throws YoutubeException {
		YoutubeChannel ytChannel = new YoutubeChannel(this.json);
		ytChannel.setChannelId("nuovo channelId");
		assertEquals(ytChannel.getChannelId(), "nuovo channelId");
		ytChannel.setChannelTitle("nuovo channelTitle");
		assertEquals(ytChannel.getChannelTitle(), "nuovo channelTitle");
		List<String> videoIds = new ArrayList<String>();
		videoIds.add("nuovo idVideo");
		ytChannel.setVideoIds(videoIds);
		assertEquals(ytChannel.getVideoIds(), videoIds);
		ytChannel.setSubscribers(10);
		assertEquals(ytChannel.getSubscribers(), 10);
	}
	
	@Test
	public void addVideo() throws YoutubeException {
		YoutubeChannel ytChannel = new YoutubeChannel(this.json);
		List<String> videoIds = ytChannel.getVideoIds();
		String videoId = "nuovo id video";
		videoIds.add(videoId);
		ytChannel.addVideo(videoId);
		assertEquals(ytChannel.getVideoIds(), videoIds);
	}
	
	@Test
	public void setSubscribersTest() throws YoutubeException {
		String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/Channel.json", "");
		YoutubeChannel ytChannel = new YoutubeChannel(this.json);
		ytChannel.setSubscribers(json);
		assertEquals(ytChannel.getSubscribers(), 1);
	}
	
	@Test
	public void setSubscribersTestJsonNull() throws YoutubeException {
		YoutubeChannel ytChannel = new YoutubeChannel(this.json);
		ytChannel.setSubscribers(0);
		try { 
			ytChannel.setSubscribers(null);
		}catch(YoutubeException e) {
			assertEquals(e.getMessage(), "json string for updatechannel null");
			assertEquals(e.getErrorCode(), ErrorCode.ParsingUpdateChannelError);
		}
		assertEquals(ytChannel.getSubscribers(), 0);
	}
	
	@Test
	public void setSubscribersTestNoSubscribers() throws YoutubeException {
		YoutubeChannel ytChannel = new YoutubeChannel(this.json);
		ytChannel.setSubscribers(0);
		try { 
			String json =  YoutubeConnector.jsonGetRequest("http://localhost:8080/test/ChannelNoSubscribers.json", "");
			ytChannel.setSubscribers(json);
		}catch(YoutubeException e) {
			assertEquals(e.getMessage(), "Error parsing json update channel: No value for subscriberCount");
			assertEquals(e.getErrorCode(), ErrorCode.ParsingUpdateChannelError);
		}
		assertEquals(ytChannel.getSubscribers(), 0);
	}
	
	
	
}
