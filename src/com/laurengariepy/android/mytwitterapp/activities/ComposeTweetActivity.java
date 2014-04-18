package com.laurengariepy.android.mytwitterapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.laurengariepy.android.mytwitterapp.fragments.ComposeTweetFragment.OnComposeTweetListener;
import com.laurengariepy.android.mytwitterapp.models.Tweet;

/** 
 * Class hosts fragment for composing and posting tweets
 */
public class ComposeTweetActivity extends ActionBarActivity implements OnComposeTweetListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_compose_tweet); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}
	
	@Override
	public void onTweetCanceled() {
		Intent i = new Intent();
		setResult(RESULT_CANCELED, i);
		finish(); 
	}
	
	@Override
	public void onTweetPosted(Tweet postedTweet) {
		Intent i = new Intent();
		i.putExtra("new_tweet", postedTweet);
		setResult(RESULT_OK, i);
		finish(); 
	}
	
}
