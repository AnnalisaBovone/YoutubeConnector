package test_strutturali;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.example.youtubeconnector.UpdateVideo;

@RunWith(Suite.class)
@SuiteClasses({
	YoutubeVideoTest.class,
	UpdateVideoTest.class,
	YoutubeChannelTest.class,
	YoutubeCommentTest.class,
	YoutubeAnswerTest.class,
	YoutubeConnectorTest.class,
	YoutubeControllerTest.class,
	YoutubeConfigTest.class
})
public class AllTests {

}