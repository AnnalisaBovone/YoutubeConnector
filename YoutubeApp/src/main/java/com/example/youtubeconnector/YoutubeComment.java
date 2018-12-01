package com.example.youtubeconnector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;

/**
 * 
 * @author annal
 *
 */
@Document(collection="Comments")
public class YoutubeComment extends YoutubeAnswer {
	private List<YoutubeAnswer> answer;
	
	public YoutubeComment() {
		
	}
	
	/**
	 * creazione di oggetto YoutubeComment dalla risposta json delle API di YT
	 * 
	 * @param json risposta della chiamata alle API di Youtube per ottenere tutti i commenti ad un determinato video
	 * @param i scorre tutti i commenti
	 * @throws JSONException
	 */
	public YoutubeComment(String json, int i) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray item = jsonObject.getJSONArray("items");
		JSONObject p = item.getJSONObject(i);
		this.commentId = p.getString("id");
		JSONObject snippet = p.getJSONObject("snippet");
		int totalReplyCount = snippet.getInt("totalReplyCount");
		JSONObject topLevelComment = snippet.getJSONObject("topLevelComment");
		JSONObject info = topLevelComment.getJSONObject("snippet");
		this.author = info.getString("authorDisplayName");
		JSONObject authorChannelId = info.getJSONObject("authorChannelId");
		this.authorId = authorChannelId.getString("value");
		this.text = info.getString("textDisplay");
		this.like = -1;
		if(info.has("likeCount")) {
			this.like = info.getInt("likeCount");
		}
		String[] publishedAt = YoutubeConnector.setData(info.getString("publishedAt"));
		this.publishedAtDate = publishedAt[0];
		this.publishedAtTime = publishedAt[1];
		this.timestamp = new Long(publishedAt[2]);
		String[] updatedAt = YoutubeConnector.setData(info.getString("updatedAt"));	
		this.updatedAt = new Long(updatedAt[2]);
		this.rating = -1;
		this.confidence = -1;
		this.answer = new ArrayList<YoutubeAnswer>();
	}
	
	public YoutubeComment(String commentId, String author, String authorId, String text, String publishedAtDate,
			String publishedAtTime, long timestamp, long updatedAt, int like, int rating, double confidence) {
		super();
		this.commentId = commentId;
		this.author = author;
		this.authorId = authorId;
		this.text = text;
		this.publishedAtDate = publishedAtDate;
		this.publishedAtTime = publishedAtTime;
		this.timestamp = timestamp;
		this.updatedAt = updatedAt;
		this.like = like;
		this.rating = rating;
		this.confidence = confidence;
		this.answer = new ArrayList<YoutubeAnswer>();
	}
	
	public List<YoutubeAnswer> getAnswer() {
		return answer;
	}

	public void setAnswer(ArrayList<YoutubeAnswer> answer) {
		this.answer = answer;
	}

	/**
	 * interpreta il json e crea tutti i commenti lasciati ad uno specifico video
	 * 
	 * @param json risposta della chiamata alle API di Youtube per ottenere tutti i commenti ad un video
	 * @param videoId stringa che contiene l'id del video di cui si vogliono conoscere i commenti
	 * @return tutti i commenti legati al video specifico
	 * @throws YoutubeException cattura errori di parsing
	 */
	public static ArrayList<YoutubeComment> commentsParser(String json, String videoId) throws YoutubeException{
		ArrayList<YoutubeComment> comments = new ArrayList<YoutubeComment>();
		String nextPageToken = "";
		try {
			JSONObject jsonObject = new JSONObject(json);
			if(jsonObject.has("nextPageToken")){
				nextPageToken = jsonObject.getString("nextPageToken");
			}
			JSONObject pageInfo = jsonObject.getJSONObject("pageInfo");
			int totalResults = pageInfo.getInt("totalResults");
			JSONArray item = jsonObject.getJSONArray("items");
			for(int i=0; i<totalResults; i++){
				YoutubeComment comment = new YoutubeComment(json, i);
				comments.add(comment);
			}
		}catch(JSONException e) {
			throw new YoutubeException("Error parsing json comment: " + e.getMessage(), ErrorCode.ParsingCommentError);
		}
		if(nextPageToken != ""){
			String request = YoutubeConnector.createRequest(videoId, RequestType.Comments, nextPageToken);
			String jsony = YoutubeConnector.jsonGetRequest(request, "");
			comments.addAll(commentsParser(jsony, videoId));
		}
		return comments;
	}
	
	/**
	 * aggiunge o aggiorna una risposta ad un commento
	 * 
	 * @param ytAnswer oggetto YoutubeAnswer 
	 */
	public void addAnswer(YoutubeAnswer ytAnswer) {
		for(int i=0; i<this.answer.size(); i++) {
			YoutubeAnswer answer = this.answer.get(i);
			if(answer.getCommentId().equals(ytAnswer.getCommentId())) {
				this.answer.remove(i);
				this.answer.add(i, ytAnswer);
				return;
			}
		}
		this.answer.add(ytAnswer);
	}
	
}
