package com.smartikyapps.smartmail;

import static com.smartikyapps.smartmail.gcm.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.smartikyapps.smartmail.gcm.CommonUtilities.EXTRA_MESSAGE;
import static com.smartikyapps.smartmail.gcm.CommonUtilities.SENDER_ID;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gcm.GCMRegistrar;
import com.smartikyapps.smartmail.fragments.AboutFragment;
import com.smartikyapps.smartmail.fragments.ChangepasswordFragment;
import com.smartikyapps.smartmail.fragments.FeedbackFragment;
import com.smartikyapps.smartmail.fragments.HomeFragment;
import com.smartikyapps.smartmail.fragments.SettingsFragment;
import com.smartikyapps.smartmail.gcm.AlertDialogManager;
import com.smartikyapps.smartmail.gcm.ConnectionDetector;
import com.smartikyapps.smartmail.gcm.WakeLocker;

@SuppressLint("NewApi")
public class ReadEmails extends FragmentActivity {
	Typeface myTypefaceboldItalic, myTypeface;
	Typeface myTypefacebold, myTypefaceitalic;
	public static ProgressDialog pDialog;
	// TextView nosentmessages;
	//JSONParser jsonParser = new JSONParser();
	String postID, postURL;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	PagerTabStrip PTS;
	private static boolean backedFromOtherFragment = false;
	// TextView loggeduser, logout;
	AsyncTask<Void, Void, Void> mRegisterTask;
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;

	// new shit
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.read_emails);
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		myTypefaceboldItalic = Typeface.createFromAsset(getAssets(),
				"Roboto-BoldCondensedItalic.ttf");
		myTypefacebold = Typeface.createFromAsset(getAssets(),
				"Roboto-BoldCondensed.ttf");
		myTypefaceitalic = Typeface.createFromAsset(getAssets(),
				"Roboto-CondensedItalic.ttf");
		myTypeface = Typeface.createFromAsset(getAssets(),
				"Roboto-Condensed.ttf");

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		PTS = (PagerTabStrip) findViewById(R.id.pager_title_strip);
		// PTS.setTypeface(myTypefacebold, Typeface.NORMAL);
		// PTS.setViewPager(mViewPager);
		// logout.setTypeface(myTypefacebold);
		for (int i = 0; i < PTS.getChildCount(); i++) {
			if (PTS.getChildAt(i) instanceof TextView) {
				((TextView) PTS.getChildAt(i)).setTypeface(myTypefacebold);
				// ((TextView) PTS.getChildAt(i)).setTextSize(19);
				;
			}
		}

		mTitle = mDrawerTitle = "";
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		// SpannableString content = new SpannableString("Log Out");
		// content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		// logout.setText(content);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		final String regId = GCMRegistrar.getRegistrationId(this);
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(ReadEmails.this);
		Editor edit = sp.edit();
		edit.putString("regId", regId);
		edit.commit();
		Log.v("regID", regId);
		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
		}

		navDrawerItems = new ArrayList<NavDrawerItem>();

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));

		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
		pDialog= new ProgressDialog(this);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		//if (savedInstanceState == null) {
			// on first time display view for first nav item
			//displayView(0);
		//}
		sp.getString("emailpassword","doesnt exist");
		//Boolean mailivy=false;
		//mailivy= sp.getBoolean("mailivy", false);
		//Log.v("message mailivy", Boolean.toString(mailivy));
		

		//if (mailivy){
		Log.v("salam","calling service: "+DateFormat.getDateTimeInstance().format(new Date()));
		Intent downloader = new Intent(getApplicationContext(), EmailReceiver.class);
		//downloader.putExtra("username", post_username_from); 
		//downloader.putExtra("password", emailpassword);
		downloader.putExtra("regid", regId);
	    downloader.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(ReadEmails.this, 0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
	    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 20*60*1000, pendingIntent); 
		//}
	}
	
	public void CreateDialog(){
		pDialog= new ProgressDialog(this);
	}

	public static void setBackFromOther(boolean input) {
		backedFromOtherFragment = input;
	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_newmail:
			Intent newmail = new Intent(this, AddEmail.class);
			startActivity(newmail);
			return true;
		case R.id.action_settings:
			mDrawerLayout.openDrawer(mDrawerList);
			return true;
		case R.id.Refresh:
			EmailsFragment ef0,
			ef1;	
			
			ef0 = (EmailsFragment) mSectionsPagerAdapter.getItem(0);
			ef1 = (EmailsFragment) mSectionsPagerAdapter.getItem(1);
			ef0.refresh();
			ef1.refresh();
			return true;
		case R.id.logout:
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(ReadEmails.this);
			Editor edit = sp.edit();
			// edit.putString("password", password);
			edit.remove("password");
			edit.remove("fullname");
			edit.commit();
			finish();
			Intent i = new Intent(ReadEmails.this, Login.class);
			finish();
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		menu.findItem(R.id.Refresh).setVisible(!drawerOpen);
		menu.findItem(R.id.logout).setVisible(!drawerOpen);
		menu.findItem(R.id.action_newmail).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	public void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			
			if (backedFromOtherFragment) {
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.anim.transition_left_to_right,
						R.anim.transition_tohomefragment);
				ft.replace(R.id.frame_container, fragment, "fragment");
				ft.commit();
				backedFromOtherFragment = false;
			}
			PTS.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.VISIBLE);
			EmailsFragment.SetVisibility(true);
			
			break;

		case 1:
			fragment = new SettingsFragment();
			PTS.setVisibility(View.GONE);
			mViewPager.setVisibility(View.GONE);
			EmailsFragment.SetVisibility(false);
			break;
			
		case 2:
			Intent newmail = new Intent(this, AddEmail.class);
			startActivity(newmail);
			mDrawerLayout.closeDrawer(mDrawerList);
			displayView(0);
			break;

		case 3:
			fragment = new ChangepasswordFragment();
			PTS.setVisibility(View.GONE);
			mViewPager.setVisibility(View.GONE);
			EmailsFragment.SetVisibility(false);
			break;
		case 4:
			fragment = new FeedbackFragment();
			PTS.setVisibility(View.GONE);
			mViewPager.setVisibility(View.GONE);
			EmailsFragment.SetVisibility(false);
			break;
		case 5:
			fragment = new AboutFragment();
			PTS.setVisibility(View.GONE);
			mViewPager.setVisibility(View.GONE);
			EmailsFragment.SetVisibility(false);
			break;
		case 6:
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(ReadEmails.this);
			Editor edit = sp.edit();
			edit.remove("password");
			edit.remove("fullname");
			edit.commit();
			finish();
			Intent i = new Intent(ReadEmails.this, Login.class);
			mDrawerLayout.closeDrawer(mDrawerList);
			finish();
			startActivity(i);
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);

			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());

			String post_username_from = sp.getString("username_from", "");
			String useremail;
			if (post_username_from.contains("@"))
				useremail=post_username_from;
			else
				useremail=post_username_from+"@mailivy.com";
			SpannableStringBuilder SS = new SpannableStringBuilder(
					useremail);
			// SS.setSpan(new CustomTypefaceSpan("", myTypeface), 0,
			// 6,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			SS.setSpan(new CustomTypefaceSpan("", myTypeface), 0,
					useremail.length(),
					Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

			SpannableStringBuilder SD = new SpannableStringBuilder(
					navMenuTitles[position]);
			SD.setSpan(new CustomTypefaceSpan("", myTypeface), 0,
					navMenuTitles[position].length(),
					Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

			if (position > 0)
				setTitle(SD);
			else
				setTitle(SS);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onResume() {
		super.onResume();
		displayView(0);
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(ReadEmails.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}
		try{
			pDialog= new ProgressDialog(this);
		}
		catch (Exception e){
			Log.e("dialog", "new progess dialog error");
		}
		
		/*
		 * SharedPreferences sp = PreferenceManager
		 * .getDefaultSharedPreferences(getBaseContext()); String
		 * post_username_from = sp.getString("username_from", "");
		 * SpannableStringBuilder SS = new
		 * SpannableStringBuilder("Logged in as: " + post_username_from);
		 * SS.setSpan(new CustomTypefaceSpan("", myTypefaceitalic), 0, 14,
		 * Spanned.SPAN_EXCLUSIVE_INCLUSIVE); SS.setSpan(new
		 * CustomTypefaceSpan("", myTypefacebold), 14, 14 +
		 * post_username_from.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		 * loggeduser.setText(SS);
		 */
		// loggeduser.setText("Logged in as: "+post_username_from);
	}

	@Override
	public void onStart() {
		super.onStart();
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(ReadEmails.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
		// The rest of your onStart() code.
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			intent.getExtras().getString(EXTRA_MESSAGE);

			WakeLocker.acquire(getApplicationContext());
			Log.v("kar kone", "bache");
			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			// Toast.makeText(getApplicationContext(), "New Message: "
			// +newMessage, Toast.LENGTH_LONG).show();

			((EmailsFragment) (mSectionsPagerAdapter.getItem(0))).refresh();
			((EmailsFragment) (mSectionsPagerAdapter.getItem(1))).refresh();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	
	
	
	
	@Override
	public void onBackPressed() {
		
	}
	
	
	
	

	/*
	 * public void addEmail(View v) { Intent i = new Intent(this,
	 * AddEmail.class); startActivity(i); }
	 */

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		Fragment[] fragments;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new Fragment[2];
		}

		@Override
		public Fragment getItem(int position) {
			if (fragments[position] == null)
				fragments[position] = getNewItem(position);
			return fragments[position];
		}

		public Fragment getNewItem(int position) {
			switch (position) {
			case 0:
				return EmailsFragment.newInstance(EmailsFragment.OUTBOX);

			case 1:
				return EmailsFragment.newInstance(EmailsFragment.INBOX);
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Sent";
			case 1:
				return "Received";

			}
			return null;
		}
	}

}