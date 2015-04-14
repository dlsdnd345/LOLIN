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

import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.iris.config.Config;
import com.iris.lolin.R;

/**
 * 상세내용 전적 검색 프래그먼트
 */
@SuppressLint("NewApi")
public class DetailRecordSearchFragment extends Fragment {

	private static final String BASE_URL = "http://www.op.gg/summoner/userName=";


    private String summonerName;

	private PullToRefreshWebView mPullRefreshWebView;
	
	public Fragment newInstance(String summonerName) {

        DetailRecordSearchFragment fragment = new DetailRecordSearchFragment();
		Bundle args = new Bundle();

        args.putString(Config.FLAG.BOARD_SUMMERNER_NAME, summonerName);
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

        summonerName = getArguments().getString(Config.FLAG.BOARD_SUMMERNER_NAME);

        webViewInit();


	}

	private void init(View rootView) {
		mPullRefreshWebView = (PullToRefreshWebView)rootView.findViewById(R.id.pull_refresh_webview);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void webViewInit() {
		WebView webView = mPullRefreshWebView.getRefreshableView();
		webView.getSettings().setJavaScriptEnabled(true);

		webView.loadUrl(BASE_URL +summonerName);
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
