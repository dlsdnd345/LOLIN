package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

import com.iris.config.Config;
import com.iris.util.SharedpreferencesUtil;

public class ComposerService {
	
	/**
	 * ComposerService subUrl 반환
	 * @param boardId
	 * @param facebookId
	 * @param title
	 * @param content
	 * @param rank
	 * @param position
	 * @param playTime
	 * @param tea
	 * @return
	 */
	public String getSubUrl(String boardId ,String facebookId , String title , String content, String rank,
			 				  String position , String playTime , String tea){
		
		String encodeBoardId 	= null;
		String encodeTitle 		= null;
		String encodeContent	= null;
		String encodeRank 		= null;
		String encodePosition 	= null;
		String encodePlayTime 	= null;
		String encodeTea 		= null;
		try {
			
			if(boardId == null){
				boardId ="";
			}
			
			encodeBoardId   = URLEncoder.encode(boardId,"UTF-8");
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
