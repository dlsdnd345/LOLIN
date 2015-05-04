/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iris.lolin;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.iris.config.Config;
import com.iris.util.SharedpreferencesUtil;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
	
	public static final String TAG = GcmIntentService.class.getName();
	public static final String TOAST_MESSAGE_ACTION = "org.androidtown.gcm.push.TOAST_MESSAGE";

	private String msg , boardId , summernerName , facebookId , repleId , writeTime;
	private SharedpreferencesUtil sharedpreferencesUtil;
	
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification(extras);
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.

                Log.i("데이터 도착 ","들어와라 " );

                /**
                 * 노티바 생성
                 */
                sendNotification(extras);
                /**
                 * 데이터 브로드 캐스트 진행
                 */
                sendMessageBroadCast(intent);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Bundle extras) {
    	
    	Context context = getApplicationContext();
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
    	
    	msg = (String) extras.get("message"); // 데이터 변수에 담는 부분
		boardId = (String) extras.get("boardId"); 
		summernerName = (String) extras.get("summernerName");
		facebookId = (String) extras.get("facebookId");
        repleId = (String) extras.get("repleId");
        writeTime = (String) extras.get("writeTime");

		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
		
		//노티 선택시 화면 이동 설정

        PendingIntent pendingIntent = null;

        if(facebookId == null){

            //전체 푸시 경우
            pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0); // 노티 선택시 화면전환
        }else{
            // 댓글 푸시 경우
            pendingIntent = PendingIntent.getActivity(context, 1, new Intent(context, BoardDetailActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Config.FLAG.MESSAGE, msg), PendingIntent.FLAG_UPDATE_CURRENT); // 노티 선택시 화면전환
        }


		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    	
		sharedpreferencesUtil.put(Config.BOARD.BOARD_ID, boardId);

		mBuilder.setContentTitle(getString(R.string.gcm_service_noti_title)); // 제목
		mBuilder.setContentText(msg); //내용
		mBuilder.setSmallIcon(R.drawable.icon); // 아이콘
		mBuilder.setTicker(getString(R.string.gcm_service_noti_title)); // 상태바 제목
		mBuilder.setAutoCancel(true);
		mBuilder.setWhen(System.currentTimeMillis()); // 진동시간
		mBuilder.setVibrate(new long[] { 1000, 3000, 1000, 3000}); // 진동패턴
		mBuilder.setDefaults(Notification.DEFAULT_SOUND); //기본 사운드
		mBuilder.setContentIntent(pendingIntent);
		//노티 아이디 부여 (노티 아이디는 노티 알림을 다른 페이지에서도 접근할수 있도록 도와준다.)
		notificationManager.notify(0, mBuilder.build());

		sharedpreferencesUtil.put(Config.FLAG.EDIT_STATE, true);
		
		/*
		 * 어플이 실행되고 있을시, GCM 화면 막기위해 사용
		 *  SharedPreferences : PUSH : notiblock 은 노티가 올때 두가지 경우가 존재한다.
		 *  어플이 실행되고 있을시와 , 어플이 실행되고 있지 않을시 .
		 *  어플이 실행되고 있을때는 노티만 와야하고 푸시화면은 나타나지 않아야한다.
		 *  또한 어플이 실행되고 있지 않을시에는 노티와 푸시화면이 동시에 나타나야 하는것이 맞다.
		 *  그러므로 어플이 실행模?있을시와 아닐시를 구분하는 값이다.
		 *  NotificationView 에는 Wake Up 기능이 있어, 폰 화면이 꺼져있을시 깨워주는 기능이 있다.
		 *  또한 화면이 꺼져 있을 시에는 푸시화면이 나와 있는 것이 맞다 .
		 *  어플이 실행되있는 경우가 있지만 화면이 꺼져 있는경우가 있기 때문에
		 *  스크린이 꺼져있는지 안꺼져있는지 확인이 필요하다.
		 */
		if(sharedpreferencesUtil.getValue("notiblock", "").equals("false")){

            //전체 푸시 경우 화면을 보여줄 필요가 없다.
            if(facebookId == null){
                return;
            }

			Intent newIntent = new Intent(context, PushActivity.class);  
			newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			newIntent.putExtra(Config.FLAG.MESSAGE, msg);
			newIntent.putExtra(Config.BOARD.BOARD_ID, boardId);
			newIntent.putExtra(Config.FLAG.FACEBOOK_ID, facebookId);
			newIntent.putExtra(Config.FLAG.SUMMERNER_NAME, summernerName);
			context.startActivity(newIntent);				
		}

    }

    /**
     * 데이터 브로드캐스트로 전달
     * @param intent
     */
    private void sendMessageBroadCast(Intent intent){

        //전체 푸시 경우 화면을 보여줄 필요가 없다.
        if(intent.getExtras().getString("facebookId") != null) {
            return;
        }

            Intent i = new Intent("BoardDetailActivity");
            i.putExtra("message", intent.getExtras().getString("message"));
            i.putExtra("boardId", intent.getExtras().getString("boardId"));
            i.putExtra("summernerName", intent.getExtras().getString("summernerName"));
            i.putExtra("facebookId", intent.getExtras().getString("facebookId"));
            i.putExtra("repleId", intent.getExtras().getString("repleId"));
            i.putExtra("writeTime", intent.getExtras().getString("writeTime"));
            sendBroadcast(i);

    }
}
