package com.iris.lolin;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.iris.config.Config;
import com.iris.util.SharedpreferencesUtil;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	public static final String TOAST_MESSAGE_ACTION = "org.androidtown.gcm.push.TOAST_MESSAGE";
	private static final String TITLE = "롤인 앱으로 부터 메세지가 도착했습니다.";

	private String msg;
	private SharedpreferencesUtil sharedpreferencesUtil;


	public GCMIntentService() { // 생성자
		super(Config.GCM.PROJECT_ID);
		Log.d(TAG, "GCMIntentService() called.");
	}
	public void onRegistered(Context context, String registrationId) { // 등록시 실행되는 메서드
		Log.d(TAG, "onRegistered called : " + registrationId);
		sendToastMessage(context, "등록되었습니다");
	}
	public void onUnregistered(Context context, String registrationId) { // 등록되지않았을때 실행되는 메서드
		Log.d(TAG, "onUnregistered called.");
		sendToastMessage(context, "등록 되지 않았습니다");
	}
	public void onError(Context context, String errorId) { // 에러시 실행되는 메서드
		Log.d(TAG, "onError called.");
		sendToastMessage(context, "에러: " + errorId);
	}
	protected void onDeletedMessages(Context context, int total) { // 삭제시 실행되는 메서드
		Log.d(TAG, "onDeletedMessages called.");
		super.onDeletedMessages(context, total);
	}
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.d(TAG, "onRecoverableError called.");
		return super.onRecoverableError(context, errorId);
	}
	
	public void onMessage(Context context, Intent intent) { // 메세지가 오면 실행되는 메서드
		Log.d(TAG, "onMessage called.");

		Bundle extras = intent.getExtras(); // 서버로 부터 데이터 받는 부분

		if (extras != null) {
			msg = (String) extras.get("message"); // 데이터 변수에 담는 부분
			
			NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
			
			//노티 선택시 화면 이동 설정
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class)
			.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)
			.putExtra("message", msg), 0); // 노티 선택시 화면전환

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

			mBuilder.setContentTitle(TITLE); // 제목
			mBuilder.setContentText(msg); //내용
			mBuilder.setSmallIcon(R.drawable.ic_launcher); // 아이콘
			mBuilder.setTicker(TITLE); // 상태바 제목
			mBuilder.setAutoCancel(true);
			mBuilder.setWhen(System.currentTimeMillis()); // 진동시간
			mBuilder.setVibrate(new long[] { 1000, 3000, 1000, 3000}); // 진동패턴
			mBuilder.setDefaults(Notification.DEFAULT_SOUND); //기본 사운드
			mBuilder.setContentIntent(pendingIntent);
			//노티 아이디 부여 (노티 아이디는 노티 알림을 다른 페이지에서도 접근할수 있도록 도와준다.)
			notificationManager.notify(0, mBuilder.build());

			/*
			 * 어플이 실행되고 있을시, GCM 화면 막기위해 사용
			 *  SharedPreferences : PUSH : notiblock 은 노티가 올때 두가지 경우가 존재한다.
			 *  어플이 실행되고 있을시와 , 어플이 실행되고 있지 않을시 .
			 *  어플이 실행되고 있을때는 노티만 와야하고 푸시화면은 나타나지 않아야한다.
			 *  또한 어플이 실행되고 있지 않을시에는 노티와 푸시화면이 동시에 나타나야 하는것이 맞다.
			 *  그러므로 어플이 실행됙소 있을시와 아닐시를 구분하는 값이다.
			 *  NotificationView 에는 Wake Up 기능이 있어, 폰 화면이 꺼져있을시 깨워주는 기능이 있다.
			 *  또한 화면이 꺼져 있을 시에는 푸시화면이 나와 있는 것이 맞다 .
			 *  어플이 실행되있는 경우가 있지만 화면이 꺼져 있는경우가 있기 때문에
			 *  스크린이 꺼져있는지 안꺼져있는지 확인이 필요하다.
			 */
			sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
			if(sharedpreferencesUtil.getValue("notiblock", "").equals("false")){
				Intent newIntent = new Intent(context, PushActivity.class);  
				newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				newIntent.putExtra("message", msg);
				context.startActivity(newIntent);
			}
		}
	}
	static void sendToastMessage(Context context, String message) {
		Intent intent = new Intent(TOAST_MESSAGE_ACTION);
		intent.putExtra("message", message);
		context.sendBroadcast(intent);
	}
}

