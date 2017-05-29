function sendMessage(){
	var content = $("#chat-input").val();
	$.post(getContextPath()+"/api/message/add",{toId:toId,content:content},function(data,status){
	    //
		$("#chat-input").val("");
	  });
}

$(document).ready(function () {
	$("#btn-chat").on("click", function(event){
		//取消事件行为，非常重要！否则add中的post请求会被取消
		event.preventDefault();		
		sendMessage();
	});
});	