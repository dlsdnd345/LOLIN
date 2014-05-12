package com.iris.fragment;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.iris.adapter.BoardAdapter;
import com.iris.adapter.ComposerAdapter;
import com.iris.entities.Board;
import com.iris.lolin.BoardDetailActivity;
import com.iris.lolin.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class ComposerFragment extends Fragment {

	private static final String BOOK_LIST 				= "boardList";
	
	private ComposerAdapter 		composerAdapter;
	private ListView 				writeTextListView;
	
	public Fragment newInstance(ArrayList<Board> boardList) {
		ComposerFragment fragment = new ComposerFragment();
		Bundle args = new Bundle();
		args.putSerializable(BOOK_LIST, boardList);
		fragment.setArguments(args);
		return fragment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_write_text, container,false);
		
		init(rootView);
		listViewIInit();
		
		return rootView;
	}

	@SuppressWarnings("unchecked")
	private void listViewIInit() {
		ArrayList<Board> boardList = (ArrayList<Board>)getArguments().get(BOOK_LIST);
		composerAdapter = new ComposerAdapter(getActivity(), R.layout.row_write_text_list, boardList);
		writeTextListView.setAdapter(composerAdapter);
		writeTextListView.setOnItemClickListener(mOnItemClickListener);
	}

	private void init(View rootView) {
		writeTextListView = (ListView)rootView.findViewById(R.id.list_write_text);
	}
	
	OnItemClickListener mOnItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			Intent intent = new Intent(getActivity(), BoardDetailActivity.class);
			startActivity(intent);
		}
		
	};
}
