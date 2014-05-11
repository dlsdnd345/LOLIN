package com.iris.lolin;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.iris.entities.FaceBookUser;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;


public class FaceBookLoginActivity extends Activity {

	private ProgressBar progressBar;
	private ImageView imgProfile;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private FaceBookUser faceBookUser;
	private TextView txtHello;
	private EditText editSummerner;
	private Button btnFacebookLogin , btnNotFacebookLogin;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_login);

		init();
		dataInit();
		facebookInit(savedInstanceState);

	}

	private void init() {

		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		imgProfile = (ImageView)findViewById(R.id.img_profile);
		txtHello= (TextView)findViewById(R.id.txt_hello);
		editSummerner = (EditText)findViewById(R.id.edit_summerner);
		btnNotFacebookLogin = (Button)findViewById(R.id.btn_not_facebook_login);
		btnFacebookLogin = (Button)findViewById(R.id.btn_facebook_login);
	}

	@SuppressLint("NewApi")
	private void dataInit() {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs()
		.build();

		ImageLoader.getInstance().init(config);
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showImageOnFail(Color.TRANSPARENT) // 에러 났을때 나타나는 이미지
		.cacheInMemory(true)
		.displayer(new RoundedBitmapDisplayer(1000))
		.cacheOnDisc(true)
		.considerExifParams(true)
		.build();
		

		faceBookUser = new FaceBookUser();

		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.facebook_login_activity_title);
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

			btnFacebookLogin.setVisibility(View.GONE);
			btnNotFacebookLogin.setVisibility(View.GONE);

		} else {
			btnFacebookLogin.setText("로그인");
			btnFacebookLogin.setOnClickListener(new OnClickListener() {
				public void onClick(View view) { onClickLogin(); 
				}
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

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {

			getFaceBookMe(session);
			updateView();    
		}
	}

	private void getFaceBookMe(Session session){

		if(session.isOpened()){
			Request.newMeRequest(session, new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user, Response response) {
					response.getError();

					txtHello.setText("Hello "+ user.getName());
					
					String url = "http://graph.facebook.com/"+ user.getId()+"/picture?type=large";
					
					imageLoader.displayImage(url, imgProfile, options, new ImageLoadingListener() {
					    @Override
					    public void onLoadingStarted(String imageUri, View view) {
					    	progressBar.setVisibility(View.VISIBLE);
					    }
					    @Override
					    public void onLoadingCancelled(String imageUri, View view) {}
						@Override
						public void onLoadingComplete(String arg0, View arg1,Bitmap arg2) {
							progressBar.setVisibility(View.INVISIBLE);
							imgProfile.setVisibility(View.VISIBLE);
							txtHello.setVisibility(View.VISIBLE);
							editSummerner.setVisibility(View.VISIBLE);
						}
						@Override
						public void onLoadingFailed(String arg0, View arg1,FailReason arg2) {}
					});
					
					
					faceBookUser.setUserId( user.getId());
				}
			}).executeAsync();
		}
	}

	public void notFacebookLogin(View view){

		Intent inetnt = new Intent(FaceBookLoginActivity.this , MainActivity.class);
		startActivity(inetnt);
	}

}

