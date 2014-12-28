package com.iris.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.iris.config.Config;
import com.iris.entities.User;
import com.iris.lolin.R;
import com.iris.service.UserService;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class DetailRecordSearchFragment extends Fragment {

	private static final String BASE_URL = "http://www.op.gg/summoner/userName=";

    private ProgressBar progressBar;

    private User user;
    private UserService userService;
	private PullToRefreshWebView mPullRefreshWebView;
	
	public Fragment newInstance() {

		RecordSearchFragment fragment = new RecordSearchFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public DetailRecordSearchFragment() {}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail_record_search, container,false);

		init(rootView);
		dataInit();

		return rootView;
	}

	private void dataInit() {
        getUser();

	}

	private void init(View rootView) {
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
		mPullRefreshWebView = (PullToRefreshWebView)rootView.findViewById(R.id.pull_refresh_webview);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void webViewInit() {
		WebView webView = mPullRefreshWebView.getRefreshableView();
		webView.getSettings().setJavaScriptEnabled(true);

        Log.i("@@@@@11111",user.getSummonerName());

		webView.loadUrl(BASE_URL +user.getSummonerName());
		webView.setWebViewClient(new BasicWebViewClient());
	}

    /**
     * 유저 정보 조회
     */
    public void getUser(){

        progressBar.setVisibility(View.VISIBLE);

        String sub_url = userService.getUserSubUrl();

        RequestQueue request = Volley.newRequestQueue(getActivity());
        request.add(new StringRequest(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.USER_FIND_ONE +sub_url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user = userService.getUser(response);
                webViewInit();
                Log.i("@@@@@00000",user.getSummonerName());
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Config.FLAG.ERROR, error.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
            }
        }));

    }

	private class BasicWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	
}
