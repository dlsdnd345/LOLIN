package com.iris.adapter;

import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iris.fragment.PlaceholderFragment;
import com.iris.lolin.R;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private Context context;
	private PlaceholderFragment placeholderFragment;
	
	public SectionsPagerAdapter(FragmentManager fm ,Context context) {
		super(fm);
		this.context = context;
		placeholderFragment = new PlaceholderFragment();
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		return placeholderFragment.newInstance(position + 1);
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 3;
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
		case 2:
			return R.drawable.ic_launcher;
		}
		return 0;
	}
}

