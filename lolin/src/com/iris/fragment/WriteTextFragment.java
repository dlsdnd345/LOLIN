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
public class WriteTextFragment extends Fragment {

	public Fragment newInstance() {
		WriteTextFragment fragment = new WriteTextFragment();
		return fragment;
	}

	public WriteTextFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.write_text_fragment_main, container,
				false);
		TextView textView = (TextView) rootView.findViewById(R.id.section_label);
		textView.setText("내가 쓴글");
		return rootView;
	}
}
