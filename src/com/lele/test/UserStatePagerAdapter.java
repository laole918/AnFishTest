package com.lele.test;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class UserStatePagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Fragment> fragments;
	
	public UserStatePagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new ArrayList<Fragment>();
	}
	
//	public float getPageWidth(int position) {
//		return 0.8f;
//	}
	
	public void addFragments(ArrayList<Fragment> fragments) {
		this.fragments.addAll(fragments);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
