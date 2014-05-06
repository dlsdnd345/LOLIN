package com.iris.lolin;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.astuetz.PagerSlidingTabStrip;
import com.iris.adapter.SectionsPagerAdapter;
import com.iris.entities.Board;
import com.iris.util.SharedpreferencesUtil;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity  {

	private final static int RECORD_SEARCH = 2;
	private final static int WRITE_TEXT = 1;
	private final static int SETTING = 3;

	float firstGetY , preGetY= 0;
	
	private SharedpreferencesUtil		sharedpreferencesUtil;
	private String[] 					rankData,positionData,timeData;
	private View 						bottomBar;
	private ViewPager 					mViewPager;
	private int 						viewPagerPosition;
	private PagerSlidingTabStrip 		tabs;
	private ArrayList<Board> 			boardList; 
	RelativeLayout.LayoutParams  		bottomBarLayoutParams;
	private SectionsPagerAdapter 		mSectionsPagerAdapter;
	private Spinner 					rankSpinner,positionSpinner,timeSpinner;	
	private ArrayAdapter<String> 		rankSpinnerAdapter,positionSpinnerAdapter,timeSpinnerAdapter;
	public 	 HorizontalScrollView 		scrollViewRank, scrollViewPosition, scrollViewTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		dataInit();
		viewPagerConfig();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.write_text_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	private void init() {
		bottomBar = (LinearLayout)findViewById(R.id.bottom_bar);
		rankSpinner = (Spinner)findViewById(R.id.spinner_rank);
		positionSpinner = (Spinner)findViewById(R.id.spinner_position);
		timeSpinner = (Spinner)findViewById(R.id.spinner_time);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	}


	private void dataInit() {

		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
		
		bottomBarLayoutParams = (RelativeLayout.LayoutParams)bottomBar.getLayoutParams();

		//spinner init
		rankData = getResources().getStringArray(R.array.main_rank_array_list);
		rankSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), android.R.layout.simple_spinner_item,rankData);
		rankSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		rankSpinner.setAdapter(rankSpinnerAdapter); 
		rankSpinner.setOnItemSelectedListener(rankOnItemSelectedListener);
		rankSpinner.setSelection(sharedpreferencesUtil.getValue("RankDataPosition", 0));

		positionData = getResources().getStringArray(R.array.main_position_array_list);
		positionSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), android.R.layout.simple_spinner_item,positionData);
		positionSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		positionSpinner.setAdapter(positionSpinnerAdapter); 
		positionSpinner.setOnItemSelectedListener(positionOnItemSelectedListener);
		positionSpinner.setSelection(sharedpreferencesUtil.getValue("PositionDataPosition", 0));

		timeData = getResources().getStringArray(R.array.main_time_array_list);
		timeSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), android.R.layout.simple_spinner_item,timeData);
		timeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		timeSpinner.setAdapter(timeSpinnerAdapter); 
		timeSpinner.setOnItemSelectedListener(timeOnItemSelectedListener);
		timeSpinner.setSelection(sharedpreferencesUtil.getValue("TimeDataPosition", 0));

		//Data Init

		boardList = new ArrayList<Board>();
		Board board1 = new Board();
		board1.setRank("unrank");
		board1.setPosition("미드");
		board1.setTitle("듀오 하실분 모십니다.");
		board1.setSummonerName("SK T1 Faker");
		board1.setContent("서폿 유저 입니다 . 블랭크 , 쓰레쉬 , 서폿 말파 유져 입니다.");
		boardList.add(board1);

		Board board2 = new Board();
		board2.setRank("bronze");
		board2.setPosition("미드");
		board2.setTitle("듀오 하실분 모십니다.");
		board2.setSummonerName("SK T1 Faker");
		board2.setContent("서폿 유저 입니다 . 블랭크 , 쓰레쉬 , 서폿 말파 유져 입니다.");
		boardList.add(board2);

		Board board3 = new Board();
		board3.setRank("silver");
		board3.setPosition("미드");
		board3.setTitle("듀오 하실분 모십니다.");
		board3.setSummonerName("SK T1 Faker");
		board3.setContent("서폿 유저 입니다 . 블랭크 , 쓰레쉬 , 서폿 말파 유져 입니다.");
		boardList.add(board3);

		Board board4 = new Board();
		board4.setRank("gold");
		board4.setPosition("미드");
		board4.setTitle("듀오 하실분 모십니다.");
		board4.setSummonerName("SK T1 Faker");
		board4.setContent("서폿 유저 입니다 . 블랭크 , 쓰레쉬 , 서폿 말파 유져 입니다.");
		boardList.add(board4);

		Board board5 = new Board();
		board5.setRank("platinum");
		board5.setPosition("미드");
		board5.setTitle("듀오 하실분 모십니다.");
		board5.setSummonerName("SK T1 Faker");
		board5.setContent("서폿 유저 입니다 . 블랭크 , 쓰레쉬 , 서폿 말파 유져 입니다.");
		boardList.add(board5);

		Board board6 = new Board();
		board6.setRank("diamond");
		board6.setPosition("미드");
		board6.setTitle("듀오 하실분 모십니다.");
		board6.setSummonerName("SK T1 Faker");
		board6.setContent("서폿 유저 입니다 . 블랭크 , 쓰레쉬 , 서폿 말파 유져 입니다.");
		boardList.add(board6);

		Board board7 = new Board();
		board7.setRank("challenger");
		board7.setPosition("미드");
		board7.setTitle("듀오 하실분 모십니다.");
		board7.setSummonerName("SK T1 Faker");
		board7.setContent("서폿 유저 입니다 . 블랭크 , 쓰레쉬 , 서폿 말파 유져 입니다.");
		boardList.add(board7);

		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.title_section1);

	}

	OnItemSelectedListener rankOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			sharedpreferencesUtil.put("RankDataPosition", position);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	OnItemSelectedListener positionOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			sharedpreferencesUtil.put("PositionDataPosition", position);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	OnItemSelectedListener timeOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			sharedpreferencesUtil.put("TimeDataPosition", position);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.ic_action_new:
			Intent composerActivityintent = new Intent(MainActivity.this, ComposerActivity.class);
			startActivity(composerActivityintent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void viewPagerConfig() {

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext(),boardList);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		tabs.setIndicatorColor(Color.parseColor("#0099cc"));
		tabs.setViewPager(mViewPager);

		tabs.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {

				viewPagerPosition = position;

				if(position == WRITE_TEXT){
					getActionBar().setTitle(R.string.title_section2);
					invalidateOptionsMenu();
				}else if(position == RECORD_SEARCH){
					getActionBar().setTitle(R.string.title_section3);
					invalidateOptionsMenu();
				}else if(position == SETTING){
					getActionBar().setTitle(R.string.title_section4);
					invalidateOptionsMenu();
				}else{
					getActionBar().setTitle(R.string.title_section1);
					invalidateOptionsMenu();
				}
			}
			@Override
			public void onPageScrolled(int position, float positionOffest, int positionOffestPixel) {}
			@Override
			public void onPageScrollStateChanged(int position) {}
		});

	}

}
