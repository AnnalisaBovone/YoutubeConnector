/*var infoVideo = null;
var commentsFromWord = null;

$(document).ready(function(){
	var url = document.location.href;
    var i = url.indexOf('?');
    url= url.substr(i+1);
    i= url.indexOf('=');
    var request = url.substr(0,i);
    url = url.substr(i+1);
    var videoId = url;
    var bool = true;
    if(request == "ID"){
    	console.log(videoId);
    }
	getInfoVideo(videoId);
});

function getInfoVideo(videoId){
	$.ajax({
        type: "GET", 
        url: "../result/getInfoVideo/" + videoId,
        success: function (res) {
            console.log("data arrived");
            infoVideo = res;
            onDataArrived(infoVideo);
        },
        error: function (err) {
            console.log(err);
        },
        dataType: "json",
    });
}

function onDataArrived(infoVideo){
	//console.log("alelee " + infoVideo['video'].thumbnails);
	var video = infoVideo['video'];
	var channel = infoVideo['channel'];
	var updateVideo = video.updates;
	var lastVideoUpdate = updateVideo.length;
	var like = updateVideo[lastVideoUpdate-1].like;
	var dislike = updateVideo[lastVideoUpdate-1].dislike;
	var views = updateVideo[lastVideoUpdate-1].views;
	var updateChannel = channel.updates;
	var lastChannelUpdate = updateChannel.length;
	var subscriber = updateChannel[lastChannelUpdate-1].subscriber;
	$("#panel-img").attr("src", video.thumbnails);
	$('#titleVideo').text(video.title);
	$('#titleChannel').text(channel.channelTitle);
	$('#panel-info').append('<div><p>Titolo: '+video.title+'</p><p>Nome Canale Youtube: '+channel.channelTitle+'</p><p>Iscritti al canale: '+subscriber+'</p><p>Durata: '+video.duration+'</p><p>Data di pubblicazione: '+video.publishedAtDate+'</p><p>Ora di pubblicazione: '+video.publishedAtTime+'</p><p>Descrizione: '+video.description+'</p><p>Like: '+like+'</p><p>Dislike: '+dislike+'</p><p>Views: '+views+'</p></div>');
	$('#panel-comment').height($('#panel-info-totale').height());
	var nComments = infoVideo['comment'].length;
	for(i=0; i<nComments; i++){
		var comment = infoVideo['comment'][i];
		var nAnswer = comment.answer.length;
		if(nAnswer>0){
			if(nAnswer==1){ 
				$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+comment.confidence+'</p><a id="show_'+i+'" href="#" onclick="showAnswer('+nAnswer+','+i+');">Visualizza risposta</a></div>');
			}else{
				$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+comment.confidence+'</p><a id="show_'+i+'" href="#" onclick="showAnswer('+nAnswer+','+i+');">Visualizza risposte</a></div>');
			}
		}else{
			$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+comment.confidence+'</p></div>');
		}
	}
}

function showAnswer(nAnswer,i){
	var comment = infoVideo['comment'][i];
	for(j=0; j<nAnswer; j++){
		$('#comment_'+i).append('<div id=answer_'+i+' style=border:groove;margin-left:25px><p style=font-weight:bold>'+comment.answer[j].author+'</p><p>'+comment.answer[j].text+'</p><p>data pubblicazione: '+comment.answer[j].publishedAtDate + ' ora: ' + comment.answer[j].publishedAtTime +'</p><p>like: '+comment.answer[j].like+' sentiment: '+comment.answer[j].rating+ ' confidence: '+comment.answer[j].confidence+'</p></div>');
	}
	$('#show_'+i).attr('onclick', 'hide('+nAnswer+','+i+')');
	if(nAnswer == 1){ 
		$('#show_'+i).text("Nascondi risposta");
	}else{
		$('#show_'+i).text("Nascondi risposte");
	}
}

function hide(nAnswer, i){
	var comment = infoVideo['comment'][i];
	for(j=0; j<nAnswer; j++){
		$('#answer_'+i).remove();
	}
	$('#show_'+i).attr('onclick', 'showAnswer('+nAnswer+','+i+')');
	if(nAnswer == 1){ 
		$('#show_'+i).text("Visualizza risposta");
	}else{
		$('#show_'+i).text("Visualizza risposte");
	}
}

function search(){
	var input = $('#input-search').val();
	var videoId = infoVideo['video'].videoId;
	console.log(videoId);
	$.ajax({
        type: "GET", 
        url: "../result/getCommentFromWord/" + input + "/" + videoId,
        success: function (res) {
            console.log("data arrived");
            commentsFromWord = res;
            commentsDisplaySearch(commentsFromWord);
        },
        error: function (err) {
            console.log(err);
        },
        dataType: "json",
    });
}

function commentsDisplaySearch(comments){
	$('#panel-comment').empty();
	$('#button-search').remove();
	$('#button-delete').remove();
	$('#span-button').append('<button id="button-delete" class="btn btn-default" onclick="commentsDisplay()"><img src="https://banner2.kisspng.com/20180328/toe/kisspng-mitchell-aluminium-american-red-cross-symbol-clip-wrong-5abc6250e6c9b6.5732349715222953769453.jpg" height=15px width=15px></button><button id="button-search" class="btn btn-default" onclick="search()"><img src="https://png.icons8.com/metro/1600/search.png" height=15px width=15px></button>');
	$('#panel-comment').height($('#panel-info-totale').height());
	var nComments = comments.length;
	for(i=0; i<nComments; i++){
		var comment = comments[i];
		var nAnswer = comment.answer.length;
		 if(nAnswer>0){
			if(nAnswer==1){ 
				$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+comment.confidence+'</p><a id="show_'+i+'" href="#" onclick="showAnswerSearch('+nAnswer+','+i+');">Visualizza risposta</a></div>');
			}else{
				$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+comment.confidence+'</p><a id="show_'+i+'" href="#" onclick="showAnswerSearch('+nAnswer+','+i+');">Visualizza risposte</a></div>');
			}
		}else{
			$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+comment.confidence+'</p></div>');
		}
	}
}

function showAnswerSearch(nAnswer,i){
	var comment = commentsFromWord[i];
	for(j=0; j<nAnswer; j++){
		$('#comment_'+i).append('<div id=answer_'+i+' style=border:groove;margin-left:25px><p style=font-weight:bold>'+comment.answer[j].author+'</p><p>'+comment.answer[j].text+'</p><p>data pubblicazione: '+comment.answer[j].publishedAtDate + ' ora: ' + comment.answer[j].publishedAtTime +'</p><p>like: '+comment.answer[j].like+' sentiment: '+comment.answer[j].rating+ ' confidence: '+comment.answer[j].confidence+'</p></div>');
	}
	$('#show_'+i).attr('onclick', 'hide('+nAnswer+','+i+')');
	if(nAnswer == 1){ 
		$('#show_'+i).text("Nascondi risposta");
	}else{
		$('#show_'+i).text("Nascondi risposte");
	}
}

function hideSearch(nAnswer, i){
	var comment = commentsFromWord[i];
	for(j=0; j<nAnswer; j++){
		$('#answer_'+i).remove();
	}
	$('#show_'+i).attr('onclick', 'showAnswer('+nAnswer+','+i+')');
	if(nAnswer == 1){ 
		$('#show_'+i).text("Visualizza risposta");
	}else{
		$('#show_'+i).text("Visualizza risposte");
	}
}

function commentsDisplay(){
	$('#button-delete').remove();
	$('#panel-comment').empty();
	$('#panel-comment').height($('#panel-info-totale').height());
	var nComments = infoVideo['comment'].length;
	for(i=0; i<nComments; i++){
		var comment = infoVideo['comment'][i];
		var nAnswer = comment.answer.length;
		if(nAnswer>0){
			if(nAnswer==1){ 
				$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+comment.confidence+'</p><a id="show_'+i+'" href="#" onclick="showAnswer('+nAnswer+','+i+');">Visualizza risposta</a></div>');
			}else{
				$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+comment.confidence+'</p><a id="show_'+i+'" href="#" onclick="showAnswer('+nAnswer+','+i+');">Visualizza risposte</a></div>');
			}
		}else{
			$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+comment.confidence+'</p></div>');
		}
	}
}


*/

