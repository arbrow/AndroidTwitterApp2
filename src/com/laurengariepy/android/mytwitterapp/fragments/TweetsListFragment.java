package com.laurengariepy.android.mytwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.laurengariepy.android.mytwitterapp.activities.R;
import com.laurengariepy.android.mytwitterapp.adapters.TweetsAdapter;
import com.laurengariepy.android.mytwitterapp.helpers.EndlessScrollListener;
import com.laurengariepy.android.mytwitterapp.models.Tweet;
import com.laurengariepy.android.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Abstract class represents a list of tweets implemented in a ListView object.
 * Subclasses should specify the async http request to be used to populate the 
 * ListView via the Twitter REST API.
 * 
 * Class supports endless scrolling using the max_id method recommended in 
 * Twitter documentation (see: https://dev.twitter.com/docs/working-with-timelines) 
 */
public abstract class TweetsListFragment extends Fragment {
	
	protected Activity         mActivity; 
	protected ArrayList<Tweet> mTweetsArray; 
	protected ListView         mLvTweets; 
	protected TweetsAdapter    mAdapter;
	private   String           mMaxId; 
	private   int 			   mTotalItemsCount; 
	private   OnTweetsListItemSelectedListener mListener; 
	
	
	public interface OnTweetsListItemSelectedListener {
		public void onTweetSelected(AdapterView<?> parent, View view, int position, long id,
				String screenName, User tweetUser); 
	}
	
	protected class TwitterListViewHandler extends JsonHttpResponseHandler {
		@Override 
		public void onSuccess(JSONArray jsonTweets) {
			ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
			
			// Get maxId to enable endless scrolling
			if (tweets.size() != 0) { 
				Tweet lastTweet   = tweets.get(tweets.size() - 1); 
				long numericMaxId = lastTweet.getTweetId(); 
					// Decrement to avoid duplication of tweet corresponding to maxId 
				mMaxId = String.valueOf(numericMaxId - 1); 		
			}
			mTweetsArray.addAll(tweets); 
			mAdapter.notifyDataSetChanged(); 
		}
		
		@Override
		public void onFailure(Throwable e, JSONArray error) {
			Log.e("ERROR", e.getMessage()); 
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity); 
		
		if (activity instanceof OnTweetsListItemSelectedListener) {
			mListener = (OnTweetsListItemSelectedListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implement "
										+ "OnTweetsListItemSelectedListener interface"); 
		}
		mActivity = activity; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		super.onCreateView(inflater, parent, savedInstanceState); 
		View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false); 
		
		mLvTweets = (ListView) v.findViewById(R.id.lvTweets); 
		mLvTweets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User tweetUser    = mTweetsArray.get(position).getUser(); 
				String screenName = tweetUser.getScreenName(); 
				mListener.onTweetSelected(parent, view, position, id, screenName, tweetUser); 
			}
		});
		return v; 
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 
		
		setupListView(savedInstanceState);
	}
	
	private void setupListView(Bundle savedInstanceState) {
		mTweetsArray = new ArrayList<Tweet>();
		mAdapter     = new TweetsAdapter(mActivity, mTweetsArray); 
		mLvTweets.setAdapter(mAdapter);
		mAdapter.clear(); 
		
		mLvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				mTotalItemsCount = totalItemsCount; 
				makeAsyncHttpRequest(mTotalItemsCount);
			}
		}); 
	}

	// Persistence based on ActiveAndroid disabled pending further review
/*	
 *	@Override
 *	public void onStop() {
 *		super.onStop(); 
 *		if (mTweetsArray != null) {
 *			new AsyncTweetsSave().execute(mTweetsArray); 
 *		}
 *	}
 */	
	
	public void showProgressBar() {
		mActivity.setProgressBarIndeterminateVisibility(true);
	}
	
	public void hideProgressBar() {
		mActivity.setProgressBarIndeterminateVisibility(false); 
	}
	
	public TweetsAdapter getAdapter() {
		return mAdapter; 
	}

	public ArrayList<Tweet> getTweetsArray() {
		return mTweetsArray;
	}
	
	public String getMaxIdAsString() {
		return mMaxId; 
	}
	
	/*
	 * Implementation of abstract method should specify async HTTP request meaningful 
	 * to subclass (e.g., GET home timeline for a fragment that produces a ListView  
	 * of tweets representing the user's home timeline). 
	 */
	 protected abstract void makeAsyncHttpRequest(int totalItemCount);
	
}
