package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.iris.config.Config;
import com.iris.libs.TrippleDes;
import com.iris.util.SignatureUtil;

public class FacebookLoginService {

	public FacebookLoginService(){
	}
	
	/**
	 * url 만드는 작업
	 * @param id
	 * @param userName
	 * @param content
	 * @param facebookId
	 * @return
	 */
	public String getSaveLoginInfoSubUrl(String facebookId , String summonerName , String regId){
		
		String hash = null;
		String subUrl = null;
		String encodeFacebookId = null;
		String encodeSummonerName = null;
		String encodePushId = null;
		String encodeHash = null;
		
		try {
			TrippleDes trippleDes = new TrippleDes();
			regId = trippleDes.encrypt(regId);
			facebookId = trippleDes.encrypt(facebookId);

			String signatureData = facebookId + summonerName + regId + Config.KEY.SECRET;
			hash = SignatureUtil.getHash(signatureData);
			
			encodeHash = URLEncoder.encode(hash,"UTF-8");
			encodeFacebookId = URLEncoder.encode(facebookId,"UTF-8");
			
			encodeSummonerName = URLEncoder.encode(summonerName,"UTF-8");
			encodePushId = URLEncoder.encode(regId,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subUrl =  "?faceBookId="+encodeFacebookId+"&summonerName="+encodeSummonerName+"&pushId="+encodePushId+
				"&hash="+encodeHash;
	}
	
}
