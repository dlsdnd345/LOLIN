package com.iris.service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.config.Config;
import com.iris.entities.User;
import com.iris.libs.TrippleDes;
import com.iris.util.SharedpreferencesUtil;
import com.iris.util.SignatureUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

/**
 * 유저 서비스
 */
public class UserService {

    private SharedpreferencesUtil sharedpreferencesUtil;

	private User 	user;
	private Gson 	gson;

	public UserService(Context context){
		gson = new Gson();
        sharedpreferencesUtil = new SharedpreferencesUtil(context);
	}

    /**
     * 유저 데이터 얻기
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
     * 유저 정보 얻기 Url
     * @return
     */
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

            encodeFacebookId = URLEncoder.encode(facebookId, "UTF-8");
            encodeHash = URLEncoder.encode(hash,"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "?faceBookId="+ encodeFacebookId + "&hash=" + encodeHash;
    }

}
