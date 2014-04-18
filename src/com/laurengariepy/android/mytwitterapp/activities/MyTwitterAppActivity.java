package com.laurengariepy.android.mytwitterapp.activities;

import android.content.Context;

import com.laurengariepy.android.mytwitterapp.clients.TwitterClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Class is a non-UI worker used to configure various application settings, including 
 * image cache in memory and on disk. Class also adds a singleton for accessing the
 * relevant Twitter REST client.
 */
public class MyTwitterAppActivity extends com.activeandroid.app.Application {
	
	private static Context context;
	
    @Override
    public void onCreate() {
        super.onCreate();
        MyTwitterAppActivity.context = this;
        
        // Create global configuration and initialize ImageLoader with configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions
        											.Builder()
        											.cacheInMemory()
        											.cacheOnDisc()
        											.build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
        											.Builder(getApplicationContext())
            										.defaultDisplayImageOptions(defaultOptions)
            										.build();
        ImageLoader.getInstance().init(config);
    }
    
    /*
     * Returns a singleton REST client for accessing the Twitter REST v1.1 API. Relies on the static
     * factory method getInstance(...) inherited from the OAuthBaseClient class. 
     */
    public static TwitterClient getRestClient() {
    	return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, MyTwitterAppActivity.context);
    }
}