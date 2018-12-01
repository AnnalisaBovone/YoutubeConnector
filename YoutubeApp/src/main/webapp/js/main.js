function ciao(){
    console.log("ciao");
}

var videos = null;
var channel = null;

$(document).ready(function(){
	getAllVideo();
});

function getAllVideo(){
    $.ajax({
        type: "GET", 
        url: "../result/getAllVideo",
        success: function (res) {
            console.log("data arrived");
            videos = res;
            onDataArrived(videos);
        },
        error: function (err) {
            console.log(err);
        },
        dataType: "json",
    });
}

function getChannelFromId(channelId, channelLabelId){
	$.ajax({
        type: "GET", 
        url: "../result/getChannelFromId/"+channelId,
        success: function (res) {
            console.log("data arrived");
            channel = res;
            setChannelLabelName(channel.channelTitle, channelLabelId);
        },
        error: function (err) {
            console.log(err);
        },
        dataType: "json",
    });
}

function onDataArrived(videos){
	$divrow = $( "<div class='row'></div>" );
	for($i=0; $i<videos.length; $i++){
		$divcol = $("<div class='col-lg-4'></div>");
		$divpanelred = $("<button class='panel panel-red'></button>").attr('onclick','myfunction("'+videos[$i].videoId+'")');
		var title = videos[$i].title;
		if(title.length > 45){
			title = title.substring(0,30) + "...";
		}
		$divpanelheading = $("<div class='panel-heading'>" + title + "</div>");
		var channelLabelId = 'channelName_' + $i;
		getChannelFromId(videos[$i].channelId, channelLabelId);
		$divpanelfooter = $("<div id='"+ channelLabelId +"' class='panel-footer'>" + videos[$i].channelId + "</div>");
		$divpanelbody = $("<div class='panel-body'></div>");
		$img = $("<img src=" + videos[$i].thumbnails + " height=250px width=350px>");
		$divpanelbody.append($img);
		$divpanelred.append($divpanelheading);
		$divpanelred.append($divpanelbody);
		$divpanelred.append($divpanelfooter);
		$divcol.append($divpanelred);
		$divrow.append($divcol);
		$("#page-wrapper").append($divrow);
	}
}

function myfunction(videoId){
	window.location.href = 'infoVideo.html?ID=' + videoId;
}

function setChannelLabelName(channelTitle, channelLabelId){
	$('#'+channelLabelId).text(channelTitle);
}
















