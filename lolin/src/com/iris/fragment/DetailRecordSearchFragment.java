package com.iris.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.iris.config.Config;
import com.iris.lolin.R;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class DetailRecordSearchFragment extends Fragment {

	private static final String BASE_URL = "http://www.op.gg/summoner/userName=";
	
	private String summernerName ;
	private PullToRefreshWebView mPullRefreshWebView;
	
	public Fragment newInstance(String smmonerName) {
		RecordSearchFragment fragment = new RecordSearchFragment();
		Bundle args = new Bundle();
		args.putString(Config.FLAG.SUMMERNER_NAME, smmonerName);
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
		webViewInit();  
		return rootView;
	}

	private void dataInit() {
		// DetailActivity 로 부터 넘겨 받은 summernerName
		summernerName = getArguments().getString(Config.FLAG.SUMMERNER_NAME);
	}

	private void init(View rootView) {
		mPullRefreshWebView = (PullToRefreshWebView)rootView.findViewById(R.id.pull_refresh_webview);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void webViewInit() {
		WebView webView = mPullRefreshWebView.getRefreshableView();
		webView.getSettings().setJavaScriptEnabled(true); 
		webView.loadUrl(BASE_URL +summernerName);
		webView.setWebViewClient(new BasicWebViewClient());
	}
	
	private class BasicWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	
}
