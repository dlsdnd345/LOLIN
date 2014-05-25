package com.iris.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.iris.adapter.RepleAdapter;
import com.iris.entities.Reple;
import com.iris.lolin.R;

public class RepleFragment extends Fragment {

	private static final String USER_NAME = "userName";	
	private static final String REPLE = "reple";	
	
	private RepleAdapter 		repleAdapter;
	private ListView 			repleListView;
	private TextView			textNoRepleMessage;
	
	public Fragment newInstance(ArrayList<Reple> repleList,String userName) {
		RepleFragment fragment = new RepleFragment();
		Bundle args = new Bundle();
		args.putSerializable(REPLE, repleList);
		args.putString(USER_NAME, userName);
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
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_reple, container, false);
		
		@SuppressWarnings("unchecked")
		ArrayList<Reple> repleList = (ArrayList<Reple>) getArguments().get(REPLE);
		String userName = getArguments().getString(USER_NAME);
		
		init(rootView);
		
		if(repleList.size() == 0){
			textNoRepleMessage.setVisibility(View.VISIBLE);
		}
		repleAdapter = new RepleAdapter(getActivity(), R.layout.row_left_reple, repleList,userName);
		repleListView.setAdapter(repleAdapter);
		
		return rootView;
	}

	private void init(ViewGroup rootView) {
		textNoRepleMessage 	= (TextView)rootView.findViewById(R.id.text_no_reple_message);
		repleListView 		= (ListView)rootView.findViewById(R.id.list_reple);
	}
}