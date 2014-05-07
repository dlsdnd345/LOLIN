package com.iris.lolin;

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

	private ViewPager 					mViewPager;
	private PagerAdapter 				mPagerAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.acticity_board_detail);
	    
	    init();
		dataInit();
		
	}

	private void init() {
		mViewPager = (ViewPager) findViewById(R.id.pager);
	}
	
	@SuppressLint("NewApi")
	private void dataInit() {
		
		//Pager Init
		mPagerAdapter = new BoardDetailPagerAdapter(getApplicationContext(),getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(mOnPageChangeListener);
		
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.board_detail_activity_title);
	}
	
	
	@SuppressLint("NewApi")
	OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener(){

		@Override
		public void onPageSelected(int position) {
			
			if(position == CONTENT_FRAGMENT){
				getActionBar().setTitle(R.string.detail_title_section1);
			}else if(position == RECORD_SEARCH_FRAGMENT){
				getActionBar().setTitle(R.string.detail_title_section2);
			}else{
				getActionBar().setTitle(R.string.detail_title_section3);
			}
		}
		@Override
		public void onPageScrollStateChanged(int position) {}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		
	};
	
}
