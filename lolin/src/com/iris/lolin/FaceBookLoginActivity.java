package com.iris.lolin;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;










import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.google.gson.reflect.TypeToken;
import com.iris.entities.Board;
import com.iris.entities.FaceBookUser;
import com.iris.util.SharedpreferencesUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


public class FaceBookLoginActivity extends ActionBarActivity {

	private static final String OK = "ok";
	private static final String TRUE = "true";
	private static final String DATA = "data";
	
	private final static String USER_SAVE = "http://192.168.219.6:8080/user/save";

	private final static String 		ERROR 							= "Error";
	private static final String 		EMPRY_SUMMERNER_MESSAGE  		= "소환사 명 을 입력해 주세요.";
	private static final String 		ACCESS_TOKEN  					= "ACCESS_TOKEN";
	private static final String 		FACEBOOK_ID  					= "FACEBOOK_ID";
	private static final String 		HELLO_MESSAGE  					= "HELLO ";
	private static final String 		FACEBOOK_BASE_URL  				= "http://graph.facebook.com/";
	private static final String 		PICTURE_TYPE					= "/picture?type=large";

	private StringRequest 				stringRequest;
	private RequestQueue 				request;
	private DisplayImageOptions 		options;
	private Animation 					verticalShake;
	private Session 					sessionTemp;
	private ProgressBar 				progressBar;
	private ImageView 					imgProfile;
	private ImageLoader 				imageLoader;
	private FaceBookUser 				faceBookUser;
	private EditText 					editSummerner;
	private SharedpreferencesUtil 		sharedpreferencesUtil;
	private TextView 					txtHello , txtWarnningMessage;
	private View 						layoutLogin , layoutBtnLogin;
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
			
			saveLoginInfo();
			
			if(!editSummerner.getText().toString().equals("")){
				request.add(stringRequest);
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

	public void saveLoginInfo(){
		
		String sub_url = "?faceBookId="+faceBookUser.getUserId()+"&summonerName="+editSummerner.getText().toString();
		stringRequest =new StringRequest(Method.GET, USER_SAVE+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				JSONObject JsonObject;
				String ok = null;
				
				try {
					JsonObject = new JSONObject(response);
					ok = JsonObject.getString(OK);
					if(ok.equals(TRUE)){
						sharedpreferencesUtil.put(ACCESS_TOKEN, sessionTemp.getAccessToken());
						sharedpreferencesUtil.put(FACEBOOK_ID, faceBookUser.getUserId());
						Intent intent = new Intent(FaceBookLoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		});
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
		request = Volley.newRequestQueue(getApplicationContext());  
		
		imageLoderInit();
		faceBookInit();
		actionBarInit();
	}

	@SuppressLint("NewApi")
	private void actionBarInit() {
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.facebook_login_activity_title);
	}

	private void faceBookInit(){
		// FaceBook Init
		faceBookUser = new FaceBookUser();
	}

	private void imageLoderInit() {
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
			Request.newMeRequest(session, new  com.facebook.Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user,com.facebook.Response response) {
					response.getError();
					txtHello.setText(HELLO_MESSAGE+ user.getName());
					String url = FACEBOOK_BASE_URL+ user.getId()+PICTURE_TYPE;
					imageLoader.displayImage(url, imgProfile, options, mImageLoadingListener);
					faceBookUser.setUserId( user.getId());
				}
			}).executeAsync();
		}
	}

	ImageLoadingListener mImageLoadingListener = new ImageLoadingListener(){
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
	};

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
			Animation alphaFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_fade_in);
			verticalShake.setRepeatCount(Animation.INFINITE);
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

