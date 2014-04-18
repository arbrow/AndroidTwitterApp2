package com.laurengariepy.android.mytwitterapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.codepath.oauth.OAuthLoginActivity;
import com.laurengariepy.android.mytwitterapp.clients.TwitterClient;

/**
*  Activity governs user login to the Twitter REST API v1.1. Class handles callbacks for login 
*  success or failure. If login succeeds, a class callback method initiates main UI app 
*  activity. If login fails, a class callback method responds. 
*/
public class LoginActivity extends OAuthLoginActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button btnLogIn = (Button) findViewById(R.id.btnLogIn);
		btnLogIn.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loginToRest(v); 
			}
		});
		
	}

	// Inflate the menu. (Adds items to the action bar if it's present)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	// OAuth authenticated successfully: launch main activity
    @Override
    public void onLoginSuccess() {
    	 Intent i = new Intent(this, TweetsActivity.class);
    	 startActivity(i);
    }
    
    // OAuth authentication flow failed: handle the error
    @Override
    public void onLoginFailure(Exception e) {
        Log.e("ERROR", e.getMessage());
    }
    
    // Click handler method for the button used to start OAuth flow. 
    // Uses the client to initiate OAuth authorization and calls either onLoginSuccess() or onLoginFailure().
    public void loginToRest(View view) {
    	getClient().connect();
    }

}
