package com.smartikyapps.smartmail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.UIDFolder.FetchProfileItem;
import javax.mail.internet.InternetAddress;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class EmailsFragment extends ListFragment {

	// private static ProgressDialog pDialog;
	private static final String READ_EMAILS_URL = "http://www.smartikymail.com/webservice/ivy/emailstest.php";
	private static final String DELETE_EMAIL_URL = "http://www.smartikymail.com/webservice/ivy/deleteemail.php";
	private static final String OPEN_EMAIL_URL = "http://www.smartikymail.com/webservice/ivy/openemail.php";
	private static final String MODIFY_EMAIL_URL = "http://www.smartikymail.com/webservice/editemail.php";

	// JSON IDS:
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_TITLE = "title";
	private static final String TAG_POSTS = "posts";
	private static final String TAG_POST_ID = "post_id";
	private static final String TAG_USERNAME_FROM = "username_from";
	private static final String TAG_USERNAME_TO = "username_to";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_OPENED = "opened";
	private static final String TAG_IMGURL = "photo_url";
	private static final String TAG_URL = "url_code";
	private static final String TAG_THEME = "theme";
	private static final String TAG_FIRSTREAD = "firstread";
	private static final String TAG_IVYINBOX = "ivyInbox";
	PullAndLoadListView listView;
	private boolean loadmore = true;
	boolean refreshing = false;
	boolean loadingmore = false;

	private JSONArray mEmails = null;
	// private LayoutAnimationController controller;
	private ArrayList<Email> mEmailList;
	private static boolean visibility = true;
	public static boolean refreshAfterDeleteInbox, refreshAfterDeleteOutbox,
			refreshAfterSent = false;
	public static int deletePosition = 0;

	protected static final String TAG = "EmailsFragment";
	public static final int INBOX = 1;
	Session mailSession; 
	IMAPStore iMAPStore;
	public static final int OUTBOX = 0;
	private int mainoffset = 0; 
	private int mainlimit = 9;
	public static final String MAIL_SELECTION = "MailSelection";
	EmailAdapter adapter;
	TextView nosentmessages;
	int emailSelection = 0;
	public static int loadingThreads = 0;
	JSONParser jsonParser = new JSONParser();

	// private String postID, postURL, hMessage, hUsernamefrom, hTitle;
	private int hRead;
	Typeface myTypefaceboldItalic;
	Typeface myTypefacebold, myTypeface;
	private String post_username_from;
	private String emailpassword = "doesnt exist";

	public static EmailsFragment newInstance(int mailSelection) {
		EmailsFragment f = new EmailsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(MAIL_SELECTION, mailSelection);
		f.setArguments(bundle);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		emailSelection = getArguments().getInt(MAIL_SELECTION);
		try {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getActivity().getBaseContext());
			post_username_from = sp.getString("username_from", "doesnt exist");
			emailpassword = sp.getString("emailpassword", "doesnt exist");
			Log.v("emailpas", emailpassword);
			mEmailList = new ArrayList<Email>();
		} catch (Exception e) {
			Log.v("sp error", e.toString());
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.emails_fragment, container, false);

		// controller =
		// AnimationUtils.loadLayoutAnimation(getActivity(),R.anim.list_layout_controller);

		myTypefaceboldItalic = Typeface.createFromAsset(getActivity()
				.getApplicationContext().getAssets(),
				"Roboto-BoldCondensedItalic.ttf");
		myTypefacebold = Typeface.createFromAsset(getActivity()
				.getApplicationContext().getAssets(),
				"Roboto-BoldCondensed.ttf");
		myTypeface = Typeface.createFromAsset(getActivity()
				.getApplicationContext().getAssets(), "Roboto-Condensed.ttf");
		nosentmessages = (TextView) view.findViewById(R.id.nosentmessages);
		nosentmessages.setTypeface(myTypefaceboldItalic);
		listView = (PullAndLoadListView) view.findViewById(android.R.id.list);
		listView.setDivider(null);
		// listView.setLayoutAnimation(controller);

		nosentmessages.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newmail = new Intent(getActivity(), AddEmail.class);
				startActivity(newmail);
			}
		});

		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				if (loadmore) {
					Log.v("load", "loading");
					loadingmore = true;
					// Log.v("listnum", "in load more");
					new LoadEmails().execute();
				}
				// Log.v("load", "not loading");
				// Log.v("listnum", "not in load more");
			}
		});

		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				refresh();
			}
		});

		if (GetVisibility()) {

			mainoffset = 0;
			loadmore = true;
			loadingmore = false;
			// listView.setLayoutAnimation(controller);
			new LoadEmails().execute();
		}

		return view;
	}

	@Override
	public void onResume() {

		super.onResume();
		if (refreshAfterDeleteInbox && emailSelection == 1) {
			// Log.v("delete", "salam " + deletePosition);
			refreshAfterDeleteInbox = false;
			removeItem(deletePosition);
		}
		if (refreshAfterDeleteOutbox && emailSelection == 0) {
			// Log.v("delete", "salam " + deletePosition);
			refreshAfterDeleteOutbox = false;
			removeItem(deletePosition);
		}
		if (refreshAfterSent && emailSelection == 0) {
			// Log.v("delete", "salam " + deletePosition);
			refreshAfterSent = false;
			mainoffset = 0;
			loadmore = true;
			loadingmore = false;
			// listView.setLayoutAnimation(controller);
			new LoadEmails().execute();
		}
		/*
		 * if (GetVisibility()) {
		 * 
		 * mainoffset = 0; loadmore = true; loadingmore = false;
		 * listView.setLayoutAnimation(controller); new LoadEmails().execute();
		 * }
		 */

	}

	/*
	 * public void update() { new LoadEmails().execute(); }
	 */

	void setLayout() {

		if (emailSelection == 0) // outbox
			if (mEmails.length() > 0)
				nosentmessages.setVisibility(View.GONE);
			else
				nosentmessages.setVisibility(View.VISIBLE);

		if (emailSelection == 1)
			try {
				if (Login.f.getMessageCount() > 0)
					nosentmessages.setVisibility(View.GONE);
				else
					nosentmessages.setVisibility(View.VISIBLE);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				Log.e("set layout", "error");
			}

	}

	public void fillInbox(int offset, int limit, ArrayList<Email> templist) {
		Log.v("stuck", "here-1");

		try {
			int endToLoad = 0;
			if (refreshing)
				templist.clear();
			if ((offset + limit) >= Login.f.getMessageCount()) {
				loadmore = false;
				listView.setComplete(true);
				endToLoad = Login.f.getMessageCount();
			} else {
				loadmore = true;
				endToLoad = offset + limit;
			}
			Log.v("stuck", "0");
			//if(iMAPStore!=null)
			//Log.v("stuck", "IMAPStore connected: "+Login.iMAPStore.isConnected());
			
			//if (!firstTime){
				openEmailFolder(post_username_from, emailpassword);
			//	mainlimit=20;
			//}
			//else
			//	firstTime = false;  
			 
			Message[] messages = Login.f.getMessages(Login.f.getMessageCount()
					- endToLoad + 1, Login.f.getMessageCount() - offset);
			FetchProfile fp = new FetchProfile();
			Log.v("stuck", "1");
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfileItem.FLAGS);
			fp.add(FetchProfileItem.CONTENT_INFO);
			Log.v("stuck", "2");
			fp.add("X-mailer");
			Log.v("stuck", "3");
			Login.f.fetch(messages, fp);
			Log.v("stuck", "here0");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			for (int i = offset; i < endToLoad; i++) {
				Log.v("stuck", "here+" + i);
				// HashMap<String, String> map = new HashMap<String, String>();
				Email email;
				// gets the content of each tag
				Message message = Login.f.getMessage(Login.f.getMessageCount()
						- i);
				Log.v("date", message.getSentDate().toString());
				email = new Email(message.getSubject(),
						Integer.toString(Login.f.getMessageCount() - i),
						"content", post_username_from, "0");
				Address[] froms = message.getFrom();
				String emailaddress = froms == null ? null
						: ((InternetAddress) froms[0]).getAddress();
				email.setUserName(emailaddress);
				String strDate = df.format(message.getSentDate());
				String createdDate = setmailstatus("sent", strDate, "sent");
				email.setRead(createdDate);
				if (message.isSet(Flags.Flag.SEEN)) {
					email.setOpenstatus("read");
					// Log.v("inbox", "read");
				} else {
					email.setOpenstatus("unread");
					// Log.v("inbox", "unread");
				}
				// email.setOpenstatus(c.getString(TAG_OPENED));
				templist.add(email);
			}
			mainoffset += limit;
		} catch (Exception e) {
			// e.printStackTrace();
			Log.e("error in creation", e.toString());
		}

	}

	public void openEmailFolder(String username, String password) {
		try {
			mailSession = Session.getInstance(System.getProperties(),
					null);
			
			iMAPStore = (IMAPStore) mailSession.getStore("imaps");
			
			if (username.contains("@gmail.com")) {
				iMAPStore.connect("imap.gmail.com", username, password);
			} else if (username.contains("@yahoo.com")) {
				iMAPStore.connect("imap.mail.yahoo.com", username, password);

			} else {
				iMAPStore.connect("mail.mailivy.com",
						username + "@mailivy.com", password);
			}

			Login.f = (IMAPFolder) iMAPStore.getFolder("INBOX");
			Login.f.open(Folder.READ_WRITE);

		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			Log.e("error", "Messaging Exception");
		}

	}

	public void updateJSONdata(int offset, int limit, ArrayList<Email> templist) {

		JSONParser jParser = new JSONParser();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username_value", post_username_from));
		params.add(new BasicNameValuePair("emailpassword", emailpassword));
		params.add(new BasicNameValuePair("Outbox", "1"));

		JSONObject json = jParser.makeHttpRequest(READ_EMAILS_URL, "GET",
				params);
		try {
			int endToLoad = 0;
			if (refreshing)
				templist.clear();
			mEmails = json.getJSONArray(TAG_POSTS);

			if ((offset + limit) >= mEmails.length()) {
				loadmore = false;
				listView.setComplete(true);
				endToLoad = mEmails.length();
			} else {
				loadmore = true;
				endToLoad = offset + limit;
			}

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			for (int i = offset; i < endToLoad; i++) {
				// HashMap<String, String> map = new HashMap<String, String>();
				Email email;
				// gets the content of each tag
				JSONObject c = mEmails.getJSONObject(i);
				String postid = c.getString(TAG_POST_ID);
				String title = c.getString(TAG_TITLE);
				String content = c.getString(TAG_MESSAGE);
				String username = c.getString(TAG_USERNAME_TO);
				String ivyinbox = c.getString(TAG_IVYINBOX);
				email = new Email(title, postid, content, username, ivyinbox);
				String imageURL = c.getString(TAG_IMGURL);
				String URL = c.getString(TAG_URL);
				String theme = c.getString(TAG_THEME);
				String firstread = c.getString(TAG_FIRSTREAD);
				String opened = c.getString(TAG_OPENED);
				String mailstatus = setmailstatus(opened, firstread, "read");
				email.setImgURL(imageURL);
				email.setURL(URL);
				email.setTheme(theme);
				email.setRead(mailstatus);
				email.setOpenstatus(opened);
				templist.add(email);

				// mainoffset = limit;
			}
			mainoffset += limit;
		} catch (Exception e) {
			// e.printStackTrace();
			Log.e("error in creation", e.toString());
		}

	}

	public class LoadEmails extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {

			// if (emailSelection==0) outboxLoading=true;
			// if (emailSelection==1) inboxLoading=true;

			if (loadingThreads == 0 && !loadingmore) {
				loadingThreads += 1;
				// ReadEmails.pDialog= new
				// ProgressDialog(getActivity().getApplicationContext());
				try {
					((ReadEmails) getActivity()).CreateDialog();
					ReadEmails.pDialog.setMessage("Loading Emails...");
					ReadEmails.pDialog.setIndeterminate(false);
					ReadEmails.pDialog.setCancelable(true);
					ReadEmails.pDialog.show();
				} catch (Exception e) {
					Log.e("EmailFragment", "create dialog error");
				}
				Log.v("loading thread", "show");
			} else {
				loadingThreads += 1;
			}
			Log.v("loading thread", Integer.toString(loadingThreads));

			super.onPreExecute();
			Log.v("load", "in loading");
			if (!loadingmore) {
				Log.v("load", "loading more");
				if (adapter != null) {
					mEmailList.clear();
					adapter.notifyDataSetInvalidated();
				}
			}


		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			Log.v("load", "mainoffset: " + Integer.toString(mainoffset)
					+ " mainlimit: " + Integer.toString(mainlimit));
			if (emailSelection == 0)
				// outbox
				updateJSONdata(mainoffset, mainlimit, mEmailList);
			else
				// inbox
				fillInbox(mainoffset, mainlimit, mEmailList);
			return null;

		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			listView.onLoadMoreComplete();
			Log.v("loading thread", Integer.toString(loadingThreads));
			loadingThreads -= 1;
			ReadEmails.pDialog.dismiss();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			loadingThreads -= 1;
			Log.v("loading thread", Integer.toString(loadingThreads) + "hello");

			try {
				if (loadingThreads == 0) {
					Log.v("loading thread", "before dismiss");
					ReadEmails.pDialog.dismiss();
					Log.v("loading thread", "after dismiss");
				}

				if (emailSelection == 0) // outbox
					if (mEmails.length() > 0)
						updateList();

				if (emailSelection == 1) // inbox
					if (Login.f.getMessageCount() > 0)
						updateList();

				setLayout();
			} catch (Exception e) {
				Log.e("error", "error in post execute");
			}

			if (refreshing) {
				listView.onRefreshComplete();
				refreshing = false;
			}
			if (loadingmore) {
				listView.onLoadMoreComplete();
				Log.v("loading", "more");
				loadingmore = false;
			}
		}
	}

	class MakeEdits extends AsyncTask<String, String, String> {
		String post_id;
		int success;
		String changed_Message = "";

		public MakeEdits(String postid, String changedMessage) {
			post_id = postid;
			changed_Message = changedMessage;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// pDialog = new ProgressDialog(getActivity());
			ReadEmails.pDialog.setMessage("Editing Message...");
			ReadEmails.pDialog.setIndeterminate(false);
			ReadEmails.pDialog.setCancelable(true);
			ReadEmails.pDialog.show();
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
			ReadEmails.pDialog.dismiss();
			if (success == 1) {
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(getActivity());
				if (sp.getBoolean("sendsound", true)) {
					MediaPlayer mp = MediaPlayer.create(getActivity(),
							R.raw.sent);
					mp.start();
				}
				mEmailList.clear();
				mainoffset = 0;
				loadmore = true;
				loadingmore = false;
				new LoadEmails().execute();
			}

			if (file_url != null) {
				Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

	class OpenEmail extends AsyncTask<String, String, String> {
		String post_id, mail_ivy;
		int success;

		public OpenEmail(String postid, String mailivy) {
			post_id = postid;
			mail_ivy = mailivy;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
				JSONObject json = jsonParser.makeHttpRequest(OPEN_EMAIL_URL,
						"POST", params);
				success = json.getInt(TAG_SUCCESS);
				// Log.v("delete email", json.getString(TAG_MESSAGE));

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {

			if (file_url != null) {
				Log.v("opened email", file_url);
			}

		}

	}

	class DeleteEmailInbox extends AsyncTask<String, String, String> {
		String post_id, mail_ivy;
		int success, position;

		public DeleteEmailInbox(String postid, String mailivy, int positionC) {
			post_id = postid;
			mail_ivy = mailivy;
			position = positionC;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// pDialog = new ProgressDialog(getActivity());
			Log.v("remove", "position:"+position); 
			Log.v("remove", "post id:"+post_id); 
			ReadEmails.pDialog.setMessage("Deleting Message...");
			ReadEmails.pDialog.setIndeterminate(false);
			ReadEmails.pDialog.setCancelable(true);
			ReadEmails.pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			try {
				if (!Login.f.isOpen())
					Login.f.open(Folder.READ_WRITE);
				Login.f.getMessage(Integer.parseInt(post_id)).setFlag(
						Flags.Flag.DELETED, true);
				Login.f.close(true);
				// f.open(Folder.READ_WRITE);
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
			ReadEmails.pDialog.dismiss();

			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			if (sp.getBoolean("sendsound", true)) {
				MediaPlayer mp = MediaPlayer
						.create(getActivity(), R.raw.delete);
				mp.start();
			}
			// mEmailList.remove(position - 1);
			// adapter.notifyDataSetChanged();
			removeItem(position);

			if (file_url != null) {
				Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

	public void removeItem(int position) {
		//adapter.remove(mEmailList.get(position - 1));	
		for (int x=position;x>0;x--){
			int newpostid=Integer.parseInt(mEmailList.get(x-1).getpostID())-1;
			mEmailList.get(x-1).setpostID(Integer.toString(newpostid));
		}
		mEmailList.remove(position - 1);
		adapter.notifyDataSetChanged();
	}

	class DeleteEmail extends AsyncTask<String, String, String> {
		String post_id, mail_ivy;
		int success, position;

		public DeleteEmail(String postid, String mailivy, int positionC) {
			post_id = postid;
			mail_ivy = mailivy;
			position = positionC;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// pDialog = new ProgressDialog(getActivity());
			ReadEmails.pDialog.setMessage("Deleting Message...");
			ReadEmails.pDialog.setIndeterminate(false);
			ReadEmails.pDialog.setCancelable(true);
			ReadEmails.pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("post_id", post_id));
				params.add(new BasicNameValuePair("username_value",
						post_username_from));
				params.add(new BasicNameValuePair("mailivy", mail_ivy));
				params.add(new BasicNameValuePair("emailpassword",
						emailpassword));
				// Log.v("delete email mailivy", mail_ivy);
				JSONObject json = jsonParser.makeHttpRequest(DELETE_EMAIL_URL,
						"POST", params);
				success = json.getInt(TAG_SUCCESS);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			ReadEmails.pDialog.dismiss();
			if (success == 1) {
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(getActivity());
				if (sp.getBoolean("sendsound", true)) {
					MediaPlayer mp = MediaPlayer.create(getActivity(),
							R.raw.delete);
					mp.start();
				}
				mEmailList.remove(position - 1);
				adapter.notifyDataSetChanged();
				// refresh();
			}

			if (file_url != null) {
				Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

	private String setmailstatus(String opened, String firstread, String Outbox) {
		if (((opened.equals("unread") || firstread
				.equals("0000-00-00 00:00:00"))) && Outbox.equals("read"))
			return opened;

		else {
			String status = " ";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			// String rightnow = df.format(new Date());
			Date rightnow = new Date();
			Date convertedDate = new Date();
			try {
				convertedDate = df.parse(firstread);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("time exception", e.toString());
			}

			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();
			calendar1.setTime(convertedDate);
			calendar2.setTime(rightnow);
			calendar1.setTimeZone(TimeZone.getTimeZone("UTC"));
			calendar2.setTimeZone(TimeZone.getTimeZone("UTC"));
			long milliseconds1 = calendar1.getTimeInMillis();
			long milliseconds2 = calendar2.getTimeInMillis();
			long diff = milliseconds2 - milliseconds1;
			// long diffSeconds = diff / 1000;
			long diffMinutes = diff / (60 * 1000);
			long diffHours = diff / (60 * 60 * 1000);
			long diffDays = diff / (24 * 60 * 60 * 1000);

			if (diffDays > 365)
				status = Outbox + " more than a year ago";
			else if (Math.floor(diffDays) >= 60)
				status = Outbox + " " + (int) Math.floor(diffDays / 30)
						+ " months ago";

			else if (Math.floor(diffDays) >= 30)
				status = Outbox + " a month ago";

			else if (Math.floor(diffDays) > 1)
				status = Outbox + " " + (int) Math.floor(diffDays)
						+ " days ago";

			else if (Math.floor(diffDays) == 1)
				status = Outbox + " yesterday";

			else if (Math.floor(diffHours) > 1)
				status = Outbox + " " + (int) Math.floor(diffHours)
						+ " hours ago";

			else if (Math.floor(diffHours) == 1)
				status = Outbox + " an hour ago";

			else if (Math.floor(diffMinutes) > 1)
				status = Outbox + " " + (int) Math.floor(diffMinutes)
						+ " minutes ago";

			else
				status = Outbox + " a minute ago";

			return status;
		}
	}

	private void updateList() {
		// needs fixing here probably!!
		if (!loadingmore) {
			adapter = new EmailAdapter(getActivity().getApplicationContext(),
					R.layout.single_post, mEmailList);
			listView.setAdapter(adapter);

		}

		adapter.notifyDataSetChanged();
		if (!loadingmore)
			listView.startLayoutAnimation();

		if (emailSelection == 1)
			nosentmessages.setText("You have no messages \n in your inbox");

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					final int position, long id) {

				EmailHolder holder = ((EmailHolder) view.getTag());
				final String postID = holder.getpostID();
				final String postURL = holder.getURL();
				final String hMessage = holder.getMessage();
				final String hTitle = holder.getTitle().getText().toString();
				final String hUsernamefrom = holder.getUserName().getText()
						.toString();
				final String hivyInbox = holder.getIvyInbox();
				Log.v("remove", "post id(onlick):"+postID); 
				String beginRead = holder.getRead().getText().toString();
				if (beginRead.startsWith("read"))
					hRead = 0; // read
				else
					hRead = 1; // unread
				String[] messageOptionsOutboxRead = { "Preview Message",
						"Delete Message" };
				String[] messageOptionsOutboxUnread = { "Preview Message",
						"Edit Message", "Delete Message" };
				String[] messageOptionsInbox = { "Open Message", "Reply To",
						"Delete Message" };

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				switch (emailSelection) {
				case 0: // outbox
					switch (hRead) {
					case 0: // read
						alertDialogBuilder
								.setIcon(0)
								.setCancelable(true)
								.setItems(messageOptionsOutboxRead,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												switch (which) {
												case 0:
													previewEmail(postURL,
															hUsernamefrom,
															postID, hivyInbox,
															hMessage, hTitle,
															hRead);
													deletePosition = position;
													break;
												case 1:
													deleteEmailDialog(view,
															postID, hivyInbox,
															position, 0);
													break;
												}

											}
										});
						break;

					case 1: // unread
						alertDialogBuilder
								.setIcon(0)
								.setCancelable(true)
								.setItems(messageOptionsOutboxUnread,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												switch (which) {
												case 0:
													previewEmail(postURL,
															hUsernamefrom,
															postID, hivyInbox,
															hMessage, hTitle,
															hRead);
													deletePosition = position;
													break;
												case 1:
													editEmail(view, hMessage,
															postID);

													break;
												case 2:
													deleteEmailDialog(view,
															postID, hivyInbox,
															position, 0);
													break;
												}

											}
										});
						break;
					}
					break;

				case 1: // inbox

					alertDialogBuilder
							.setIcon(0)
							.setCancelable(true)
							.setItems(messageOptionsInbox,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											switch (which) {
											case 0:
												// new OpenEmail(postID,
												// hivyInbox).execute();
												mEmailList.get(position - 1)
														.setOpenstatus("read");
												adapter.notifyDataSetChanged();
												deletePosition = position;
												Intent intent = new Intent(
														getActivity(),
														SingleEmail.class);
												intent.putExtra(TAG_POST_ID,
														postID);
												intent.putExtra(TAG_TITLE,
														hTitle);
												intent.putExtra(TAG_MESSAGE,
														hMessage);
												intent.putExtra(
														TAG_USERNAME_FROM,
														hUsernamefrom);
												intent.putExtra(TAG_IVYINBOX,
														hivyInbox);
												startActivity(intent);
												break;

											case 1:
												Intent replyintent = new Intent(
														getActivity(),
														AddEmail.class);
												replyintent.putExtra(
														TAG_POST_ID, postID);
												replyintent.putExtra(TAG_TITLE,
														hTitle);
												replyintent.putExtra(
														TAG_USERNAME_FROM,
														hUsernamefrom);
												startActivity(replyintent);
												break;
											case 2:
												deleteEmailDialog(view, postID,
														hivyInbox, position, 1);
												break;
											}

										}

									});
					break;

				}

				AlertDialog alertDialog = alertDialogBuilder.create();

				alertDialog.show();

			}
		});

	}

	public void editEmail(View view, String original, final String postID) {

		AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
				getActivity());

		final InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		final EditText input = new EditText(view.getContext());
		input.setLines(8);
		input.setGravity(Gravity.TOP);
		input.setTextColor(Color.BLACK);
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
								imm.hideSoftInputFromWindow(getActivity()
										.getCurrentFocus().getWindowToken(), 0);
								new MakeEdits(postID, input.getText()
										.toString().replaceAll("\\n", "<br />"))
										.execute();

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								imm.hideSoftInputFromWindow(getActivity()
										.getCurrentFocus().getWindowToken(), 0);
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

	public void previewEmail(String postURL, String userTo, String postID,
			String hivyInbox, String message, String title, int read) {
		/*
		 * Intent i = new Intent(Intent.ACTION_VIEW); i.setData(Uri
		 * .parse("http://www.smartikymail.com/webservice/previewemail.php/" +
		 * postURL)); startActivity(i);
		 */

		Intent intent = new Intent(getActivity(), PreviewEmail.class);
		intent.putExtra(TAG_URL, postURL);
		intent.putExtra(TAG_USERNAME_TO, userTo);
		intent.putExtra(TAG_POST_ID, postID);
		intent.putExtra(TAG_USERNAME_FROM, post_username_from);
		intent.putExtra(TAG_IVYINBOX, hivyInbox);
		intent.putExtra(TAG_OPENED, read);
		intent.putExtra(TAG_MESSAGE, message);
		intent.putExtra(TAG_TITLE, title);
		startActivity(intent);

	}

	public void deleteEmailDialog(View view, final String postID,
			final String mailivy, final int position, final int inoroutbox) {
		AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
				getActivity());

		alertDialogBuilder1
				.setMessage("Are you sure you want to delete this email?")
				.setCancelable(true)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (inoroutbox == 0)
									new DeleteEmail(postID, mailivy, position)
											.execute();
								else
									new DeleteEmailInbox(postID, mailivy,
											position).execute();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});

		AlertDialog alertDialog1 = alertDialogBuilder1.create();

		try {
			alertDialog1.show();
		} catch (Exception e) {
			Log.e("Emails Fragment Acitivity", "showing dialog error");
		}

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

	void refresh() {
		mainoffset = 0;
		refreshing = true;
		loadmore = true;
		// ReadEmails.pDialog = null;
		loadingmore = false;
		// listView.onLoadMoreComplete();
		new LoadEmails().execute();
		// listView.setLayoutAnimation(controller);
		//listView.setComplete(false);

	}

	public static boolean GetVisibility() {
		return visibility;
	}

	public static void SetVisibility(boolean visibile) {
		visibility = visibile;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if (ReadEmails.pDialog != null)
			ReadEmails.pDialog.dismiss();
		super.onPause();
	}

}
