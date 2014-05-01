package com.iris.lolin;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.iris.adapter.SectionsPagerAdapter;
import com.iris.entities.Board;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity  {

	private final static int RECORD_SEARCH = 2;
	private final static int WRITE_TEXT = 1;
	private final static int SETTING = 3;
	
	private int viewPagerPosition;
	private ViewPager mViewPager;
	private PagerSlidingTabStrip tabs;
	private ArrayList<Board> boardList; 
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
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
	    
	    if(viewPagerPosition == 0){
		    inflater.inflate(R.menu.main, menu);
	    }else{
	    	inflater.inflate(R.menu.write_text_menu, menu);
	    }
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.ic_action_new:
	        	Intent composerActivityintent = new Intent(MainActivity.this, ComposerActivity.class);
	        	startActivity(composerActivityintent);
	            return true;
	        case R.id.ic_action_sort_by_size:
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

	private void init() {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	}


	private void dataInit() {
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
		
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.title_section1);
		
	}

}
