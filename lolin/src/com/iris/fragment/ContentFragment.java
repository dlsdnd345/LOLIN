package com.iris.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iris.config.Config;
import com.iris.entities.Board;
import com.iris.lolin.R;

/**
 * 내용 프래그먼트
 */
public class ContentFragment extends Fragment {


	private String boardContent;
	
	public Fragment newInstance(String boardContent) {
		ContentFragment fragment = new ContentFragment();
		Bundle args = new Bundle();
		args.putString(Config.FLAG.BOARD_CONTENT, boardContent);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_content, container, false);
		
		dataInit();
		TextView textContent = (TextView)rootView.findViewById(R.id.text_content);
		textContent.setText(boardContent);
		return rootView;
	}

	/**
	 * 데이터 초기화
	 */
	private void dataInit() {
		boardContent = getArguments().getString(Config.FLAG.BOARD_CONTENT);
	}
}