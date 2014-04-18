package com.laurengariepy.android.mytwitterapp.adapters;

import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.laurengariepy.android.mytwitterapp.activities.R;
import com.laurengariepy.android.mytwitterapp.helpers.RelativeTimestamp;
import com.laurengariepy.android.mytwitterapp.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Class implements ArrayAdapter for ListView of tweets.
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {

	private AssetManager mAssetManager;
	private String       mFont = "Roboto-Light.ttf"; 
	private Typeface     mTypeface;
	
	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets); 
		mAssetManager = context.getAssets(); 
	}
	
	/*
	 * Nested class boosts performance of associated AdapterView by retaining handles
	 * to View objects retrieved from findViewById(int).
	 */
	private static class ViewHolder {
		ImageView imageView;
		TextView  nameView;
		TextView  createdAtView;
		TextView  bodyView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position); 
		
		ViewHolder viewHolder;								
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.tweet_item, null); 
			viewHolder.imageView     = (ImageView) convertView.findViewById(R.id.ivProfile); 
			viewHolder.nameView      = (TextView)  convertView.findViewById(R.id.tvName);
			viewHolder.createdAtView = (TextView)  convertView.findViewById(R.id.tvCreatedAt); 
			viewHolder.bodyView      = (TextView)  convertView.findViewById(R.id.tvBody);
			
			// Avoid superfluous calls to (expensive) findViewById(int) by tagging the now 
			// assembled convertView
			convertView.setTag(viewHolder); 
		} else {
			// Exploit the assembled View handles in convertView by retrieving its tag
			viewHolder = (ViewHolder) convertView.getTag(); 	
		}
		
		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), 
				viewHolder.imageView);
		
		mTypeface = Typeface.createFromAsset(mAssetManager, mFont); 
		viewHolder.nameView.setTypeface(mTypeface); 
		String formattedName = "<b>" + tweet.getUser().getName() + "</b>" +
							   " <small><font color='#777777'>@" + tweet.getUser().getScreenName() + 
							   "</font></small>";
		viewHolder.nameView.setText(Html.fromHtml(formattedName));
		
		viewHolder.createdAtView.setTypeface(mTypeface); 
		RelativeTimestamp rt   = new RelativeTimestamp(getContext(), tweet.getCreatedAt());  
		String timestamp       = rt.getRelativeTimestamp(); 
		viewHolder.createdAtView.setText(Html.fromHtml(timestamp));
		
		viewHolder.bodyView.setTypeface(mTypeface); 
		viewHolder.bodyView.setText(Html.fromHtml(tweet.getBody())); 
		
		return convertView;
	}
	
}