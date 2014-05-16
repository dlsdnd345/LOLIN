package com.iris.lolin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.iris.util.SharedpreferencesUtil;

public class IntroActivity extends Activity {

	private static final int SLEEP_TIME = 2000;
	
	private SharedpreferencesUtil sharedpreferencesUtil;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_intro);
	    
	    dataInit();
	    
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
                }else{
                	Intent i = new Intent(IntroActivity.this, MainActivity.class);
                	startActivity(i);
                }
                
                finish();
            }
        }).start();
	    
	}

	private void dataInit() {
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
	}

}
