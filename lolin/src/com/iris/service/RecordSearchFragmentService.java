package com.iris.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

import com.iris.config.Config;
import com.iris.libs.TrippleDes;
import com.iris.util.SharedpreferencesUtil;
import com.iris.util.SignatureUtil;

public class RecordSearchFragmentService {

	private SharedpreferencesUtil		sharedpreferencesUtil;
	
	public RecordSearchFragmentService(Context context){
		sharedpreferencesUtil = new SharedpreferencesUtil(context);
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
}
