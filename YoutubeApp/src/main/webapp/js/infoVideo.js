var infoVideo = null;
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
	
	$('input:checkbox[name=research]').click(function() {
		if($("#research-word").is(":checked")){
			$("#input-search").prop("disabled", false);
			$("#button-submit").prop("disabled", false);
			$("#button-submit").attr('onclick', 'search()');
			if($("#research-date").is(":checked")){
				$("#button-submit").attr('onclick', 'combine_filter()');
			}
		}else{
			$("#input-search").prop("disabled", true);
			if(!$("#research-date").is(":checked")){
				$("#button-submit").prop("disabled", true);
			}else{
				$("#button-submit").attr('onclick', 'filter()');
			}
		}
		if($("#research-date").is(":checked")){
			$("#input-from").prop('disabled', false);
			$("#input-to").prop('disabled', false);
			$("#button-submit").prop("disabled", false);
			$("#button-submit").attr('onclick', 'filter()');
			if($("#research-word").is(":checked")){
				$("#button-submit").attr('onclick', 'combine_filter()');
			}
		}else{
			$("#input-from").prop('disabled', true);
			$("#input-to").prop('disabled', true);
			if(!$("#research-word").is(":checked")){
				$("#button-submit").prop("disabled", true);
			}else{
				$("#button-submit").attr('onclick', 'search()');
			}
		}
	});
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
	var video = infoVideo['video'];
	var channel = infoVideo['channel'];
	$('#titleVideo').text(video.title);
	$('#titleChannel').text(channel.channelTitle);
	$("#panel-img").attr("src", video.thumbnails);
	var updateVideo = video.updates;
	var lastVideoUpdate = updateVideo.length;
	var like = updateVideo[lastVideoUpdate-1].like;
	var dislike = updateVideo[lastVideoUpdate-1].dislike;
	var views = updateVideo[lastVideoUpdate-1].views;
	var subscriber = channel.subscribers;
	if(like == -1){
		$('#panel-info').append('<div><p>Titolo: '+video.title+'</p><p>Nome Canale Youtube: '+channel.channelTitle+'</p><p>Iscritti al canale: '+subscriber+'</p><p>Durata: '+video.duration+'</p><p>Data di pubblicazione: '+video.publishedAtDate+'</p><p>Ora di pubblicazione: '+video.publishedAtTime+'</p><p>Descrizione: '+video.description+'</p><p>Views: '+views+'</p></div>');
	}else{ 
		$('#panel-info').append('<div><p>Titolo: '+video.title+'</p><p>Nome Canale Youtube: '+channel.channelTitle+'</p><p>Iscritti al canale: '+subscriber+'</p><p>Durata: '+video.duration+'</p><p>Data di pubblicazione: '+video.publishedAtDate+'</p><p>Ora di pubblicazione: '+video.publishedAtTime+'</p><p>Descrizione: '+video.description+'</p><p>Like: '+like+'</p><p>Dislike: '+dislike+'</p><p>Views: '+views+'</p></div>');
	}
	commentsDisplay(infoVideo['comment'], "allComments");
	graphicDisplay();
}

function pieChart(id, val1, val2, total, string1, string2, color1, color2){
	var ctx = document.getElementById(id);
	var myPieChart = new Chart(ctx, {
	  type: 'pie',
	  data: {
	    labels: [string1 + ": " + Math.round(val1/(total)*100) + "%", string2 + ": " + Math.round(val2/(total)*100) + "%"],
	    datasets: [{
	      data: [val1, val2],
	      backgroundColor: [color1, color2],
	    }],
	  },
	});
}

function barChart(id, labels, label, color, data, max){
	var ctx = document.getElementById(id);
	var myLineChart = new Chart(ctx, {
	  type: 'bar',
	  data: {
	    labels: labels,
	    datasets: [{
	      label: label,
	      backgroundColor: color,
	      borderColor: "rgba(2,117,216,1)",
	      data: data,
	    }],
	  },
	  options: {
	    scales: {
	      xAxes: [{
	        time: {
	          unit: 'month'
	        },
	        gridLines: {
	          display: false
	        },
	        ticks: {
	          maxTicksLimit: 6
	        }
	      }],
	      yAxes: [{
	        ticks: {
	          min: 0,
	          max: max,
	          maxTicksLimit: 5
	        },
	        gridLines: {
	          display: true
	        }
	      }],
	    },
	    legend: {
	      display: false
	    }
	  },
	});
} 

function areaChart(id, labels, label, color, data, max){
	var ctx = document.getElementById(id);
	var myLineChart = new Chart(ctx, {
	  type: 'line',
	  data: {
	    labels: labels,
	    datasets: [{
	      label: label,
	      lineTension: 0.3,
	      backgroundColor: color,
	      borderColor: color,
	      pointRadius: 5,
	      pointBackgroundColor: color,
	      pointBorderColor: "rgba(255,255,255,0.8)",
	      pointHoverRadius: 5,
	      pointHoverBackgroundColor: color,
	      pointHitRadius: 50,
	      pointBorderWidth: 2,
	      data: data,
	      fill:false,
	    }],
	  },
	  options: {
	    scales: {
	      xAxes: [{
	        time: {
	          unit: 'date'
	        },
	        gridLines: {
	          display: false
	        },
	        ticks: {
	          maxTicksLimit: nUpdates
	        }
	      }],
	      yAxes: [{
	        ticks: {
	          min: 0,
	          max: max,	
	          maxTicksLimit: 10
	        },
	        gridLines: {
	          color: "rgba(0, 0, 0, .125)",
	        }
	      }],
	    },
	    legend: {
	      display: false
	    }
	  }
	});
}

function areaChartDouble(id, labels, label1, color1, data1, label2, color2, data2, max){
	var ctx = document.getElementById(id);
	var myLineChart = new Chart(ctx, {
	  type: 'line',
	  data: {
	    labels: labels,
	    datasets: [{
	      label: label1,
	      lineTension: 0.3,
	      backgroundColor: color1,
	      borderColor: color1,
	      pointRadius: 5,
	      pointBackgroundColor: color1,
	      pointBorderColor: "rgba(255,255,255,0.8)",
	      pointHoverRadius: 5,
	      pointHoverBackgroundColor: color1,
	      pointHitRadius: 50,
	      pointBorderWidth: 2,
	      data: data1,
	      fill:false,
	    }, 
	    {label: label2,
	      lineTension: 0.3,
	      backgroundColor: color2,
	      borderColor: color2,
	      pointRadius: 5,
	      pointBackgroundColor: color2,
	      pointBorderColor: "rgba(255,255,255,0.8)",
	      pointHoverRadius: 5,
	      pointHoverBackgroundColor: color2,
	      pointHitRadius: 50,
	      pointBorderWidth: 2,
	      data: data2,
	      fill:false,}],
	  },
	  options: {
	    scales: {
	      xAxes: [{
	        time: {
	          unit: 'date'
	        },
	        gridLines: {
	          display: false
	        },
	        ticks: {
	          maxTicksLimit: nUpdates
	        }
	      }],
	      yAxes: [{
	        ticks: {
	          min: 0,
	          max: max,	
	          maxTicksLimit: 10
	        },
	        gridLines: {
	          color: "rgba(0, 0, 0, .125)",
	        }
	      }],
	    },
	    legend: {
	      display: false
	    }
	  }
	});
}

function graphicDisplay(){
	var updateVideo = infoVideo['video'].updates;
	var lastVideoUpdate = updateVideo.length;
	var like = updateVideo[lastVideoUpdate-1].like;
	var dislike = updateVideo[lastVideoUpdate-1].dislike;
	var views = updateVideo[lastVideoUpdate-1].views;
	//pie chart
	var total = like + dislike;
	console.log(like);
	if(like!=-1){ 
		pieChart("like-dislikePie", like, dislike, total, "Like", "Dislike",'#00C200', '#F51212');
	}else{
		$("#like-dislikePieDiv").attr("style", "visibility: hidden");
	}
	
	var totalViews = infoVideo['totalViews'];
	var tot = totalViews - views;
	pieChart("viewsPie", views, tot, totalViews, "Views", "ViewsTotali", '#0275D8', '#19115E');
	
	var totalLike = infoVideo['totalLike'];
	tot = totalLike - like;
	if(like!=-1){ 
		pieChart("likePie", like, tot, totalLike, "Like", "LikeTotali", '#00C200', '#004200');
	}else{
		$("#likePieDiv").attr("style", "visibility: hidden");
	}
	
	var sentiment = [0, 0, 0, 0, 0];
	var nComment = infoVideo['comment'].length;
	for(var i=0; i<nComment; i++){
		if(infoVideo['comment'][i].rating == 1){
			sentiment[0] = sentiment[0] + 1;
		}else if(infoVideo['comment'][i].rating == 2){
			sentiment[1] = sentiment[1] + 1;
		}else if(infoVideo['comment'][i].rating == 3){
			sentiment[2] = sentiment[2] + 1;
		}else if(infoVideo['comment'][i].rating == 4){
			sentiment[3] = sentiment[3] + 1;
		}else if(infoVideo['comment'][i].rating == 5){
			sentiment[4] = sentiment[4] + 1;
		}	
	}
	var totalSentiment = sentiment[0] + sentiment[1] + sentiment[2] + sentiment[3] + sentiment[4];
	var ctx = document.getElementById("sentimentPie");
	var myPieChart = new Chart(ctx, {
	  type: 'doughnut',
	  data: {
	    labels: ["1: " + Math.round(sentiment[0]/totalSentiment*100) + "%", "2: " + Math.round(sentiment[1]/totalSentiment*100) + "%", "3: " + Math.round(sentiment[2]/totalSentiment*100) + "%", "4: " + Math.round(sentiment[3]/totalSentiment*100) + "%", "5: " + Math.round(sentiment[4]/totalSentiment*100) + "%"],
	    datasets: [{
	      data: [sentiment[0], sentiment[1], sentiment[2], sentiment[3], sentiment[4]],
	      backgroundColor: ['#cc6600', '#ff9933', '#E9F013', '#b2ff66', '#2DC33C'],
	    }],
	  },
	  options:{
		  circumference: 0.80 * Math.PI,
		  rotation: -0.9 * Math.PI, 
	  }
	});
	
	//area chart
	nUpdates = infoVideo['video']['updates'].length;
	var timestamp = [nUpdates];
	var views = [nUpdates];
	var like = [nUpdates];
	var dislike = [nUpdates];
	for(var i=0;i<nUpdates;i++){
		timestamp[i] = infoVideo['video']['updates'][i].date;
		views[i] = infoVideo['video']['updates'][i].views;
		like[i] = infoVideo['video']['updates'][i].like;
		dislike[i] = infoVideo['video']['updates'][i].dislike;
	}
	var max = Math.max.apply(null, views);
	areaChart("viewsArea", timestamp, "Views", "rgba(2,117,216,1)", views, max);
	
	max = Math.max.apply(null, like);
	if(like != -1){ 
		areaChart("likeArea", timestamp, "Like", "#00C200", like, max);
	}else{
		$("#likeAreaDiv").attr("style", "visibility: hidden");
	}
	max = Math.max.apply(null, dislike);
	if(dislike != -1){ 
		areaChart("dislikeArea", timestamp, "Dislike", "#F51212", dislike, max);
	}else{
		$("#dislikeAreaDiv").attr("style", "visibility: hidden");
	}
	
	//se non volessi considerare le risposte al posto di allComment metto comment
	var comments = infoVideo['allComment'];
	var nComments = infoVideo['allComment'].length;
	var arrayComments = new Array();
	arrayComments['date'] = [nComments];
	arrayComments['occurence'] = [nComments];
	arrayComments['rating'] = [nComments];
	var k = 0;
	for(var i=0;i<nComments;i++){
		var data = new Date(comments[i].timestamp);
		temp = data.getDate() + "/" + (data.getMonth()+1) + "/" + data.getFullYear();
		var bool = true;
		for(var j=0;j<arrayComments['date'].length;j++){
			if(temp == arrayComments['date'][j]){
				arrayComments['occurence'][j] = arrayComments['occurence'][j] + 1;
				if(comments[i].rating>0 && comments[i].rating<6){
					for(var p=0; p<arrayComments['rating'][j].length; p++){ 
						if(arrayComments['rating'][j][p] == 0){
							arrayComments['rating'][j][p] = comments[i].rating;
							break;
						}
					}
				}
				bool = false;
			}
		}
		if(bool){
			arrayComments['date'][k] = temp;
			arrayComments['occurence'][k] = 1;
			arrayComments['rating'][k] = new Array(nComments).fill(0);
			if(comments[i].rating>0 && comments[i].rating<6){
				arrayComments['rating'][k][0] = comments[i].rating;
			}
			k = k + 1;
		}
	}
	var rate = [arrayComments['rating'].length];
	for(var i=0;i<arrayComments['rating'].length;i++){
		var temp = 0;
		console.log("******");
		console.log(arrayComments['rating'][i]);
		var tot = arrayComments['rating'][i].length;
		if(arrayComments['rating'][i].indexOf(0) != -1){
			tot = arrayComments['rating'][i].indexOf(0);
		}
			console.log(tot);
		for(var j=0;j<tot;j++){
			temp = temp + arrayComments['rating'][i][j];
		}
		rate[i] = Math.round((temp/tot)*100.0)/100.0;
	}
	
	barChart("sentimentBar2", arrayComments['date'], "Media sentiment", "rgba(233,240,19,1)", rate, 6);
	var max = Math.max.apply(null, arrayComments['occurence']);
	barChart("commentArea", arrayComments['date'], "Comments", "rgba(14,150,32,0.2)", arrayComments['occurence'], max);
}

function showAnswer(event){
	comments = event.data.param1;
	nAnswer = event.data.param2;
	i = event.data.param3;
	var comment = comments[i];
	for(j=0; j<nAnswer; j++){
		if(comment.answer[j].rating == 0 || comment.answer[j].rating == 6 || comment.answer[j].rating == -1){
			$('#comment_'+i).append('<div id=answer_'+i+'_'+j+' style=border:groove;margin-left:25px><p style=font-weight:bold>'+comment.answer[j].author+'</p><p>'+comment.answer[j].text+'</p><p>data pubblicazione: '+comment.answer[j].publishedAtDate + ' ora: ' + comment.answer[j].publishedAtTime +'</p><p>like: '+comment.answer[j].like+' sentiment: N.D.</p></div>');
		}else{
			$('#comment_'+i).append('<div id=answer_'+i+'_'+j+' style=border:groove;margin-left:25px><p style=font-weight:bold>'+comment.answer[j].author+'</p><p>'+comment.answer[j].text+'</p><p>data pubblicazione: '+comment.answer[j].publishedAtDate + ' ora: ' + comment.answer[j].publishedAtTime +'</p><p>like: '+comment.answer[j].like+' sentiment: '+comment.answer[j].rating+ ' confidence: '+Math.round((comment.answer[j].confidence)*100)+'%</p></div>');
		}
	}
	$('#show_'+i).unbind("click");
	$('#show_'+i).click({param1: comments, param2: nAnswer, param3: i}, hideAnswer);
	if(nAnswer == 1){ 
		$('#show_'+i).text("Nascondi risposta");
	}else{
		$('#show_'+i).text("Nascondi risposte");
	}
}

function hideAnswer(event){
	comments = event.data.param1;
	nAnswer = event.data.param2;
	i = event.data.param3;
	var comment = comments[i];
	for(j=0; j<nAnswer; j++){
		$('#answer_'+i+'_'+j).remove();
	}
	$('#show_'+i).unbind("click");
	$('#show_'+i).click({param1: comments, param2: nAnswer, param3: i}, showAnswer);
	if(nAnswer == 1){ 
		$('#show_'+i).text("Visualizza risposta");
	}else{
		$('#show_'+i).text("Visualizza risposte");
	}
}

function search(){
	var input = $('#input-search').val();
	var videoId = infoVideo['video'].videoId;
	$.ajax({
        type: "GET", 
        url: "../result/getCommentFromWord/" + input + "/" + videoId,
        success: function (res) {
            console.log("data arrived");
            commentsFromWord = res;
            commentsDisplay(commentsFromWord, "searchWord");
        },
        error: function (err) {
            console.log(err);
        },
        dataType: "json",
    });
}

function commentsDisplay(comments, type){
	if(comments.length == 0){
		$('#nocomment').text("Non ci sono commenti");
	}else{
		$('#nocomment').text("");
	}
	$('#button-delete').remove();
	$('#panel-comment').empty();
	if(type == "searchWord"){
		$('#td-button').append('<button id="button-delete" class="btn btn-default"><img src="https://banner2.kisspng.com/20180328/toe/kisspng-mitchell-aluminium-american-red-cross-symbol-clip-wrong-5abc6250e6c9b6.5732349715222953769453.jpg" height=15px width=15px></button>');
		$('#button-delete').click(function(){
			commentsDisplay(infoVideo['comment'], "allComments");
		})
	}
	if(type == "searchTemporalInterval"){
		$('#td-button').append('<button id="button-delete" class="btn btn-default"><img src="https://banner2.kisspng.com/20180328/toe/kisspng-mitchell-aluminium-american-red-cross-symbol-clip-wrong-5abc6250e6c9b6.5732349715222953769453.jpg" height=15px width=15px></button>');
		$('#button-delete').click(function(){
			commentsDisplay(infoVideo['comment'], "allComments");
		})
	}
	if(type == "searchCombineFilter"){
		$('#td-button').append('<button id="button-delete" class="btn btn-default"><img src="https://banner2.kisspng.com/20180328/toe/kisspng-mitchell-aluminium-american-red-cross-symbol-clip-wrong-5abc6250e6c9b6.5732349715222953769453.jpg" height=15px width=15px></button>');
		$('#button-delete').click(function(){
			commentsDisplay(infoVideo['comment'], "allComments");
		})
	}
	if(type == "allComments"){
		$('#input-search').val("");
		$('#input-from').val("gg/mm/aaaa");
		$('#input-to').val("gg/mm/aaaa");
		$("#input-search").prop('disabled', true);
		//$("#button-search").prop("disabled", true);
		$("#input-from").prop('disabled', true);
		$("#input-to").prop('disabled', true);
		//$("#button-date").prop("disabled", true);
		$("#research-word").prop("checked", false);
		$("#research-date").prop("checked", false);
		$("#button-submit").prop("disabled", true);
	}
	$('#panel-comment').height($('#panel-info-totale').height());
	var nComments = comments.length;
	for(i=0; i<nComments; i++){
		var comment = comments[i];
		var nAnswer = comment.answer.length;
		if(nAnswer>0){
			if(nAnswer==1){ 
				if(comment.like == -1){
					if(comment.rating == 0 || comment.rating == 6 || comment.rating == -1){
						$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>sentiment: N.D.</p><a id="show_'+i+'" style=cursor:pointer>Visualizza risposta</a></div>');
					}else{ 
						$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>sentiment: '+comment.rating+ ' confidence: '+Math.round((comment.confidence)*100)+'%</p><a id="show_'+i+'" style=cursor:pointer>Visualizza risposta</a></div>');
					}
				}else{
					if(comment.rating == 0 || comment.rating == 6 || comment.rating == -1){
						$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: N.D.</p><a id="show_'+i+'" style=cursor:pointer>Visualizza risposta</a></div>');
					}else{
						$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+Math.round((comment.confidence)*100)+'%</p><a id="show_'+i+'" style=cursor:pointer>Visualizza risposta</a></div>');
					}
				}
				$('#show_'+i).click({param1: comments, param2: nAnswer, param3: i}, showAnswer);
			}else{
				if(comment.like == -1){
					if(comment.rating == 0 || comment.rating == 6 || comment.rating == -1){
						$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>sentiment: N.D.</p><a id="show_'+i+'" style=cursor:pointer>Visualizza risposte</a></div>');
					}else{
						$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>sentiment: '+comment.rating+ ' confidence: '+Math.round((comment.confidence)*100)+'%</p><a id="show_'+i+'" style=cursor:pointer>Visualizza risposte</a></div>');
					}
				}else{
					if(comment.rating == 0 || comment.rating == 6 || comment.rating == -1){
						$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: N.D.</p><a id="show_'+i+'" style=cursor:pointer>Visualizza risposte</a></div>');
					}else{ 
						$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+Math.round((comment.confidence)*100)+'%</p><a id="show_'+i+'" style=cursor:pointer>Visualizza risposte</a></div>');
					}
				}

				$('#show_'+i).click({param1: comments, param2: nAnswer, param3: i}, showAnswer);
			}
		}else{
			if(comment.like == -1){
				if(comment.rating == 0 || comment.rating == 6 || comment.rating == -1){
					$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>sentiment: N.D.</p></div>');
				}else{ 
					$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>sentiment: '+comment.rating+ ' confidence: '+Math.round((comment.confidence)*100)+'%</p></div>');
				}
			}else{
				if(comment.rating == 0 || comment.rating == 6 || comment.rating == -1){
					$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: N.D.</p></div>');
				}else{ 
					$('#panel-comment').append('<div id=comment_'+i+' style=border:groove><p style=font-weight:bold>'+comment.author+'</p><p>'+comment.text+'</p><p>data pubblicazione: '+comment.publishedAtDate + ' ora: ' + comment.publishedAtTime +'</p><p>like: '+comment.like+' sentiment: '+comment.rating+ ' confidence: '+Math.round((comment.confidence)*100)+'%</p></div>');
				}
			}
		}
	}
}


function filter(){
	console.log("im in filter");
	console.log($('#input-from').val());
	console.log(new Date($('#input-from').val()));
	from = new Date($('#input-from').val()).getTime();
	console.log("from: " + from);
	to = new Date($('#input-to').val()).getTime();
	console.log("to: " + to);
	var videoId = infoVideo['video'].videoId;
	$.ajax({
        type: "GET", 
        url: "../result/getCommentFromTemporalInterval/" + from + "/" + to + "/" + videoId,
        success: function (res) {
            console.log("data arrived");
            commentsFromTemporalInterval = res;
            commentsDisplay(commentsFromTemporalInterval, "searchTemporalInterval");
        },
        error: function (err) {
            console.log(err);
        },
        dataType: "json",
    });
}

function combine_filter(){
	console.log("Filtro combinato");
	var input = $('#input-search').val();
	from = new Date($('#input-from').val()).getTime();
	to = new Date($('#input-to').val()).getTime();
	var videoId = infoVideo['video'].videoId;
	$.ajax({
        type: "GET", 
        url: "../result/getCommentFromCombineFilter/" + input + "/" + from + "/" + to + "/" + videoId,
        success: function (res) {
            console.log("data arrived");
            commentsFromCombineFilter = res;
            commentsDisplay(commentsFromCombineFilter, "searchCombineFilter");
        },
        error: function (err) {
            console.log(err);
        },
        dataType: "json",
    });
}







