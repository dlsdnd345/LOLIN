package com.iris.analytics;


import android.app.Activity;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by woong on 2015. 4. 3..
 */
public class GoogleTracker {

    private static GoogleTracker googleTracker = null;

    private static Tracker tracker;


    /**
     * 싱글톤
     * @return
     */
    public static GoogleTracker getInstance(Activity activity){

        if(googleTracker == null){
            googleTracker = new GoogleTracker();
        }

        init(activity);

        return googleTracker;
    }

    /**
     * tracker 초기화
     */
    public static void init(Activity activity){

        tracker = ((GoogleAnalyticsApp) activity.getApplication()).getTracker(
                GoogleAnalyticsApp.TrackerName.APP_TRACKER);
    }

    /**
     * Screen Tracker 전송
     * @param activityName
     */
    public void sendScreenView(String activityName){

        // Set screen name.
        tracker.setScreenName(activityName);
        // Send a screen view.
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * Event Tracker 전송
     * @param category 액티비티
     * @param action 위젯
     * @param label 이름
     */
    public void sendEventView(String category , String action , String label){

        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }


    /**
     * 액티비티 시작 이벤트 전송 액티비티 마다 들어가야 함.
     * @param activity
     */
    public void actionActivityStart(Activity activity){
        GoogleAnalytics.getInstance(activity).reportActivityStart(activity);
    }

    /**
     * 액티비티 종료 이벤트 전송 액티비티 마다 들어가야 함.
     * @param activity
     */
    public void actionActivityStop(Activity activity){
        GoogleAnalytics.getInstance(activity).reportActivityStop(activity);
    }

}
