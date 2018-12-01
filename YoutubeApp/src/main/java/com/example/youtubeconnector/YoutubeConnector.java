package com.example.youtubeconnector;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.example.youtubeException.ErrorCode;
import com.example.youtubeException.YoutubeException;

/**
 * 
 * @author annal
 *
 */
public class YoutubeConnector implements Runnable {
	@Autowired
	private YoutubeChannelRepository youtubeChannelRepository;
	@Autowired
	private YoutubeVideoRepository youtubeVideoRepository;
	@Autowired
	private YoutubeCommentRepository youtubeCommentRepository;
	private List<String> listVideoId;
	@Autowired
	private YoutubeConnectorConfig youtubeConnectorConfig;
	private boolean TestMode;
	private String state;
	
	public YoutubeConnector(YoutubeChannelRepository youtubeChannelRepository, YoutubeVideoRepository youtubeVideoRepository, YoutubeCommentRepository youtubeCommentRepository, YoutubeConnectorConfig youtubeConnectorConfig){
		this.youtubeChannelRepository = youtubeChannelRepository;
		this.youtubeVideoRepository = youtubeVideoRepository;
		this.youtubeCommentRepository = youtubeCommentRepository;
		this.listVideoId = new ArrayList<String>();
		this.TestMode = false;
		this.youtubeConnectorConfig = youtubeConnectorConfig;
	}
	
	@Override
	public void run() {
		if(this.youtubeConnectorConfig.check()) {
			String lastUpdate = null;
			do {
				String date = Calendar.getInstance().get(Calendar.DATE) + "/" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
				System.out.println("mi sveglio");
				if(inTime(Calendar.getInstance(), this.youtubeConnectorConfig.getTime())) {
					if(lastUpdate == null || !lastUpdate.equals(date)) {
						System.out.println("inizio il task");
						
						loadVideoId(this.youtubeConnectorConfig.getUrl());
						
						String request = "";
						String json = "";
						for(String videoId:this.listVideoId) {
							System.out.println("starting download data for id video " + videoId);
							request = createRequest(videoId, RequestType.Video, "");
							try {
								json = jsonGetRequest(request, "");
								saveVideo(videoId, json);
							} catch (YoutubeException e) {
								switch(e.getErrorCode()) {
									case ParsingVideoError:
										break;
									case ParsingChannelError:
										break;
									case ParsingUpdateVideoError:
										break;
									case ConnectionError:
										break;
									case VideoNotFound:
										break;
									case ChannelNotFound:
										break;
								}
							}
							
							request = createRequest(videoId, RequestType.Comments, "");
							try {
								json = jsonGetRequest(request, "");
								saveComment(videoId, json);
							}catch(YoutubeException e) {
								switch(e.getErrorCode()) {
									case ParsingCommentError:
										break;
									case ConnectionError:
										break;
									case VideoNotFound:
										break;
								}
							}
							
							Optional<YoutubeVideo> optionalVideo = this.youtubeVideoRepository.findById(videoId);
							if(optionalVideo.isPresent()) {
								YoutubeVideo ytVideo = optionalVideo.get();
								List<String> comments = ytVideo.getComments();
								for(String commentId: comments) {
									request = createRequest(commentId, RequestType.Answers, "");
									try {
										json = jsonGetRequest(request, "");
										saveAnswer(commentId, json);
									}catch(YoutubeException e) {
										switch(e.getErrorCode()) {
											case ParsingCommentError:
												break;
											case ParsingSentimentError:
												break;
											case ConnectionError:
												break;
											case VideoNotFound:
												break;
										}
									}
								}
								System.out.println("saving video  " + videoId);
								this.youtubeVideoRepository.save(ytVideo);
							}
						}
						
						for(YoutubeChannel ytChannel: this.youtubeChannelRepository.findAll()) {
							System.out.println("update channel  " + ytChannel.getChannelId());
							request = createRequest(ytChannel.getChannelId(), RequestType.Channel, "");
							try {
								json = jsonGetRequest(request, "");
								ytChannel.setSubscribers(json);
								this.youtubeChannelRepository.save(ytChannel);
							} catch (YoutubeException e) {
								switch(e.getErrorCode()) {
									case ParsingCommentError:
										break;
									case ParsingSentimentError:
										break;
									case ConnectionError:
										break;
									case VideoNotFound:
										break;
								}
							}
						}
						
						saveRatingForComment();
						
						lastUpdate = date;
					}else {
						System.out.println("Sono nel range ma ho già eseguito la scansione");
					}
				}else {
					this.state = "non sono nel range";
					System.out.println("Non sono nel range");
				}
				System.out.println("Fine task, dormo");
				sleep(5);
			}while(!this.TestMode);
		}else {
			this.state = "missing properties";
			System.out.println("MISSING PROPERTIES");
		}
	}
	
	/**
	 * aggiorna i commenti sul database che non hanno ancora ricevuto una votazione dal Sentiment
	 * Engine, inserendo il rating e la confidence. Se la richiesta fallisce il rating viene impostato
	 * a -1 e quindi la volta successiva che verr&agrave; lanciato il processo verr&agrave; rieseguita la richiesta
	 */
	public void saveRatingForComment() {
		String request = "";
		String json = "";
		for(YoutubeComment ytComment: this.youtubeCommentRepository.findAll()) {
			if(ytComment.getRating() == -1) {
				try {
					System.out.println("calcolo sentiment: " + ytComment.getText());
					request = createRequest("", RequestType.SentimentEngine, "");
					json = jsonGetRequest(request, "{text:\""+ytComment.getText()+"\"}");
					ytComment.setSentimentEngine(json);
				}catch(Exception e) {
					System.out.println("sentiment fallito: " + ytComment.getText());
					ytComment.setRating(-1);
				}
			}
			for(YoutubeAnswer ytAnswer: ytComment.getAnswer()) {
				if(ytAnswer.getRating() == -1) {
					try {
						System.out.println("calcolo sentiment: " + ytAnswer.getText());
						request = createRequest("", RequestType.SentimentEngine, "");
						json = jsonGetRequest(request, "{text:\""+ytAnswer.getText()+"\"}");
						ytAnswer.setSentimentEngine(json);
					}catch(Exception e) {
						System.out.println("sentiment fallito: " + ytAnswer.getText());
						ytAnswer.setRating(-1);
					}
				}
			}
			this.youtubeCommentRepository.save(ytComment);
		}
	}

	/**
	 * analizza le risposte ad un determinato commento e, se non sono ancora presenti
	 * o se sono state modificate, le aggiorna sul database
	 * 
	 * @param commentId stringa che contiene l'id del commento del quale si stanno analizzando le risposte
	 * @param json risposta alla richiesta di ottenere tutte le risposte ad un determinato commento
	 * @return la lista di risposte al commento specificato
	 * @throws YoutubeException
	 */
	public List<YoutubeAnswer> saveAnswer(String commentId, String json) throws YoutubeException {
		List<YoutubeAnswer> answers = new ArrayList<YoutubeAnswer>();
		answers = YoutubeAnswer.answersParser(json, commentId);
		List<YoutubeAnswer> ytAnswers = this.youtubeCommentRepository.findById(commentId).get().getAnswer();
		for(YoutubeAnswer answer: answers) {
			String answerId = answer.getCommentId();
			boolean bool = true;
			for(YoutubeAnswer ytAnswer: ytAnswers) {
				if(answerId.equals(ytAnswer.getCommentId())) {
					if(answer.getUpdatedAt() <= ytAnswer.getUpdatedAt()) {
						bool = false;
					}
					break;
				}
			}
			if(bool) {
				answer.setRating(-1);
				Optional<YoutubeComment> optionalComment = this.youtubeCommentRepository.findById(commentId);
				if(optionalComment.isPresent()) {
					YoutubeComment comment = optionalComment.get();
					comment.addAnswer(answer);
					this.youtubeCommentRepository.save(comment);
				}
			}
		}
		return answers;
	}

	
	/**
	 * analizza i commenti ad un determinato video e, se non sono ancora presenti
	 * o se sono stati modificati, li aggiorna sul database
	 * 
	 * @param videoId stringa che contiene l'id del video del quale si stanno analizzando i commenti
	 * @param json risposta alla richiesta di ottenere tutti i commenti ad un determinato video
	 * @return la lista di commenti al video specificato
	 * @throws YoutubeException
	 */
	public List<YoutubeComment> saveComment(String videoId, String json) throws YoutubeException {
		Optional<YoutubeVideo> optionalVideo = this.youtubeVideoRepository.findById(videoId);
		if(optionalVideo.isPresent()) {
			YoutubeVideo ytVideo = optionalVideo.get();
			List<YoutubeComment> comments = new ArrayList<YoutubeComment>();
			comments = YoutubeComment.commentsParser(json, videoId);
			for(YoutubeComment comment: comments) {
				String commentId = comment.getCommentId();
				System.out.println("Reading comment from youtube answer: " + comment.getText()); 
				Optional<YoutubeComment> optionalComment = this.youtubeCommentRepository.findById(commentId);
				//se il commento non è nel db
				if(!optionalComment.isPresent()) {
					System.out.println("The comment is not in db");
					comment.setRating(-1);
					if(this.youtubeCommentRepository.save(comment) != null) {
						System.out.println("saving comment on db");
						ytVideo.addComment(commentId);
						this.youtubeVideoRepository.save(ytVideo);
					}
				}else {
					
					YoutubeComment ytComment = optionalComment.get();	
					//se il commento è stato modificato
					if(comment.getUpdatedAt() > ytComment.getUpdatedAt()) {
						System.out.println("The comment is on db but needs update");
						comment.setRating(-1);
						this.youtubeCommentRepository.save(comment);
					}else {
						System.out.println("The comment is up to date");
					}
				}
			}
			return comments;
		}
		return null;
	}

	/**
	 * analizza il video specificato: se &egrave; gi&agrave; presente sul database aggiunge un oggetto UpdateVideo 
	 * con cui si tiene traccia dei dati variabili del video: numero di "mi piace", "non mi piace" e 
	 * visualizzazioni; altrimenti viene salvato un nuovo video sul database.
	 * 
	 * @param videoId
	 * @param json
	 * @return
	 * @throws YoutubeException
	 */
	public YoutubeVideo saveVideo(String videoId, String json) throws YoutubeException {
		YoutubeVideo ytVideo = null;
		YoutubeChannel ytChannel = null;
		Optional<YoutubeVideo> optionalVideo = this.youtubeVideoRepository.findById(videoId);
		if(!optionalVideo.isPresent()) {
			ytVideo = new YoutubeVideo(json);
			String channelId = ytVideo.getChannelId();
			Optional<YoutubeChannel> optionalChannel = this.youtubeChannelRepository.findById(channelId);
			if(!optionalChannel.isPresent()) {
				ytChannel = new YoutubeChannel(json);
			}else {
				ytChannel = optionalChannel.get();
			}
			ytChannel.addVideo(videoId);
			System.out.println("saving channel  " + channelId);
			this.youtubeChannelRepository.save(ytChannel);
		}else {
			ytVideo = optionalVideo.get();
		}
		UpdateVideo updateVideo = new UpdateVideo(json);
		ytVideo.addUpdate(updateVideo);
		this.youtubeVideoRepository.save(ytVideo);
		return ytVideo;
	}

	/**
	 * restituisce vero se l'orario attuale &egrave;:
	 * maggiore dell'orario prestabilito dal file di configurazione
	 * ma minore dei 15 minuti successivi
	 * 
	 * @param cal oggetto Calendar che indica la data e l'ora attuale del momento in cui l'oggetto viene creato
	 * @param start stringa che contiene l'orario di avvio del processo nel formato hh:mm
	 * @return
	 */
	public boolean inTime(Calendar cal, String start) {
		int i = start.indexOf(":");
		int startHour = Integer.parseInt(start.substring(0, i));
		int startMinutes = Integer.parseInt(start.substring(i+1, start.length()));
		int startMin = (startHour*60) + startMinutes;
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int actualMin = (hour*60) + minutes;
		if(actualMin >= startMin && actualMin <= startMin + 15){
			return true;
		}
		return false;
	}

	public static String streamToString(InputStream inputStream) {
		String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
	    return text;
	}
	
	/**
	 * effettua le chiamate sia a Youtube sia al Sentiment Engine, in base ai parametri
	 * 
	 * @param urlQueryString stringa che contiene la richiesta da effettuare
	 * @param body stringa che contiene il body della richiesta, presente solo quando la chiamata &egrave; 
	 * indirizzata al Sentiment Engine, contiene il testo del commento da far analizzare
	 * @return il json di risposta alla chiamata specifica
	 * @throws YoutubeException
	 */
	public static String jsonGetRequest(String urlQueryString, String body) throws YoutubeException {
		URL url = null;
		InputStream inStream = null;
		String json = null;
	    try {
	    	url = new URL(urlQueryString);
	    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    	connection.setDoOutput(true);
	    	connection.setInstanceFollowRedirects(false);
	    	connection.setRequestProperty("Content-Type", "application/json");
	    	if(body != ""){
	    		connection.setRequestMethod("POST");
	      		connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
	      		connection.getOutputStream().write(body.getBytes("UTF8"));	
	    	}else{
	    		connection.setRequestMethod("GET");
	    		connection.setRequestProperty("charset", "utf-8");
	    	}
	      	connection.connect();
	      	inStream = connection.getInputStream();
	      	json = streamToString(inStream); // input stream to string
	    } catch (IOException ex) {
	    	throw new YoutubeException("Connection failed: " + ex.getMessage(), ErrorCode.ConnectionError);
	    }
	    return json;
	}

	/**
	 * crea le particolari richieste in base ai parametri
	 * 
	 * @param id stringa che pu&ograve; contenere l'id del video, del canale Youtube o del commento da analizzare
	 * @param type tipo di richiesta, gestito dall'Enum RequestType
	 * @param npt next page token: stringa che identifica la pagina successiva della risposta
	 * @return la richiesta da effettuare
	 */
	public static String createRequest(String id, RequestType type, String npt) {
		String youtubeKey = YoutubeConnectorConfig.youtubeKey;
		String sentimentKey = YoutubeConnectorConfig.sentimentKey;
		String richiesta = "https://www.googleapis.com/youtube/v3/";
		switch(type){
			case Video:	
				richiesta += "videos?part=snippet,contentDetails,statistics&id=";
				richiesta += id + "&key=" + youtubeKey;
				break;
			case Channel:
				richiesta += "channels?part=snippet,contentDetails,statistics&id=";
				richiesta += id + "&key=" + youtubeKey;
				break;
			case Comments:
				if(npt != ""){
					richiesta += "commentThreads?part=snippet&videoId=";
					richiesta += id + "&order=time&pageToken=" + npt + "&key=" + youtubeKey;
				}else{
					richiesta += "commentThreads?part=snippet&videoId=";
					richiesta += id + "&order=time&key=" + youtubeKey;
				}
				break;
			case Answers:
				if(npt != ""){
					richiesta += "comments?part=snippet&parentId=";
					richiesta += id + "&pageToken=" + npt + "&key=" + youtubeKey;
				}else{
					richiesta += "comments?part=snippet&parentId=";
					richiesta += id + "&key=" + youtubeKey;
				}
				break;
			case SentimentEngine:
				richiesta = "https://webapp.app2check.com/api/analyze?apiKey="+ sentimentKey+"&language=it&split=false";
				break;
			default:
				System.out.println("Tipo richiesta non valido");
				richiesta = null;
		}
		return richiesta;
	}

	/**
	 * produce una pausa nel processo
	 * 
	 * @param i contiene i minuti durante i quali il processo pu&ograve; dormire
	 * @return vero se la pausa &egrave; andata a buon fine, falso se si sono verificati dei problemi
	 */
	public boolean sleep(long i) {
		try {
			Thread.sleep(i*1000*60);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	/**
	 * carica in una lista gli id dei video da analizzare
	 * 
	 * @param urls array di stringhe che contiene gli url dei video da analizzare presi dal file di configurazione
	 */
	public void loadVideoId(String[] urls) {
		String videoId = "";
		ArrayList<String> videoIds = new ArrayList<String>();
		for(String url: urls) {
			videoId = fromUrlToId(url);
			videoIds.add(videoId);
		}
		this.listVideoId = videoIds;
	}

	/**
	 * preso un url di un video da analizzare restituisce l'id del video
	 * 
	 * @param url url di un video da analizzare
	 * @return l'id del video
	 */
	public String fromUrlToId(String url) {
		int i = url.indexOf("=");
		int j = url.indexOf("&");
		String idvideo = "";
		if(j!=-1){
			idvideo = url.substring(i+1, j);
		}else{
			idvideo = url.substring(i+1);
		}
		return idvideo;
	}
	
	/**
	 * dalla data che forniscono le API di Youtube estrapolo la data, l'ora e il timestamp sotto
	 * forma di stringhe
	 * 
	 * @param data nel formato aaaa-MM-ggThh:mm:ss.00Z
	 * @return una stringa che contiene la data, l'ora e il timestamp di pubblicazione
	 */
	public static String[] setData(String data){
		Pattern patternDuration = Pattern.compile("(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(\\d+).(\\d+)Z");
		Matcher matcherDuration = patternDuration.matcher(data);
		if(matcherDuration.matches()) {
			int year = Integer.parseInt(matcherDuration.group(1));
			int month = Integer.parseInt(matcherDuration.group(2));
			int day = Integer.parseInt(matcherDuration.group(3));
			int hours = Integer.parseInt(matcherDuration.group(4));
			int minutes = Integer.parseInt(matcherDuration.group(5));
			int seconds = Integer.parseInt(matcherDuration.group(6));
			//con cambiamento di fuso orario
			Calendar c = Calendar.getInstance();
			c.clear();
			c.set(year, month-1, day, hours, minutes, seconds);
			c.setTimeZone(TimeZone.getTimeZone("Italy/Rome"));
			String timestamp = c.getTimeInMillis()+"";
			Date d = new Date(c.getTimeInMillis());
						
			day = d.getDate();
			month = d.getMonth();
			month = month+1;
			year = d.getYear() + 1900;
			hours = d.getHours();
			minutes = d.getMinutes();
			seconds = d.getSeconds();
			String date = day + "/" + month + "/" + year;
			String time = hours + ":" + minutes + ":" + seconds;
			return new String[]{date, time, timestamp};
		}
		return null;
	}
	
	/**
	 *  dal pattern della durata del video che fornisce Youtube estrapolo la durata come stringa
	 * 
	 * @param time stringa che rappresenta la durata di un video nel seguente formato 
	 * PTnHnMnS dove n indica un numero
	 * @return un oggetto Time che rappresenta la durata
	 */
	public static Time timeConverter(String time){
		Pattern patternDuration = Pattern.compile("PT((\\d+)H)?((\\d+)M)?((\\d+)S)?");
		Matcher matcherDuration = patternDuration.matcher(time);
		if(matcherDuration.matches()) {
			int hours = 0;
			int minutes = 0;
			int seconds = 0;
			if(matcherDuration.group(2) != null) {
				hours = Integer.parseInt(matcherDuration.group(2));
			}
			if(matcherDuration.group(4) != null) {
				minutes = Integer.parseInt(matcherDuration.group(4));
			}
			if(matcherDuration.group(6) != null) {
				seconds = Integer.parseInt(matcherDuration.group(6));
			}
			return new Time(hours, minutes, seconds);
		}
		return null;
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
	
	public void setListVideoId(List<String> listVideoId) {
		this.listVideoId = listVideoId;
	}

	public List<String> getListVideoId() {
		return listVideoId;
	}

	public YoutubeConnectorConfig getYoutubeConnectorConfig() {
		return youtubeConnectorConfig;
	}

	public boolean isTestMode() {
		return TestMode;
	}

	public void setTestMode(boolean testMode) {
		TestMode = testMode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	

}
