package com.iris.service;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.entities.User;

public class RecordSearchService {

	private static final String OK = "ok";
	private static final String TRUE = "true";
	private static final String DATA = "data";
	
	private User 			user;
	private Gson 			gson;
	
	public RecordSearchService(){
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
	
}
