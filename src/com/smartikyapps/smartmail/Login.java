package com.smartikyapps.smartmail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.smartikyapps.smartmail.gcm.AlertDialogManager;
import com.smartikyapps.smartmail.gcm.ConnectionDetector;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class Login extends Activity implements OnClickListener {

	private EditText user, pass;
	// private static SimpleAdapter adapter;
	private static CustomSimpleAdapter adapter;
	private ImageView clearusername, clearpassword;
	private Button mSubmit, mRegister;
	// private ArrayList<HashMap<String, String>> ContactList;
	private ArrayList<Contact> ContactList;
	TextView TV1, TV2;
	Button Login, Register, feedback, forgotpassword;
	ImageView background;
	public static IMAPFolder f = null;
	static int backgroundNumber = 2;
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	private static final String[] PROJECTION = new String[] {
			ContactsContract.CommonDataKinds.Email.CONTACT_ID,
			ContactsContract.Contacts.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Email.DATA };

	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();

	private static final String LOGIN_URL = "http://www.smartikymail.com/webservice/ivy/login.php";
	private static final String FORGOTPASSWORD_URL = "http://www.smartikymail.com/webservice/forgotpassword.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_CONTACTEMAIL = "contactemail";
	private static final String TAG_CONTACTNAME = "contactname";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.login);
		overridePendingTransition(R.anim.transition_left_to_right,
				R.anim.transition_left_to_right_out);
		/*
		 * adapter = new ContactAdapter(this, R.layout.listview_item_row, new
		 * ArrayList<String>());
		 */
		// ContactList=new ArrayList<Contact>();

		/*
		 * adapter = new ArrayAdapter<String>(this,
		 * R.layout.simple_dropdown_item, new ArrayList<String>());;
		 */

		// ContactList = new ArrayList<HashMap<String, String>>();
		ContactList = new ArrayList<Contact>();
		/*
		 * adapter = new SimpleAdapter(this, ContactList,
		 * R.layout.simple_dropdown_item, new String[] {TAG_CONTACTEMAIL,
		 * TAG_CONTACTNAME }, new int[] { R.id.contactemail, R.id.contactname
		 * });
		 */

		/*
		 * if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() ==
		 * 0 || SENDER_ID.length() == 0) { alert.showAlertDialog(Login.this,
		 * "Configuration Error!",
		 * "Please set your Server URL and GCM Sender ID", false); // stop
		 * executing code by return return; }
		 */

		TV1 = (TextView) findViewById(R.id.textView1);
		TV2 = (TextView) findViewById(R.id.textView2);
		mSubmit = (Button) findViewById(R.id.login);
		mRegister = (Button) findViewById(R.id.register);
		feedback = (Button) findViewById(R.id.feedback);
		forgotpassword = (Button) findViewById(R.id.forgotpassword);
		background = (ImageView) findViewById(R.id.background);

		clearusername = (ImageView) findViewById(R.id.clearusername);

		Typeface myTypeface = Typeface.createFromAsset(getAssets(),
				"Roboto-Condensed.ttf");
		Typeface myTypefacebold = Typeface.createFromAsset(getAssets(),
				"Roboto-BoldCondensed.ttf");
		Typeface myTypefacebolditalic = Typeface.createFromAsset(getAssets(),
				"Roboto-BoldCondensedItalic.ttf");

		TV1.setTypeface(myTypeface);
		TV2.setTypeface(myTypeface);

		mSubmit.setTypeface(myTypefacebold);
		mRegister.setTypeface(myTypefacebold);

		feedback.setTypeface(myTypefacebolditalic);
		forgotpassword.setTypeface(myTypefacebolditalic);

		// setup input fields
		user = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);

		user.setTypeface(myTypeface);
		pass.setTypeface(myTypeface);

		clearpassword = (ImageView) findViewById(R.id.clearpassword);

		clearusername.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				user.setText("");

			}
		});

		user.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (user.getText().length() > 0)
						clearusername.setVisibility(View.VISIBLE);
				} else
					clearusername.setVisibility(View.GONE);

			}
		});

		user.addTextChangedListener(new TextWatcher() {
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
				if (user.getText().length() > 0)
					clearusername.setVisibility(View.VISIBLE);
				else
					clearusername.setVisibility(View.GONE);
			}
		});

		clearpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pass.setText("");

			}
		});

		pass.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (pass.getText().length() > 0)
						clearpassword.setVisibility(View.VISIBLE);
				} else
					clearpassword.setVisibility(View.GONE);

			}
		});

		pass.addTextChangedListener(new TextWatcher() {
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
				if (pass.getText().length() > 0)
					clearpassword.setVisibility(View.VISIBLE);
				else
					clearpassword.setVisibility(View.GONE);
			}
		});

		// register listeners
		mSubmit.setOnClickListener(this);
		mRegister.setOnClickListener(this);
		feedback.setOnClickListener(this);
		forgotpassword.setOnClickListener(this);

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(Login.this);
		int size = sp.getInt("array_size", 0);
		boolean contactUpdate = sp.getBoolean("updatecontact", true);
		if (sp.getBoolean("my_first_time", true)) {
			// The app is being launched for first time.
			Log.v("firsttime", "First time");
			addShortcut();
			// record the fact that the app has been started at least once
			sp.edit().putBoolean("my_first_time", false).commit();
		} else
			Log.v("firsttime", "second");

		Log.v("updateContact", Boolean.toString(contactUpdate));
		Log.v("size", Integer.toString(size));
		if (size == 0 || contactUpdate) {
			new readContacts().execute();

		} else {
			String contactDetails = "";
			String cEmail, cName;
			for (int i = 0; i < size; i++) {
				// HashMap<String, String> map = new HashMap<String, String>();
				contactDetails = sp.getString("array_" + i, null);
				// Log.v("contact2", contactDetails);
				// map.put(TAG_CONTACTEMAIL,
				// contactDetails.substring(0,contactDetails.lastIndexOf("/")));
				// map.put(TAG_CONTACTNAME,
				// contactDetails.substring(contactDetails.lastIndexOf("/") +
				// 1));
				cEmail = contactDetails.substring(0,
						contactDetails.lastIndexOf("/"));
				cName = contactDetails.substring(contactDetails
						.lastIndexOf("/") + 1);
				// adapter.add(new Contact(cName, cEmail));
				ContactList.add(new Contact(cName, cEmail));
			}

			// adapter.setDropDownViewResource(R.id.contactemail);
			adapter = new CustomSimpleAdapter(getApplicationContext(),
					R.layout.simple_dropdown_item, ContactList);

			// adapter.add(sp.getString("array_" + i, null));
		}

	}

	private void addShortcut() {
		// Adding shortcut for MainActivity on Home screen
		Intent shortcutIntent = new Intent(getApplicationContext(), Login.class);

		shortcutIntent.setAction(Intent.ACTION_MAIN);

		Intent addIntent = new Intent();
		Intent removeIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Smart Mail");
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(
						getApplicationContext(), R.drawable.ic_launcher));

		removeIntent
				.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
		getApplicationContext().sendBroadcast(removeIntent);

		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		getApplicationContext().sendBroadcast(addIntent);
	}

	@Override
	protected void onResume() {

		super.onResume();
		cd = new ConnectionDetector(getApplicationContext());

		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(Login.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String username = sp.getString("username_from", "doesnt exist");
		String password = sp.getString("password", "doesnt exist");
		if (!username.equals("doesnt exist")) {
			user.setText(username);
		}
		if (!password.equals("doesnt exist")) {
			pass.setText(password);
			new AttemptLogin().execute();
		} else
			pass.setText("");
	}

	class readContacts extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Log.v("size", "here");
		}

		@Override
		protected String doInBackground(String... args) {
			try {

				ContentResolver cr = getBaseContext().getContentResolver();
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(Login.this);
				Editor edit = sp.edit();
				int i = 0;
				HashMap<String, String> map;
				Cursor cursor = cr.query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						PROJECTION, null, null, null);
				if (cursor != null) {
					try {
						// final int contactIdIndex =
						// cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
						// final int displayNameIndex =
						final int displayNameIndex = cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
						final int emailIndex = cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
						// long contactId;
						String displayName;
						String emailAddress;

						while (cursor.moveToNext()) {
							// contactId = cursor.getLong(contactIdIndex);
							displayName = cursor.getString(displayNameIndex);
							emailAddress = cursor.getString(emailIndex);
							// ContactList.add(object)
							// adapter.add(emailAddress);
							// map = new HashMap<String, String>();
							edit.putString("array_" + i, emailAddress + "/"
									+ displayName);
							// Log.v("contact", emailAddress);

							// map.put(TAG_CONTACTNAME,displayName);
							// map.put(TAG_CONTACTEMAIL,emailAddress);
							// ContactList.add(map);
							// adapter.add(new
							// Contact(displayName,emailAddress));
							ContactList.add(new Contact(displayName,
									emailAddress));
							i++;
							Log.v("sizehere1", Integer.toString(i));
						}

					} finally {

						// adapter.notifyDataSetChanged();
						Log.v("sizehere3", Integer.toString(i));
						edit.putInt("array_size", i);
						edit.putBoolean("updatecontact", false);
						edit.commit();
						cursor.close();

					}

				}

			} catch (Exception e) {
				Log.i("AutocompleteContacts", "Exception : " + e);
			}
			return null;

		}

		protected void onPostExecute(String end) {
			adapter = new CustomSimpleAdapter(getApplicationContext(),
					R.layout.simple_dropdown_item, ContactList);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			String username = null;
			String password = null;
			try {
				username = user.getText().toString().toLowerCase();
				password = pass.getText().toString();
			} catch (Exception e) {
				Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG)
						.show();
			}
			if (username.isEmpty() || password.isEmpty()) {
				Toast.makeText(Login.this,
						"Please enter a username and password",
						Toast.LENGTH_LONG).show();
			} else {
				new AttemptLogin().execute();
			}
			break;
		case R.id.register:
			Intent i = new Intent(this, Register.class);
			startActivity(i);
			break;
		case R.id.feedback:
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("text/html");
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Smart Mail feedback");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { "admin@smartiky.com" });

			startActivity(Intent.createChooser(emailIntent, "Feedback Email"));
			break;
		case R.id.forgotpassword:
			String usernamef = null;
			try {
				usernamef = user.getText().toString().toLowerCase();

			} catch (Exception e) {
				Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG)
						.show();
			}
			if (usernamef.isEmpty()) {
				Toast.makeText(
						Login.this,
						"Please enter your username and we'll email your password",
						Toast.LENGTH_LONG).show();
			} else {
				new Sendpassword().execute();
			}
			break;

		default:
			break;
		}
	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;
			String username = user.getText().toString().toLowerCase();
			String password = pass.getText().toString();
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(Login.this);

			if (username.contains("@gmail.com")) {
				try {
					Session mailSession = Session.getInstance(
							System.getProperties(), null);
					IMAPStore iMAPStore = (IMAPStore) mailSession
							.getStore("imaps");
					iMAPStore.connect("imap.gmail.com", username, password);
					//f = (IMAPFolder) iMAPStore.getFolder("INBOX");
					//f.open(Folder.READ_WRITE);
					Editor edit = sp.edit();
					edit.putString("username_from", username);
					edit.putString("password", password);
					edit.putString("service", "gmail");
					edit.putString("emailpassword", password);
					edit.commit();
					// moveTaskToBack(true);
					Intent i = new Intent(Login.this, ReadEmails.class);
					// finish();
					startActivity(i);
				} catch (AuthenticationFailedException e) {
					return "Invalid Credentials!";
				} catch (Exception e) {
					Log.e("Login Exception", e.getMessage());
				}
			} else if (username.contains("@yahoo.com")) {
				try {
					Session mailSession = Session.getInstance(
							System.getProperties(), null);
					IMAPStore iMAPStore = (IMAPStore) mailSession
							.getStore("imaps");
					iMAPStore
							.connect("imap.mail.yahoo.com", username, password);
					f = (IMAPFolder) iMAPStore.getFolder("INBOX");
					f.open(Folder.READ_WRITE);
					Editor edit = sp.edit();
					edit.putString("username_from", username);
					edit.putString("password", password);
					edit.putString("service", "yahoo");
					edit.putString("emailpassword", password);
					edit.commit();
					// moveTaskToBack(true);
					Intent i = new Intent(Login.this, ReadEmails.class);
					// finish();
					startActivity(i);
				} catch (AuthenticationFailedException e) {
					return "Invalid Credentials!";
				} catch (Exception e) {
					Log.e("Login Exception", e.getMessage());
				}
			} else {
				try {

					// Building Parameters
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", username));
					params.add(new BasicNameValuePair("password", password));
					JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL,
							"POST", params);
					success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						// Log.d("Login Successful!", json.toString());
						// save user data
						Editor edit = sp.edit();
						edit.putString("username_from", username);
						edit.putString("password", password);
						edit.putString("emailpassword",
								json.getString("emailpassword"));
						edit.putString("service", "mailivy");
						if (json.getString("emailpassword").length() > 0)
							edit.putBoolean("mailivy", true);
						edit.commit();
						Session mailSession = Session.getInstance(
								System.getProperties(), null);
						IMAPStore iMAPStore = (IMAPStore) mailSession
								.getStore("imaps");
						iMAPStore.connect("mail.mailivy.com", username
								+ "@mailivy.com",
								json.getString("emailpassword"));
						f = (IMAPFolder) iMAPStore.getFolder("INBOX");
						f.open(Folder.READ_WRITE);
						Intent i = new Intent(Login.this, ReadEmails.class);
						startActivity(i);
						return json.getString(TAG_MESSAGE);
					} else {
						// Log.d("Login Failure!", json.getString(TAG_MESSAGE));
						return json.getString(TAG_MESSAGE);

					}
				} catch (Exception e) {
					// e.printStackTrace();
					Log.e("Error", "Error in attempt login");
				}
			}
			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				Log.e("dialog dismiss error", e.toString());
			}
			if (file_url != null) {
				if (file_url.equals("Invalid Credentials!"))
					Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG)
							.show();
				else {
					if (!file_url.equals("Login successful!")) {
						SharedPreferences sp = PreferenceManager
								.getDefaultSharedPreferences(Login.this);
						Editor edit = sp.edit();
						edit.putString("fullname", file_url);
						edit.commit();
					}

				}
				// Log.v("Logged in", file_url);
			}

		}

	}

	class Sendpassword extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Emailing your password");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			int success;
			String username = user.getText().toString().toLowerCase();

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));

				JSONObject json = jsonParser.makeHttpRequest(
						FORGOTPASSWORD_URL, "POST", params);

				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					return json.getString(TAG_MESSAGE);
				} else {
					return json.getString(TAG_MESSAGE);

				}
			} catch (Exception e) {
				Log.e("Error", "Error in emailing password");
			}

			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				Log.e("dialog dismiss error", e.toString());
			}
			if (file_url != null) {
				Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
			}

		}

	}

	public static CustomSimpleAdapter GetContacts() {
		return adapter;
	}

	@Override
	public void onStart() {
		super.onStart();
		// The rest of your onStart() code.
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(Login.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
		EasyTracker.getInstance(this).activityStart(this); // Add this method.

	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

}