package com.iris.lolin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class FaceBookLoginActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_login);
		
		dataInit();
		
	}

	@SuppressLint("NewApi")
	private void dataInit() {
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.board_detail_activity_title);
	}

	public void loginFaceBook(View view){
		
		System.err.println("페이스북 로그인");
		
	}
	
}
