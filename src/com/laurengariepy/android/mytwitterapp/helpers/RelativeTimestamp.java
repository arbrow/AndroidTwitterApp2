package com.laurengariepy.android.mytwitterapp.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

/**
 * Class converts a Date object with its associated time of Tweet creation into a relative timestamp 
 * string for display in a text-based widget.  
 * 
 * Tweet JSON objects contain a "created_at" key-value pair, which has the following format:
 *    "created_at":"Wed Aug 27 13:08:45 +0000 2008"
 */   
public class RelativeTimestamp {

	private Date   mTimeTweetCreated;
	private String mRelativeTimestamp;
	
	
	public RelativeTimestamp(Context context, String createdAt) {
		mTimeTweetCreated  = parseString(createdAt);
		
		mRelativeTimestamp = (String) DateUtils.getRelativeDateTimeString(
										context,
										mTimeTweetCreated.getTime(),
										DateUtils.MINUTE_IN_MILLIS,
										DateUtils.WEEK_IN_MILLIS,
										0);
	}
	
	/*
	 * Parses Twitter "created_at" date string using the SimpleDateFormat class.
	 */
	private Date parseString(String createdAt) {
			// Note: As a practical matter, '+' need not be reflected in time zone specifier (zzzz) of the 
			// SimpleDateFormat constructor in order to produce proper output 
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");  	
		Date createdAtDate         = null;
		
		try {
			createdAtDate = formatter.parse(createdAt);
		} catch (ParseException e) {
			Log.d("DEBUG", "Threw ParseException"); 
			e.printStackTrace(); 
		}
		return createdAtDate;
	}
	
	public Date getTimeTweetCreated() {
		return mTimeTweetCreated;
	}

	public void setTimeTweetCreated(Date timeCreated) {
		mTimeTweetCreated = timeCreated;
	}

	public String getRelativeTimestamp() {
		return mRelativeTimestamp;
	}

	public void setRelativeTimestamp(String timestamp) {
		mRelativeTimestamp = timestamp;
	}
	
}

/*
 * Notes:
 * 
 * Calendar is an abstract base class used to convert between Date objects and their String representations.
 * GregorianCalendar is a subclass of Calendar. 
 * 
 * The DateUtils class contains various date-related utilities for creating text for things like elapsed
 * time and date ranges, Strings for days of the week, etc.
 * 
 * public static CharSequence getRelativeDateTimeString(Context c, long startTime, long minResolution, long 
 * transitionResolution, int flags) --> Returns a CharSequence describing the elapsed time since startTime
 * formatted as "[relative time/date], [time]", e.g., 3 mins ago, 10:15 AM. minResolution is the minimum
 * elapsed time (in milliseconds) to report when showing relative times. E.g., a time 3 seconds in the past
 * will be reported as "0 minutes ago" if this is set to MINUTE_IN_MILLIS. transitionResolution is the 
 * elapsed time (in milliseconds) at which to stop reporting relative measurements. Elapsed times greater
 * than this resolution will default to normal date formatting. E.g., will transition from "6 days ago" to
 * "Dec 12" when using WEEK_IN_MILLIS. 
 */
