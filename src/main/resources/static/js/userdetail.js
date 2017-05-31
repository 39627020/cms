function follow(){
	var btn = $("#btn-follow").text();
	if(btn == "关注"){
		$.get(getContextPath()+"/api/user/follow/"+userToFollow);
		$("#btn-follow").text("取消关注");
	}else{
		$("#btn-follow").text("关注");
		$.get(getContextPath()+"/api/user/unfollow/"+userToFollow);
	}
}

$(document).ready(function(){
	$("#btn-follow").on("click", function(event){
		//取消事件行为，非常重要！否则add中的post请求会被取消
		event.preventDefault();
		follow();
	});    	

});