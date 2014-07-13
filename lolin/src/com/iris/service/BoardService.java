package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.entities.Board;
import com.iris.lolin.R;
import com.iris.util.SharedpreferencesUtil;

public class BoardService {

	private static final String RANK_DATA_POSITION 		= "RankDataPosition";
	private static final String POSITION_DATA__POSITION 	= "PositionDataPosition";
	private static final String TIME_DATA_POSITION 		= "TimeDataPosition";
	
	private static final String OK = "ok";
	private static final String TRUE = "true";
	private static final String DATA = "data";
	
	private Context						context;
	private Gson 						gson;
	private ArrayList<Board> 			boardListFromGson;
	private SharedpreferencesUtil		sharedpreferencesUtil;
	private String[] 					rankData,positionData,timeData;
	
	public BoardService(Context context){
		this.context =context;
		gson = new Gson();
		sharedpreferencesUtil = new SharedpreferencesUtil(context);
	}
	
	public ArrayList<Board> getBoardFindAll(String jsonData){
		
		System.err.println("#########   jsonData :  "+ jsonData);
		
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
	
	public String getSubUrl(){
		
		rankData = context.getResources().getStringArray(R.array.main_rank_array_list);
		positionData = context.getResources().getStringArray(R.array.main_position_array_list);
		timeData = context.getResources().getStringArray(R.array.main_time_array_list);
		
		String subUrl = null;
		int rankCount = sharedpreferencesUtil.getValue(RANK_DATA_POSITION, 0);
		int positionCount = sharedpreferencesUtil.getValue(POSITION_DATA__POSITION, 0);
		int timeCount = sharedpreferencesUtil.getValue(TIME_DATA_POSITION, 0);
		
		String encodePosition = null;
		String encodeTime = null;
		
		String transformRank = transformRank(rankData[rankCount]);
		String transformPosition = transformPosition(positionData[positionCount]);
		String transformTime = transformTime(timeData[timeCount]);
		
		try {
			encodePosition = URLEncoder.encode(transformPosition,"UTF-8");
			encodeTime = URLEncoder.encode(transformTime,"UTF-8");
			
			subUrl = "?rank="+transformRank+"&position="+encodePosition+"&playTime="+encodeTime;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return subUrl;
		
	}
	
	public String transformRank(String rank){
		if(rank.equals("언랭크")){
			return "unrank";
		}else if(rank.equals("브론즈")){
			return "bronze";
		}else if(rank.equals("실버")){
			return "silver";
		}else if(rank.equals("골드")){
			return "gold";
		}else if(rank.equals("플래티넘")){
			return "platinum";
		}else if(rank.equals("다이아")){
			return "diamond";
		}else if(rank.equals("챌린져")){
			return "challenger";
		}else{
			return "";
		}
	}
	
	public String transformPosition(String position){
		if(position.equals("포지션[전체]")){
			return "";
		}else{
			return position;
		}
	}
	
	public String transformTime(String time){
		if(time.equals("시간[전체]")){
			return "";
		}else{
			return time;
		}
	}
	
}
