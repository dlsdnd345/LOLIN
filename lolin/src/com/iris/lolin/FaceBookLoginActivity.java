package com.iris.lolin;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

	private final static String TAG = "GCM";
	
	private final static String USER_SAVE = "http://192.168.219.6:8080/user/save";

	private final static String 		ERROR 							= "Error";
	private static final String 		EMPRY_SUMMERNER_MESSAGE  		= "소환사 명 을 입력해 주세요.";
	private static final String 		IS_LOGIN  						= "isLogin";
	private static final String 		ACCESS_TOKEN  					= "ACCESS_TOKEN";
	private static final String 		FACEBOOK_ID  					= "FACEBOOK_ID";
	private static final String 		HELLO_MESSAGE  					= "HELLO ";
	private static final String 		FACEBOOK_BASE_URL  				= "http://graph.facebook.com/";
	private static final String 		PICTURE_TYPE					= "/picture?type=large";

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String SENDER_ID 			= "376992068498";
    
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    String regid;
    
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

	private EditText 					editSummerner;
	private TextView 					txtHello , txtWarnningMessage;
	private View 						layoutLogin , layoutBtnLogin;
	private ProgressBar 				progressBar;
	private ImageView 					imgProfile;
	private ImageButton 				btnFacebookLogin , btnNotFacebookLogin;

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

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
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
	 * 액션바 이벤트 발생
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.ic_action_next:

			saveLoginInfo();
			checkEmptyEditSummerner();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
			Toast.makeText(getApplicationContext(), EMPRY_SUMMERNER_MESSAGE, Toast.LENGTH_LONG).show();

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
		
		String sub_url = "?faceBookId="+faceBookUser.getUserId()+"&summonerName="+editSummerner.getText().toString()
				+"&pushId="+regId;
		
		stringRequest =new StringRequest(Method.GET, USER_SAVE+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				JSONObject JsonObject;
				String ok = null;

				try {
					JsonObject = new JSONObject(response);
					ok = JsonObject.getString(OK);
					if(ok.equals(TRUE)){
						sharedpreferencesUtil.put(IS_LOGIN, true);
						sharedpreferencesUtil.put(ACCESS_TOKEN, sessionTemp.getAccessToken());
						sharedpreferencesUtil.put(FACEBOOK_ID, faceBookUser.getUserId());
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
				VolleyLog.d(ERROR, error.getMessage());  
				progressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
			}  
		});
	}

	/**
	 * 레이아웃 초기화
	 */
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

	/**
	 * 데이터 초기화
	 */
	@SuppressLint("NewApi")
	private void dataInit() {

		context = getApplicationContext();
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
		request = Volley.newRequestQueue(getApplicationContext());  

		imageLoderInit();
		faceBookInit();
		actionBarInit();
	}

	/**
	 * 액션바 초기화
	 */
	@SuppressLint("NewApi")
	private void actionBarInit() {
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.facebook_login_activity_title);
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

			if (session.isOpened()) {
				final Animation alphaFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_fade_out);
				layoutBtnLogin.setVisibility(View.GONE);
				layoutBtnLogin.startAnimation(alphaFadeOut);
				alphaFadeOut.setAnimationListener(alphaFadeOutAnimationListener);
			}
			updateView();    
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

	/**
	 * 비로그인 시
	 * @param view
	 */
	public void notFacebookLogin(View view){

		sharedpreferencesUtil.put(IS_LOGIN, false);

		Intent inetnt = new Intent(FaceBookLoginActivity.this , MainActivity.class);
		startActivity(inetnt);
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
                Log.i(TAG, "This device is not supported.");
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
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
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
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
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
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    
                    System.out.println("###############    regid  :  " + regid);
                    
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
    
	AnimationListener alphaFadeInAnimationListener = new AnimationListener(){

		@Override
		public void onAnimationStart(Animation animation) {}
		@Override
		public void onAnimationRepeat(Animation animation) {}
		@SuppressLint("NewApi")
		@Override
		public void onAnimationEnd(Animation animation) {
			verticalShake.setRepeatCount(Animation.INFINITE);
			imgProfile.startAnimation(verticalShake);

			isActionBar = true;
			invalidateOptionsMenu();
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

