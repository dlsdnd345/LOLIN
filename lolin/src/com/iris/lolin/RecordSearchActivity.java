package com.iris.lolin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class RecordSearchActivity extends Activity {

    private static final String BASE_URL = "http://www.op.gg/summoner/userName=";

    private AdView adView;

    private WebView recordSearchWebview;
    private ProgressBar prograssBar;
    private TextView txtSearching;

    private String summernerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_search);

        initLayout();
        dataInit();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (adView != null) {
            adView.pause();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (adView != null) {
            adView.destroy();
        }
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout(){

        adView = (AdView)findViewById(R.id.adView);
        txtSearching = (TextView)findViewById(R.id.txt_searching);
        prograssBar = (ProgressBar)findViewById(R.id.progressBar);
        recordSearchWebview = (WebView)findViewById(R.id.record_search_webview);
    }

    /**
     * 데이터 초기화
     */
    private void dataInit(){

        String deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(deviceid).build();
        adView.loadAd(adRequest);

        Intent intent = getIntent();
        summernerName = intent.getStringExtra("summernerName");

        webViewInit();
    }

    /**
     * 웹뷰 초기화
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void webViewInit() {

        recordSearchWebview.getSettings().setJavaScriptEnabled(true);
        recordSearchWebview.loadUrl(BASE_URL + summernerName);
        recordSearchWebview.getSettings().setUseWideViewPort(true);
        recordSearchWebview.setInitialScale(1);
        recordSearchWebview.setWebViewClient(new BasicWebViewClient());
    }

    private class BasicWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            txtSearching.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            txtSearching.setVisibility(View.INVISIBLE);
        }

    }


}
