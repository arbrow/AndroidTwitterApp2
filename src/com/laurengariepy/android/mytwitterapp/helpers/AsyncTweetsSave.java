package com.laurengariepy.android.mytwitterapp.helpers;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;

import com.laurengariepy.android.mytwitterapp.models.Tweet;

/**
 * Helper class uses Tweet.saveTweets(...) to save an array of tweets asynchronously.
 */

// --- NOTE: Persistence based on ActiveAndroid disabled pending further review ---

public class AsyncTweetsSave extends AsyncTask<ArrayList<Tweet>, Void, Void> {

	@Override
	protected Void doInBackground(ArrayList<Tweet>... tweets) {
//		Tweet.saveTweets(tweets[0]); 
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		Log.i("INFO", "Tweet.saveTweets(...) executed."); 
	}
	
}
