package com.iris.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

	private static final String OK = "ok";
	private static final String TRUE = "true";
	private static final String DATA = "data";

	private final static String 		ERROR 							= "Error";

	private User						user;
	private TextView					txtVersion;
	private RequestQueue 				request;
	private	 Button						btnUpdate;
	private StringRequest 				stringRequest;
	private EditText					editSummonerName;
	private SettingService				settingService;
	private SharedpreferencesUtil 		sharedpreferencesUtil;

	public Fragment newInstance() {
		SettingFragment fragment = new SettingFragment();
		return fragment;
	}

	public SettingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_setting, container,false);

		init(rootView);
		dataInit();
		getUser();

		return rootView;
	}

	private void init(View rootView) {
		txtVersion = (TextView)rootView.findViewById(R.id.txt_version);
		editSummonerName = (EditText)rootView.findViewById(R.id.setting_edit_summonerName);
		btnUpdate = (Button)rootView.findViewById(R.id.btn_update);
		btnUpdate.setOnClickListener(mClickListener);
	}

	private void dataInit() {
		settingService = new SettingService();
		request = Volley.newRequestQueue(getActivity());  
		sharedpreferencesUtil = new SharedpreferencesUtil(getActivity());
		txtVersion.setText(getVersion());

	}

	private String getVersion() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageInfo.versionName;
	}

	public void getUser(){

		String sub_url = "?faceBookId="+ sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");

		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest(Request.Method.GET, Config.API.USER_FIND_ONE+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				user = settingService.getUser(response);
				editSummonerName.setText(user.getSummonerName());

			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));

	}

	public void updateSummonerName(){

		String encodeSummonerName = settingService.getEncodeSummonerName(editSummonerName.getText().toString());
		String sub_url = "?faceBookId="+user.getFacebookId()+"&summonerName="+encodeSummonerName;
		stringRequest =new StringRequest(Method.GET, Config.API.USER_SAVE+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				JSONObject JsonObject;
				String ok = null;

				try {
					JsonObject = new JSONObject(response);

					ok = JsonObject.getString(OK);
					if(ok.equals(TRUE)){
						Toast.makeText(getActivity(), "소환사 명이  " +editSummonerName.getText().toString()+ "  으로 변경 되었습니다.",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		});
		request.add(stringRequest);

	}

	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_update:
				updateSummonerName();
				break;
			}
		}
	};
}
