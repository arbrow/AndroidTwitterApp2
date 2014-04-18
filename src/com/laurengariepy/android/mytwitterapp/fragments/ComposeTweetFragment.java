package com.laurengariepy.android.mytwitterapp.fragments;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laurengariepy.android.mytwitterapp.activities.MyTwitterAppActivity;
import com.laurengariepy.android.mytwitterapp.activities.R;
import com.laurengariepy.android.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Class represents a screen to compose and post tweets. User is warned if text
 * typed passes the 140-character limit
 */
public class ComposeTweetFragment extends Fragment {
	
	private Activity  mActivity; 
	private Button 	  mBtnCancel,
					  mBtnTweet;
	private ImageView mIvUserImage;
	private TextView  mTvUserName;
	private EditText  mEtNewTweet;
	private boolean   mAlreadyToasted = false;

	private Typeface  mTypeface;
	private String    mFont = "Roboto-Light.ttf"; 
	
	private OnComposeTweetListener mListener; 
	
	
	public interface OnComposeTweetListener {
		public void onTweetPosted(Tweet postedTweet);
		public void onTweetCanceled();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity); 
		
		if (activity instanceof OnComposeTweetListener) {
			mListener = (OnComposeTweetListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implement "
										+ "OnTweetComposedListener interface");
		}
		mActivity = activity; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		super.onCreateView(inflater, parent, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_compose_tweet, parent, false); 

		mTypeface = Typeface.createFromAsset(mActivity.getAssets(), mFont);
		setupButtons(v);
		setupImageView(v);
		setupTextView(v);
		setupEditText(v);
		return v; 
	}

	private void setupButtons(View v) {
		mBtnCancel = (Button) v.findViewById(R.id.btnCancel); 
		mBtnCancel.setTypeface(mTypeface); 
		mBtnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onTweetCanceled(); 
			}
		});

		mBtnTweet = (Button) v.findViewById(R.id.btnTweet);
		mBtnTweet.setTypeface(mTypeface); 
		mBtnTweet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String tweetBody = mEtNewTweet.getText().toString();
				tweet(tweetBody);
			}
		});	
	}

	private void setupImageView(View v) {
		mIvUserImage = (ImageView) v.findViewById(R.id.ivUserImage);
		ImageLoader.getInstance().displayImage(getActivity().getIntent().getStringExtra("user_image_url"), mIvUserImage); 
	}

	private void setupTextView(View v) {
		mTvUserName = (TextView) v.findViewById(R.id.tvUserName);
		mTvUserName.setTypeface(mTypeface); 
		mTvUserName.setText("@" + getActivity().getIntent().getStringExtra("screen_name")); 
	}

	private void setupEditText(View v) {
		mEtNewTweet = (EditText) v.findViewById(R.id.etNewTweet); 
		mEtNewTweet.setTypeface(mTypeface); 

		// Show soft keyboard when EditText field requests focus
		if (mEtNewTweet.requestFocus()) {
			InputMethodManager mgr = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.showSoftInput(mEtNewTweet, InputMethodManager.SHOW_IMPLICIT); 
		}

		mEtNewTweet.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!mAlreadyToasted && s.length() == 140) {
					Toast.makeText(mActivity, "You've reached the 140-character"
							+ " limit", Toast.LENGTH_LONG).show(); 
					mAlreadyToasted = true; 
				}
				else if (s.length() > 140) {
					mEtNewTweet.setTextColor(Color.RED); 
				} else {
					mEtNewTweet.setTextColor(Color.BLACK); 
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) { }
		});

	}

	private void tweet(String tweetBody) {
		MyTwitterAppActivity.getRestClient().postTweet(tweetBody, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, JSONObject jsonTweetResponse) {
				Tweet newTweet = Tweet.fromJson(jsonTweetResponse); 
			 /*	new AsyncTweetSave().execute(newTweet); */  // Persistence based on ActiveAndroid disabled pending further review 
				mListener.onTweetPosted(newTweet);
			}	

			@Override
			public void onFailure(Throwable e, JSONObject error) {
				Log.e("ERROR", e.getMessage());
			}
		});
	}

}
