package com.smartikyapps.smartmail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

public class PreviewEmail extends Activity {

	// Button back;
	String URL, userTo;
	WebView webView;
	LinearLayout LL;
	TextView titleTV;
	JSONParser jsonParser = new JSONParser();
	String post_username_from, emailpassword;
	String html;
	String postID = " ";
	String from = " ";
	String ivyInbox = " ";
	String emailpass = " ";
	String message = " ";
	String title = " ";
	int open;

	Typeface myTypefaceboldItalic, myTypefacebold, myTypeface;
	private static final String DELETE_EMAIL_URL = "http://www.smartikymail.com/webservice/ivy/deleteemail.php";
	private static final String MODIFY_EMAIL_URL = "http://www.smartikymail.com/webservice/editemail.php";
	private static final String TAG_URL = "url_code";
	private static final String TAG_USERNAME_TO = "username_to";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_POST_ID = "post_id";
	private static final String TAG_USERNAME_FROM = "username_from";
	private static final String TAG_IVYINBOX = "ivyInbox";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_OPENED = "opened";
	private static final String TAG_TITLE = "title";

	// testing on Emulator:

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.transition_right_to_left,
				R.anim.transition_right_to_left_out);
		myTypefaceboldItalic = Typeface.createFromAsset(getAssets(),
				"Roboto-BoldCondensedItalic.ttf");
		myTypefacebold = Typeface.createFromAsset(getAssets(),
				"Roboto-BoldCondensed.ttf");
		myTypeface = Typeface.createFromAsset(getAssets(),
				"Roboto-Condensed.ttf");
		setContentView(R.layout.preview_email);
		try {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			post_username_from = sp.getString("username_from", "doesnt exist");
			emailpassword = sp.getString("emailpassword", "doesnt exist");
			Log.v("emailpas", emailpassword);
			;
		} catch (Exception e) {
			Log.v("sp error", e.toString());
		}

		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			URL = extras.getString(TAG_URL);
			userTo = extras.getString(TAG_USERNAME_TO);
			postID = extras.getString(TAG_POST_ID);
			from = extras.getString(TAG_USERNAME_FROM);
			ivyInbox = extras.getString(TAG_IVYINBOX);
			message = extras.getString(TAG_MESSAGE);
			open = extras.getInt(TAG_OPENED);
			title = extras.getString(TAG_TITLE);
			// and get whatever type user account id is
			// Log.v("URL", URL);
		}

		SpannableStringBuilder SD = new SpannableStringBuilder(userTo);
		SD.setSpan(new CustomTypefaceSpan("", myTypeface), 0, userTo.length(),
				Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

		getActionBar().setTitle(SD);

		// LL= (LinearLayout) findViewById(R.id.linearlayout);
		webView = (WebView) findViewById(R.id.webView);
		titleTV = (TextView) findViewById(R.id.titlepreview);
		titleTV.setText(title);
		titleTV.setTypeface(myTypefaceboldItalic);
		// webView.setLayoutParams(new LayoutParams(width, height));
		// LL.addView(webView, new LinearLayout.LayoutParams(width,
		// LayoutParams.FILL_PARENT));
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(false);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		webView.getSettings().setPluginState(android.webkit.WebSettings.PluginState.ON_DEMAND);
		// webView.loadUrl("http://www.smartikymail.com/webservice/previewemailmobile.php/"+URL);
		DownloadTask task = new DownloadTask();
		task.execute("http://www.smartikymail.com/webservice/previewemailmobile.php/"
				+ URL);
		
		

		/*
		 * back = (Button) findViewById(R.id.back);
		 * 
		 * back.setTypeface(myTypefacebold);
		 * 
		 * back.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { onBackPressed();
		 * 
		 * } });
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.preview, menu);

		switch (open) {
		case 0:
			menu.getItem(0).setVisible(false);
			return true;
		case 1:
			menu.getItem(0).setVisible(true);
			return true;
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_delete:
			deleteEmailDialog(postID, ivyInbox);
			// new DeleteEmail(postID,ivyInbox).execute();
			return true;
		case R.id.action_edit:
			editEmail(message, postID);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.transition_left_to_right,
				R.anim.transition_left_to_right_out);
	}

	@Override
	public void onStart() {
		super.onStart();
		// The rest of your onStart() code.
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	public void deleteEmailDialog(final String postID, final String mailivy) {
		AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);

		alertDialogBuilder1
				.setMessage("Are you sure you want to delete this email?")
				.setCancelable(true)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								new DeleteEmail(postID, mailivy).execute();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});

		AlertDialog alertDialog1 = alertDialogBuilder1.create();

		alertDialog1.show();

		Button n = alertDialog1.getButton(DialogInterface.BUTTON_NEGATIVE);
		if (n != null) {
			n.setTypeface(myTypefaceboldItalic);
			n.setTextSize(17);
			n.setTextColor(Color.BLACK);
		}

		Button p = alertDialog1.getButton(DialogInterface.BUTTON_POSITIVE);
		if (p != null) {
			p.setTypeface(myTypefaceboldItalic);
			p.setTextSize(17);
			p.setTextColor(Color.BLACK);

		}

		TextView msgTxt = (TextView) alertDialog1
				.findViewById(android.R.id.message);
		if (msgTxt != null) {
			msgTxt.setTypeface(myTypefacebold);
			msgTxt.setTextSize(19);
		}

	}

	class MakeEdits extends AsyncTask<String, String, String> {
		String post_id;
		ProgressDialog pDialog;
		int success;
		String changed_Message = "";

		public MakeEdits(String postid, String changedMessage) {
			post_id = postid;
			changed_Message = changedMessage;
			message = changedMessage;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PreviewEmail.this);
			pDialog.setMessage("Editing Message...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("post_id", post_id));
				params.add(new BasicNameValuePair("message", changed_Message));
				JSONObject json = jsonParser.makeHttpRequest(MODIFY_EMAIL_URL,
						"POST", params);
				success = json.getInt(TAG_SUCCESS);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (success == 1) {
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(PreviewEmail.this);
				if (sp.getBoolean("sendsound", true)) {
					MediaPlayer mp = MediaPlayer.create(PreviewEmail.this,
							R.raw.sent);
					mp.start();
				}
				DownloadTask task = new DownloadTask();
				task.execute("http://www.smartikymail.com/webservice/previewemailmobile.php/"
						+ URL);

			}

			if (file_url != null) {
				Toast.makeText(PreviewEmail.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

	public void editEmail(String original, final String postID) {

		AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		final EditText input = new EditText(this);
		input.setLines(8);
		input.setGravity(Gravity.TOP);
		input.setBackgroundResource(R.drawable.edittext_modified_states);
		input.setTypeface(myTypeface);
		String updatedOriginal = original.replaceAll("<br />",
				System.getProperty("line.separator"));
		input.setText(updatedOriginal);
		input.setSelection(updatedOriginal.length());
		alertDialogBuilder1.setView(input);

		alertDialogBuilder1
				.setCancelable(true)
				.setPositiveButton("Make Changes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								imm.hideSoftInputFromWindow(getCurrentFocus()
										.getWindowToken(), 0);
								new MakeEdits(postID, input.getText()
										.toString().replaceAll("\\n", "<br />"))
										.execute();

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								imm.hideSoftInputFromWindow(getCurrentFocus()
										.getWindowToken(), 0);
								dialog.cancel();
							}
						});

		AlertDialog alertDialog1 = alertDialogBuilder1.create();

		alertDialog1.show();
		alertDialog1.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		Button n = alertDialog1.getButton(DialogInterface.BUTTON_NEGATIVE);
		if (n != null) {
			n.setTypeface(myTypefaceboldItalic);
			n.setTextSize(17);
			n.setTextColor(Color.BLACK);
		}

		Button p = alertDialog1.getButton(DialogInterface.BUTTON_POSITIVE);
		if (p != null) {
			p.setTypeface(myTypefaceboldItalic);
			p.setTextSize(17);
			p.setTextColor(Color.BLACK);

		}

		TextView msgTxt = (TextView) alertDialog1
				.findViewById(android.R.id.message);
		if (msgTxt != null) {
			msgTxt.setTypeface(myTypefacebold);
			msgTxt.setTextSize(19);
		}

	}

	class DeleteEmail extends AsyncTask<String, String, String> {
		String post_id, mail_ivy;
		int success;
		ProgressDialog pDialog;

		public DeleteEmail(String postid, String mailivy) {
			post_id = postid;
			mail_ivy = mailivy;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PreviewEmail.this);
			pDialog.setMessage("Deleting Message...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("post_id", post_id));
				params.add(new BasicNameValuePair("username_value",
						post_username_from));
				params.add(new BasicNameValuePair("emailpassword",
						emailpassword));
				params.add(new BasicNameValuePair("mailivy", mail_ivy));
				// Log.v("delete email mailivy", mail_ivy);
				JSONObject json = jsonParser.makeHttpRequest(DELETE_EMAIL_URL,
						"POST", params);
				success = json.getInt(TAG_SUCCESS);
				// Log.v("delete email", json.getString(TAG_MESSAGE));

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (success == 1) {
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(PreviewEmail.this);
				if (sp.getBoolean("sendsound", true)) {
					MediaPlayer mp = MediaPlayer.create(PreviewEmail.this,
							R.raw.delete);
					mp.start();
				}
				EmailsFragment.refreshAfterDeleteOutbox=true;
				finish();
			}

			if (file_url != null) {
				Toast.makeText(PreviewEmail.this, file_url, Toast.LENGTH_LONG)
						.show();
				finish();
			}

		}

	}

	private class DownloadTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			HttpResponse response = null;
			HttpGet httpGet = null;
			HttpClient mHttpClient = null;
			String s = "";

			try {
				if (mHttpClient == null) {
					mHttpClient = new DefaultHttpClient();
				}

				httpGet = new HttpGet(urls[0]);

				response = mHttpClient.execute(httpGet);
				s = EntityUtils.toString(response.getEntity(), "UTF-8");

			} catch (IOException e) {
				e.printStackTrace();
			}
			return s;
		}

		@Override
		protected void onPostExecute(String result) {
			html = result;
			//webView.loadDataWithBaseURL("file:///android_asset/", html,"text/html", "UTF-8", "");
			String localhtml = html.replaceAll("http://smartikymail.com/webservice/", "");
			// Log.v("html",localhtml);
			webView.loadDataWithBaseURL("file:///android_asset/", localhtml,"text/html", "UTF-8", "");
			
		}
	}

}
