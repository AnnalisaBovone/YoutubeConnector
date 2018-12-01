package com.example.youtubeconnector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;

/**
 * 
 * @author annal
 *
 */
public class YoutubeAnswer {
	@Id
	protected String commentId;
	protected String author;
	protected String authorId;
	protected String text;
	protected String publishedAtDate;
	protected String publishedAtTime;
	protected long timestamp;
	protected long updatedAt;
	protected int like;
	protected int rating;
	protected double confidence;
	
	public YoutubeAnswer() {
		
	}
	
	/**
	 * creazione di oggetto YoutubeAnswer dalla risposta json delle API di YT
	 * 
	 * @param json risposta della chiamata alle API di Youtube per ottenere tutte le risposte ad un determinato commento
	 * @param i scorre le risposte allo stesso commento
	 * @throws JSONException
	 */
	public YoutubeAnswer(String json, int i) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray item = jsonObject.getJSONArray("items");
		JSONObject p = item.getJSONObject(i);
		this.commentId = p.getString("id");
		JSONObject snippet = p.getJSONObject("snippet");
		this.author = snippet.getString("authorDisplayName");
		JSONObject authorChannelId = snippet.getJSONObject("authorChannelId");
		this.authorId = authorChannelId.getString("value");
		this.text = snippet.getString("textDisplay");
		this.like = -1;
		if(snippet.has("likeCount")) {
			this.like = snippet.getInt("likeCount");
		}
		String[] publishedAt = YoutubeConnector.setData(snippet.getString("publishedAt"));
		this.publishedAtDate = publishedAt[0];
		this.publishedAtTime = publishedAt[1];
		this.timestamp = new Long(publishedAt[2]);
		String[] updatedAt = YoutubeConnector.setData(snippet.getString("updatedAt"));
		this.updatedAt = new Long(updatedAt[2]);
		this.rating = -1;
		this.confidence = -1;
	}
	
	public YoutubeAnswer(String commentId, String author, String authorId, String text, String publishedAtDate,
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
	}
	
	public String getCommentId() {
		return commentId;
	}
	
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getAuthorId() {
		return authorId;
	}
	
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
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
	
	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getLike() {
		return like;
	}
	
	public void setLike(int like) {
		this.like = like;
	}
	
	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	/**
	 * assegna al testo del commento il voto del Sentiment Engine attraverso la risposta alle API di X2Check
	 * 
	 * @param json contiene la risposta del Sentiment Engine
	 * @throws JSONException
	 */
	public void setSentimentEngine(String json) throws JSONException {
		if(json != null) {
			JSONObject jsonObject = new JSONObject(json);
			int rating = jsonObject.getInt("sentiment");
			JSONArray distr = jsonObject.getJSONArray("distr");
			double confidence = 0.0;
			if(rating>0 && rating<6){
				confidence = distr.getDouble(rating-1);
			}
			this.rating = rating;
			this.confidence = confidence;
		}
	}

	/**
	 * interpreta il json e crea tutte le risposte lasciate ad uno specifico commento
	 * 
	 * @param json risposta della chiamata alle API di Youtube per ottenere tutte le risposte ad un commento
	 * @param commentId stringa che contiene l'id del commento di cui si vogliono prendere le risposte
	 * @return tutte le risposte legate al commento specifico
	 * @throws YoutubeException cattura errori di parsing
	 */
	public static ArrayList<YoutubeAnswer> answersParser(String json, String commentId) throws YoutubeException{
		ArrayList<YoutubeAnswer> answers = new ArrayList<YoutubeAnswer>();
		String nextPageToken = "";
		try {
			JSONObject jsonObject = new JSONObject(json);
			if(jsonObject.has("nextPageToken")){
				nextPageToken = jsonObject.getString("nextPageToken");
			}
			JSONArray item = jsonObject.getJSONArray("items");
			for(int i=0; i<item.length(); i++){
				JSONObject p = item.getJSONObject(i);
				String answerId = p.getString("id");
				JSONObject info = p.getJSONObject("snippet");
				YoutubeAnswer answer = new YoutubeAnswer(json, i);
				answers.add(answer);
			}
		}catch(JSONException e) {
			throw new YoutubeException("Error parsing json answer: " + e.getMessage(), ErrorCode.ParsingAnswerError);
		}
		if(nextPageToken != ""){
			String request = YoutubeConnector.createRequest(commentId, RequestType.Answers, nextPageToken);
			String jsony = YoutubeConnector.jsonGetRequest(request, "");
			answers.addAll(answersParser(jsony, commentId));
		}
		return answers;
	}
}
