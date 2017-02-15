package com.smartikyapps.smartmail;

import java.text.DateFormat;
import java.util.Date;

import com.smartikyapps.smartmail.gcm.IMAPWakeLocker;
import com.smartikyapps.smartmail.gcm.WakeLocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EmailReceiver extends BroadcastReceiver {
	String username, password, registrationID;

 
	@Override
	public void onReceive(final Context context, Intent intent) {
		IMAPWakeLocker.acquire(context);
		Log.v("salam","service called: "+DateFormat.getDateTimeInstance().format(new Date()));
		//username = intent.getExtras().getString("username");
		//password = intent.getExtras().getString("password");
		registrationID = intent.getExtras().getString("regid");
		Intent mServiceIntent = new Intent(context, EmailPullService.class);
		//mServiceIntent.setData(Uri.parse(dataUrl));
		//mServiceIntent.putExtra("username", username);
		//mServiceIntent.putExtra("password", password);
		mServiceIntent.putExtra("regid", registrationID);
		context.startService(mServiceIntent);
		IMAPWakeLocker.release();
	}
}