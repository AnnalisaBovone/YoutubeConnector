package com.example.youtubeconnector;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;

/**
 * 
 * @author annal
 *
 */
public class UpdateVideo {
	private String date;
	private int like;
	private int dislike;
	private int views;
	
	public UpdateVideo() {
		
	}
	
	/**
	 * creazione di un oggetto UpdateVideo dalla risposta json alle API di Youtube
	 * 
	 * @param json risposta della chiamata alle API di Youtube per ottenere informazioni sul video
	 * @throws YoutubeException cattura possibili errori quali video non trovato, errore nel parsing
	
	 */
	public UpdateVideo(String json) throws YoutubeException {
		if(json != null) { 
			JSONObject jsonObject = null;
			int totalResults = 0;
			try {
				jsonObject = new JSONObject(json);
				JSONObject pageInfo = jsonObject.getJSONObject("pageInfo");
				totalResults = pageInfo.getInt("totalResults");
			}catch(JSONException e) {
				throw new YoutubeException("Error parsing json update video: " + e.getMessage(), ErrorCode.ParsingUpdateVideoError);
			}
			if(totalResults > 0) {
				try {
					JSONArray items = jsonObject.getJSONArray("items");
					JSONObject p = items.getJSONObject(0);
					JSONObject statistics = p.getJSONObject("statistics");
					this.views = Integer.parseInt(statistics.getString("viewCount"));
					this.like = -1;
					if(statistics.has("likeCount")){
						this.like = Integer.parseInt(statistics.getString("likeCount"));
					}
					this.dislike = -1;
					if(statistics.has("dislikeCount")){
						this.dislike = Integer.parseInt(statistics.getString("dislikeCount"));
					}
				}catch(JSONException e) {
					throw new YoutubeException("Error parsing json update video: " + e.getMessage(), ErrorCode.ParsingUpdateVideoError);
				}
				this.date = Calendar.getInstance().get(Calendar.DATE) + "/" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
			}else {
				throw new YoutubeException("video not found", ErrorCode.VideoNotFound);
			}
		}else {
			throw new YoutubeException("string json for updatevideo is null", ErrorCode.ParsingUpdateVideoError);
		}
	}

	public UpdateVideo(String date, int like, int dislike, int views) {
		super();
		this.date = date;
		this.like = like;
		this.dislike = dislike;
		this.views = views;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public int getDislike() {
		return dislike;
	}

	public void setDislike(int dislike) {
		this.dislike = dislike;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}
}
