package com.smartikyapps.smartmail;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

public class Register extends FragmentActivity {

	protected static final String TAG = "RegisterUserActivity";

	public static final int FULLNAME = 0;
	public static final int USERNAME = 1;
	public static final int PASSWORD = 2;
	public static final int RECOVERYPASS = 3;

	private static final String CREATEEMAIL_URL = "http://www.mailivy.com/mail/index.php";
	private static final String REGISTER_URL = "http://www.smartikymail.com/webservice/ivy/register.php";
	private static final String CHECKUSERNAME_URL = "http://www.smartikymail.com/webservice/ivy/checkusername.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	ViewPager mViewPager;
	SectionsPagerAdapter mSectionsPagerAdapter;
	Button backButton, nextButton, createButton;
	PagerTabStrip PTS;
	String fullName = "";
	String userName = "";
	String passWord = "";
	String recoveryEamil = "";

	int checkedRadioButtonId;
	Bitmap picture;
	Uri selectedImageUri;
	Typeface myTypefaceBold, myTypeface;
	JSONParser jsonParser = new JSONParser();

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.register_mailivy);

		overridePendingTransition(R.anim.transition_right_to_left,
				R.anim.transition_right_to_left_out);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		myTypefaceBold = Typeface.createFromAsset(this.getAssets(),
				"Roboto-BoldCondensed.ttf");
		myTypeface = Typeface.createFromAsset(this.getAssets(),
				"Roboto-Condensed.ttf");
		PTS = (PagerTabStrip) findViewById(R.id.pager_title_strip);
		for (int i = 0; i < PTS.getChildCount(); i++) {
			if (PTS.getChildAt(i) instanceof TextView) {
				((TextView) PTS.getChildAt(i)).setTypeface(myTypefaceBold);
				((TextView) PTS.getChildAt(i)).setTextSize(19);
				;
			}
		}

		/*
		 * String title = "Register New User"; SpannableStringBuilder SD = new
		 * SpannableStringBuilder(title); SD.setSpan(new CustomTypefaceSpan("",
		 * myTypefaceBold), 0, title.length(),
		 * Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		 * 
		 * getActionBar().setTitle(SD);
		 */

		backButton = (Button) findViewById(R.id.buttonBack);
		nextButton = (Button) findViewById(R.id.buttonNext);
		createButton = (Button) findViewById(R.id.buttonCreate);
		backButton.setTypeface(myTypefaceBold);
		nextButton.setTypeface(myTypefaceBold);
		createButton.setTypeface(myTypefaceBold);

		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nextPage();
				fixlayoutAccordingly();
			}
		});

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mViewPager.getCurrentItem() == FULLNAME) {
					finish();
					overridePendingTransition(R.anim.transition_left_to_right,
							R.anim.transition_left_to_right_out);
				}
				previousPage();
				fixlayoutAccordingly();
			}
		});

		createButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!storeFragmentData(false))
					return;
				// createPoll();
				if (userName.equals(""))
					showMessage("Please enter a username");
				else if (fullName.equals(""))
					showMessage("Please enter your fullname");
				else if (passWord.equals(""))
					showMessage("Please enter a password");
				else
					new CreateUser().execute();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "OnActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		// resetForm();
		// super.onBackPressed();
		if (mViewPager.getCurrentItem() == FULLNAME) {
			finish();
			overridePendingTransition(R.anim.transition_left_to_right,
					R.anim.transition_left_to_right_out);
		}
		previousPage();
		fixlayoutAccordingly();

		// getParent().onBackPressed();
	}

	boolean storeFragmentData(boolean returning) {
		String msg = mSectionsPagerAdapter.fragments[mViewPager
				.getCurrentItem()].storeData(returning);
		if (msg != null) {
			showMessage(msg);
			return false;
		}
		return true;
	}

	void resetForm() {
		mViewPager.setCurrentItem(0);
		mSectionsPagerAdapter.notifyDataSetChanged();
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
			mSectionsPagerAdapter.fragments[i].resetForm();

	}

	void nextPage() {
		if (mViewPager.getCurrentItem() + 1 < mSectionsPagerAdapter.getCount()) {
			Log.v("returingnext", "false");
			if (!storeFragmentData(false))
				return;
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
			mSectionsPagerAdapter.notifyDataSetChanged();
		}
	}

	void previousPage() {
		if (mViewPager.getCurrentItem() - 1 >= 0) {
			Log.v("returingprevious", "true");
			if (!storeFragmentData(true))
				return;
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
			mSectionsPagerAdapter.notifyDataSetChanged();
		}
	}

	public void setSelectedTab(int item) {
		mViewPager.setCurrentItem(item);
		mSectionsPagerAdapter.notifyDataSetChanged();
		Fragment fragment = mSectionsPagerAdapter.fragments[item];
		/*
		 * if (fragment instanceof PollsFragment) { ((PollsFragment)
		 * fragment).refresh(); }
		 */
	}

	public void fixlayoutAccordingly() {
		if (mViewPager.getCurrentItem() == RECOVERYPASS) {
			nextButton.setVisibility(View.GONE);
			createButton.setVisibility(View.VISIBLE);
		} else {
			nextButton.setVisibility(View.VISIBLE);
			createButton.setVisibility(View.GONE);
		}

		if (mViewPager.getCurrentItem() == FULLNAME)
			backButton.setText("Cancel");
		else
			backButton.setText("Back");
		// } else {
		// backButton.setVisibility(View.VISIBLE); }
		//

		/*
		 * switch (mViewPager.getCurrentItem()) { case FULLNAME: break; case
		 * USERNAME: break; case PASSWORD: break; case RECOVERYPASS: break; }
		 */
	}

	void showMessage(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	private void returnWithMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		onBackPressed();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		ItemFragment[] fragments;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new ItemFragment[getCount()];
		}

		@Override
		public Fragment getItem(int position) {
			if (fragments[position] == null)
				fragments[position] = getNewItem(position);
			return fragments[position];
		}

		public ItemFragment getNewItem(int position) {
			return newInstance(position);
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "NAME";
			case 1:
				return "USERNAME";
			case 2:
				return "PASSWORD";
			case 3:
				return "PASSWORD RECOVERY";
			}
			return null;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	public ItemFragment newInstance(int step) {
		ItemFragment u = new ItemFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("Step", step);
		u.setArguments(bundle);
		return u;
	}

	public class ItemFragment extends Fragment {

		protected static final String TAG = "UsersFragment";

		Activity activity;

		int step;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			activity = getActivity();
			step = getArguments().getInt("Step");
		}

		public String storeData(boolean returning) {
			try {
				switch (step) {
				case FULLNAME:
					if (firstnameET.getText().toString().isEmpty()
							|| lastnameET.getText().toString().isEmpty())
						return "Please enter your full name";
					fullName = firstnameET.getText().toString() + " "
							+ lastnameET.getText().toString();

					break;
				case USERNAME:
					if (!returning) {
						if (usernameET.getText().toString().isEmpty())
							return "Please enter a usename";
						//Log.v("returning", Boolean.toString(returning));
						if (!usernameET.getText().toString().matches("[a-zA-Z0-9.?_]*"))
							return "Your username cannot contain special characters or spaces";

						String result = new CheckUserName(usernameET.getText()
								.toString()).execute().get();

						if (result.equals("0"))
							return "Username already exists please choose another username";
						if (result.equals("-1"))
							return "error";
						else
							userName = usernameET.getText().toString();
					}
					break;
				case PASSWORD:
					if (!returning) {
						String pass1 = (pass1ET.getText().toString());
						String pass2 = (pass2ET.getText().toString());
						if (!pass1.equals(pass2)) {
							Log.v("steps", "3");
							return "Passwords don't match";

						}
						if (pass1.length() < 4)
							return "Password too short";
						else
							passWord = pass1;
					}

					break;
				case RECOVERYPASS:
					if (!returning) {
						if (isEmailValid(recoveryET.getText().toString())) {
							recoveryEamil = recoveryET.getText().toString();
						} else
							return "Please enter a valid email";
					}

					break;
				}

			} catch (Exception e) {
				Log.v("unknown error", e.getCause().toString());
				return "An unknown error occured.";
			}
			return null;
		}

		void resetForm() {
			switch (step) {
			case FULLNAME:
				firstnameET.setText("");
				lastnameET.setText("");
				break;
			case USERNAME:
				usernameET.setText("");
				break;
			case PASSWORD:
				pass1ET.setText("");
				pass2ET.setText("");

				break;
			case RECOVERYPASS:
				recoveryET.setText("");
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = null;
			switch (step) {
			case FULLNAME:
				view = inflater.inflate(R.layout.register_fullname, container,
						false);
				setupFirstName(view);
				break;
			case USERNAME:
				view = inflater.inflate(R.layout.register_username, container,
						false);
				setupPicture(view);
				break;
			case PASSWORD:
				view = inflater.inflate(R.layout.register_password, container,
						false);
				setupChoices(view);
				break;
			case RECOVERYPASS:
				view = inflater.inflate(R.layout.register_recovery, container,
						false);
				setupShare(view);
				break;

			}
			return view;
		}

		@Override
		public void onResume() {
			super.onResume();
			fixlayoutAccordingly();
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			Log.i(TAG, "OnActivityResult");
		}

		TextView firstnameTV, lastnameTV;
		EditText firstnameET, lastnameET;
		ImageView clearfirstname, clearlastname;

		void setupFirstName(View view) {
			firstnameET = (EditText) view.findViewById(R.id.firstNameET);
			lastnameET = (EditText) view.findViewById(R.id.lastNameET);
			firstnameTV = (TextView) view.findViewById(R.id.firstNameTV);
			lastnameTV = (TextView) view.findViewById(R.id.lastNameTV);
			firstnameTV.setTypeface(myTypeface);
			lastnameTV.setTypeface(myTypeface);
			firstnameET.setTypeface(myTypeface);
			lastnameET.setTypeface(myTypeface);
			clearfirstname = (ImageView) view.findViewById(R.id.clearfirstnameET);
			clearlastname = (ImageView) view.findViewById(R.id.clearlastNameET);

			setclear(clearfirstname, firstnameET);
			setclear(clearlastname, lastnameET);

		}

		EditText usernameET;
		ImageView clearusername;

		void setupPicture(View view) {
			((TextView) view.findViewById(R.id.usernameTV))
					.setTypeface(myTypeface);
			TextView address;
			address = (TextView) view.findViewById(R.id.emailaddress);
			address.setTypeface(myTypeface);
			address.setText("@mailivy.com");
			usernameET = ((EditText) view.findViewById(R.id.usernameET));
			usernameET.setTypeface(myTypeface);
			clearusername = (ImageView) view.findViewById(R.id.clearusernameET);
			setclear(clearusername, usernameET);

		}

		TextView pass1TV, pass2TV;
		EditText pass1ET, pass2ET;
		ImageView clearpass1, clearpass2;

		void setupChoices(View view) {
			pass1ET = (EditText) view.findViewById(R.id.password1ET);
			pass1ET.setTypeface(myTypeface);
			pass2ET = (EditText) view.findViewById(R.id.password2ET);
			pass2ET.setTypeface(myTypeface);
			((TextView) view.findViewById(R.id.password1TV))
					.setTypeface(myTypeface);
			((TextView) view.findViewById(R.id.password2TV))
					.setTypeface(myTypeface);

			clearpass1 = (ImageView) view.findViewById(R.id.clearpassword1ET);

			clearpass2 = (ImageView) view.findViewById(R.id.clearpassword2ET);

			setclear(clearpass1, pass1ET);
			setclear(clearpass2, pass2ET);

		}

		EditText recoveryET;
		ImageView clearrecovery;

		void setupShare(View view) {
			((TextView) view.findViewById(R.id.recoveryTV))
					.setTypeface(myTypeface);
			recoveryET = (EditText) view.findViewById(R.id.recoveryET);
			recoveryET.setTypeface(myTypeface);
			clearrecovery = (ImageView) view.findViewById(R.id.clearrecoveryET);
			setclear(clearrecovery, recoveryET);
		}

		public void SetUserName(String name) {
			userName = name;
		}

		class CheckUserName extends AsyncTask<String, String, String> {

			ProgressDialog pDialog;
			boolean failure = false;
			String username;

			public CheckUserName(String UserName) {
				username = UserName;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(Register.this);
				pDialog.setMessage("Checking username availability...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			@Override
			protected String doInBackground(String... args) {
				String success;

				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", username));

					JSONObject json = jsonParser.makeHttpRequest(
							CHECKUSERNAME_URL, "POST", params);
					return Integer.toString(json.getInt(TAG_SUCCESS));

				} catch (Exception e) {
					Log.e("Creeate User Error", e.toString());
					return Integer.toString(-1);
				}

			}

			protected void onPostExecute(String Result) {
				// dismiss the dialog once product deleted

				try {
					Log.v("result", Result);
					pDialog.dismiss();
					if (Result.equals("0"))
						super.onPostExecute("Username already exists please choose another username");
					else if (Result.equals("1"))
						super.onPostExecute(username);
				} catch (Exception e) {
					Log.e("dialog dismiss error", e.toString());
					super.onPostExecute("error");

				}

			}

		}

	}

	boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	class CreateUser extends AsyncTask<String, String, String> {

		boolean failure = false;
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Register.this);
			pDialog.setMessage("Creating User...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) { // TODO

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("fullname", fullName));
				params.add(new BasicNameValuePair("username", userName));
				params.add(new BasicNameValuePair("password", passWord));
				params.add(new BasicNameValuePair("email", recoveryEamil));

				List<NameValuePair> params2 = new ArrayList<NameValuePair>();
				params2.add(new BasicNameValuePair("username", userName));
				params2.add(new BasicNameValuePair("password", passWord));

				JSONObject json1 = jsonParser.makeHttpRequest(REGISTER_URL,
						"POST", params);
				JSONObject json2 = jsonParser.makeHttpRequest(CREATEEMAIL_URL,
						"POST", params2);
				int success = json1.getInt(TAG_SUCCESS)
						* json2.getInt(TAG_SUCCESS);
				if (success == 1) {
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(Register.this);
					Editor edit = sp.edit();
					edit.putString("username_from", userName);
					edit.putString("fullname", fullName);
					edit.putString("password", passWord);
					edit.putBoolean("mailivy", true);
					edit.commit();
					finish();
					return json1.getString(TAG_MESSAGE);
				} else {
					return json1.getString(TAG_MESSAGE);

				}
			} catch (Exception e) {
				Log.e("Creeate User Error", e.toString());
			}

			return null;

		}

		protected void onPostExecute(String file_url) {

			try {
				pDialog.dismiss();
			} catch (Exception e) {
				Log.e("dialog dismiss error", e.toString());
			}
			if (file_url != null) {
				Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}
	}

	private void setclear(final ImageView imageView, final EditText edittext) {
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edittext.setText("");

			}
		});

		edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (edittext.getText().length() > 0)
						imageView.setVisibility(View.VISIBLE);
				} else
					imageView.setVisibility(View.GONE);

			}
		});

		edittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (edittext.getText().length() > 0)
					imageView.setVisibility(View.VISIBLE);
				else
					imageView.setVisibility(View.GONE);
			}
		});
	}

}
