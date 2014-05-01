package com.iris.fragment;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.iris.adapter.BoardAdapter;
import com.iris.entities.Board;
import com.iris.lolin.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class WriteTextFragment extends Fragment {

	private BoardAdapter boardAdapter;
	private ListView writeTextListView;
	
	public Fragment newInstance(ArrayList<Board> boardList) {
		WriteTextFragment fragment = new WriteTextFragment();
		Bundle args = new Bundle();
		args.putSerializable("boardList", boardList);
		fragment.setArguments(args);
		return fragment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_write_text, container,false);
		
		ArrayList<Board> boardList = (ArrayList<Board>)getArguments().get("boardList");
		
		writeTextListView = (ListView)rootView.findViewById(R.id.list_write_text);
		boardAdapter = new BoardAdapter(getActivity(), R.layout.row_write_text_list, boardList);
		writeTextListView.setAdapter(boardAdapter);
		
		return rootView;
	}
}
