package com.iris.fragment;

import java.util.ArrayList;

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
public class BoardFragment extends Fragment {

	private BoardAdapter boardAdapter;
	
	public Fragment newInstance(ArrayList<Board> boardList) {
		BoardFragment fragment = new BoardFragment();
		Bundle args = new Bundle();
		args.putSerializable("boardList", boardList);
		fragment.setArguments(args);
		return fragment;
	}

	public BoardFragment() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.board_fragment_main, container,false);

		ArrayList<Board> boardList = (ArrayList<Board>)getArguments().get("boardList");

		ListView boardListView = (ListView)rootView.findViewById(R.id.list_board);
		boardAdapter = new BoardAdapter(getActivity(), R.layout.row_board_list, boardList);
		boardListView.setAdapter(boardAdapter);
		
		return rootView;
	}
}
