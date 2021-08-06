layui.define(['table', 'jquery', 'element'], function (exports) {
	"use strict";

	var MOD_NAME = 'frame',
		$ = layui.jquery,
		element = layui.element;

	var wayneFrame = function (opt) {
		this.option = opt;
	};

	wayneFrame.prototype.render = function (opt) {
		var option = {
			elem:opt.elem,
			url:opt.url,
			title:opt.title,
			width:opt.width,
			height:opt.height,
			done:opt.done ? opt.done: function(){ console.log("菜单渲染成功");}
		}
		createFrameHTML(option);
		$("#"+option.elem).width(option.width);
		$("#"+option.elem).height(option.height);
		return new wayneFrame(option);
	}

	wayneFrame.prototype.changePage = function(url,title,loading){
		if(loading){
			var loading = $("#"+this.option.elem).find(".wayne-frame-loading");
			loading.css({display:'block'});
		}
		$("#"+this.option.elem+" iframe").attr("src",url);
		$("#"+this.option.elem+" .title").html(title);
		if(loading){
			var loading = $("#"+this.option.elem).find(".wayne-frame-loading");
			setTimeout(function(){
				loading.css({display:'none'});
			},800)
		}
	}

	wayneFrame.prototype.changePageByElement = function(elem,url,title,loading){
		if(loading){
			var loading = $("#"+elem).find(".wayne-frame-loading");
			loading.css({display:'block'});
		}
		$("#"+elem+" iframe").attr("src",url);
		$("#"+elem+" .title").html(title);
		if(loading){
			var loading = $("#"+elem).find(".wayne-frame-loading");
			setTimeout(function(){
				loading.css({display:'none'});
			},800)
		}
	}

	wayneFrame.prototype.refresh = function (time) {
		if(time!=false){
			var loading = $("#"+this.option.elem).find(".wayne-frame-loading");
			loading.css({display:'block'});
			if(time!=0){
				setTimeout(function(){
					loading.css({display:'none'});
				},time)
			}
		}
		$("#"+this.option.elem).find("iframe")[0].contentWindow.location.reload(true);
	}

	function createFrameHTML(option){
		var header = "<div class='wayne-frame-title'><div class='dot'></div><div class='title'>"+option.title+"</div></div>"
		var iframe = "<iframe class='wayne-frame-content' style='width:100%;height:100%;'  scrolling='auto' frameborder='0' src='"+option.url+"' ></iframe>";
		var loading = '<div class="wayne-frame-loading">'+
			'<div class="ball-loader">'+
			'<span></span><span></span><span></span><span></span>'+
			'</div>'+
			'</div></div>';
		$("#"+option.elem).html("<div class='wayne-frame'>"+header+iframe+loading+"</div>");
	}
	exports(MOD_NAME,new wayneFrame());
})