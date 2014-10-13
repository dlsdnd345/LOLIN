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
import com.iris.config.Config;
import com.iris.entities.Board;
import com.iris.libs.TrippleDes;
import com.iris.lolin.R;
import com.iris.util.SharedpreferencesUtil;
import com.iris.util.SignatureUtil;

public class BoardService {

	private static final String RANK_DATA_POSITION 		= "RankDataPosition";
	private static final String POSITION_DATA__POSITION 	= "PositionDataPosition";
	private static final String TIME_DATA_POSITION 		= "TimeDataPosition";
	
	private static final String POSITION 		= "포지션 전체";
	private static final String TIME 			= "시간 전체";
	
	private Context						context;
	private Gson 						gson;
	private Board 						board;
	private ArrayList<Board> 			boardListFromGson;
	private SharedpreferencesUtil		sharedpreferencesUtil;
	private String[] 					rankData,positionData,timeData;
	
	
	public BoardService(Context context){
		this.context =context;
		gson = new Gson();
		board = new Board();
		sharedpreferencesUtil = new SharedpreferencesUtil(context);
	}
	
	/**
	 * 게시판 전체 얻기
	 * @param jsonData
	 * @return
	 */
	public ArrayList<Board> getBoardFindAll(String jsonData){
		
		JSONObject JsonObject;
		String ok = null;
		String data = null;
		
		try {
			JsonObject = new JSONObject(jsonData);
			ok = JsonObject.getString(Config.FLAG.OK);
			if(ok.equals(Config.FLAG.TRUE)){
				data = JsonObject.getString(Config.FLAG.DATA);
				Type type = new TypeToken<List<Board>>(){}.getType();
				boardListFromGson = gson.fromJson(data, type);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return boardListFromGson;
	}
	
	/**
	 * 게시판 subUrl Make
	 * @return
	 */
	public String getSubUrl(){
		
		rankData = context.getResources().getStringArray(R.array.main_rank_array_list);
		positionData = context.getResources().getStringArray(R.array.main_position_array_list);
		timeData = context.getResources().getStringArray(R.array.main_time_array_list);
		
		String subUrl = null;
		String hash = null;
		int rankCount = sharedpreferencesUtil.getValue(RANK_DATA_POSITION, 0);
		int positionCount = sharedpreferencesUtil.getValue(POSITION_DATA__POSITION, 0);
		int timeCount = sharedpreferencesUtil.getValue(TIME_DATA_POSITION, 0);
		
		String encodeRank = null;
		String encodePosition = null;
		String encodeTime = null;
		String encodeHash = null;
		
		String transformRank = board.transformRank(rankData[rankCount]);
		String transformPosition = transformPosition(positionData[positionCount]);
		String transformTime = transformTime(timeData[timeCount]);
		
		String signatureData = transformRank + transformPosition + transformTime + Config.KEY.SECRET;
		hash = SignatureUtil.getHash(signatureData);
		
		try {
			encodeRank = URLEncoder.encode(transformRank,"UTF-8");
			encodePosition = URLEncoder.encode(transformPosition,"UTF-8");
			encodeTime = URLEncoder.encode(transformTime,"UTF-8");
			encodeHash = URLEncoder.encode(hash,"UTF-8");
			
			subUrl = "?rank="+encodeRank+"&position="+encodePosition+"&playTime="+encodeTime+"&hash="+encodeHash;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return subUrl;
		
	}
	
	public String getUserSubUrl(){
		
		String hash = null;
		String encodeHash = null;
		String encodeFacebookId = null;
		
		String facebookId = sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");
		
		try {
			TrippleDes trippleDes = new TrippleDes();
			facebookId = trippleDes.encrypt(facebookId);

			String signatureData = facebookId + Config.KEY.SECRET;
			hash = SignatureUtil.getHash(signatureData);
			
			encodeFacebookId = URLEncoder.encode(facebookId,"UTF-8");
			encodeHash = URLEncoder.encode(hash,"UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "?faceBookId="+ encodeFacebookId + "&hash=" + encodeHash;
	}
	
	/**
	 * 포지션 데이터 변환
	 * @param position
	 * @return
	 */
	public String transformPosition(String position){
		if(position.equals(POSITION)){
			return "";
		}else{
			return position;
		}
	}
	
	/**
	 * time 데이터 변환
	 * @param time
	 * @return
	 */
	public String transformTime(String time){
		if(time.equals(TIME)){
			return "";
		}else{
			return time;
		}
	}
	
	/**
	 * 리플 포맷 변경
	 * @param repleCount
	 * @return
	 */
	public String transformRepleCount(String repleCount){
		return "[" +repleCount+"]";
	}
	
}
