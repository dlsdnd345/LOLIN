package com.iris.lolin;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class PushActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_push);
	
	}

	@Override
	protected void onResume() {
		super.onResume();
		//Wake Up 폰 화면 꺼져있을시에 푸시가 도착하면 화면을 깨워주기 위함.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED   
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}
	
}
