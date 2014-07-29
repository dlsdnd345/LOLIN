package com.iris.service;

import android.content.Context;

import com.google.android.gcm.GCMRegistrar;
import com.iris.config.Config;

public class MainService {

	private Context context;
	
	public MainService(Context context){
		this.context = context;
	}
	
	/**
	 * registerDevice 이메서드는 GCM 푸쉬를 받기위해 등록을 도와주는 메서드.
	 * PROJECT_ID 아이디를 통해 REG_ID를 얻고 구글서버에 등록
	 */
	public String registerDevice() {
		
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		final String regId = GCMRegistrar.getRegistrationId(context); // red ID 값 얻어옴		
		if (regId.equals("")) {
			//GCMRegistrar.unregister(this);
			// red 값은 해지 시켜주지 않으면 어플을 지워도 같기 때문에 해지 시켜줘야함
			GCMRegistrar.register(context, Config.GCM.PROJECT_ID); // 재등록
			System.out.println("등록했습니다");
		} else {
			if (GCMRegistrar.isRegisteredOnServer(context)) {
				System.out.println("등록 되어 있습니다.");
			}
		}
		System.out.println("**********************************   regId   :  " + regId);
		return regId;
	}
	
}
