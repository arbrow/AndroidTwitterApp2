package com.laurengariepy.android.mytwitterapp.clients;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/** 
* Class outfits an AsyncHttpClient to communicate with Twitter's REST API v1.1. AsyncHttpClient is 
* provided with OAuth constants required by the API as well as methods for making HTTP requests to
* the relevant API endpoints. 
* 
* To modify class: 
* Specify constants as required by the API of interest.
* 
* 	See a full list of supported API classes: 
*   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
* 
* 	Key and Secret are provided by resource owner on the developer site for the given API, e.g., 
* 	dev.twitter.com.
* 
* Add GET/REQUEST/POST/etc. methods for relevant API endpoints.
*/
public class TwitterClient extends OAuthBaseClient {
	
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; 
    public static final String REST_URL 					= "https://api.twitter.com/1.1"; 
    public static final String REST_CONSUMER_KEY 			= "aaNBezHHPEGtRKTwE4B1Q";       
    public static final String REST_CONSUMER_SECRET 		= "Itd66mLMZYuYYxI9YPxcUwaeKljL94FuD7hn0e5850"; 
    public static final String REST_CALLBACK_URL 			= "oauth://mytwitterapp"; 
   
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    /* 
     * Method makes GET request for home timeline resource from Twitter REST API v1.1. Twitter returns  
     * a rate-limited json-formatted response consisting of a collection of the most recent tweets and 
     * re-tweets posted by the authenticated user and the users he/she follows. 
     */
    public void getHomeTimeline(int totalItemsCount, String maxId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("count", "20");
    	
    	 // To implement endless scrolling, update maxID (ie, the ID of the first tweet to be loaded) 
    	 // when you're making a follow-on request (ie, totalItemsCount > 0)
    	if (totalItemsCount > 0) {
    		params.put("max_id", maxId); 
    	}
    	client.get(url, params, handler); 
    }
    
    /*
     * Method makes GET request to retrieve tweets mentioning current user from Twitter REST API v1.1. 
     * Twitter returns a rate-limited json-formatted response.
     */
    public void getMentions(int totalItemsCount, String maxId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json");  // By default returns 20 most recent mentions
    	RequestParams params = new RequestParams();
    	
       	if (totalItemsCount > 0) {
    		params.put("max_id", maxId);
    		client.get(url, params, handler); 
    	} else {
    		client.get(url, handler); 
    	}
    }
    
    /*
     * Method makes GET request to retrieve user timeline from Twitter REST API v1.1. 
     * Twitter returns a rate-limited json-formatted response.
     */
    public void getUserTimeline(String screenName, int totalItemsCount, String maxId, 
    							AsyncHttpResponseHandler handler) {
    	Log.d("DEBUG", "TwitterClient getUserTimeline() fired. screenName is: " + screenName); 
    	String url = getApiUrl("statuses/user_timeline.json"); 
    	RequestParams params = new RequestParams(); 
    	params.put("screen_name", screenName); 
    	params.put("count", "20"); 			// Default count value not specified in API documentation
    	params.put("include_rts", "1"); 	// Recommended by API documentation
    	
    	if (totalItemsCount > 0) {
    		params.put("max_id", maxId);  
    	}
    	client.get(url, params, handler); 
    }
    
    /*
     * Method issues POST command for submitting a tweet via Twitter REST API v1.1. Tweet text  
     * typically limited to 140 characters.  
     */
    public void postTweet(String tweetBody, AsyncHttpResponseHandler handler) { 
    	String url = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", tweetBody);
    	client.post(url, params, handler); 
    }
    
    /*
     * Method makes GET request to verify account credentials with Twitter REST API v1.1, e.g., to recover
     * a profile_image_url. Twitter returns a rate-limited json-formatted response consisting of
     * key-value pairs representing the requesting user's current settings. 
     */
    public void getRegisteredUserInfo(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("account/verify_credentials.json");
    	client.get(url, handler);
    }
    
}