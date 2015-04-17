package com.iris.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.iris.vo.BoardResponseVO;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class BoardFragment extends Fragment {

	private static final String RANK_DATA_POSITION 			= "RankDataPosition";
	private static final String POSITION_DATA__POSITION 	= "PositionDataPosition";
	private static final String TIME_DATA_POSITION 			= "TimeDataPosition";

	private int						pageCount;
	private int						totalPageCount;

	// 하단 계속 스크롤시 않될수 있도록 방지
	private boolean 					isScrollblock = true;
	private boolean 					lastItemVisibleFlag;

	private BoardService 				boardService;
	private SharedpreferencesUtil		sharedpreferencesUtil;

	private List<Board>					boardList;
	private List<Board>					totalBoardList;
	private BoardResponseVO 			boardResponseVO;

	private User 						user;
	private String[] 					rankData,positionData,timeData;
	private ArrayAdapter<String> 		rankSpinnerAdapter,positionSpinnerAdapter,timeSpinnerAdapter;

	private SettingService				settingService;

	private TextView					txtNoListMessage;
	private LinearLayout				bottomBar;
	private ProgressBar					prograssBar;
	private BoardAdapter 				boardAdapter;
	private PullToRefreshListView 		boardListView;
	private Spinner 					rankSpinner,positionSpinner,timeSpinner;	

	boolean lastitemVisibleFlag = false; 

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

        sharedpreferencesUtil = new SharedpreferencesUtil(getActivity().getApplicationContext());
        sharedpreferencesUtil.put("isLoad",true);
		init(rootView);

		return rootView;
	}



	@Override
	public void onResume() {
		super.onResume();

        boolean isLoad = sharedpreferencesUtil.getValue("isLoad",true);

        if(isLoad) {

            dataInit();
        }

        sharedpreferencesUtil.put("isLoad",true);
	}

    @Override
    public void onPause() {
        super.onPause();

        sharedpreferencesUtil.put("isLoad",false);
    }

    /**
	 * 레이아웃 초기화
	 * @param rootView
	 */
	private void init(View rootView) {

		txtNoListMessage	=  (TextView)rootView.findViewById(R.id.txtNoListMessage);
		prograssBar 		=  (ProgressBar)rootView.findViewById(R.id.progressBar);
		bottomBar  			=  (LinearLayout)rootView.findViewById(R.id.bottom_bar);
		rankSpinner 		= (Spinner)rootView.findViewById(R.id.spinner_rank);
		timeSpinner 		= (Spinner)rootView.findViewById(R.id.spinner_time);
		positionSpinner		= (Spinner)rootView.findViewById(R.id.spinner_position);
		boardListView 		= (PullToRefreshListView)rootView.findViewById(R.id.list_board);

		boardListView.setOnScrollListener(mOnScrollListener);
	}

	/**
	 * 데이터 초기화
	 */
	private void dataInit(){

        //뷰페이져 이동으로 인해서 초기화 진행
        pageCount = 1;

		settingService = new SettingService();
		boardService = new BoardService(getActivity());
		totalBoardList = new ArrayList<Board>();

		spinnerInit();
		getBoardFindAll(pageCount,true); 
		getUser();
	}

	/**
	 * 게시판 조회
	 */
	private void getBoardFindAll(int pageCount , final boolean noAsync) {

		if(noAsync){
			prograssBar.setVisibility(View.VISIBLE);
		}

		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest
				(Request.Method.GET, Config.API.DEFAULT_URL +
						Config.API.BOARD_FINDALL+boardService.getSubUrl(pageCount,Config.COMMON.PAGE_SIZE),new Response.Listener<String>() {  

					@Override  
					public void onResponse(String response) {  

                        Log.i("response",response);

						rankSpinner.setOnItemSelectedListener(rankOnItemSelectedListener);
						positionSpinner.setOnItemSelectedListener(positionOnItemSelectedListener);
						timeSpinner.setOnItemSelectedListener(timeOnItemSelectedListener);
						
						isScrollblock = true;

						boardResponseVO = boardService.getBoardFindAll(response);
						boardList = boardResponseVO.getBoardList();

						for (int i = 0; i < boardList.size(); i++) {
							totalBoardList.add(boardList.get(i));
						}

						visibleEmptyMessage();
						listViewInit(totalBoardList);
						if(noAsync){
							prograssBar.setVisibility(View.INVISIBLE);
						}
					}

				}, new Response.ErrorListener() {  
					@Override  
					public void onErrorResponse(VolleyError error) {  
						VolleyLog.d(Config.FLAG.ERROR, error.getMessage());  
						prograssBar.setVisibility(View.INVISIBLE);
						Toast.makeText(getActivity().getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
					}  
				}));
	}

	/**
	 * 유정 정보 get
	 */
	public void getUser(){

		prograssBar.setVisibility(View.VISIBLE);

		String sub_url = boardService.getUserSubUrl();

		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.USER_FIND_ONE+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				user = settingService.getUser(response);
				prograssBar.setVisibility(View.INVISIBLE);
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(Config.FLAG.ERROR, error.getMessage());  
			}  
		}));
	}

	/**
	 * 스피너 초기화
	 */
	private void spinnerInit() {

		Context context = getActivity();
		//spinner init
		rankData = context.getResources().getStringArray(R.array.main_rank_array_list);
		//랭크
		rankSpinnerAdapter= new ArrayAdapter<>
		(context, R.layout.item_spinner_black,rankData);
		rankSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		rankSpinner.setAdapter(rankSpinnerAdapter); 
		rankSpinner.setSelection(sharedpreferencesUtil.getValue(RANK_DATA_POSITION, 0));
		//포지션
		positionData = context.getResources().getStringArray(R.array.main_position_array_list);
		positionSpinnerAdapter= new ArrayAdapter<>
		(context, R.layout.item_spinner_black,positionData);
		positionSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		positionSpinner.setAdapter(positionSpinnerAdapter); 
		positionSpinner.setSelection(sharedpreferencesUtil.getValue(POSITION_DATA__POSITION, 0));
		//시간
		timeData = context.getResources().getStringArray(R.array.main_time_array_list);
		timeSpinnerAdapter= new ArrayAdapter<>
		(context, R.layout.item_spinner_black,timeData);
		timeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		timeSpinner.setAdapter(timeSpinnerAdapter); 
		timeSpinner.setSelection(sharedpreferencesUtil.getValue(TIME_DATA_POSITION, 0));
		
	}

	/**
	 * 데이터 비어있을시 예외처리
	 */
	private void visibleEmptyMessage() {
		if(boardList != null){
			if(totalBoardList.size() == 0){
				txtNoListMessage.setVisibility(View.VISIBLE);
			}else{
				txtNoListMessage.setVisibility(View.INVISIBLE);
			}
		}
	}  

	/**
	 * 게시판 리스트뷰 초기화
	 * @param boardList
	 */
	private void listViewInit(List<Board> boardList) {

		if(pageCount == 1){
			boardAdapter = new BoardAdapter(getActivity(), R.layout.row_board_list, boardList);
			boardListView.setAdapter(boardAdapter);
			boardListView.setOnRefreshListener(mOnRefreshListener);
			boardListView.setOnItemClickListener(mOnItemClickListener);
			ListView actualListView = boardListView.getRefreshableView();
			registerForContextMenu(actualListView);
		}else{
			boardAdapter.notifyDataSetChanged();
		}
	}

	// Pull To Refresh 시 실행되는 Task
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(1500);
				
				pageCount = 1;
				totalBoardList = new ArrayList<Board>();
				getBoardFindAll(pageCount,false);
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

	/**
	 * 하단 랭크 스피너 선택시
	 */
	OnItemSelectedListener rankOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			
			pageCount = 1;
			totalBoardList = new ArrayList<Board>();
			sharedpreferencesUtil.put(RANK_DATA_POSITION, position);
			getBoardFindAll(pageCount,true);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

	/**
	 * 하단 포지션 스피너 선택시
	 */
	OnItemSelectedListener positionOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			
			pageCount = 1;
			totalBoardList = new ArrayList<Board>();
			sharedpreferencesUtil.put(POSITION_DATA__POSITION, position);
			getBoardFindAll(pageCount,true);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

	/**
	 * 하단 시간 스피너 선택시
	 */
	OnItemSelectedListener timeOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			
			pageCount = 1;
			totalBoardList = new ArrayList<Board>();
			sharedpreferencesUtil.put(TIME_DATA_POSITION, position);
			getBoardFindAll(pageCount,true);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

	/**
	 * Pull To Refresh 당길시
	 */
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

	/**
	 * 게시판 row 선택시
	 */
	OnItemClickListener mOnItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

			Boolean editState = null;
			if(user != null){
				editState = totalBoardList.get(position-1).getUserId() == user.getId();
			}

            if(editState == null){
                editState = false;
            }

			Intent intent = new Intent(getActivity(), BoardDetailActivity.class);
			sharedpreferencesUtil.put(Config.BOARD.BOARD_ID, String.valueOf(totalBoardList.get(position-1).getId()));
			sharedpreferencesUtil.put(Config.FLAG.EDIT_STATE, editState);
			startActivity(intent);

            sharedpreferencesUtil.put("isLoad",false);
		}

	};

	
	/**
	 * 리스트뷰 하단에 닿았을시
	 */
	/**
	 * 피드 리스트뷰 리스너
	 */
	OnScrollListener mOnScrollListener = new OnScrollListener(){

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			//현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) 
			//+ 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
			lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);     
		}   
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			//OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다. 
			//즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻

			if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && isScrollblock) {

				pageCount++;
				isScrollblock = false;

				if(boardResponseVO.getPageTotalCount() != 0){
					totalPageCount = boardResponseVO.getPageTotalCount()/Config.COMMON.PAGE_SIZE;

					if(totalPageCount != 0){					
						if(boardResponseVO.getPageTotalCount()%Config.COMMON.PAGE_SIZE != 0){
							totalPageCount = totalPageCount + 1;
						}
					}
				}

				if(totalPageCount >= pageCount){
					getBoardFindAll(pageCount,true);
				}
			} 
		}

	};



}
