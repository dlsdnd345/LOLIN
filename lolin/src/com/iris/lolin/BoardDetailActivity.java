package com.iris.lolin;

import com.astuetz.PagerSlidingTabStrip;
import com.iris.adapter.BoardDetailPagerAdapter;
import com.iris.adapter.SectionsPagerAdapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;

public class BoardDetailActivity extends ActionBarActivity {

	private static final int CONTENT_FRAGMENT = 0;
	private static final int RECORD_SEARCH_FRAGMENT = 1;

	private int 						viewPagerPosition;
	private ViewPager 					mViewPager;
	private PagerAdapter 				mPagerAdapter;
	private PagerSlidingTabStrip 		tabs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.acticity_board_detail);
	    
	    init();
		dataInit();
		
	}

	private void init() {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	}
	
	@SuppressLint("NewApi")
	private void dataInit() {
		
		//Pager Init
		mPagerAdapter = new BoardDetailPagerAdapter(getApplicationContext(),getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		
		tabs.setIndicatorColor(Color.parseColor("#0099cc"));
		tabs.setViewPager(mViewPager);

		tabs.setOnPageChangeListener(new OnPageChangeListener() {
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
		});
		
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.board_detail_activity_title);
	}
	
}
