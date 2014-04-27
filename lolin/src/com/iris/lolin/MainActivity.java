package com.iris.lolin;

import java.util.ArrayList;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.iris.adapter.SectionsPagerAdapter;
import com.iris.entities.Board;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

	private Board board;
	private ViewPager mViewPager;
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

	private void viewPagerConfig() {
		final ActionBar actionBar = actionBarConfig();  
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext(),boardList);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					//.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setIcon(mSectionsPagerAdapter.getPageIcon(i))
					.setTabListener(this));
		}
	}

	private void init() {
		mViewPager = (ViewPager) findViewById(R.id.pager);
	}

	private ActionBar actionBarConfig() {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		return actionBar;
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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

}
