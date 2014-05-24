package com.iris.lolin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
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
import com.iris.entities.Board;
import com.iris.service.BoardDetailService;

public class BoardDetailActivity extends ActionBarActivity {

	private final static String BOARD_FIND_ONE = "http://192.168.219.6:8080/board/findOne";
	private final static String SUB_URL = "?id=";
	
	private final static String ERROR = "Error";
	private final static String ID = "id";
	
	private static final int CONTENT_FRAGMENT = 0;
	private static final int RECORD_SEARCH_FRAGMENT = 1;

	BoardDetailService 					boardDetailService;
	
	private PagerSlidingTabStrip 		tabs;
	private ViewPager 					mViewPager;
	private PagerAdapter 				mPagerAdapter;
	
	private ImageView					imgRank;
	private	 TextView					textPosition;
	private TextView					textPlayTime;
	private TextView					textDetailTitle;
	private TextView					textSummernerName;
	
	private int						id;
	private Board						board;
	private int 						viewPagerPosition;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.acticity_board_detail);
	    
	    init();
		dataInit();
		
	}

	private void init() {
		
		imgRank				= (ImageView)findViewById(R.id.img_rank);
		textPlayTime        = (TextView)findViewById(R.id.text_play_time);
		textPosition 		= (TextView)findViewById(R.id.text_position);
		textSummernerName 	= (TextView)findViewById(R.id.text_summernerName);
		textDetailTitle 	= (TextView)findViewById(R.id.text_detail_title);
		mViewPager 			= (ViewPager) findViewById(R.id.pager);
		tabs 				= (PagerSlidingTabStrip) findViewById(R.id.tabs);
		
	}
	
	private void dataInit() {
		
		RequestQueue request = Volley.newRequestQueue(getApplicationContext());  
		boardDetailService = new BoardDetailService(getApplicationContext());
		
		Intent intent = getIntent();
		id = intent.getIntExtra(ID, 0);
		
		getFindOne(request);
		
		//Pager Init
		actionBarInit();
		
	}

	private void getFindOne(RequestQueue request) {
		request.add(new StringRequest(Request.Method.GET, BOARD_FIND_ONE + SUB_URL+id ,new Response.Listener<String>() {  
			
			@Override  
			public void onResponse(String response) {  
				board = boardDetailService.getBoardFindOne(response);
				
				textDetailTitle.setText(board.getTitle());
				textSummernerName.setText(board.getSummonerName());
				textPosition.setText(board.getPosition());
				textPlayTime.setText(board.getPlayTime());
				// 이름별 랭크 이미지 삽입
				int resource = getResources().getIdentifier
				( "img_rank_"+board.getRank(), "drawable", getApplicationContext().getPackageName());
				imgRank.setBackgroundResource(resource);
				
				viewPagerInit();
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
