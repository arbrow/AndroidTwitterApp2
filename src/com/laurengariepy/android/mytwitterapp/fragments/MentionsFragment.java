package com.laurengariepy.android.mytwitterapp.fragments;

import android.os.Bundle;

import com.laurengariepy.android.mytwitterapp.activities.MyTwitterAppActivity;

/**
 * Class represents a mentions timeline of tweets available through the Twitter REST API
 */
public class MentionsFragment extends TweetsListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 

		makeAsyncHttpRequest(0); 
	}

	@Override
	protected void makeAsyncHttpRequest(int totalItemsCount) {
		String maxId = super.getMaxIdAsString(); 
		
		MyTwitterAppActivity.getRestClient().getMentions(totalItemsCount, maxId, new TweetsListFragment.TwitterListViewHandler());
	}
	
}
