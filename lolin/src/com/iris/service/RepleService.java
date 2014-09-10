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
import com.iris.libs.TrippleDes;
import com.iris.util.SignatureUtil;

public class RepleService {

	private Gson 	gson;
	private ArrayList<Reple> repleListFromGson;
	
	public RepleService(){
		gson = new Gson();
	}
	
	/**
	 * url 만드는 작업
	 * @param boardId
	 * @param userName
	 * @param content
	 * @param facebookId
	 * @return
	 */
	public String getSubUrl(int boardId , String userName , String content , String facebookId){
		
		String hash = null;
		String subUrl = null;
		String encodeHash = null;
		String encodeUserName = null;
		String encodeContent = null;
		String encodeFacebookId = null;
		
		try {
			
			TrippleDes trippleDes = new TrippleDes();
			facebookId = trippleDes.encrypt(facebookId);
			
			String signatureData = boardId + userName + content + facebookId + Config.KEY.SECRET;
			hash = SignatureUtil.getHash(signatureData);
			
			encodeHash = URLEncoder.encode(hash,"UTF-8");
			encodeUserName = URLEncoder.encode(userName,"UTF-8");
			encodeContent = URLEncoder.encode(content,"UTF-8");
			encodeFacebookId = URLEncoder.encode(facebookId,"UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subUrl = "?boardId="+boardId+"&userName="+encodeUserName+"&content="+encodeContent+"&facebookId="+encodeFacebookId +
				"&hash="+encodeHash;
	}
	
	/**
	 * 푸시 subUrl 만드는 작업
	 * @param os
	 * @param boardId
	 * @param summernerName
	 * @param reple
	 * @param facebookId
	 * @return
	 */
	public String getSendPushSubUrl(String os , String boardId , String summernerName ,String reple, String facebookId){
		
		String hash = null;
		String subUrl = null;
		String encodeHash = null;
		String encodeOs = null;
		String encodeBoardId = null;
		String encodeSummernerName = null;
		String encodeReple = null;
		String encodeFacebookId = null;
		
		try {
			
			TrippleDes trippleDes = new TrippleDes();
			facebookId = trippleDes.encrypt(facebookId);
			
			String signatureData = os + boardId + summernerName + facebookId + reple + Config.KEY.SECRET;
			hash = SignatureUtil.getHash(signatureData);
			
			encodeHash = URLEncoder.encode(hash,"UTF-8");
			encodeOs = URLEncoder.encode(os,"UTF-8");
			encodeBoardId = URLEncoder.encode(boardId,"UTF-8");
			encodeSummernerName = URLEncoder.encode(summernerName,"UTF-8");
			encodeReple = URLEncoder.encode(reple,"UTF-8");
			encodeFacebookId = URLEncoder.encode(facebookId,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return subUrl = "?os="+encodeOs+"&boardId="+encodeBoardId+"&summernerName="+encodeSummernerName
				+"&reple="+encodeReple+"&facebookId="+encodeFacebookId+"&hash="+encodeHash;
	}
	
	/**
	 * 리플 갱신 url
	 * @param boardId
	 * @return
	 */
	public String getFindRepleSubUrl(int boardId){
		
		String hash;
		String encodeBoardId 	= null;
		String encodeHash 		= null;
		
		try {
			
			String signatureData = boardId + Config.KEY.SECRET;
			hash = SignatureUtil.getHash(signatureData);
			
			encodeBoardId   = URLEncoder.encode(String.valueOf(boardId),"UTF-8");
			encodeHash   	= URLEncoder.encode(hash,"UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "?boardId=" + encodeBoardId+"&hash="+encodeHash;
	}
	
	/**
	 * 리플 삭제 url
	 * @param boardId
	 * @return
	 */
	public String getDeleteRepleSubUrl(int repleId){
		
		String hash;
		String encodeRepleId 	= null;
		String encodeHash 		= null;
		
		try {
			
			String signatureData = repleId + Config.KEY.SECRET;
			hash = SignatureUtil.getHash(signatureData);
			
			encodeRepleId   = URLEncoder.encode(String.valueOf(repleId),"UTF-8");
			encodeHash   	= URLEncoder.encode(hash,"UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "?repleId=" + encodeRepleId+"&hash="+encodeHash;
	}
	
	/**
	 * 리플 저장후 피드백 온 데이터 파싱
	 * @param jsonData
	 * @return
	 */
	public String saveReplePasing(String jsonData){
		
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
	
	/**
	 * 리플 삭제후 돌아오는 데이터 파싱
	 * @param jsonData
	 * @return
	 */
	public String deleteReplePasing(String jsonData){
		
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
	
	/**
	 * 게시판 한개의 댓글 파싱
	 * @param jsonData
	 * @return
	 */
	public ArrayList<Reple> getRepleFindOne(String jsonData){
		
		JSONObject JsonObject;
		String ok 	= null;
		String data = null;
		
		try {
			JsonObject = new JSONObject(jsonData);
			ok = JsonObject.getString(Config.FLAG.OK);
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
