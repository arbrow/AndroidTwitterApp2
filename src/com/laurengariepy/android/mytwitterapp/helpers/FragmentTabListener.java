package com.laurengariepy.android.mytwitterapp.helpers;

import java.io.Serializable;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class FragmentTabListener<T extends Fragment> implements TabListener, Serializable {
	
	private final FragmentActivity mActivity;
	private final String           mTag;
	private final Class<T>         mClass;
	private final int              mFragmentContainerId; 
	private Fragment 			   mFragment;
	
	
	// Constructor defaults to replacing the entire activity content area (via android.R.id.content)
	// eg: new FragmentTabListener<SomeFragment>(this, "first", SomeFragment.class)
	public FragmentTabListener(FragmentActivity activity, String tag, Class<T> cls) {
		mActivity 			 = activity; 
		mTag      			 = tag;
		mClass    			 = cls; 
		mFragmentContainerId = android.R.id.content; 
	}

	// Constructor supports specifying the container to be replaced with fragment content
	// eg: new FragmentTabListener<SomeFragment>(R.id.flContainer, this, "first", SomeFragment.class))
	public FragmentTabListener(int fragmentContainerId, FragmentActivity activity, String tag, 
								Class<T> cls) {
		mActivity			 = activity;
		mTag				 = tag; 
		mClass				 = cls;
		mFragmentContainerId = fragmentContainerId; 
	}
	
	public void onTabSelected(Tab tab, android.app.FragmentTransaction transaction) {
		FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction(); 
		
		if (mFragment == null) {
			mFragment = Fragment.instantiate(mActivity, mClass.getName()); 
			ft.add(mFragmentContainerId, mFragment, mTag); 
		} else {
			ft.show(mFragment); 
		}
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit(); 
	}
	
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction transaction) {
		FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction(); 
		
		if (mFragment != null) {
			ft.hide(mFragment); 
		}
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit(); 
	}
	
	public void onTabReselected(Tab tab, android.app.FragmentTransaction transaction) { } 
	
}
