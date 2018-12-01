package com.example.youtubeconnector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/result")
public class YoutubeConnectorController {
	@Autowired
	private YoutubeChannelRepository youtubeChannelRepository;
	
	@Autowired
	private YoutubeVideoRepository youtubeVideoRepository;
	
	@Autowired
	private YoutubeCommentRepository youtubeCommentRepository;
	
	@Autowired
	private YoutubeConnectorConfig youtubeConnectorConfig;
	
	public YoutubeConnectorController(YoutubeChannelRepository youtubeChannelRepository, YoutubeVideoRepository youtubeVideoRepository, YoutubeCommentRepository youtubeCommentRepository, YoutubeConnectorConfig youtubeConnectorConfig) {
		this.youtubeChannelRepository = youtubeChannelRepository;
		this.youtubeVideoRepository = youtubeVideoRepository;
		this.youtubeCommentRepository = youtubeCommentRepository;
		this.youtubeConnectorConfig = youtubeConnectorConfig;
		YoutubeConnector youtubeConnector = new YoutubeConnector(this.youtubeChannelRepository, this.youtubeVideoRepository, this.youtubeCommentRepository, this.youtubeConnectorConfig);
		Thread t = new Thread(youtubeConnector);
		t.start();
	}
	
	public YoutubeChannelRepository getYoutubeChannelRepository() {
		return youtubeChannelRepository;
	}

	public YoutubeVideoRepository getYoutubeVideoRepository() {
		return youtubeVideoRepository;
	}

	public YoutubeCommentRepository getYoutubeCommentRepository() {
		return youtubeCommentRepository;
	}

	public YoutubeConnectorConfig getYoutubeConnectorConfig() {
		return youtubeConnectorConfig;
	}	
	
	/**
	 * mostra tutti i video analizzati da Youtube Connector
	 * 
	 * @return la lista di oggetti YoutubeVideo che sono stati analizzati dal tool
	 */
	@GetMapping("/getAllVideo")
	public List<YoutubeVideo> getAllVideo(){
		return youtubeVideoRepository.findAll();
	}
	
	/**
	 * restituisce tutte le informazioni disponibili sul database per uno specifico video: le informazioni
	 * vengono trasferite come un hashMap che ha come chiave una stringa che specifica l'oggetto associato.
	 * Le informazioni che vengono restituite sono: l'oggetto YoutubeVideo con quel particolare id, lo
	 * YoutubeChannel che ha pubblicato il video, una lista di YoutubeComment associati al video, il 
	 * numero totale di "mi piace" e visualizzazioni che hanno ricevuto i video dello stesso canale 
	 * e una lista che contiene tutti i commenti e le risposte al video in ordine cronologico
	 * 
	 * @param id id del video di cui si vogliono ottenere maggiori informazioni
	 * @return hashMap che ha come chiave una stringa che specifica l'oggetto associato
	 */
	@GetMapping("/getInfoVideo/{id}")
	public HashMap<String, Object> getInfoVideo(@PathVariable String id){
		HashMap<String, Object> info = new HashMap<String, Object>();
		Optional<YoutubeVideo> optionalVideo = youtubeVideoRepository.findById(id);
		if(optionalVideo.isPresent()) {
			YoutubeVideo ytVideo = optionalVideo.get();
			info.put("video", ytVideo);
			Optional<YoutubeChannel> optionalChannel = youtubeChannelRepository.findById(ytVideo.getChannelId());
			if(optionalChannel.isPresent()) {
				YoutubeChannel ytChannel = optionalChannel.get();
				info.put("channel", ytChannel);
				List<String> videoIds = ytChannel.getVideoIds();
				int totalViews = 0;
				int totalLike = 0;
				for(String videoId: videoIds) {
					Optional<YoutubeVideo> temp = youtubeVideoRepository.findById(videoId);
					if(temp.isPresent()) {
						YoutubeVideo v = temp.get();
						int dim = v.getUpdates().size();
						totalViews = totalViews + v.getUpdates().get(dim-1).getViews();
						if(v.getUpdates().get(dim-1).getLike() != -1) { 
							totalLike = totalLike + v.getUpdates().get(dim-1).getLike();
						}
					}
				}
				info.put("totalViews", totalViews);
				info.put("totalLike", totalLike);
			}
			List<YoutubeComment> comments = new ArrayList<YoutubeComment>();
			for(int i=0; i<ytVideo.getComments().size(); i++) {
				Optional<YoutubeComment> optionalComment = youtubeCommentRepository.findById(ytVideo.getComments().get(i));
				if(optionalComment.isPresent()) {
					YoutubeComment ytComment = optionalComment.get();
					comments.add(ytComment);
				}
			}
			Collections.sort(comments, new SortByTimestamp());
			info.put("comment", comments);
			List<YoutubeAnswer> messages = new ArrayList<YoutubeAnswer>();
			for(YoutubeComment ytComment: comments) {
				messages.add(ytComment);
				messages.addAll(ytComment.getAnswer());
			}
			Collections.sort(messages, new SortByTimestamp());
			info.put("allComment", messages);
			return info;
		}
		return null;
	}
	
	/**
	 * filtra i commenti di uno specifico video che presentano nel testo una specifica parola/frase
	 * 
	 * @param input parola/frase che si vuole cercare nei commenti
	 * @param videoId id del video sui cui commenti si vuole effettuare la ricerca
	 * @return la lista di commenti che presentano l'input nel testo del commento
	 */
	@GetMapping("/getCommentFromWord/{input}/{videoId}")
	public List<YoutubeComment> getCommentFromWord(@PathVariable String input, @PathVariable String videoId){
		Optional<YoutubeVideo> optionalVideo = youtubeVideoRepository.findById(videoId);
		if(optionalVideo.isPresent()) {
			YoutubeVideo ytVideo = optionalVideo.get();
			List<String> ytCommentIds = ytVideo.getComments();
			List<YoutubeComment> ytComments = new ArrayList<YoutubeComment>();
			Boolean bool = false;
			for(String commentId: ytCommentIds) {
				bool = false;
				Optional<YoutubeComment> optionalComment = youtubeCommentRepository.findById(commentId);
				if(optionalComment.isPresent()) {
					YoutubeComment comment = optionalComment.get();
					String msg = comment.getText();
					if(Pattern.compile(Pattern.quote(input), Pattern.CASE_INSENSITIVE).matcher(msg).find()) {
						int start = msg.toLowerCase().indexOf(input.toLowerCase());
						int end = start + input.length();
						bool = true;
						msg = msg.replaceAll("(?i)" + Pattern.quote(input), "<span style=background-color:yellow>"+msg.substring(start, end)+"</span>");
						comment.setText(msg);
					}
					List<YoutubeAnswer> ytAnswers = comment.getAnswer();
					for(YoutubeAnswer ytAnswer: ytAnswers) {
						msg = ytAnswer.getText();
						if(Pattern.compile(Pattern.quote(input), Pattern.CASE_INSENSITIVE).matcher(msg).find()) {
							int start = msg.toLowerCase().indexOf(input.toLowerCase());
							int end = start + input.length();
							bool = true;
							msg = msg.replaceAll("(?i)" + Pattern.quote(input), "<span style=background-color:yellow>"+msg.substring(start, end)+"</span>");
							ytAnswer.setText(msg);
						}
					}
					if(bool) {
						ytComments.add(comment);
					}
				}
			}
			return ytComments;
		}
		return null;
	}
	
	/**
	 * filtra i commenti di uno specifico video che presentano nel testo una specifica parola/frase
	 * e sono stati pubblicati in un certo intervallo temporale
	 * 
	 * @param input parola/frase che si vuole cercare nei commenti
	 * @param from timestamp che rappresenta l'inizio dell'intervallo temporale
	 * @param to timestamp che rappresenta la fine dell'intervallo temporale
	 * @param videoId id del video sui cui commenti si vuole effettuare la ricerca
	 * @return la lista di commenti che rispettano i filtri
	 * 
	 */
	@GetMapping("getCommentFromCombineFilter/{input}/{from}/{to}/{videoId}")
	public List<YoutubeComment> getCommentFromCombineFilter(@PathVariable String input, @PathVariable long from, @PathVariable long to, @PathVariable String videoId){
		List<YoutubeComment> ytComments = new ArrayList<YoutubeComment>();
		List<YoutubeComment> comments = getCommentFromTemporalInterval(from, to, videoId);
		for(YoutubeComment comment: comments) {
			boolean bool = false;
			String msg = comment.getText();
			if(Pattern.compile(Pattern.quote(input), Pattern.CASE_INSENSITIVE).matcher(msg).find()) {
				int start = msg.toLowerCase().indexOf(input.toLowerCase());
				int end = start + input.length();
				bool = true;
				msg = msg.replaceAll("(?i)" + Pattern.quote(input), "<span style=background-color:yellow>"+msg.substring(start, end)+"</span>");
				comment.setText(msg);
			}
			List<YoutubeAnswer> ytAnswers = comment.getAnswer();
			for(YoutubeAnswer ytAnswer: ytAnswers) {
				msg = ytAnswer.getText();
				if(Pattern.compile(Pattern.quote(input), Pattern.CASE_INSENSITIVE).matcher(msg).find()) {
					int start = msg.toLowerCase().indexOf(input.toLowerCase());
					int end = start + input.length();
					bool = true;
					msg = msg.replaceAll("(?i)" + Pattern.quote(input), "<span style=background-color:yellow>"+msg.substring(start, end)+"</span>");
					ytAnswer.setText(msg);
				}
			}
			if(bool) {
				ytComments.add(comment);
			}
		}
		return ytComments;
	}
	
	/**
	 * filtra i commenti di uno specifico video che sono stati pubblicati in un certo intervallo temporale
	 * 
	 * @param from timestamp che rappresenta l'inizio dell'intervallo temporale
	 * @param to timestamp che rappresenta la fine dell'intervallo temporale
	 * @param videoId id del video sui cui commenti si vuole effettuare la ricerca
	 * @return la lista di commenti che rispettano i filtri
	 */
	@GetMapping("/getCommentFromTemporalInterval/{from}/{to}/{videoId}")
	public List<YoutubeComment> getCommentFromTemporalInterval(@PathVariable long from, @PathVariable long to, @PathVariable String videoId){
		Optional<YoutubeVideo> optionalVideo = youtubeVideoRepository.findById(videoId);
		if(optionalVideo.isPresent()) {
			YoutubeVideo ytVideo = optionalVideo.get();
			List<String> ytCommentIds = ytVideo.getComments();
			List<YoutubeComment> ytComments = new ArrayList<YoutubeComment>();
			Boolean bool = false;
			for(String commentId: ytCommentIds) {
				bool = false;
				Optional<YoutubeComment> optionalComment = youtubeCommentRepository.findById(commentId);
				if(optionalComment.isPresent()) {
					YoutubeComment comment = optionalComment.get();
					if(comment.getTimestamp()>=from && comment.getTimestamp()<=to) {
						bool = true;
					}
					List<YoutubeAnswer> ytAnswers = comment.getAnswer();
					for(YoutubeAnswer ytAnswer: ytAnswers) {
						if(ytAnswer.getTimestamp()>from && ytAnswer.getTimestamp()<to) {
							bool = true;
						}
					}
					if(bool) {
						ytComments.add(comment);
					}
				}
			}
			return ytComments;
		}
		return null;
	}
	
	/**
	 * restituisce il canale Youtube corrispondente al particolare id passato alla funzione
	 * 
	 * @param id id del canale Youtube di cui si vogliono avere informazioni
	 * @return il canale Youtube con quel particolare id
	 */
	@GetMapping("getChannelFromId/{id}")
	public YoutubeChannel getChannelFromId(@PathVariable String id) {
		Optional<YoutubeChannel> optionalChannel = youtubeChannelRepository.findById(id);
		if(optionalChannel.isPresent()) {
			YoutubeChannel ytChannel = optionalChannel.get();
			return ytChannel;
		}
		return null;
	}
	
	
}


