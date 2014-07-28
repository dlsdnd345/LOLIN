package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.entities.User;
import com.iris.util.SharedpreferencesUtil;

public class SettingService {

	private static final String OK = "ok";
	private static final String TRUE = "true";
	private static final String DATA = "data";

	private User 						user;
	private Gson 						gson;

	public SettingService(){
		gson = new Gson();
	}

	public User getUser(String jsonData){

		JSONObject JsonObject;
		String ok = null;
		String data = null;

		try {
			JsonObject = new JSONObject(jsonData);
			ok = JsonObject.getString(OK);
			if(ok.equals(TRUE)){
				data = JsonObject.getString(DATA);
				Type type = new TypeToken<User>(){}.getType();
				user = gson.fromJson(data, type);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return user;
	}

	public String getEncodeSummonerName(String summonerName){

		String encodeSummonerName = null;
		try {
			encodeSummonerName = URLEncoder.encode(summonerName,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return encodeSummonerName;
	}
	
	public String updateSummonerName(String jsonData){
		
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
