package com.iris.adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iris.entities.Board;
import com.iris.entities.Reple;
import com.iris.fragment.ContentFragment;
import com.iris.fragment.DetailRecordSearchFragment;
import com.iris.fragment.RepleFragment;
import com.iris.lolin.R;
import com.iris.util.SharedpreferencesUtil;


public class BoardDetailPagerAdapter extends FragmentPagerAdapter {

	private static final int CONTENT_FRAGMENT			 = 0;
	private static final int REPLE_FRAGMENT 			= 1;
	private static final int RECORD_SEARCH_FRAGMENT 	= 2;
	
	private static final String 		IS_LOGIN  						= "isLogin";
	
	private Board							board;
	private Context 						context;
	private RepleFragment 					repleFragment;
	private ContentFragment 				contentFragment;
	private SharedpreferencesUtil 			sharedpreferencesUtil;
	private DetailRecordSearchFragment 		detailRecordSearchFragment;
	
	
	public BoardDetailPagerAdapter(Context context ,FragmentManager fragmentManager, Board board) {
		super(fragmentManager);
		
		this.board =board;
		this.context = context;
		repleFragment = new RepleFragment();
		contentFragment = new ContentFragment();
		sharedpreferencesUtil = new SharedpreferencesUtil(context);
		detailRecordSearchFragment = new DetailRecordSearchFragment();
	}

	@Override
	public Fragment getItem(int position) {
		
		if(position == CONTENT_FRAGMENT){
			return contentFragment.newInstance(board.getContent());
		}else if(position == REPLE_FRAGMENT){
			return repleFragment.newInstance(board.getId(),(ArrayList<Reple>) board.getRepleList(),board.getSummonerName());
		}else{
			return detailRecordSearchFragment.newInstance(board.getSummonerName());
		}
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		
		//탭바 Title 이름 지정
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getString(R.string.detail_title_section1).toUpperCase(l);
		case 1:
			return context.getString(R.string.detail_title_section3).toUpperCase(l);
		case 2:
			return context.getString(R.string.detail_title_section2).toUpperCase(l);
		}
		return null;
	}
	
	@Override
	public int getCount() {
		boolean isLogin = sharedpreferencesUtil.getValue(IS_LOGIN, false);
		if(isLogin){
			return 3;
		}else{
			return 2;
		}
	}

}
