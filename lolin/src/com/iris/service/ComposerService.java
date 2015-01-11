package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

import com.iris.config.Config;
import com.iris.libs.TrippleDes;
import com.iris.util.SharedpreferencesUtil;
import com.iris.util.SignatureUtil;

/**
 * 글쓰기 화면 서비스
 */
public class ComposerService {
	
	/**
	 * 글쓰기 Url
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
		
		String hash = null;

        String os = "android";

		String encodeBoardId 	= null;
		String encodeTitle 		= null;
		String encodeContent	= null;
		String encodeRank 		= null;
		String encodePosition 	= null;
		String encodePlayTime 	= null;
		String encodeTea 		= null;
		String encodeHash 		= null;
		String encodeOs         = null;

		try {
			
			if(boardId == null){
				boardId ="";
			}
			
			TrippleDes trippleDes = new TrippleDes();
			facebookId = trippleDes.encrypt(facebookId);
			
			String signatureData = boardId +facebookId+ title + content + position + rank + playTime + tea + os + Config.KEY.SECRET;
			hash = SignatureUtil.getHash(signatureData);
			
			encodeHash   	= URLEncoder.encode(hash,"UTF-8");
			encodeBoardId   = URLEncoder.encode(boardId,"UTF-8");
			encodeTitle 	= URLEncoder.encode(title,"UTF-8");
			encodeContent 	= URLEncoder.encode(content,"UTF-8");
			encodePosition 	= URLEncoder.encode(position,"UTF-8");
			encodeRank 		= URLEncoder.encode(rank,"UTF-8");
			encodePlayTime 	= URLEncoder.encode(playTime,"UTF-8");
			encodeTea  		= URLEncoder.encode(tea,"UTF-8");
            encodeOs        = URLEncoder.encode(os,"UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String sub_url="?boardId="+encodeBoardId+"&facebookId="+facebookId+"&title="+encodeTitle+"&content="+encodeContent+
				"&position="+encodePosition+"&rank="+encodeRank+"&playTime="+ encodePlayTime +"&tea="+encodeTea+
                "&os="+encodeOs+ "&hash="+encodeHash;
		return sub_url;
	}

    /**
     * 한개의 게시판  Url
     * @param boardId
     * @return
     */
	public String getFindOneSubUrl(String boardId){
		
		String hash;
		String encodeBoardId 	= null;
		String encodeHash 		= null;
		
		try {
			
			String signatureData = boardId + Config.KEY.SECRET;
			hash = SignatureUtil.getHash(signatureData);
			
			encodeBoardId   = URLEncoder.encode(boardId,"UTF-8");
			encodeHash   	= URLEncoder.encode(hash,"UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "?boardId=" + encodeBoardId+"&hash="+encodeHash;
	}
	
}
