package com.smartikyapps.smartmail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class SingleEmail extends Activity {

	private static final String TAG_TITLE = "title";
	private static final String TAG_POST_ID = "post_id";
	private static final String TAG_USERNAME_FROM = "username_from";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_IVYINBOX = "ivyInbox";
	private static final String TAG_EMAILPASSWORD = "emailpassword";

	TextView TV2, TV3;
	WebView WV;
	// Button back, delete, reply;
	ScrollView SV;
	String post_username_from, emailpassword;
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	LinearLayout messageLayout;
	ProgressDialog pd;
	//IMAPFolder f;
	String messageContent;
	private boolean textIsHtml = false;

	// testing on Emulator:
	private static final String DELETE_EMAIL_URL = "http://www.smartikymail.com/webservice/ivy/deleteemail.php";
	String postID = " ";
	String title = " ";
	String message = " ";
	String from = " ";
	String ivyInbox = " ";
	String emailpass = " ";
	Typeface myTypefaceboldItalic, myTypefacebold, myTypeface;

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
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		// getActionBar().hide();
		setContentView(R.layout.single_email);
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

		pd = new ProgressDialog(this);
		pd.setMessage("Downloading Image");

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			postID = extras.getString(TAG_POST_ID);
			title = extras.getString(TAG_TITLE);
			message = extras.getString(TAG_MESSAGE).replaceAll("<br />",
					System.getProperty("line.separator"));
			from = extras.getString(TAG_USERNAME_FROM);
			ivyInbox = extras.getString(TAG_IVYINBOX);
			// emailpass = extras.getString(TAG_EMAILPASSWORD);
			// and get whatever type user account id is
		}

		SpannableStringBuilder SD = new SpannableStringBuilder(from);
		SD.setSpan(new CustomTypefaceSpan("", myTypeface), 0, from.length(),
				Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

		getActionBar().setTitle(SD);

		// TV1 = (TextView) findViewById(R.id.postid);
		TV2 = (TextView) findViewById(R.id.title);
		TV3 = (TextView) findViewById(R.id.messageTV);
		WV = (WebView) findViewById(R.id.messageWV);
		SV = (ScrollView) findViewById(R.id.scrollView);

		messageLayout = (LinearLayout) findViewById(R.id.messageLayout);

		// TV1.setText(postID);
		TV2.setText(title);
		// if (ivyInbox.equals("true")) {
		TV3.setVisibility(View.GONE);
		SV.setVisibility(View.GONE);
		WV.setVisibility(View.VISIBLE);
		new LoadMail().execute();
		// } else {
		// TV3.setText(message);
		// }
		TV2.setTypeface(myTypefaceboldItalic);
		TV3.setTypeface(myTypeface);
		// TV2.getBackground().setAlpha(51);
		messageLayout.getBackground().setAlpha(140);

		/*
		 * back = (Button) findViewById(R.id.back); reply = (Button)
		 * findViewById(R.id.reply);
		 * 
		 * back.setTypeface(myTypefacebold); reply.setTypeface(myTypefacebold);
		 * 
		 * back.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { onBackPressed();
		 * 
		 * } });
		 * 
		 * reply.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * Intent replyintent = new Intent(getBaseContext(), AddEmail.class);
		 * replyintent.putExtra(TAG_POST_ID, postID);
		 * replyintent.putExtra(TAG_TITLE, title);
		 * replyintent.putExtra(TAG_USERNAME_FROM, from);
		 * startActivity(replyintent); finish();
		 * 
		 * } });
		 */

		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.openmail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_reply:
			Intent replyintent = new Intent(getBaseContext(), AddEmail.class);
			replyintent.putExtra(TAG_POST_ID, postID);
			replyintent.putExtra(TAG_TITLE, title);
			replyintent.putExtra(TAG_USERNAME_FROM, from);
			startActivity(replyintent);
			finish();
			return true;
		case R.id.action_delete:
			deleteEmailDialog(postID, ivyInbox);
			// new DeleteEmail(postID,ivyInbox).execute();
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

	public class LoadMail extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog == null) {
				pDialog = new ProgressDialog(SingleEmail.this);
				pDialog.setMessage("Loading Message");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
			// pDialog = ProgressDialog.show(getActivity(), "",
			// "Loading Emails...", true, false);
			Log.v("dialog", "show");
			// }
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				Session mailSession = Session.getInstance(
						System.getProperties(), null);
				IMAPStore iMAPStore = (IMAPStore) mailSession.getStore("imaps");

				// iMAPStore.connect("mail.mailivy.com",
				// post_username_from+"@mailivy.com", emailpassword);
				// iMAPStore.connect("imap.gmail.com",
				// "hosein88@gmail.com","13371343");
				//iMAPStore.connect("imap.mail.yahoo.com","hosein_ghasem42@yahoo.com", "Arezoo88");
				//f = (IMAPFolder) iMAPStore.getFolder("INBOX");
				//f.open(Folder.READ_WRITE);
				// messageContent=multipart(f.getMessage(Integer.parseInt(postID)).getContent());
				// f.getMessage(Integer.parseInt(postID)).setFlag(Flags.Flag.SEEN,
				// true);
				// f.setFlags(new Message[]
				// {f.getMessage(Integer.parseInt(postID))}, new
				// Flags(Flags.Flag.SEEN), true);
				Object message = Login.f.getMessage(Integer.parseInt(postID))
						.getContent();
				Log.v("class", message.getClass().toString());
				if (message.getClass().equals(String.class))
					messageContent = (String) message;
				else
					messageContent = handleMultipart((Multipart) Login.f.getMessage(
							Integer.parseInt(postID)).getContent());
				// messageContent=test((Multipart)f.getMessage(Integer.parseInt(postID)).getContent());
				// f.getMessage(Integer.parseInt(postID)).setFlags(new
				// Flags(Flags.Flag.SEEN), true);

			} catch (Exception e) {
				// e.printStackTrace();
				Log.e("error in creation", e.toString());
			}
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			try {
				Log.v("dialog", "dismiss");
				if (pDialog != null)
					pDialog.dismiss();
				final String mimeType = "text/html";
				final String encoding = "UTF-8";
				// WV.getSettings().setLoadWithOverviewMode(true);
				// WV.getSettings().setUseWideViewPort(true);
				WV.loadDataWithBaseURL("", messageContent, mimeType, encoding,
						"");
				WV.getSettings().setBuiltInZoomControls(true);
				WV.getSettings().setDisplayZoomControls(false);
			} catch (Exception e) {
				Log.e("error", e.toString());
			}
		}

	}

	class DeleteEmail extends AsyncTask<String, String, String> {
		String post_id, mail_ivy;
		int success;

		public DeleteEmail(String postid, String mailivy) {
			post_id = postid;
			mail_ivy = mailivy;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SingleEmail.this);
			pDialog.setMessage("Deleting Message...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			/*
			 * List<NameValuePair> params = new ArrayList<NameValuePair>();
			 * params.add(new BasicNameValuePair("post_id", post_id));
			 * params.add(new BasicNameValuePair("username_value",
			 * post_username_from)); params.add(new
			 * BasicNameValuePair("emailpassword", emailpassword));
			 * params.add(new BasicNameValuePair("mailivy", mail_ivy)); //
			 * Log.v("delete email mailivy", mail_ivy); JSONObject json =
			 * jsonParser.makeHttpRequest(DELETE_EMAIL_URL, "POST", params);
			 * success = json.getInt(TAG_SUCCESS); // Log.v("delete email",
			 * json.getString(TAG_MESSAGE));
			 */

			try {
				if(!Login.f.isOpen()) Login.f.open(Folder.READ_WRITE);
				Login.f.getMessage(Integer.parseInt(post_id)).setFlag(
						Flags.Flag.DELETED, true);
				Login.f.close(true);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				Log.e("Delete Error", e.getMessage().toString());
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				Log.e("Delete Error", e.getMessage().toString());
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(SingleEmail.this);
			if (sp.getBoolean("sendsound", true)) {
				MediaPlayer mp = MediaPlayer.create(SingleEmail.this,
						R.raw.delete);
				mp.start();
				}
				EmailsFragment.refreshAfterDeleteInbox=true;

				finish();
			

			if (file_url != null) {
				Toast.makeText(SingleEmail.this, file_url, Toast.LENGTH_LONG)
						.show();
				finish();
			}

		}

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

	private String multipart(Object content) {
		// Object content = message.getContent();
		String returnvalue = "";
		if (content instanceof Multipart) {
			Multipart mp = (Multipart) content;
			try {
				for (int i = 0; i < mp.getCount(); i++) {
					BodyPart bp = mp.getBodyPart(i);
					if (Pattern
							.compile(Pattern.quote("text/html"),
									Pattern.CASE_INSENSITIVE)
							.matcher(bp.getContentType()).find()) {
						// found html part
						returnvalue += (String) bp.getContent();
					} else {
						returnvalue += (String) bp.getContent();// some other
																// bodypart...
					}
				}
			} catch (PatternSyntaxException e) {
				// TODO Auto-generated catch block
				Log.e("error", e.getMessage());
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				Log.e("error", e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("error", e.getMessage());
			}
		}
		return returnvalue;
	}

	private String handleMultipart(Multipart multipart)
			throws MessagingException, IOException {
		String message = "";
		Part part;
		for (int i = 0, n = multipart.getCount(); i < n; i++) {
			// message+="PART: "+i+" ="+getText(multipart.getBodyPart(i))+";";
			// multipart.getBodyPart(i).
			part = multipart.getBodyPart(i);
			if (getText(part) != null)
				message += getText(part);
		}
		return message;
	}

	private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/html")) {
			String s = (String) p.getContent();
			// textIsHtml = p.isMimeType("text/html");
			Log.v("multipar", "text/html 2 " + p.getContentType());
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					Log.v("multipar", "text/plain");
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					Log.v("multipar", "text/html");
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Log.v("multipar", "multipart/*");
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

}
