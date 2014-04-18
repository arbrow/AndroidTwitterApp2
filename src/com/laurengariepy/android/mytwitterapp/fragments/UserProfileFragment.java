package com.laurengariepy.android.mytwitterapp.fragments;

import android.app.Activity;
import android.os.Bundle;

import com.laurengariepy.android.mytwitterapp.activities.MyTwitterAppActivity;

/**
 * Class represents a user timeline of tweets available through the Twitter REST API
 */
public class UserProfileFragment extends TweetsListFragment {

	private OnProfileListener mListener; 
	private String            mScreenName; 
	
	
	public interface OnProfileListener {
		public void onProfileCreated(); 
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity); 
		
		if (activity instanceof OnProfileListener) {
			mListener = (OnProfileListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implement OnProfileListener"
										+ "interface");
		}
	}
	
	// Async HTTP request initiated in onActivityCreated() rather than onCreate() to 
	// accommodate response timing for screenName
	@Override
	public void onActivityCreated(Bundle savedInstanceState) { 
		super.onActivityCreated(savedInstanceState); 
		
		mListener.onProfileCreated(); 
		makeAsyncHttpRequest(0); 
	}
	
	@Override
	protected void makeAsyncHttpRequest(int totalItemsCount) {
		String maxId = super.getMaxIdAsString(); 
		
		MyTwitterAppActivity.getRestClient().getUserTimeline(mScreenName, totalItemsCount, maxId, new TweetsListFragment.TwitterListViewHandler());
	}

	public void setActivity(Activity activity) {
		mActivity = activity; 
	}
	
	public void setScreenName(String screenName) {
		mScreenName = screenName; 
	}
	
}
