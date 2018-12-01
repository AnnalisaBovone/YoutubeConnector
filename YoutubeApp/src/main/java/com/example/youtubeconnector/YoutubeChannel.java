package com.example.youtubeconnector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;

/**
 * 
 * @author annal
 *
 */
@Document(collection = "youtubeChannels")
public class YoutubeChannel {
	@Id
	private String channelId;
	private String channelTitle;
	private List<String> videoIds; 
	private int subscribers;
	
	public YoutubeChannel() {
		
	}
	
	/**
	 * creazione di un oggetto YoutubeChannel dalla risposta json alle API di Youtube
	 * 
	 * @param json risposta della chiamata alle API di Youtube per ottenere informazioni sul video
	 * @throws YoutubeException cattura possibili errori nel parsing
	 * 
	 */
	public YoutubeChannel(String json) throws YoutubeException {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray items = jsonObject.getJSONArray("items");
			JSONObject p = items.getJSONObject(0);
			JSONObject snippet = p.getJSONObject("snippet");
			this.channelId = snippet.getString("channelId");
			this.channelTitle = snippet.getString("channelTitle");
			this.subscribers = -1;
		}catch(JSONException e) {
			throw new YoutubeException("Error parsing json channel: " + e.getMessage(), ErrorCode.ParsingChannelError);
		}
		this.videoIds = new ArrayList<String>();
	}
	
	public YoutubeChannel(String channelId, String channelTitle) {
		this.channelId = channelId;
		this.channelTitle = channelTitle;
		this.subscribers = -1;
		this.videoIds = new ArrayList<String>();
	}
	
	public String getChannelId() {
		return channelId;
	}
	
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	public String getChannelTitle() {
		return channelTitle;
	}
	
	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}
	
	public List<String> getVideoIds() {
		return videoIds;
	}
	
	public void setVideoIds(List<String> videoIds) {
		this.videoIds = videoIds;
	}
	
	public int getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(int subscribers) {
		this.subscribers = subscribers;
	}
	
	/**
	 * modifica il numero di iscritti ad un canale Youtube ogni volta che viene eseguito il processo
	 * 
	 * @param json risposta della chiamata alle API di Youtube per ottenere informazioni sul canale 
	 * @throws YoutubeException cattura evenutuali errori di parsing
	 */
	public void setSubscribers(String json) throws YoutubeException {
		if(json != null) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONArray items = jsonObject.getJSONArray("items");
				JSONObject p = items.getJSONObject(0);
				JSONObject statistics = p.getJSONObject("statistics");
				this.subscribers = Integer.parseInt(statistics.getString("subscriberCount"));
			}catch(JSONException e) {
				throw new YoutubeException("Error parsing json update channel: " + e.getMessage(), ErrorCode.ParsingUpdateChannelError);
			}
		}else {
			throw new YoutubeException("json string for updatechannel null", ErrorCode.ParsingUpdateChannelError);
		}
	}
	
	/**
	 * aggiunge l'id del video alla lista di id di video caricati dal canale YT
	 * 
	 * @param videoId stringa che contiene l'id del video
	 */
	public void addVideo(String videoId) {
		this.videoIds.add(videoId);
	}
}
