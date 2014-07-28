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

public class RepleService {

	
	private static final String OK = "ok";
	
	private Gson 						gson;
	private ArrayList<Reple> 			repleListFromGson;
	
	public RepleService(){
		
		gson = new Gson();
		
	}
	
	public String getSubUrl(int id , String userName , String content , String facebookId){
		
		String subUrl = null;
		String encodeUserName = null;
		String encodeContent = null;
		String encodeFacebookId = null;
		
		try {
			encodeUserName = URLEncoder.encode(userName,"UTF-8");
			encodeContent = URLEncoder.encode(content,"UTF-8");
			encodeFacebookId = URLEncoder.encode(facebookId,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return subUrl = "?boardId="+id+"&userName="+encodeUserName+"&content="+encodeContent+"&facebookId="+encodeFacebookId;
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
	
	public String deleteReplePasing(String jsonData){
		
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
	
	/**
	 * 게시판 한개의 댓글 파싱
	 * @param jsonData
	 * @return
	 */
	public ArrayList<Reple> getRepleFindOne(String jsonData){
		
		System.out.println("#########   :  " + jsonData);
		
		JSONObject JsonObject;
		String ok 	= null;
		String data = null;
		
		try {
			JsonObject = new JSONObject(jsonData);
			ok = JsonObject.getString(OK);
			if(ok.equals(Config.FLAG.TRUE)){
				data = JsonObject.getString(Config.FLAG.DATA);
				Type type = new TypeToken<List<Reple>>(){}.getType();
				repleListFromGson = gson.fromJson(data, type);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return repleListFromGson;
	}
	
}
