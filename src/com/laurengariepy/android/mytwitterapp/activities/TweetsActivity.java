package com.laurengariepy.android.mytwitterapp.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.laurengariepy.android.mytwitterapp.fragments.HomeTimelineFragment;
import com.laurengariepy.android.mytwitterapp.fragments.MentionsFragment;
import com.laurengariepy.android.mytwitterapp.fragments.TweetsListFragment.OnTweetsListItemSelectedListener;
import com.laurengariepy.android.mytwitterapp.helpers.FragmentTabListener;
import com.laurengariepy.android.mytwitterapp.models.Tweet;
import com.laurengariepy.android.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Class represents primary application UI, containing a home timeline of tweets and 
 * a list of tweets mentioning the registered user. Each of the home timeline and 
 * mentions lists is represented by a tab-accessed fragment.
 */
public class TweetsActivity extends ActionBarActivity implements OnTweetsListItemSelectedListener {

	private static final int    COMPOSE_TWEET_REQUEST 	  = 1;
	private static final String TAG_HOMETIMELINE_FRAGMENT = "TagHomeTimelineFragment";
	private static final String TAG_MENTIONS_FRAGMENT     = "TagMentionsFragment"; 
	
	private User    mUser; 
	private String  mScreenName;
	private String  mUserImageUrl;
	
	private FragmentTabListener<HomeTimelineFragment> mHtFragmentTabListener;
	private FragmentTabListener<MentionsFragment>     mMnFragmentTabListener; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweets);

		if (savedInstanceState == null) {
			getRegisteredUserInfo();	// Method's results needed for ComposeTweetActivity. (Initiated here 
		} 								// due to async delay
		setupTabs(savedInstanceState); 
	}
	
	private void getRegisteredUserInfo() {
		MyTwitterAppActivity.getRestClient().getRegisteredUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonObject) {
				mUser         = User.fromJson(jsonObject); 
				mScreenName   = mUser.getScreenName(); 
				mUserImageUrl = mUser.getProfileImageUrl(); 
			}

			@Override
			public void onFailure(Throwable e, JSONObject error) {
				Log.e("ERROR", e.getMessage()); 
			}
		});
	}
	
	private void setupTabs(Bundle savedInstanceState) {
		mHtFragmentTabListener = new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer,
				this, TAG_HOMETIMELINE_FRAGMENT, HomeTimelineFragment.class);
		mMnFragmentTabListener = new FragmentTabListener<MentionsFragment>(R.id.flContainer,
				this, TAG_MENTIONS_FRAGMENT, MentionsFragment.class); 
		
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true); 
		
		Tab tabHomeTimeline = actionBar.newTab()
							.setText("Home Timeline")
							.setIcon(R.drawable.ic_tab_home_timeline)
							.setTabListener(mHtFragmentTabListener); 
		
		Tab tabMentions = actionBar.newTab()
							.setText("Mentions")
							.setIcon(R.drawable.ic_tab_mentions)
							.setTabListener(mMnFragmentTabListener); 
		
		actionBar.addTab(tabHomeTimeline);
		actionBar.addTab(tabMentions); 
		actionBar.selectTab(tabHomeTimeline); 
	}
			
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == COMPOSE_TWEET_REQUEST) {
			Tweet newTweet = (Tweet) data.getSerializableExtra("new_tweet"); 
			HomeTimelineFragment htFragment = (HomeTimelineFragment) getSupportFragmentManager()
												.findFragmentByTag(TAG_HOMETIMELINE_FRAGMENT); 
			htFragment.getTweetsArray().add(0, newTweet); 
			htFragment.getAdapter().notifyDataSetChanged(); 
		} 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_compose_tweet:
			Intent i = new Intent(TweetsActivity.this, ComposeTweetActivity.class);
			i.putExtra("screen_name", mScreenName); 
			i.putExtra("user_image_url", mUserImageUrl); 
			startActivityForResult(i, COMPOSE_TWEET_REQUEST); 
			break;
		case R.id.action_user_profile:
			Intent j = new Intent(TweetsActivity.this, UserProfileActivity.class); 
			j.putExtra("screen_name", mScreenName); 
			j.putExtra("user", mUser); 
			startActivity(j); 
			break;
		}
		return true; 
	}
	
	@Override
	public void onTweetSelected(AdapterView<?> parent, View view, int position, long id,
			String screenName, User tweetUser) {
		Intent i = new Intent(this, UserProfileActivity.class);
		i.putExtra("tweet_screen_name", screenName); 
		i.putExtra("tweet_user", tweetUser); 
		startActivity(i); 
	}
	
	public void showProgressBar() {
		setProgressBarIndeterminateVisibility(true);
	}
	
	public void hideProgressBar() {
		setProgressBarIndeterminateVisibility(false); 
	}
	
}
