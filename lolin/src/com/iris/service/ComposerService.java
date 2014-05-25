package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ComposerService {

	public String getSubUrl(String facebookId , String title , String content, String rank,
			 				  String position , String playTime){
		
		String encodeTitle = null;
		String encodeContent = null;
		String encodeRank = null;
		String encodePosition = null;
		String encodePlayTime = null;
		try {
			encodeTitle = URLEncoder.encode(title,"UTF-8");
			encodeContent = URLEncoder.encode(content,"UTF-8");
			encodeRank = URLEncoder.encode(rank,"UTF-8");
			encodePosition = URLEncoder.encode(position,"UTF-8");
			encodePlayTime = URLEncoder.encode(playTime,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String sub_url="?facebookId="+facebookId+"&title="+encodeTitle+"&content="+encodeContent+
				"&position="+encodePosition+"&rank="+encodeRank+"&playTime="+ encodePlayTime;
		return sub_url;
	}
	
	public String transformRank(String rank){
		if(rank.equals("언랭크")){
			return "unrank";
		}else if(rank.equals("브론즈")){
			return "bronze";
		}else if(rank.equals("실버")){
			return "silver";
		}else if(rank.equals("골드")){
			return "gold";
		}else if(rank.equals("플래티넘")){
			return "platinum";
		}else if(rank.equals("다이아")){
			return "diamond";
		}else if(rank.equals("챌린져")){
			return "challenger";
		}else{
			return "";
		}
	}
	
}
