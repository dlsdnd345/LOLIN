package com.iris.adapter;

import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iris.fragment.ContentFragment;
import com.iris.fragment.DetailRecordSearchFragment;
import com.iris.fragment.RepleFragment;
import com.iris.lolin.R;


public class BoardDetailPagerAdapter extends FragmentPagerAdapter {

	private static final int CONTENT_FRAGMENT = 0;
	private static final int RECORD_SEARCH_FRAGMENT = 1;
	
	private Context 						context;
	private RepleFragment 					repleFragment;
	private ContentFragment 				contentFragment;
	private DetailRecordSearchFragment 		detailRecordSearchFragment;
	
	
	public BoardDetailPagerAdapter(Context context ,FragmentManager fragmentManager) {
		super(fragmentManager);
		this.context = context;
		repleFragment = new RepleFragment();
		contentFragment = new ContentFragment();
		detailRecordSearchFragment = new DetailRecordSearchFragment();
	}

	@Override
	public Fragment getItem(int position) {
		
		if(position == CONTENT_FRAGMENT){
			return contentFragment.newInstance(1);
		}else if(position == RECORD_SEARCH_FRAGMENT){
			return detailRecordSearchFragment.newInstance();
		}else{
			return repleFragment.newInstance(3);
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
			return context.getString(R.string.detail_title_section2).toUpperCase(l);
		case 2:
			return context.getString(R.string.detail_title_section3).toUpperCase(l);
		}
		return null;
	}
	
	@Override
	public int getCount() {
		return 3;  // 총 5개의 page를 보여줍니다.
	}

}
