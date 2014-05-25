package com.iris.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.entities.Board;

public class BoardDetailService {

	
	private static final String OK = "ok";
	private static final String TRUE = "true";
	private static final String DATA = "data";
	private static final String REPLE_DATA = "repleList";
	
	private Gson 						gson;
	private Context						context;
	private Board 						boardOneFromGson;
	
	
	public BoardDetailService(Context context){
		this.context =context;
		gson = new Gson();
	}
	
	public Board getBoardFindOne(String jsonData){
		
		JSONObject JsonObject;
		String ok = null;
		String data = null;
		String repleData = null;
		
		try {
			JsonObject = new JSONObject(jsonData);
			ok = JsonObject.getString(OK);
			if(ok.equals(TRUE)){
				data = JsonObject.getString(DATA);
				Type type = new TypeToken<Board>(){}.getType();
				boardOneFromGson = gson.fromJson(data, type);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return boardOneFromGson;
	}
	
}
