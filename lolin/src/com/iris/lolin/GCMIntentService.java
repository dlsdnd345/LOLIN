package com.iris.lolin;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.iris.config.Config;

public class GCMIntentService extends GCMBaseIntentService {
	
	public static final String TOAST_MESSAGE_ACTION = "org.androidtown.gcm.push.TOAST_MESSAGE";
    private static final String tag = "GCMIntentService";
	
	private String msg;
	
	public GCMIntentService() { // 생성자
		super(Config.GCM.PROJECT_ID);
		Log.d(tag, "GCMIntentService() called.");
	}
	public void onRegistered(Context context, String registrationId) { // 등록시 실행되는 메서드
		Log.d(tag, "onRegistered called : " + registrationId);
		sendToastMessage(context, "등록되었습니다");
	}
	public void onUnregistered(Context context, String registrationId) { // 등록되지않았을때 실행되는 메서드
		Log.d(tag, "onUnregistered called.");
		sendToastMessage(context, "등록 되지 않았습니다");
	}
	public void onError(Context context, String errorId) { // 에러시 실행되는 메서드
		Log.d(tag, "onError called.");
		sendToastMessage(context, "에러: " + errorId);
	}
	protected void onDeletedMessages(Context context, int total) { // 삭제시 실행되는 메서드
		Log.d(tag, "onDeletedMessages called.");
		super.onDeletedMessages(context, total);
	}
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.d(tag, "onRecoverableError called.");
		return super.onRecoverableError(context, errorId);
	}
	@SuppressWarnings("static-access")
	public void onMessage(Context context, Intent intent) { // 메세지가 오면 실행되는 메서드
		Log.d(tag, "onMessage called.");
		
		//sharedPreferencesUtil = new SharedPreferencesUtil(context);
		//MPConstants.NETWORK_CHECK = true;
		
		Bundle extras = intent.getExtras(); // 서버로 부터 데이터 받는 부분
		
		if (extras != null) {
			msg = (String) extras.get("message"); // 데이터 변수에 담는 부분
			// shopId 푸시화면 선택시 해당 아이템이 있는 곳으로 이동시키기 위해 받아놓는다.
			//title = MPMessage.TITLE_NOTI_MESSAGE; // 노티 제목
			Log.d(tag, "@@@@@@@@@@@@@@@@@@@msg :" + msg);
			/*
			 * sharedPreferences 에 해당 shopId 저장
			 * 저장한 이유는 푸시화면에서 콜한 가게를 내번호표쪽가서 보여주기위해 필요
			 * selectBlock 또한 
			 */
			//sharedPreferencesUtil.setSharedPreferencesString("myTiketView", "shopId",shopId);
			//sharedPreferencesUtil.setSharedPreferencesString("myTiketView", "selectBlock","true");
			/*
			 * noti Click 시 대기현황 페이지 유도
			 * sharedPreferences : comeBack : tabMenu 는 메인뷰의 탭페이지를 의미
			 * PendingIntent 는 GCM 노티가 왔을시에 노티 선택시 화면전환이 이루어지는데
			 * 페이지 전환을 컨트롤을 의미한다.
			 */
//			NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
//			SharedPreferences sharedPreferences = context.getSharedPreferences("comeBack", Context.MODE_PRIVATE);
//			//sharedPreferencesUtil.setSharedPreferencesInt("comeBack", "tabMenu",0);
//			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainView.class)
//			.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)
//			.putExtra("message", msg).putExtra("tabMenu",sharedPreferences.getInt("tabMenu",0)), 0); // 노티 선택시 화면전환
//
//			// notification 설정
//			Notification notification = new Notification(); 
//			notification.icon = R.drawable.top_icon; // 노티 이미지 
//			notification.tickerText = msg; // 노티 메인 제목
//			notification.when = System.currentTimeMillis();
//			notification.vibrate = new long[] { 1000, 3000, 1000, 3000}; // 진동 홀수인자(슬립시간)/짝수인자(진동시간)
//			notification.defaults = notification.DEFAULT_SOUND;// 노티 사운드
//			notification.flags |= Notification.FLAG_AUTO_CANCEL; // 1:(사용자가 상태정보란 확장시까지 출력) 2:(사용자가 노티 선택시 출력 취소)
//			notification.setLatestEventInfo(context, title, msg, pendingIntent); // 노티 선택시 실행되는 메서드
//			//노티 아이디 부여 (노티 아이디는 노티 알림을 다른 페이지에서도 접근할수 있도록 도와준다.)
//			notificationManager.notify(0, notification);
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
//			SharedPreferences sharedPrefer = getSharedPreferences("push",Context.MODE_PRIVATE);			
//			if(sharedPrefer.getString("notiblock","").equals("false")){
//				Intent newIntent = new Intent(context, NotificationView.class);  
//				newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				newIntent.putExtra("message", msg);
//				context.startActivity(newIntent);
//			}
		}
	}
	static void sendToastMessage(Context context, String message) {
		Intent intent = new Intent(TOAST_MESSAGE_ACTION);
		intent.putExtra("message", message);
		context.sendBroadcast(intent);
	}
}

