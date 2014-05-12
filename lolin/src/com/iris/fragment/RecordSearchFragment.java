package com.iris.fragment;

import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.iris.lolin.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class RecordSearchFragment extends Fragment {

	private static final String BASE_URL = "http://www.op.gg/summoner/userName=";
	private static final String SUMMERNER_NAME = "dlsdnd345";
	
	private PullToRefreshWebView mPullRefreshWebView;
	
	public Fragment newInstance() {
		RecordSearchFragment fragment = new RecordSearchFragment();
		return fragment;
	}

	public RecordSearchFragment() {
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_record_search, container,false);

		init(rootView);
		webViewInit();  
		return rootView;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void webViewInit() {
		WebView webView = mPullRefreshWebView.getRefreshableView();
		webView.getSettings().setJavaScriptEnabled(true); 
		webView.loadUrl(BASE_URL+SUMMERNER_NAME);
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
