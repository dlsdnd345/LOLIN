package com.iris.lolin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;
import com.iris.adapter.BoardDetailPagerAdapter;
import com.iris.config.Config;
import com.iris.entities.Board;
import com.iris.entities.User;
import com.iris.service.BoardDetailService;
import com.iris.service.SettingService;
import com.iris.util.SharedpreferencesUtil;

public class BoardDetailActivity extends ActionBarActivity {

	
	private final static String ERROR = "Error";
	
	private static final int CONTENT_FRAGMENT = 0;
	private static final int RECORD_SEARCH_FRAGMENT = 1;

	private BoardDetailService 			boardDetailService;
	
	private PagerSlidingTabStrip 		tabs;
	private ViewPager 					mViewPager;
	private PagerAdapter 				mPagerAdapter;
	
	private ProgressBar					prograssBar;
	
	private ImageView					imgRank;
	
	private TextView					textRank;
	private	 TextView					textPosition;
	private TextView					textPlayTime;
	private TextView					textDetailTitle;
	private TextView					textSummernerName;
	
	private RequestQueue 				request;
	
	private Board						board;
	
	private boolean					editState;
	private String						boardId;
	private int 						viewPagerPosition;
	
	private SharedpreferencesUtil 		sharedpreferencesUtil;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.acticity_board_detail);
	    
	    init();
		dataInit();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//화면이 켜져 있을때 푸시화면을 보여주기 않기 위함.
		sharedpreferencesUtil.put("notiblock", "true");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//화면이 켜져 있을때 푸시화면을 보여주기 않기 위함.
		sharedpreferencesUtil.put("notiblock", "false");
	}
	
	/**
	 * 레이아웃 초기화
	 */
	private void init() {
		
		prograssBar 		= (ProgressBar)findViewById(R.id.progressBar);
		imgRank				= (ImageView)findViewById(R.id.img_rank);
		textRank			= (TextView)findViewById(R.id.text_rank);
		textPlayTime        = (TextView)findViewById(R.id.text_play_time);
		textPosition 		= (TextView)findViewById(R.id.text_position);
		textSummernerName 	= (TextView)findViewById(R.id.text_summernerName);
		textDetailTitle 	= (TextView)findViewById(R.id.text_detail_title);
		mViewPager 			= (ViewPager) findViewById(R.id.pager);
		tabs 				= (PagerSlidingTabStrip) findViewById(R.id.tabs);
		
	}
	
	/**
	 * 데이터 초기화
	 */
	private void dataInit() {
		
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
		
		request = Volley.newRequestQueue(getApplicationContext());  
		boardDetailService = new BoardDetailService(getApplicationContext());
		
		Intent intent = getIntent();
		boardId = sharedpreferencesUtil.getValue(Config.BOARD.BOARD_ID, "");
		
		System.out.println("9999999999999999997777777777777777777777777777777  boardId" + boardId);
		
		editState = intent.getBooleanExtra(Config.FLAG.EDIT_STATE , false);
		
		getFindOne(request);
		
		//Pager Init
		actionBarInit();
		
	}

	/**
	 * 액션바 생성
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if(editState){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.board_detail_menu, menu);
		}
		return super.onCreateOptionsMenu(menu);
		
	}
	
	/**
	 * 액션바 클릭 리스터
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.ic_action_edit:
			
			Intent intent = new Intent(BoardDetailActivity.this,ComposerActivity.class);
			intent.putExtra(Config.BOARD.BOARD_ID, boardId);
			startActivity(intent);
			return true;
		case R.id.ic_action_remove:
			// 게시물 삭제
			delete(request);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * 아이디를 통해서 게시판 한개의 데이터를 얻음.
	 * @param request
	 */
	private void getFindOne(RequestQueue request) {
		
		prograssBar.setVisibility(View.VISIBLE);
		
		request.add(new StringRequest(Request.Method.GET, Config.BOARD.BOARD_FIND_ONE + Config.BOARD.SUB_URL+boardId ,new Response.Listener<String>() {  
			
			@Override  
			public void onResponse(String response) {  
				
				System.out.println("99999999999999999966666666666666666666   :  board  " + boardId );
				
				board = boardDetailService.getBoardFindOne(response);
				
				if(board != null){
					
				textDetailTitle.setText(board.getTitle());
				textSummernerName.setText(board.getSummonerName());
				textPosition.setText(board.getPosition());
				textPlayTime.setText(board.getPlayTime());
				textRank.setText(board.reverseTransformRank(board.getRank())+ " " + board.getTea());
				}
				// 이름별 랭크 이미지 삽입
				int resource = getResources().getIdentifier
				( "img_rank_"+board.getRank(), "drawable", getApplicationContext().getPackageName());
				imgRank.setBackgroundResource(resource);
				
				viewPagerInit();
				
				prograssBar.setVisibility(View.INVISIBLE);
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));
	}
	
	/**
	 * 게시물 삭제 Api
	 * @param request
	 */
	private void delete(RequestQueue request) {
		
		prograssBar.setVisibility(View.VISIBLE);
		
		request.add(new StringRequest(Request.Method.GET, Config.BOARD.BOARD_DELETE + Config.BOARD.SUB_URL+boardId ,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {
				Intent intent = new Intent(BoardDetailActivity.this, MainActivity.class);
				startActivity(intent);
				
				prograssBar.setVisibility(View.INVISIBLE);
				
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));
	}
	
	@SuppressLint("NewApi")
	private void actionBarInit() {
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.board_detail_activity_title);
	}

	private void viewPagerInit() {
		
		System.err.println("99999999999998888888888888888888   :  board"  + board.getId());
		
		mPagerAdapter = new BoardDetailPagerAdapter(getApplicationContext(),getSupportFragmentManager(),board);
		mViewPager.setAdapter(mPagerAdapter);
		
		tabs.setIndicatorColor(Color.parseColor("#0099cc"));
		tabs.setOnPageChangeListener(mOnPageChangeListener);
		tabs.setViewPager(mViewPager);
	}
	
	OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener(){
		@SuppressLint("NewApi")
		@Override
		public void onPageSelected(int position) {

			viewPagerPosition = position;
			if(position == CONTENT_FRAGMENT){
				getActionBar().setTitle(R.string.detail_title_section1);
			}else if(position == RECORD_SEARCH_FRAGMENT){
				getActionBar().setTitle(R.string.detail_title_section2);
			}else{
				getActionBar().setTitle(R.string.detail_title_section3);
			}
		}
		@Override
		public void onPageScrolled(int position, float positionOffest, int positionOffestPixel) {}
		@Override
		public void onPageScrollStateChanged(int position) {}
	};
	
}
