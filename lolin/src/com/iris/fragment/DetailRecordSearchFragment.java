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
import com.iris.lolin.R;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class DetailRecordSearchFragment extends Fragment {

	private PullToRefreshWebView mPullRefreshWebView;
	
	public Fragment newInstance() {
		RecordSearchFragment fragment = new RecordSearchFragment();
		return fragment;
	}

	public DetailRecordSearchFragment() {}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail_record_search, container,false);

		mPullRefreshWebView = (PullToRefreshWebView)rootView.findViewById(R.id.pull_refresh_webview);
		WebView webView = mPullRefreshWebView.getRefreshableView();
		
		//webView.getSettings().setJavaScriptEnabled(true); 
		webView.loadUrl("http://www.op.gg/summoner/userName=dlsdnd345");
		webView.setWebViewClient(new BasicWebViewClient());  

		return rootView;
	}
	
	private class BasicWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	
}
