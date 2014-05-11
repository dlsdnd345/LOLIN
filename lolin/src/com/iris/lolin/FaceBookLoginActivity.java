package com.iris.lolin;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.iris.entities.FaceBookUser;
import com.iris.util.SharedpreferencesUtil;
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


public class FaceBookLoginActivity extends ActionBarActivity {

	private static final String 		EMPRY_SUMMERNER_MESSAGE  		= "소환사 명 을 입력해 주세요.";

	private Animation 					verticalShake;
	private Session 					sessionTemp;
	private View 						layoutLogin , layoutBtnLogin;
	private ProgressBar 				progressBar;
	private ImageView 					imgProfile;
	private ImageLoader 				imageLoader;
	private DisplayImageOptions 		options;
	private FaceBookUser 				faceBookUser;
	private TextView 					txtHello , txtWarnningMessage;
	private EditText 					editSummerner;
	private SharedpreferencesUtil 		sharedpreferencesUtil;
	private ImageButton 				btnFacebookLogin , btnNotFacebookLogin;
	private Session.StatusCallback 		statusCallback = new SessionStatusCallback();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_login);

		init();
		dataInit();
		facebookInit(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.facebook_next_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.ic_action_next:
			//서버에 프로필 정보 전송 코드 들어가야함.
			if(!editSummerner.getText().toString().equals("")){
				sharedpreferencesUtil.put("ACCESS_TOKEN", sessionTemp.getAccessToken());
				Intent intent = new Intent(FaceBookLoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}else{
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.horizontal_shake);
				editSummerner.startAnimation(shake);
				Toast.makeText(getApplicationContext(), EMPRY_SUMMERNER_MESSAGE, Toast.LENGTH_LONG).show();
			}

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void init() {

		layoutBtnLogin = (View)findViewById(R.id.layout_btn_login);
		layoutLogin = (View)findViewById(R.id.layout_login);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		imgProfile = (ImageView)findViewById(R.id.img_profile);
		txtHello= (TextView)findViewById(R.id.txt_hello);
		txtWarnningMessage = (TextView)findViewById(R.id.txt_warnning_message);
		editSummerner = (EditText)findViewById(R.id.edit_summerner);
		btnNotFacebookLogin = (ImageButton)findViewById(R.id.btn_not_facebook_login);
		btnFacebookLogin = (ImageButton)findViewById(R.id.btn_facebook_login);
	}

	@SuppressLint("NewApi")
	private void dataInit() {

		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());

		options = new DisplayImageOptions.Builder()
		.showImageOnFail(Color.TRANSPARENT) // 에러 났을때 나타나는 이미지
		.cacheInMemory(true)
		.displayer(new RoundedBitmapDisplayer(1000))
		.cacheOnDisc(true)
		.considerExifParams(true)
		.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs()
		.build();
		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();

		// FaceBook Init
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
		//Session session = Session.getActiveSession();
		btnFacebookLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View view) { onClickLogin(); }
		});
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

			sessionTemp = session;
			getFaceBookMe(session);

			if (session.isOpened()) {
				final Animation alphaFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_fade_out);
				layoutBtnLogin.setVisibility(View.GONE);
				layoutBtnLogin.startAnimation(alphaFadeOut);
				alphaFadeOut.setAnimationListener(alphaFadeOutAnimationListener);
			}
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
	
	AnimationListener alphaFadeInAnimationListener = new AnimationListener(){
		
		@Override
		public void onAnimationStart(Animation animation) {}
		@Override
		public void onAnimationRepeat(Animation animation) {}
		@Override
		public void onAnimationEnd(Animation animation) {
			verticalShake.setRepeatCount(Animation.INFINITE);
			imgProfile.startAnimation(verticalShake);
		}
	};
	
	AnimationListener alphaFadeOutAnimationListener = new AnimationListener(){

		@Override
		public void onAnimationStart(Animation animation) {}
		@Override
		public void onAnimationRepeat(Animation animation) {}
		@Override
		public void onAnimationEnd(Animation animation) {
			verticalShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vertical_shake);
			verticalShake.setRepeatCount(Animation.INFINITE);
			Animation alphaFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_fade_in);
			imgProfile.setVisibility(View.VISIBLE);
			txtHello.setVisibility(View.VISIBLE);
			editSummerner.setVisibility(View.VISIBLE);
			txtWarnningMessage.setVisibility(View.VISIBLE);
			imgProfile.startAnimation(alphaFadeIn);
			txtHello.startAnimation(alphaFadeIn);
			editSummerner.startAnimation(alphaFadeIn);
			txtWarnningMessage.startAnimation(alphaFadeIn);
			alphaFadeIn.setAnimationListener(alphaFadeInAnimationListener);
		}
		
	};

}

