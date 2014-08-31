package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.config.Config;
import com.iris.entities.Board;
import com.iris.entities.Reple;

public class FacebookLoginService {

	public FacebookLoginService(){
	}
	
	/**
	 * url 만드는 작업
	 * @param id
	 * @param userName
	 * @param content
	 * @param facebookId
	 * @return
	 */
	public String getSaveLoginInfoSubUrl(String userId , String summonerName , String regId){
		
		String subUrl = null;
		String encodeUserId = null;
		String encodeSummonerName = null;
		String encodePushId = null;
		
		try {
			encodeUserId = URLEncoder.encode(userId,"UTF-8");
			encodeSummonerName = URLEncoder.encode(summonerName,"UTF-8");
			encodePushId = URLEncoder.encode(regId,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return subUrl =  "?faceBookId="+encodeUserId+"&summonerName="+encodeSummonerName+"&pushId="+encodePushId;
	}
	
}
