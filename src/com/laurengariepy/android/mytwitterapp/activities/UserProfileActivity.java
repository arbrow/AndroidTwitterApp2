package com.laurengariepy.android.mytwitterapp.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.laurengariepy.android.mytwitterapp.fragments.TweetsListFragment.OnTweetsListItemSelectedListener;
import com.laurengariepy.android.mytwitterapp.fragments.UserProfileFragment;
import com.laurengariepy.android.mytwitterapp.fragments.UserProfileFragment.OnProfileListener;
import com.laurengariepy.android.mytwitterapp.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Class hosts a user profile timeline of tweets and includes a caption displaying the user's 
 * profile image, tag, and related information.  
 */
public class UserProfileActivity extends ActionBarActivity implements 
				OnTweetsListItemSelectedListener, OnProfileListener { 

	private User      mUser; 
	private String    mScreenName; 
	private ImageView mIvUserImage;
	private TextView  mTvUserName,
					  mTvTagLine,
					  mTvFollowers,
					  mTvFollowing; 
	
	private Typeface  mTypeface;
	private String    mFont = "Roboto-Light.ttf";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
	
		mTypeface = Typeface.createFromAsset(getAssets(), mFont); 
		
		String screenName = getIntent().getStringExtra("tweet_screen_name");
		if (screenName != null) {
			mScreenName = screenName; 
		} else {
			mScreenName = getIntent().getStringExtra("screen_name"); 
		}
		
		User tweetUser = (User) getIntent().getSerializableExtra("tweet_user"); 
		if (tweetUser != null) {
			mUser = tweetUser;
		} else {
			mUser = (User) getIntent().getSerializableExtra("user"); 
		}
		loadUserInfo(); 
	}
	
	private void loadUserInfo() {
		getActionBar().setTitle("Profile: @" + mScreenName); 
		populateProfileHeader(mUser); 
	}
	
	private void populateProfileHeader(User user) {
		mIvUserImage = (ImageView) findViewById(R.id.ivUserImage); 
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), mIvUserImage);
		
		mTvUserName = (TextView) findViewById(R.id.tvUserName); 
		mTvUserName.setTypeface(mTypeface, Typeface.BOLD); 
		mTvUserName.setText(user.getName()); 
		
		mTvTagLine  = (TextView) findViewById(R.id.tvTagLine);
		mTvTagLine.setTypeface(mTypeface); 
		mTvTagLine.setText(user.getTagLine()); 
		
		mTvFollowers = (TextView) findViewById(R.id.tvFollowers);
		mTvFollowers.setTypeface(mTypeface); 
		mTvFollowers.setText(user.getFollowersCount() + " followers, "); 
		
		mTvFollowing = (TextView) findViewById(R.id.tvFollowing); 
		mTvFollowing.setTypeface(mTypeface); 
		mTvFollowing.setText("follows " + user.getFriendsCount() + " friends");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}

	@Override
	public void onTweetSelected(AdapterView<?> parent, View view, int position, long id,
			String screenName, User tweetUser) { }
	
	@Override
	public void onProfileCreated() {
		UserProfileFragment fragment = (UserProfileFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentUserTimeline);
		fragment.setScreenName(mScreenName); 
	}
	
}
