package com.iris.adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iris.entities.Board;
import com.iris.fragment.BoardFragment;
import com.iris.fragment.RecordSearchFragment;
import com.iris.fragment.SettingFragment;
import com.iris.fragment.WriteTextFragment;
import com.iris.lolin.R;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private static final int BOARD_FRAGMENT = 0;
	private static final int RECORD_SEARCH_FRAGMENT = 1;
	private static final int WRITE_TEXT_FRAGMENT = 2;
	
	private Context context;
	private ArrayList<Board> boardList;
	private BoardFragment boardFragment;
	private SettingFragment settingFragment;
	private WriteTextFragment writeTextFragment;
	private RecordSearchFragment recordSearchFragment;
	
	public SectionsPagerAdapter(FragmentManager fm ,Context context , ArrayList<Board> boardList) {
		super(fm);
		this.context = context;
		this.boardList = boardList;
		boardFragment = new BoardFragment();
		settingFragment = new SettingFragment();
		writeTextFragment = new WriteTextFragment();
		recordSearchFragment = new RecordSearchFragment();
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		
		if(position == BOARD_FRAGMENT){
			return boardFragment.newInstance(boardList);
		}else if(position == RECORD_SEARCH_FRAGMENT){
			return recordSearchFragment.newInstance();
		}else if(position == WRITE_TEXT_FRAGMENT){
			return writeTextFragment.newInstance(boardList);
		}else{
			return settingFragment.newInstance();
		}
	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		
		//탭바 Title 이름 지정
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return context.getString(R.string.title_section2).toUpperCase(l);
		case 2:
			return context.getString(R.string.title_section3).toUpperCase(l);
		case 3:
			return context.getString(R.string.title_section4).toUpperCase(l);
		}
		return null;
	}
	
//	public int getPageIcon(int position) {
//		
//		//탭바 icon 지정
//		switch (position) {
//		case 0:
//			return R.drawable.ic_launcher;
//		case 1:
//			return R.drawable.ic_launcher;
//		case 2:
//			return R.drawable.ic_launcher;
//		}
//		return 0;
//	}
}

