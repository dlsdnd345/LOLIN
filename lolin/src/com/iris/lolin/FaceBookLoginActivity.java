package com.iris.lolin;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.iris.config.Config;
import com.iris.entities.FaceBookUser;
import com.iris.service.FacebookLoginService;
import com.iris.util.SharedpreferencesUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;


public class FaceBookLoginActivity extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    private String regid;
    private GoogleCloudMessaging gcm;
    private Context context;
    
	private boolean					isActionBar;
	private String 						regId;
	
	private StringRequest 				stringRequest;
	private RequestQueue 				request;
	private DisplayImageOptions 		options;
	private Animation 					verticalShake;
	private Session 					sessionTemp;
	private ImageLoader 				imageLoader;
	private FaceBookUser 				faceBookUser;
	private SharedpreferencesUtil 		sharedpreferencesUtil;
	private Session.StatusCallback 		statusCallback = new SessionStatusCallback();
	private FacebookLoginService		facebookLoginService;
	
	private EditText 					editSummerner;
	
	private TextView					textName;
	private TextView					txtHello;
	private TextView 					txtWarnningMessage;
	
	private Button 						btnStart;
	
	private View 						layoutSummoner , layoutLogin;
	private ProgressBar 				progressBar;
	private ImageView 					imgProfile;
	private Button 						btnFacebookLogin , btnNotFacebookLogin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_login);

		init();
		dataInit();
		facebookInit(savedInstanceState);
        gcmInit();
		
	}

	/**
	 * GCM regID 등록과정 진행
	 */
	@SuppressLint("NewApi")
	private void gcmInit() {
		// Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            Log.i(Config.GCM.TAG, "###################" + regid);
            
            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(Config.GCM.TAG, "No valid Google Play Services APK found.");
        }
	}

	@Override
	protected void onResume() {
		super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
	}
	
	/**
	 * 액션바 아이템 생성
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(isActionBar){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.facebook_next_menu, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 소환사 명이 입력이 되었는지 판단
	 */
	private void checkEmptyEditSummerner() {
		if(!editSummerner.getText().toString().equals("")){
			request.add(stringRequest);
		}else{
			// 비어있을시 쉐이크 애니메이션 및 토스트
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.horizontal_shake);
			editSummerner.startAnimation(shake);
			Toast.makeText
			(getApplicationContext(), getString(R.string.facebooklogint_activity_edit_summernername_empty_message), Toast.LENGTH_LONG).show();

			progressBar.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 로그인 정보 저장
	 * facebookId , 소환사명 저장
	 */
	public void saveLoginInfo(){

		progressBar.setVisibility(View.VISIBLE);
		String regId = getRegistrationId(context);
		
		String sub_url = facebookLoginService.getSaveLoginInfoSubUrl(faceBookUser.getUserId(), editSummerner.getText().toString(), regId);
		
		stringRequest =new StringRequest(Method.GET, Config.API.DEFAULT_URL + Config.API.USER_SAVE+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				JSONObject JsonObject;
				String ok = null;

				try {
					JsonObject = new JSONObject(response);
					ok = JsonObject.getString(Config.FLAG.OK);
					if(ok.equals(Config.FLAG.TRUE)){
						sharedpreferencesUtil.put(Config.FLAG.IS_LOGIN, true);
						sharedpreferencesUtil.put(Config.FLAG.ACCESS_TOKEN, sessionTemp.getAccessToken());
						sharedpreferencesUtil.put(Config.FACEBOOK.FACEBOOK_ID, faceBookUser.getUserId());
						Intent intent = new Intent(FaceBookLoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}

					progressBar.setVisibility(View.INVISIBLE);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(Config.FLAG.ERROR, error.getMessage());  
				progressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
			}  
		});
	}

	/**
	 * 레이아웃 초기화
	 */
	private void init() {

		btnStart	 		= (Button)findViewById(R.id.btn_start);
		textName	 		= (TextView)findViewById(R.id.text_name);
		layoutLogin 		= (View)findViewById(R.id.layout_login);
		layoutSummoner 		= (View)findViewById(R.id.layout_summoner);
		progressBar 		= (ProgressBar)findViewById(R.id.progressBar);
		imgProfile 			= (ImageView)findViewById(R.id.img_profile);
		editSummerner 		= (EditText)findViewById(R.id.edit_summerner);
		btnNotFacebookLogin = (Button)findViewById(R.id.btn_not_facebook_login);
		btnFacebookLogin 	= (Button)findViewById(R.id.btn_facebook_login);
	}

	/**
	 * 데이터 초기화
	 */
	@SuppressLint("NewApi")
	private void dataInit() {

		context = getApplicationContext();
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
		request = Volley.newRequestQueue(getApplicationContext());  
		facebookLoginService = new FacebookLoginService();
		
		final Animation alphaFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_fade_in);
		btnFacebookLogin.startAnimation(alphaFadeIn);
		btnNotFacebookLogin.startAnimation(alphaFadeIn);
		
		imageLoderInit();
		faceBookInit();
	}

	/**
	 * imageLoder초기화
	 */
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

	/**
	 * 페이스북 초기화
	 */
	private void faceBookInit(){
		// FaceBook Init
		faceBookUser = new FaceBookUser();
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

	/**
	 * 로그인 시
	 */
	@SuppressLint("NewApi")
	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
		} else {
			Session.openActiveSession(this, true, statusCallback);
		}
	}

	/**
	 * 페이스북 세션 
	 * @author 박인웅
	 *
	 */
	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {

			sessionTemp = session;
			getFaceBookMe(session);
		}
	}

	/**
	 * 페이스북 정보 조회
	 * @param session
	 */
	private void getFaceBookMe(Session session){

		if(session.isOpened()){
			Request.newMeRequest(session, new  com.facebook.Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user,com.facebook.Response response) {
					response.getError();
					
					sharedpreferencesUtil.put(Config.FLAG.FACEBOOK_NAME, user.getName());
					textName.setText(user.getName());
					String url = Config.FACEBOOK.FACEBOOK_BASE_URL+ user.getId()+Config.FACEBOOK.PICTURE_TYPE_LARGE;
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

	/**
	 * 로그인 시
	 * @param view
	 */
	public void facebookLogin(View view){
		
			final Animation alphaFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_fade_out);
			btnFacebookLogin.setVisibility(View.GONE);
			btnFacebookLogin.startAnimation(alphaFadeOut);
			btnNotFacebookLogin.setVisibility(View.GONE);
			btnNotFacebookLogin.startAnimation(alphaFadeOut);
			alphaFadeOut.setAnimationListener(alphaFadeOutAnimationListener);
			onClickLogin();
	}
	
	/**
	 * 비로그인 시
	 * @param view
	 */
	public void notFacebookLogin(View view){

		sharedpreferencesUtil.put(Config.FLAG.IS_LOGIN, false);
		Intent inetnt = new Intent(FaceBookLoginActivity.this , MainActivity.class);
		startActivity(inetnt);
	}
	
	/**
	 * 롤인 시작하기 선택시
	 */

	public void startLolin(View view){
		
		saveLoginInfo();
		checkEmptyEditSummerner();
	}
	
    /**
     * 
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(Config.GCM.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
	
    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(Config.GCM.TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Config.FLAG.PROPERTY_REG_ID, regId);
        editor.putInt(Config.FLAG.PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    @SuppressLint("NewApi")
	private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(Config.FLAG.PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(Config.GCM.TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(Config.FLAG.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(Config.GCM.TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
    
    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(Config.GCM.SENDER_ID);
                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }
    
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(FaceBookLoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
      // Your implementation here.
    	
    }


	AnimationListener alphaFadeOutAnimationListener = new AnimationListener(){

		@Override
		public void onAnimationStart(Animation animation) {}
		@Override
		public void onAnimationRepeat(Animation animation) {}
		@Override
		public void onAnimationEnd(Animation animation) {
			verticalShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vertical_shake);
			verticalShake.setRepeatCount(Animation.INFINITE);
			
			layoutSummoner.setVisibility(View.VISIBLE);
			final Animation alphaFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_fade_out);
			layoutLogin.setVisibility(View.GONE);
			layoutLogin.startAnimation(alphaFadeOut);
		}

	};

}

