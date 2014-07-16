package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ComposerService {

	public String getSubUrl(int boardId ,String facebookId , String title , String content, String rank,
			 				  String position , String playTime , String tea){
		
		String encodeBoardId 	= null;
		String encodeTitle 		= null;
		String encodeContent	= null;
		String encodeRank 		= null;
		String encodePosition 	= null;
		String encodePlayTime 	= null;
		String encodeTea 		= null;
		try {
			encodeBoardId   = URLEncoder.encode(String.valueOf(boardId),"UTF-8");
			encodeTea  		= URLEncoder.encode(tea,"UTF-8");
			encodeTitle 	= URLEncoder.encode(title,"UTF-8");
			encodeContent 	= URLEncoder.encode(content,"UTF-8");
			encodeRank 		= URLEncoder.encode(rank,"UTF-8");
			encodePosition 	= URLEncoder.encode(position,"UTF-8");
			encodePlayTime 	= URLEncoder.encode(playTime,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String sub_url="?id="+encodeBoardId+"&facebookId="+facebookId+"&title="+encodeTitle+"&content="+encodeContent+
				"&position="+encodePosition+"&rank="+encodeRank+"&playTime="+ encodePlayTime +"&tea="+encodeTea;
		return sub_url;
	}
	
}
