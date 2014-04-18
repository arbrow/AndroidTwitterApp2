package com.laurengariepy.android.mytwitterapp.helpers;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * Abstract class enabling endless scrolling of an AdapterView (e.g., ListView or GridView). 
 * Class designed to be extended as a listener object--for instance, as an anonymous class 
 * implementing the abstract method onLoadMore(int, int).  
 * 
 * To avoid unexpected problems, set up the listener in the onCreate(Bundle) method of the
 * host activity and not later. 
 */
public abstract class EndlessScrollListener implements OnScrollListener {
	
	private int mVisibleThreshold       = 5;		// Min number of items below current scroll position before reloading 
	private int mCurrentPage            = 0;		// Current offset index of data already loaded
	private int mPreviousTotalItemCount = 0;		// Total number of items in the data set following last load
	private int mStartingPageIndex      = 0;		// Sets starting page index
	private boolean mLoading            = true;		// True if waiting for the last data set to load

	
	public EndlessScrollListener() {
	}
	
	public EndlessScrollListener(int visibleThreshold) {
		mVisibleThreshold = visibleThreshold;
	}
	
	public EndlessScrollListener(int visibleThreshold, int startPage) {
		mVisibleThreshold  = visibleThreshold;
		mStartingPageIndex = startPage;
		mCurrentPage       = startPage;
	}
	
	/*
	 * Callback method for user scrolling. 
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// If condition true, assumes list has been invalidated and should be reset to initial state
		if (totalItemCount < mPreviousTotalItemCount) {
			mCurrentPage = mStartingPageIndex;
			mPreviousTotalItemCount = totalItemCount;
			if (totalItemCount == 0) mLoading = true;
		}
		// Condition tests for cessation of loading, which is assumed if the inequality holds
		if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
			mLoading = false;
			mPreviousTotalItemCount = totalItemCount;
			mCurrentPage++; 
		}
		// Condition tests whether more data needs to be loaded. If so, calls onLoadMore(int, int) to 
		// fetch more data.
		if (!mLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold)) {
			onLoadMore(mCurrentPage + 1, totalItemCount);
			mLoading = true;
		}
	}
	
	/*
	 * Defines process for loading more data, based on applicable API protocol 
	 */
	public abstract void onLoadMore(int page, int totalItemsCount);
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// Don't take any action on changed
	}

}