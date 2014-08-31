package com.iris.lolin;

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
import android.widget.HorizontalScrollView;

import com.astuetz.PagerSlidingTabStrip;
import com.iris.adapter.SectionsPagerAdapter;
import com.iris.config.Config;
import com.iris.util.SharedpreferencesUtil;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity  {

	private final static int WRITE_TEXT 		= 1;
	private final static int RECORD_SEARCH 	= 2;
	private final static int SETTING 			= 3;

	private int 						viewPagerPosition;
	
	private SharedpreferencesUtil 		sharedpreferencesUtil;
	
	private ViewPager 					mViewPager;
	private PagerSlidingTabStrip 		tabs;
	private SectionsPagerAdapter 		mSectionsPagerAdapter;
	public 	 HorizontalScrollView 		scrollViewRank, scrollViewPosition, scrollViewTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		dataInit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//화면이 켜져 있을때 푸시화면을 보여주기 않기 위함.
		sharedpreferencesUtil.put(Config.FLAG.NOTIBLOCK, Config.FLAG.TRUE);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//화면이 꺼져 있을때 푸시화면을 보여주기 위함.
		sharedpreferencesUtil.put(Config.FLAG.NOTIBLOCK, Config.FLAG.FALSE);
	}
	
	/**
	 * 액션바 아이콘 생성
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		boolean isLogin = sharedpreferencesUtil.getValue(Config.FLAG.IS_LOGIN, false);
		if(isLogin){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.write_text_menu, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 레이아웃 초기화
	 */
	private void init() {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	}

	/**
	 * 데이터 초기화
	 */
	private void dataInit() {
		actionvarInit();
		viewPagerConfig();
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
	}

	private void actionvarInit() {
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.title_section1);
	}


	/**
	 * 액션바 아이템 선택시 이벤트 발생
	 */
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

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());
		mViewPager.setAdapter(mSectionsPagerAdapter);
		tabs.setIndicatorColor(Color.parseColor("#0099cc"));
		tabs.setOnPageChangeListener(mOnPageChangeListener);
		tabs.setViewPager(mViewPager);
	}

	OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener(){
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
	};

}
