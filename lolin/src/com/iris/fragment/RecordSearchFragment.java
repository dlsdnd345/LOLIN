package com.iris.fragment;

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

	private ProgressBar progressBar;
	
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
		View rootView = inflater.inflate(R.layout.fragment_record_search, container,
				false);

		WebView WebView = (WebView) rootView.findViewById(R.id.webview);
		progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
		WebView.getSettings().setJavaScriptEnabled(true); 
		WebView.loadUrl("http://www.op.gg/summoner/userName=dlsdnd345");
		WebView.setWebChromeClient(new webViewChrome());  

		return rootView;
	}
	
	class webViewChrome extends WebChromeClient {
		
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			//현제 페이지 진행사항을 ProgressBar를 통해 알린다.
			if(newProgress < 100) {
				progressBar.setProgress(newProgress);
			} else {
				progressBar.setVisibility(View.INVISIBLE);
				progressBar.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
			}
		}
	}
	
}
