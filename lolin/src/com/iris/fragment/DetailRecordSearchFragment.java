package com.iris.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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

    private AdView adView;
	
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

	private void dataInit() {

        String deviceid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(deviceid).build();
        adView.loadAd(adRequest);

        summonerName = getArguments().getString(Config.FLAG.BOARD_SUMMERNER_NAME);

        webViewInit();


	}

	private void init(View rootView) {

        adView = (AdView) rootView.findViewById(R.id.adView);
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
