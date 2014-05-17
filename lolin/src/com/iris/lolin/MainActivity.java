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
import android.widget.HorizontalScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;
import com.iris.adapter.SectionsPagerAdapter;
import com.iris.entities.Board;
import com.iris.service.MainService;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity  {

	private final static String BOARD_FINDALL = "http://192.168.219.6:8080/board/findAll";
	private final static String ERROR = "Error";
	
	private final static int WRITE_TEXT = 1;
	private final static int RECORD_SEARCH = 2;
	private final static int SETTING = 3;

	float firstGetY , preGetY= 0;


	private MainService 				mainService;
	private ViewPager 					mViewPager;
	private int 						viewPagerPosition;
	private PagerSlidingTabStrip 		tabs;
	private ArrayList<Board> 			boardList; 
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

		System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		
		RequestQueue request = Volley.newRequestQueue(getApplicationContext());  
		request.add(new StringRequest(Request.Method.GET, BOARD_FINDALL,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				boardList = mainService.getBoardFindAll(response);
				viewPagerConfig();
				
				
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.write_text_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void init() {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	}


	private void dataInit() {

		mainService = new MainService();
		//Data Init
		boardList = new ArrayList<Board>();

		actionvarInit();
	}

	private void actionvarInit() {
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.title_section1);
	}


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
