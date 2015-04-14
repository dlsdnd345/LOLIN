package com.iris.lolin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iris.analytics.GoogleTracker;
import com.iris.config.Config;
import com.iris.entities.UpdateBoard;
import com.iris.service.IntroService;
import com.iris.util.NetworkUtil;
import com.iris.util.SharedpreferencesUtil;

public class IntroActivity extends Activity {

    private static final String TAG = IntroActivity.class.getSimpleName();

	private static final int SLEEP_TIME = 2000;
	
	private NetworkUtil	 networkUtil;
    private IntroService introService;
	private SharedpreferencesUtil sharedpreferencesUtil;

    private ProgressBar prograssBar;

    private GoogleTracker googleTracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_intro);

        init();
	    dataInit();



	    if(networkUtil.introNetworkCheck()){

            updateAppCheck();
	    }
	}

    @Override
    protected void onStart() {
        super.onStart();

        googleTracker.actionActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        googleTracker.actionActivityStop(this);
    }

    /**
     * 레이아웃 초기화
     */
    private void init(){

        prograssBar = (ProgressBar)findViewById(R.id.progressBar);
    }

	/**
	 * 데이터 초기화
	 */
	private void dataInit() {

        googleTracker = GoogleTracker.getInstance(this);
        googleTracker.sendScreenView(TAG);

        introService = new IntroService(IntroActivity.this);
		networkUtil = new NetworkUtil(IntroActivity.this);
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
	}
	
	/**
	 * 인트로 시간 지정
	 */
	private void introDelay() {
	    new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
                
                if(sharedpreferencesUtil.getValue("ACCESS_TOKEN", "").equals("")){
                	Intent i = new Intent(IntroActivity.this, FaceBookLoginActivity.class);
                	startActivity(i);
                	overridePendingTransition(0, 0);
                }else{
                	Intent i = new Intent(IntroActivity.this, MainActivity.class);
                	startActivity(i);
                }
                
                finish();
            }
        }).start();
	}

    /**
     * 앱 업데이트 여부 체크
     */
    private void updateAppCheck(){

        prograssBar.setVisibility(View.VISIBLE);

        RequestQueue request = Volley.newRequestQueue(IntroActivity.this);
        request.add(new StringRequest
                (Request.Method.GET, Config.API.DEFAULT_URL +
                        Config.API.UPDATA_APP_CHACK+introService.updateAppCheck(),new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.i(TAG,response);

                        UpdateBoard updateBoard = introService.parseUpdateAppCheck(response);

                           if(updateBoard.getOpen().equals(Config.FLAG.FALSE)){
                               updateDialog(updateBoard.getMessage(), updateBoard.getUrl());
                           }else{
                               introDelay();
                           }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(Config.FLAG.ERROR, error.getMessage());
                        prograssBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
                    }
                }));

    }

    /**
     * 업데이트 다이얼로그
     */
    private void updateDialog(String message , final String url){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle(getString(R.string.dialog_title));
        alt_bld.setMessage(message)
                .setCancelable(false).setPositiveButton(getString(R.string.dialog_clear),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (!url.equals("")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse(url);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }).setNegativeButton(getString(R.string.dialog_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alt_bld.show();
    }

}
