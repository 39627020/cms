
function showArticle(){
	//var aid = getUrlParam("id");
	if(aid  == null){
		window.location.href= getContextPath()+"/index.html";
	}
	$.get(getContextPath()+"/api/article/detail/"+aid,function(data,status){
		if(status  == "success"){
			if(data.flag=="success"){
				
				/*				var article = data.data;
				$("#artical_topic").html(article.title);
				$("#article_content").html(article.content);
				$("#article_author").html(article.author);
				$("#article_date").html(article.createDate);
*/
				var imgUrl = data.imgUrl;
				var imgs = $("#article_content").find("img");
				for(var i=0 ;i<imgs.length;i++){
					var imgid = imgs[i];
					var picid = $(imgid).attr("imgid");
					//如果是上传的图片
					if(picid!=null){
						//设置img标签的src属性
						$(imgid).attr("src",imgServer+"/"+imgUrl[picid]);
					}
				}
				
			}else{
				alert(data.flag);
				window.location.href= getContextPath()+"/index.html";
			}
		}else{
			alert("获取内容失败");
			window.location.href= getContextPath()+"/index.html";
		}
	});
}

function addComment(){
	if(userid<0 || userid==null){
		alert("您还没有登录");
		return;
	}
		
	$.post(getContextPath()+"/api/comment/add/",{articleId:aid,content:$("#comment-text").val()},function(date,status){
		if(status  == "success"){
			showComment();
		}else{
			alert("回复失败");
		}
	});
}
function showComment(){
	$.get(getContextPath()+"/api/comment/comments/"+aid,function(data,status){
		if(status  == "success"){
			comments = data.data;
			str = "";
			for(i in comments){
				cmt = comments[i];
				substr = "";
				substr +='<div class="comment-item">\
				<p class="c-username">	<a href="javascript:void(0)" target="_blank">'+cmt.user+'</a> </p>\
				<p class="p-comment">'+cmt.comment.content+'</p>\
				<div class="c-bottomBar">\
					<span class="reply"><a href="#"><span>推荐</span><em>250</em></a>	<em>/</em> <a href="#">回复</a>	<em>/</em>	<a href="#">举报</a>	</span>\
					<span class="commentTime">'+cmt.comment.createdDate+'</span>\
				</div></div>';
				str += substr;
			}
			
			$("#comment-list").html(str);
			
		}else{
			alert("回复失败");
		}
	});
}


$(document).ready(function () {
	showArticle();
	showComment();
	$("#comment-submit").on("click", function(event){
		//取消事件行为，非常重要！否则add中的post请求会被取消
		event.preventDefault();		
		addComment();
	});
});	