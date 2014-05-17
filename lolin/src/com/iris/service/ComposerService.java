package com.iris.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.entities.Board;

public class ComposerService {

	private static final String OK = "ok";
	private static final String TRUE = "true";
	private static final String DATA = "data";
	
	private Gson 						gson;
	private ArrayList<Board> 			boardListFromGson;
	
	public ComposerService(){
		gson = new Gson();
	}
	
	public ArrayList<Board> getBoardFindAll(String jsonData){
		
		JSONObject JsonObject;
		String ok = null;
		String data = null;
		
		try {
			JsonObject = new JSONObject(jsonData);
			ok = JsonObject.getString(OK);
			if(ok.equals(TRUE)){
				data = JsonObject.getString(DATA);
				Type type = new TypeToken<List<Board>>(){}.getType();
				boardListFromGson = gson.fromJson(data, type);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		return boardListFromGson;
	}
	
}
