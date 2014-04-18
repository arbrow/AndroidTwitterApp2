package com.laurengariepy.android.mytwitterapp.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.laurengariepy.android.mytwitterapp.models.Tweet;

/**
 * Helper class uses Tweet.saveTweet(...) to save a Tweet object asynchronously.
 */

// --- NOTE: Persistence based on ActiveAndroid disabled pending further review ---

public class AsyncTweetSave extends AsyncTask<Tweet, Void, Void> {

	@Override
	protected Void doInBackground(Tweet... tweets) {
//		Tweet.saveTweet(tweets[0]); 
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.i("INFO", "Tweet.saveTweet(...) executed."); 
	}
	
}
