package com.iris.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Session;
import com.iris.adapter.SectionsPagerAdapter;
import com.iris.config.Config;
import com.iris.entities.User;
import com.iris.lolin.FaceBookLoginActivity;
import com.iris.lolin.MainActivity;
import com.iris.lolin.R;
import com.iris.service.SettingService;
import com.iris.util.SharedpreferencesUtil;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class SettingFragment extends Fragment {

	private MainActivity				activity;
	
	private User						user;
	private RequestQueue 				request;
	private StringRequest 				stringRequest;
	private SettingService				settingService;
	private SharedpreferencesUtil 		sharedpreferencesUtil;

	private ProgressBar					prograssBar;
	private	 Button						btnUpdate;
	private Button						btnFacebookLogout;
	private TextView					txtVersion;
	private EditText					editSummonerName;

	public Fragment newInstance() {
		SettingFragment fragment = new SettingFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_setting, container,false);
		activity = ((MainActivity)getActivity());
		
		init(rootView);
		dataInit();
		getUser();
		return rootView;
	}

	/**
	 * 레이아웃 초기화
	 * @param rootView
	 */
	private void init(View rootView) {
		prograssBar 		= (ProgressBar)rootView.findViewById(R.id.progressBar);
		txtVersion 			= (TextView)rootView.findViewById(R.id.txt_version);
		editSummonerName 	= (EditText)rootView.findViewById(R.id.setting_edit_summonerName);
		btnUpdate 			= (Button)rootView.findViewById(R.id.btn_update);
		btnFacebookLogout	= (Button)rootView.findViewById(R.id.btn_facebook_logout);
		btnUpdate.setOnClickListener(mClickListener);
		btnFacebookLogout.setOnClickListener(mClickListener);
	}

	/**
	 * 데이터 초기화
	 */
	private void dataInit() {
		settingService = new SettingService();
		request = Volley.newRequestQueue(getActivity());  
		sharedpreferencesUtil = new SharedpreferencesUtil(getActivity());
		txtVersion.setText(getVersion());

	}

	/**
	 * 버전 정보 조회
	 * @return
	 */
	private String getVersion() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageInfo.versionName;
	}

	/**
	 *  유저정보 조회
	 */
	public void getUser(){

		prograssBar.setVisibility(View.VISIBLE);

		String sub_url = "?faceBookId="+ sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");

		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.USER_FIND_ONE+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				user = settingService.getUser(response);
				editSummonerName.setText(user.getSummonerName());

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
	 * 소환사 이름 수정
	 */
	public void updateSummonerName(){

		prograssBar.setVisibility(View.VISIBLE);

		String encodeSummonerName = settingService.getEncodeSummonerName(editSummonerName.getText().toString());
		String sub_url = "?faceBookId="+user.getFacebookId()+"&summonerName="+encodeSummonerName;
		stringRequest =new StringRequest(Method.GET, Config.API.DEFAULT_URL + Config.API.USER_SAVE_DEFAULT+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  

				String isOk =settingService.updateSummonerName(response);

				if(isOk.equals(Config.FLAG.TRUE)){
					Toast.makeText(getActivity(), "소환사 명이  " +editSummonerName.getText().toString()+ "  으로 변경 되었습니다.",
							Toast.LENGTH_SHORT).show();
					activity.changePage(SectionsPagerAdapter.BOARD_FRAGMENT);
				}

				prograssBar.setVisibility(View.INVISIBLE);

			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(Config.FLAG.ERROR, error.getMessage());  
				prograssBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity().getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
			}  
		});
		request.add(stringRequest);

	}

	/**
	 * Logout From Facebook 
	 */
	public static void callFacebookLogout(Context context) {
	    Session session = Session.getActiveSession();
	    
	    if (session != null) {

	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	        }
	    } else {

	        session = new Session(context);
	        Session.setActiveSession(session);

	        session.closeAndClearTokenInformation();
	            //clear your preferences if saved

	    }

	    Intent intent = new Intent(context,FaceBookLoginActivity.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(intent);
	}
	
	/**
	 * 버튼 리스너
	 */
	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_update:
				updateSummonerName();
				break;
			case R.id.btn_facebook_logout:
				callFacebookLogout(getActivity().getApplicationContext());
				break;
				
			}
		}
	};
}
