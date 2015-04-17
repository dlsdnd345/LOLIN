package com.iris.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.iris.config.Config;
import com.iris.entities.User;
import com.iris.lolin.R;
import com.iris.service.RecordSearchService;
import com.iris.service.UserService;
import com.iris.util.SharedpreferencesUtil;

/**
 * 전적검색 프래그먼트
 */
@SuppressLint("NewApi")
public class RecordSearchFragment extends Fragment {

    private static final String BASE_URL = "http://www.op.gg/summoner/userName=";

    private ProgressBar prograssBar;
    private TextView txtSearching;

    private User user;
    private RecordSearchService recordSearchService;
    private SharedpreferencesUtil sharedpreferencesUtil;
    private PullToRefreshWebView mPullRefreshWebView;
    private UserService userService;

    private AdView adView;

    public Fragment newInstance() {
        RecordSearchFragment fragment = new RecordSearchFragment();
        return fragment;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_search, container, false);


        init(rootView);
        dataInit();
        return rootView;
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
     * 데이터 초기화
     */
    private void dataInit() {

        String deviceid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(deviceid).build();
        adView.loadAd(adRequest);


        sharedpreferencesUtil = new SharedpreferencesUtil(getActivity());
        recordSearchService = new RecordSearchService();
        userService = new UserService(getActivity());
        getUser();
    }

    /**
     * 유저 정보 조회
     */
    public void getUser() {

        prograssBar.setVisibility(View.VISIBLE);

        String sub_url = userService.getUserSubUrl();

        RequestQueue request = Volley.newRequestQueue(getActivity());
        request.add(new StringRequest(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.USER_FIND_ONE + sub_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user = recordSearchService.getUser(response);
                webViewInit();
                prograssBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Config.FLAG.ERROR, error.getMessage());
                prograssBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
            }
        }));

    }

    /**
     * 웹뷰 초기화
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void webViewInit() {
        WebView webView = mPullRefreshWebView.getRefreshableView();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(BASE_URL + user.getSummonerName());
        webView.setWebViewClient(new BasicWebViewClient());
    }

    /**
     * 레이아웃 초기화
     *
     * @param rootView
     */
    private void init(View rootView) {

        adView = (AdView) rootView.findViewById(R.id.adView);
        txtSearching = (TextView) rootView.findViewById(R.id.txt_searching);
        prograssBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mPullRefreshWebView = (PullToRefreshWebView) rootView.findViewById(R.id.pull_refresh_webview);
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
