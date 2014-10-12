package com.iris.lolin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.iris.util.NetworkUtil;
import com.iris.util.SharedpreferencesUtil;

public class IntroActivity extends Activity {

	private static final int SLEEP_TIME = 2000;
	
	private NetworkUtil			   networkUtil;
	private SharedpreferencesUtil sharedpreferencesUtil;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_intro);
	    
	    dataInit();
		
	    if(networkUtil.introNetworkCheck()){
	    	introDelay();
	    }
	}

	/**
	 * 데이터 초기화
	 */
	private void dataInit() {
		networkUtil = new NetworkUtil(IntroActivity.this);
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
	}
	
	/**
	 * 인트로 시간 지정
	 */
	private void introDelay() {
	    new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
                
                if(sharedpreferencesUtil.getValue("ACCESS_TOKEN", "").equals("")){
                	Intent i = new Intent(IntroActivity.this, FaceBookLoginActivity.class);
                	startActivity(i);
                	overridePendingTransition(0, 0);
                }else{
                	Intent i = new Intent(IntroActivity.this, MainActivity.class);
                	startActivity(i);
                }
                
                finish();
            }
        }).start();
	}

}
