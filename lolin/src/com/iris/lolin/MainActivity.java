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
import android.util.TypedValue;

import com.astuetz.PagerSlidingTabStrip;
import com.iris.adapter.SectionsPagerAdapter;
import com.iris.entities.Board;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity  {

	private Board board;
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
		getActionBar().setDisplayShowHomeEnabled(false);
	}

	private void viewPagerConfig() {
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext(),boardList);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		final int pageMargin = (int) TypedValue.applyDimension
				(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
		mViewPager.setPageMargin(pageMargin);
		
		tabs.setIndicatorColor(Color.parseColor("#0099cc"));
		tabs.setViewPager(mViewPager);
		
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
	}

}
