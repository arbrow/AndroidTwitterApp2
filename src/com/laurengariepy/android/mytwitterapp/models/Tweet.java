package com.laurengariepy.android.mytwitterapp.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Class acts as a Java-representation of a single tweet retrieved as a JSONObject from the 
 * Twitter REST API v1.1. Fields are as specified in the API Tweets object documentation. 
 */

// --- NOTE: ActiveAndroid disabled pending further review (cf. note to constructor below) ---

//@Table(name = "Tweets")
public class Tweet /*extends Model*/ implements Serializable {
	
//	@Column(name = "tweet_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long    	mTweetId;	// Tweet ID
//	@Column(name = "created_at")
	private String  	mCreatedAt; 
//	@Column(name = "tweet_body")
	private String  	mBody;
//	@Column(name = "favorited")
	private boolean 	mFavorited;
//	@Column(name = "retweeted")
	private boolean 	mRetweeted;
//	@Column(name = "user")
    private User    	mUser;
    
    
 /* 
  * NOTE: ActiveAndroid documentation insists on inclusion of an empty-constructor with an
  * empty-argument super() call for each class that extends Model (the parent class with 
  * ActiveAndroid functionality). But the lack of arguments in super() appears to be 
  * error-prone, resulting in Tweet tweet = new Tweet() returning null. (At least sometimes, 
  * that is. This null-behavior was not present in the Activity-based version of MyTwitterApp.)  
  */
 /*
  * public Tweet() {				// Empty-argument constructor required by ActiveAndroid
  *		super(); 
  *	}	
  */	
 
 /*   
  *  // Optional helper method for ActiveAndroid to establish a direct relationship with the Users table
  *	public List<User> getUsers() {
  *		return getMany(User.class, "Tweets");
  * }
  */  
    
    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
        	tweet.mTweetId 	 = jsonObject.getLong("id");
        	tweet.mCreatedAt = jsonObject.getString("created_at");
        	tweet.mBody 	 = jsonObject.getString("text");
        	tweet.mFavorited = jsonObject.getBoolean("favorited");
        	tweet.mRetweeted = jsonObject.getBoolean("retweeted");
            tweet.mUser 	 = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            Log.v("VERBOSE", "Tweet.fromJson(JSONObject) threw JSONException: " + e.getMessage()); 
        	return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                Log.v("VERBOSE", "Tweet.fromJson(JSONArray) did not incorporate JSONObject " + i); 
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }
        return tweets;
    }

    public long getTweetId() {
        return mTweetId;
    }
    
    public String getCreatedAt() {
    	return mCreatedAt;
    }
    
    public String getBody() {
        return mBody;
    }

    public boolean isFavorited() {
        return mFavorited;
    }

    public boolean isRetweeted() {
        return mRetweeted;
    }

    public User getUser() {
        return mUser;
    }    
    
/*    
 *  public static void saveTweet(Tweet tweet) {
 *   	tweet.mUser.save();
 *    	tweet.save();
 *  }
 */
 
 /*   
  * public static void saveTweets(ArrayList<Tweet> tweets) {
  *  	ActiveAndroid.beginTransaction();
  *  	try {
  *	    	for (int i = 0; i < tweets.size(); i++) {
  *	    		Tweet t = tweets.get(i);
  *	    		if (t != null) {
  *	    			if (t.mUser != null) {
  *	    				t.mUser.save();
  *	    			} 
  *	    			t.save(); 
  *	    		}
  *	    	}
  *	    	ActiveAndroid.setTransactionSuccessful();
  *  	} finally {
  *   		ActiveAndroid.endTransaction(); 
  *  	}
  * }
  */
    
}