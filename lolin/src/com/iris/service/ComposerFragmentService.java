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
import com.iris.util.SharedpreferencesUtil;
import com.iris.util.SignatureUtil;

public class ComposerFragmentService {

	private Gson 						gson;
	private ArrayList<Board> 			boardListFromGson;
	private SharedpreferencesUtil		sharedpreferencesUtil;
	
	public ComposerFragmentService(Context context){
		gson = new Gson();
		sharedpreferencesUtil = new SharedpreferencesUtil(context);
	}
	
	/**
	 * 전체 게시판 얻기
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
	
	public String getSubUrlBoardFindMyAll(){
		
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
	
}
