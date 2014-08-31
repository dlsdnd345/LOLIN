package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.config.Config;
import com.iris.entities.User;
import com.iris.util.SharedpreferencesUtil;

public class SettingService {

	private User 	user;
	private Gson 	gson;

	public SettingService(){
		gson = new Gson();
	}

	/**
	 * user 데이터 파싱
	 * @param jsonData
	 * @return
	 */
	public User getUser(String jsonData){

		JSONObject JsonObject;
		String ok = null;
		String data = null;

		try {
			JsonObject = new JSONObject(jsonData);
			ok = JsonObject.getString(Config.FLAG.OK);
			if(ok.equals(Config.FLAG.TRUE)){
				data = JsonObject.getString(Config.FLAG.DATA);
				Type type = new TypeToken<User>(){}.getType();
				user = gson.fromJson(data, type);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * 소환사명 인코드 진행
	 * @param summonerName
	 * @return
	 */
	public String getEncodeSummonerName(String summonerName){

		String encodeSummonerName = null;
		try {
			encodeSummonerName = URLEncoder.encode(summonerName,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return encodeSummonerName;
	}
	
	/**
	 * 소환사명 수정후 데이터 파싱
	 * @param jsonData
	 * @return
	 */
	public String updateSummonerName(String jsonData){
		
		JSONObject JsonObject;
		String ok = null;
		try {
			JsonObject = new JSONObject(jsonData);
			ok = JsonObject.getString(Config.FLAG.OK);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ok;
	}


}
