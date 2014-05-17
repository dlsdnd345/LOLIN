package com.iris.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.iris.entities.User;
import com.iris.lolin.R;
import com.iris.service.RecordSearchService;
import com.iris.util.SharedpreferencesUtil;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class RecordSearchFragment extends Fragment {

	private final static String 		ERROR 							= "Error";
	private static final String 		SUMMERNER_NAME 					= "dlsdnd345";
	private static final String 		FACEBOOK_ID  					= "FACEBOOK_ID";
	private static final String 		BASE_URL 						= "http://www.op.gg/summoner/userName=";
	private final static String 		USER_FIND_ONE					= "http://192.168.219.6:8080/user/findOne";
	
	private User 						user;
	private RecordSearchService 		recordSearchService;
	private SharedpreferencesUtil 		sharedpreferencesUtil;
	private PullToRefreshWebView 		mPullRefreshWebView;
	
	public Fragment newInstance() {
		RecordSearchFragment fragment = new RecordSearchFragment();
		return fragment;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_record_search, container,false);

		
		init(rootView);
		dataInit();
		return rootView;
	}

	private void dataInit() {
		sharedpreferencesUtil = new SharedpreferencesUtil(getActivity());
		recordSearchService = new RecordSearchService();
		getUser();
	}
	
	public void getUser(){
		
		String sub_url = "?faceBookId="+ sharedpreferencesUtil.getValue(FACEBOOK_ID, "");
		
		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest(Request.Method.GET, USER_FIND_ONE+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				user = recordSearchService.getUser(response);
				webViewInit();  
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));
		
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void webViewInit() {
		WebView webView = mPullRefreshWebView.getRefreshableView();
		webView.getSettings().setJavaScriptEnabled(true); 
		webView.loadUrl(BASE_URL+user.getSummonerName());
		webView.setWebViewClient(new BasicWebViewClient());
	}

	private void init(View rootView) {
		mPullRefreshWebView = (PullToRefreshWebView)rootView.findViewById(R.id.pull_refresh_webview);
	}
	
	private class BasicWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	
}
