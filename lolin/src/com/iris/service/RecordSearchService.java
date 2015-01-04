package com.iris.service;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.config.Config;
import com.iris.entities.User;
import com.iris.util.SharedpreferencesUtil;

/**
 * 전적 검색 화면 서비스
 */
public class RecordSearchService {

	
	private User 	user;
	private Gson 	gson;
	
	public RecordSearchService(){
		gson = new Gson();
	}

    /**
     * 유저 정보 얻기
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
	
}
