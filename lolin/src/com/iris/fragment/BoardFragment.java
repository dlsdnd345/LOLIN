package com.iris.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.iris.adapter.BoardAdapter;
import com.iris.config.Config;
import com.iris.entities.Board;
import com.iris.entities.User;
import com.iris.lolin.BoardDetailActivity;
import com.iris.lolin.R;
import com.iris.service.BoardService;
import com.iris.service.SettingService;
import com.iris.util.SharedpreferencesUtil;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class BoardFragment extends Fragment {

	private final static String BOARD_FINDALL = "http://192.168.219.6:8080/board/findAll";
	private final static String ERROR = "Error";

	private static final String RANK_DATA_POSITION 		= "RankDataPosition";
	private static final String POSITION_DATA__POSITION 	= "PositionDataPosition";
	private static final String TIME_DATA_POSITION 		= "TimeDataPosition";

	private LinearLayout				bottomBar;
	
	private BoardService 				boardService;
	private ArrayList<Board> 			boardList;
	private BoardAdapter 				boardAdapter;
	private PullToRefreshListView 		boardListView;
	private SharedpreferencesUtil		sharedpreferencesUtil;
	private String[] 					rankData,positionData,timeData;
	private Spinner 					rankSpinner,positionSpinner,timeSpinner;	
	private ArrayAdapter<String> 		rankSpinnerAdapter,positionSpinnerAdapter,timeSpinnerAdapter;

	private User 						user;

	private SettingService				settingService;

	public Fragment newInstance(Context context) {

		BoardFragment fragment = new BoardFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public BoardFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_board, container,false);

		init(rootView);

		return rootView;
	}



	@Override
	public void onResume() {
		super.onResume();
		dataInit();
	}

	private void init(View rootView) {
		
		bottomBar  =  (LinearLayout)rootView.findViewById(R.id.bottom_bar);
		rankSpinner = (Spinner)rootView.findViewById(R.id.spinner_rank);
		timeSpinner = (Spinner)rootView.findViewById(R.id.spinner_time);
		positionSpinner = (Spinner)rootView.findViewById(R.id.spinner_position);
		boardListView = (PullToRefreshListView)rootView.findViewById(R.id.list_board);
	}


	private void dataInit(){

		settingService = new SettingService();
		boardService = new BoardService(getActivity());
		spinnerInit();
		getBoardFindAll(); 
		getUser();

	}

	private void getBoardFindAll() {

		RequestQueue request = Volley.newRequestQueue(getActivity());  

		request.add(new StringRequest(Request.Method.GET, BOARD_FINDALL+boardService.getSubUrl(),new Response.Listener<String>() {  

			@Override  
			public void onResponse(String response) {  
				boardList = boardService.getBoardFindAll(response);
				listViewInit(boardList);
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));
	}

	/**
	 * 유정 정보 get
	 */
	public void getUser(){

		String sub_url = "?faceBookId="+ sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");

		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest(Request.Method.GET, Config.API.USER_FIND_ONE+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				user = settingService.getUser(response);
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));

	}

	private void spinnerInit() {

		Context context = getActivity();
		sharedpreferencesUtil = new SharedpreferencesUtil(context);
		//spinner init
		rankData = context.getResources().getStringArray(R.array.main_rank_array_list);

		rankSpinnerAdapter= new ArrayAdapter<>
		(context, R.layout.white_spinner_item,rankData);
		rankSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		rankSpinner.setAdapter(rankSpinnerAdapter); 
		rankSpinner.setOnItemSelectedListener(rankOnItemSelectedListener);
		rankSpinner.setSelection(sharedpreferencesUtil.getValue(RANK_DATA_POSITION, 0));

		positionData = context.getResources().getStringArray(R.array.main_position_array_list);
		positionSpinnerAdapter= new ArrayAdapter<>
		(context, R.layout.white_spinner_item,positionData);
		positionSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		positionSpinner.setAdapter(positionSpinnerAdapter); 
		positionSpinner.setOnItemSelectedListener(positionOnItemSelectedListener);
		positionSpinner.setSelection(sharedpreferencesUtil.getValue(POSITION_DATA__POSITION, 0));

		timeData = context.getResources().getStringArray(R.array.main_time_array_list);
		timeSpinnerAdapter= new ArrayAdapter<>
		(context, R.layout.white_spinner_item,timeData);
		timeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		timeSpinner.setAdapter(timeSpinnerAdapter); 
		timeSpinner.setOnItemSelectedListener(timeOnItemSelectedListener);
		timeSpinner.setSelection(sharedpreferencesUtil.getValue(TIME_DATA_POSITION, 0));
	}

	private void listViewInit(ArrayList<Board> boardList) {
		boardAdapter = new BoardAdapter(getActivity(), R.layout.row_board_list, boardList);
		boardListView.setAdapter(boardAdapter);
		boardListView.setOnRefreshListener(mOnRefreshListener);
		boardListView.setOnItemClickListener(mOnItemClickListener);
		ListView actualListView = boardListView.getRefreshableView();
		actualListView.setOnTouchListener(mOnTouchListener);
		registerForContextMenu(actualListView);
	}

	OnItemSelectedListener rankOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			sharedpreferencesUtil.put(RANK_DATA_POSITION, position);
			getBoardFindAll();
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

	OnItemSelectedListener positionOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			sharedpreferencesUtil.put(POSITION_DATA__POSITION, position);
			getBoardFindAll();
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

	OnItemSelectedListener timeOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			sharedpreferencesUtil.put(TIME_DATA_POSITION, position);
			getBoardFindAll();
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

			//자기 것만 수정 , 삭제 할 수 있게 아이콘을 보여주기 위함.
			Boolean editState = boardList.get(position-1).getUserId() == user.getId();

			Intent intent = new Intent(getActivity(), BoardDetailActivity.class);
			intent.putExtra("id", boardList.get(position-1).getId());
			intent.putExtra("editState", editState);

			startActivity(intent);
		}

	};

//	OnTouchListener mOnTouchListener = new View.OnTouchListener(){
//
//		float startPosition ;
//		boolean blockMove = true;
//		float getY ;
//		
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_MOVE:       //터치를 한 후 움직일때
//
//				if(blockMove){
//					startPosition = event.getY();
//					blockMove = false;
//					System.out.println("##############111111111111111111    startPosition :  " + startPosition);
//				}else{
//					blockMove = true;
//				}
//
//				System.out.println("##############2222222222222222    event :  " + event.getY());
//				if(blockMove){
//
//					if(startPosition > event.getY()){
//						
//						System.out.println("##########아래아래아래아래아래아래아래아래아래아래");
//						System.out.println("#########3333333333333333333   bottomBar.getY()  :  " + bottomBar.getY());
//						
//						if(bottomBar.getY() >= 1040){
//							blockMove = false;
//						}else{
//							bottomBar.setY(bottomBar.getY() + getY);
//							getY = getY + 0.1f;
//						}
//						
//					}
//					
//					if(startPosition < event.getY()){
//						System.out.println("############위위위위위위위위");
//						if(bottomBar.getY() <= 959){
//							blockMove = false;
//						}else{
//							bottomBar.setY(bottomBar.getY() - getY);
//							getY = getY - 0.1f;
//						}
//						
//					}
//				}
//
//
//				break;
//			}
//			return false;
//		}
//
//	};

	// Pull To Refresh 시 실행되는 Task
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(3000);
				getBoardFindAll();
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			boardListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

}
