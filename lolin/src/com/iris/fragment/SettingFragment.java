package com.iris.fragment;

import com.iris.lolin.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class SettingFragment extends Fragment {

	
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
		
		TextView textView = (TextView) rootView.findViewById(R.id.section_label);
		textView.setText("설정");
		return rootView;
	}
}
