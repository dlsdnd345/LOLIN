package com.iris.adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iris.entities.Board;
import com.iris.fragment.BoardFragment;
import com.iris.fragment.ComposerFragment;
import com.iris.fragment.RecordSearchFragment;
import com.iris.fragment.SettingFragment;
import com.iris.lolin.R;
import com.iris.util.SharedpreferencesUtil;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private static final int BOARD_FRAGMENT 			= 0;
	private static final int WRITE_TEXT_FRAGMENT 		= 1;
	private static final int RECORD_SEARCH_FRAGMENT 	= 2;
	
	private static final String 		IS_LOGIN  	= "isLogin";
	
	private Context 				context;
	private BoardFragment 			boardFragment;
	private SettingFragment 		settingFragment;
	private ComposerFragment 		writeTextFragment;
	private RecordSearchFragment 	recordSearchFragment;
	private SharedpreferencesUtil 	sharedpreferencesUtil;
	
	public SectionsPagerAdapter(FragmentManager fm ,Context context) {
		super(fm);
		this.context = context;
		boardFragment = new BoardFragment();
		settingFragment = new SettingFragment();
		writeTextFragment = new ComposerFragment();
		recordSearchFragment = new RecordSearchFragment();
		sharedpreferencesUtil = new SharedpreferencesUtil(context);
	}

	@Override
	public Fragment getItem(int position) {
		if(position == BOARD_FRAGMENT){
			return boardFragment.newInstance(context);
		}else if(position == WRITE_TEXT_FRAGMENT){
			return writeTextFragment.newInstance();
		}else if(position == RECORD_SEARCH_FRAGMENT){
			return recordSearchFragment.newInstance();
		}else{
			return settingFragment.newInstance();
		}
	}

	@Override
	public int getCount() {
		
		boolean isLogin = sharedpreferencesUtil.getValue(IS_LOGIN, false);
		if(isLogin){
			return 4;
		}else{
			return 1;
		}
		
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
	
}

