package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.iris.entities.Board;

public class RepleService {

	
	private static final String OK = "ok";
	
	public String getSubUrl(int id , String userName , String content){
		
		String subUrl = null;
		String encodeUserName = null;
		String encodeContent = null;
		
		try {
			encodeUserName = URLEncoder.encode(userName,"UTF-8");
			encodeContent = URLEncoder.encode(content,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return subUrl = "?boardId="+id+"&userName="+encodeUserName+"&content="+encodeContent;
	}
	
	public String saveReplePasing(String jsonData){
		
		JSONObject JsonObject;
		String ok = null;
		
		try {
			JsonObject = new JSONObject(jsonData);
			ok = JsonObject.getString(OK);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return ok;
		
	}
}
