package com.iris.fragment;

import java.util.ArrayList;

import com.astuetz.PagerSlidingTabStrip;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.iris.adapter.BoardAdapter;
import com.iris.entities.Board;
import com.iris.lolin.BoardDetailActivity;
import com.iris.lolin.R;
import com.iris.util.SharedpreferencesUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class BoardFragment extends Fragment {

	private static final String BOOK_LIST 				= "boardList";
	private static final String RANK_DATA_POSITION 		= "RankDataPosition";
	private static final String POSITION_DATA__POSITION 	= "PositionDataPosition";
	private static final String TIME_DATA_POSITION 		= "TimeDataPosition";
	
	private ArrayList<Board> 			boardList;
	private Context 					context;
	private BoardAdapter 				boardAdapter;
	private PullToRefreshListView 		boardListView;
	private SharedpreferencesUtil		sharedpreferencesUtil;
	private String[] 					rankData,positionData,timeData;
	private Spinner 					rankSpinner,positionSpinner,timeSpinner;	
	private ArrayAdapter<String> 		rankSpinnerAdapter,positionSpinnerAdapter,timeSpinnerAdapter;

	public Fragment newInstance(Context context ,ArrayList<Board> boardList) {
		
		this.context = context;
		BoardFragment fragment = new BoardFragment();
		Bundle args = new Bundle();
		args.putSerializable(BOOK_LIST, boardList);
		fragment.setArguments(args);
		return fragment;
	}

	public BoardFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_board, container,false);

		init(rootView);
		dataInit(rootView);
		
		return rootView;
	}

	private void init(View rootView) {
		rankSpinner = (Spinner)rootView.findViewById(R.id.spinner_rank);
		positionSpinner = (Spinner)rootView.findViewById(R.id.spinner_position);
		timeSpinner = (Spinner)rootView.findViewById(R.id.spinner_time);
	}

	
	@SuppressWarnings("unchecked")
	private void dataInit(View rootView){
		
		boardList = (ArrayList<Board>)getArguments().get("boardList");
		boardListView = (PullToRefreshListView)rootView.findViewById(R.id.list_board);
		
		sharedpreferencesUtil = new SharedpreferencesUtil(rootView.getContext());
		//spinner init
		rankData = rootView.getResources().getStringArray(R.array.main_rank_array_list);
		
		rankSpinnerAdapter= new ArrayAdapter<>
		(rootView.getContext(), R.layout.white_spinner_item,rankData);
		rankSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		rankSpinner.setAdapter(rankSpinnerAdapter); 
		rankSpinner.setOnItemSelectedListener(rankOnItemSelectedListener);
		rankSpinner.setSelection(sharedpreferencesUtil.getValue(RANK_DATA_POSITION, 0));

		positionData = rootView.getResources().getStringArray(R.array.main_position_array_list);
		positionSpinnerAdapter= new ArrayAdapter<>
		(rootView.getContext(), R.layout.white_spinner_item,positionData);
		positionSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		positionSpinner.setAdapter(positionSpinnerAdapter); 
		positionSpinner.setOnItemSelectedListener(positionOnItemSelectedListener);
		positionSpinner.setSelection(sharedpreferencesUtil.getValue(POSITION_DATA__POSITION, 0));

		timeData = rootView.getResources().getStringArray(R.array.main_time_array_list);
		timeSpinnerAdapter= new ArrayAdapter<>
		(rootView.getContext(), R.layout.white_spinner_item,timeData);
		timeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		timeSpinner.setAdapter(timeSpinnerAdapter); 
		timeSpinner.setOnItemSelectedListener(timeOnItemSelectedListener);
		timeSpinner.setSelection(sharedpreferencesUtil.getValue(TIME_DATA_POSITION, 0));
		
		listViewInit();
		
	}
	
	private void listViewInit() {
		boardAdapter = new BoardAdapter(getActivity(), R.layout.row_board_list, boardList);
		boardListView.setAdapter(boardAdapter);
		boardListView.setOnRefreshListener(mOnRefreshListener);
		boardListView.setOnItemClickListener(mOnItemClickListener);
		ListView actualListView = boardListView.getRefreshableView();
		registerForContextMenu(actualListView);
	}

	OnItemSelectedListener rankOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			sharedpreferencesUtil.put(RANK_DATA_POSITION, position);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	OnItemSelectedListener positionOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			sharedpreferencesUtil.put(POSITION_DATA__POSITION, position);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	OnItemSelectedListener timeOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			sharedpreferencesUtil.put(TIME_DATA_POSITION, position);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	OnRefreshListener<ListView> mOnRefreshListener = new OnRefreshListener(){

		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
					DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			new GetDataTask().execute();
		}

	};

	OnItemClickListener mOnItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			Intent intent = new Intent(getActivity(), BoardDetailActivity.class);
			startActivity(intent);
		}

	};

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
