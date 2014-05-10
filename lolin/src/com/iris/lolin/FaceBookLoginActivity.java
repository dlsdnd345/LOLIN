package com.iris.lolin;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.LoggingBehavior;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;


public class FaceBookLoginActivity extends Activity {

    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private Button buttonLoginLogout;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_login);
		
		init();
		dataInit();
		facebookInit(savedInstanceState);
		 
	}

	private void init() {
		buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);
	}

	@SuppressLint("NewApi")
	private void dataInit() {
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.board_detail_activity_title);
	}
	
	private void facebookInit(Bundle savedInstanceState) {
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }
        
        updateView();
	}

	 @Override
	    public void onStart() {
	        super.onStart();
	        Session.getActiveSession().addCallback(statusCallback);
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	        Session.getActiveSession().removeCallback(statusCallback);
	    }

	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	    }

	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        Session session = Session.getActiveSession();
	        Session.saveSession(session, outState);
	    }

	    private void updateView() {
	        Session session = Session.getActiveSession();
	        if (session.isOpened()) {
	            buttonLoginLogout.setText("로그아웃");
	            buttonLoginLogout.setOnClickListener(new OnClickListener() {
	                public void onClick(View view) { onClickLogout(); }
	            });
	        } else {
	            buttonLoginLogout.setText("로그인");
	            buttonLoginLogout.setOnClickListener(new OnClickListener() {
	                public void onClick(View view) { onClickLogin(); }
	            });
	        }
	    }

	    private void onClickLogin() {
	        Session session = Session.getActiveSession();
	        if (!session.isOpened() && !session.isClosed()) {
	            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
	        } else {
	            Session.openActiveSession(this, true, statusCallback);
	        }
	    }

	    private void onClickLogout() {
	        Session session = Session.getActiveSession();
	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	        }
	    }

	    private class SessionStatusCallback implements Session.StatusCallback {
	        @Override
	        public void call(Session session, SessionState state, Exception exception) {
	            updateView();
	            
	            
	        }
	    }
	}

