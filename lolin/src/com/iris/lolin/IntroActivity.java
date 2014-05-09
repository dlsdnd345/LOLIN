package com.iris.lolin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class IntroActivity extends Activity {

	private static final int SLEEP_TIME = 2000;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_intro);
	    
	    new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
                Intent i = new Intent(IntroActivity.this, FaceBookLoginActivity.class);
                startActivity(i);
                finish();
            }
        }).start();
	    
	}

}
