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
import com.iris.util.SignatureUtil;

public class BoardDetailService {

	private Gson 		gson;
	private Context		context;
	private Board 		boardOneFromGson;

	public BoardDetailService(Context context){
		this.context =context;
		gson = new Gson();
	}
	
	/**
	 * 게시판 한개 의 상세정보 파싱
	 * @param jsonData
	 * @return
	 */
	public Board getBoardFindOne(String jsonData){
		
		JSONObject JsonObject;
		String ok = null;
		String data = null;
		
		try {
			JsonObject = new JSONObject(jsonData);
			ok = JsonObject.getString(Config.FLAG.OK);
			if(ok.equals(Config.FLAG.TRUE)){
				data = JsonObject.getString(Config.FLAG.DATA);
				Type type = new TypeToken<Board>(){}.getType();
				boardOneFromGson = gson.fromJson(data, type);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return boardOneFromGson;
	}
	
	public String getFindOneSubUrl(String boardId){
		
		String hash;
		String encodeBoardId 	= null;
		String encodeHash 		= null;
		
		try {
			
			String signatureData = boardId + Config.KEY.SECRET;
			hash = SignatureUtil.getHash(signatureData);
			
			encodeBoardId   = URLEncoder.encode(boardId,"UTF-8");
			encodeHash   	= URLEncoder.encode(hash,"UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "?boardId=" + encodeBoardId+"&hash="+encodeHash;
	}

	public String getDeleteSubUrl(String boardId){

		String hash;
		String encodeBoardId 	= null;
		String encodeHash 		= null;

		try {

			String signatureData = boardId + Config.KEY.SECRET;
			hash = SignatureUtil.getHash(signatureData);

			encodeBoardId   = URLEncoder.encode(boardId,"UTF-8");
			encodeHash   	= URLEncoder.encode(hash,"UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return "?boardId=" + encodeBoardId+"&hash="+encodeHash;
	}

}
