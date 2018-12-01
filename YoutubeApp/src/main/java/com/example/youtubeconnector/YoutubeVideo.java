package com.example.youtubeconnector;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
@Document(collection = "Videos")
public class YoutubeVideo {
	@Id
	private String videoId;
	private String title;
	private String channelId;
	private String publishedAtDate;
	private String publishedAtTime;
	private long timestamp;
	private String duration;
	private String thumbnails;
	private String description;
	private List<UpdateVideo> updates;
	private List<String> comments;
	
	public YoutubeVideo() {
		
	}
	
	/**
	 * creazione di un oggetto YoutubeVideo dalla risposta json alle API di Youtube
	 * 
	 * @param json risposta della chiamata alle API di Youtube per ottenere informazioni sul video
	 * @throws YoutubeException cattura possibili errori quali video non trovato, errore nel parsing
	 */
	public YoutubeVideo(String json) throws YoutubeException {
		if(json != null) {
			JSONObject jsonObject = null;
			int totalResults = 0;
			try {
				jsonObject = new JSONObject(json);
				JSONObject pageInfo = jsonObject.getJSONObject("pageInfo");
				totalResults = pageInfo.getInt("totalResults");
			}catch (JSONException e) {
				throw new YoutubeException("Error parsing json video: " + e.getMessage(), ErrorCode.ParsingVideoError);
			}
				if(totalResults > 0) {
					try {
						JSONArray items = jsonObject.getJSONArray("items");
						JSONObject p = items.getJSONObject(0);
						this.videoId = p.getString("id");
						JSONObject snippet = p.getJSONObject("snippet");
						String[] publishedAt = YoutubeConnector.setData(snippet.getString("publishedAt"));
						this.publishedAtDate = publishedAt[0];
						this.publishedAtTime = publishedAt[1];
						this.timestamp = new Long(publishedAt[2]);
						this.channelId = snippet.getString("channelId");
						this.title = snippet.getString("title");
						this.description = snippet.getString("description");
						JSONObject thumbnailsObject = snippet.getJSONObject("thumbnails");
						JSONObject h = thumbnailsObject.getJSONObject("standard");
						this.thumbnails = h.getString("url");
						JSONObject contentDetails = p.getJSONObject("contentDetails");
						this.duration = (YoutubeConnector.timeConverter(contentDetails.getString("duration"))).toString();
					}catch(JSONException e) {
						throw new YoutubeException("Error parsing json video: " + e.getMessage(), ErrorCode.ParsingVideoError);
					}
					this.updates = new ArrayList<UpdateVideo>();
					this.comments = new ArrayList<String>();
				}else {
					throw new YoutubeException("video not found", ErrorCode.VideoNotFound);
				}
		}else {
			throw new YoutubeException("json string for video is null", ErrorCode.ParsingVideoError);
		}
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPublishedAtDate() {
		return publishedAtDate;
	}

	public void setPublishedAtDate(String publishedAtDate) {
		this.publishedAtDate = publishedAtDate;
	}

	public String getPublishedAtTime() {
		return publishedAtTime;
	}

	public void setPublishedAtTime(String publishedAtTime) {
		this.publishedAtTime = publishedAtTime;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(String thumbnails) {
		this.thumbnails = thumbnails;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<UpdateVideo> getUpdates() {
		return updates;
	}

	public void setUpdates(List<UpdateVideo> updates) {
		this.updates = updates;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	
	/**
	 * se la data del nuovo aggiornamento del video non &egrave; presente, l'update viene aggiunta 
	 * altrimenti viene sostituita
	 * 
	 * @param updateVideo oggetto che contiene la data dell'aggiornamento nel formato stringa gg/mm/aaaa 
	 * e il numero di "mi piace", "non mi piace" e visualizzazioni al video nel momento dell'aggiornamento
	 */
	public void addUpdate(UpdateVideo updateVideo) {
		for(int i=0; i<this.updates.size(); i++) {
			UpdateVideo update = this.updates.get(i);
			if(update.getDate().equals(updateVideo.getDate())) {
				this.updates.remove(i);
				this.updates.add(i, updateVideo);
				return;
			}
		}
		this.updates.add(updateVideo);
	}
	
	/**
	 * aggiunge l'id del commento alla lista di id di commenti lasciati al video specifico
	 * 
	 * @param commentId stringa che contiene l'id del commento lasciato al video
	 */
	public void addComment(String commentId) {
		this.comments.add(commentId);
	}

	

}
