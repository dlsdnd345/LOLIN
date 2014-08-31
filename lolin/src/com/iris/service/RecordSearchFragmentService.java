package com.iris.service;

import android.content.Context;

import com.iris.config.Config;
import com.iris.util.SharedpreferencesUtil;

public class RecordSearchFragmentService {

	private SharedpreferencesUtil		sharedpreferencesUtil;
	
	public RecordSearchFragmentService(Context context){
		sharedpreferencesUtil = new SharedpreferencesUtil(context);
	}
	
	public String getUserSubUrl(){
		return "?faceBookId="+ sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");
	}
}
