package com.iris.lolin;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.iris.adapter.SectionsPagerAdapter;
import com.iris.entities.Board;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity  {

	private final static int RECORD_SEARCH = 1;
	private final static int WRITE_TEXT = 2;
	private final static int SETTING = 3;
	
	private Board board;
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
				
				if(position == RECORD_SEARCH){
					getActionBar().setTitle(R.string.title_section2);
					invalidateOptionsMenu();
				}else if(position == WRITE_TEXT){
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
		board = new Board();
		board.setRank("실버");
		board.setPosition("미드");
		board.setTitle("듀오 하실분 모십니다.");
		board.setSummonerName("SK T1 Faker");
		board.setContent("서폿 유저 입니다 . 블랭크 , 쓰레쉬 , 서폿 말파 유져 입니다.");
		boardList.add(board);boardList.add(board);boardList.add(board);
		boardList.add(board);boardList.add(board);boardList.add(board);
		boardList.add(board);boardList.add(board);boardList.add(board);
		boardList.add(board);boardList.add(board);boardList.add(board);
		
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.title_section1);
		
	}

}
