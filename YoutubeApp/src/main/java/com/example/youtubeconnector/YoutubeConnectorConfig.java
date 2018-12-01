package com.example.youtubeconnector;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author annal
 *
 */
@Component
@ConfigurationProperties(prefix = "config")
public class YoutubeConnectorConfig {
	private String[] url;
	private String time;
	public static String youtubeKey;
	public static String sentimentKey;
	
	public String[] getUrl() {
		return url;
	}
	
	public void setUrl(String[] url) {
		this.url = url;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}

	public String getYoutubeKey() {
		return youtubeKey;
	}

	public void setYoutubeKey(String youtubeKey) {
		YoutubeConnectorConfig.youtubeKey = youtubeKey;
	}

	public String getSentimentKey() {
		return sentimentKey;
	}

	public void setSentimentKey(String sentimentKey) {
		YoutubeConnectorConfig.sentimentKey = sentimentKey;
	}
	
	/**
	 * controlla se il file di configurazione &egrave; stato impostato
	 * 
	 * @return vero se il file è stato configurato, falso altrimenti
	 */
	public boolean check() {
		if(this.youtubeKey == null) {
			return false;
		}
		if(this.sentimentKey == null) {
			return false;
		}
		if(this.time.equals("hh:mm")) {
			return false;
		}
		if(this.url == null) {
			return false;
		}
		return true;
	}
}
