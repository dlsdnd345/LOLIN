package com.iris.adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iris.entities.Board;
import com.iris.fragment.BoardFragment;
import com.iris.fragment.WriteTextFragment;
import com.iris.lolin.R;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private static final int BOARD_FRAGMENT = 0;
	
	private Context context;
	private ArrayList<Board> boardList;
	private BoardFragment boardFragment;
	private WriteTextFragment writeTextFragment;
	
	public SectionsPagerAdapter(FragmentManager fm ,Context context , ArrayList<Board> boardList) {
		super(fm);
		this.context = context;
		this.boardList = boardList;
		boardFragment = new BoardFragment();
		writeTextFragment = new WriteTextFragment();
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		
		if(position == BOARD_FRAGMENT){
			return boardFragment.newInstance(boardList);
		}else{
			return writeTextFragment.newInstance();
		}
	}

	@Override
	public int getCount() {
		return 2;
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
		}
		return null;
	}
	
	public int getPageIcon(int position) {
		
		//탭바 icon 지정
		switch (position) {
		case 0:
			return R.drawable.ic_launcher;
		case 1:
			return R.drawable.ic_launcher;
		}
		return 0;
	}
}

