package com.iris.fragment;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.iris.adapter.BoardAdapter;
import com.iris.entities.Board;
import com.iris.lolin.R;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
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
	private PullToRefreshListView boardListView;
	
	public Fragment newInstance(ArrayList<Board> boardList) {
		BoardFragment fragment = new BoardFragment();
		Bundle args = new Bundle();
		args.putSerializable("boardList", boardList);
		fragment.setArguments(args);
		return fragment;
	}

	public BoardFragment() {}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_board, container,false);

		ArrayList<Board> boardList = (ArrayList<Board>)getArguments().get("boardList");

		boardListView = (PullToRefreshListView)rootView.findViewById(R.id.list_board);
		boardAdapter = new BoardAdapter(getActivity(), R.layout.row_board_list, boardList);
		boardListView.setAdapter(boardAdapter);
		
		boardListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new GetDataTask().execute();
			}
		});
		
		ListView actualListView = boardListView.getRefreshableView();
		registerForContextMenu(actualListView);
		
		return rootView;
	}
	
	// Pull To Refresh 시 실행되는 Task
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			boardAdapter.notifyDataSetChanged();
			boardListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
	
}
