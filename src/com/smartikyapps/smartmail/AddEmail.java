package com.smartikyapps.smartmail;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartikyapps.smartmail.R;
import com.google.analytics.tracking.android.EasyTracker;

public class AddEmail extends Activity {
	private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;
	private ImageView imgView, clearaddress, cleartitle, clearmessage;
	private Bitmap bitmap;
	private ProgressDialog dialog;
	Uri imageUri;
	// private Contact[] ContactList;
	String randomURL;
	private EditText title, message;
	ScrollView scrollView;
	String replyTitle, replyTo, postId;
	private MultiAutoCompleteTextView address;
	// private Button mSubmit, backButton;
	private Button camera, gallery;
	TextView TV1, TV2, TV3, TV4, TV5, header;
	// boolean goBack = false;

	boolean endActivity = false;
	// String howToSend = "";
	RadioButton radiobutton3, radiobutton0, radiobutton1, radiobutton2;
	RadioGroup radioGroup;
	String theme = "plain";
	HorizontalScrollView horizontalScrollView;
	// private ArrayAdapter<String> adapter;
	private static CustomSimpleAdapter adapter;
	private ProgressDialog pDialog;
	private AlertDialog alertDialog;
	private boolean mailivy = false;
	private String password, emailpassword;
	FrameLayout messageFL, titleFL;

	JSONParser jsonParser = new JSONParser();

	String post_username_from, fullname;
	String serviceProvider;
	String gcm_regID;

	private static final String POST_EMAIL_URL = "http://www.smartikymail.com/webservice/addemail.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.transition_down_to_up,
				R.anim.transition_left_to_right_out);
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		// getActionBar().hide();

		setContentView(R.layout.add_email);
		replyTitle = "";
		replyTo = "";
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		randomURL = UUID.randomUUID().toString();

		Typeface myTypeface = Typeface.createFromAsset(getAssets(),
				"Roboto-Condensed.ttf");
		final Typeface myTypefacebold = Typeface.createFromAsset(getAssets(),
				"Roboto-BoldCondensed.ttf");
		final Typeface myTypefaceboldItalic = Typeface.createFromAsset(
				getAssets(), "Roboto-BoldCondensedItalic.ttf");

		title = (EditText) findViewById(R.id.title);
		message = (EditText) findViewById(R.id.message);
		address = (MultiAutoCompleteTextView) findViewById(R.id.address);
		TV1 = (TextView) findViewById(R.id.textView1);
		TV2 = (TextView) findViewById(R.id.textView2);
		TV3 = (TextView) findViewById(R.id.TextView3);
		TV4 = (TextView) findViewById(R.id.textViewI);
		TV5 = (TextView) findViewById(R.id.TextView4);
		header = (TextView) findViewById(R.id.header);
		radiobutton3 = (RadioButton) findViewById(R.id.radio3);
		radiobutton0 = (RadioButton) findViewById(R.id.radio0);
		radiobutton1 = (RadioButton) findViewById(R.id.radio1);
		radiobutton2 = (RadioButton) findViewById(R.id.radio2);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.HorizontalScroll);
		imgView = (ImageView) findViewById(R.id.ImageView);
		scrollView = (ScrollView) findViewById(R.id.scrollviewaddemail);
		clearaddress = (ImageView) findViewById(R.id.clearaddress);
		cleartitle = (ImageView) findViewById(R.id.cleartitle);
		clearmessage = (ImageView) findViewById(R.id.clearmessage);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			postId = extras.getString("post_id");
			replyTitle = extras.getString("title");
			replyTo = extras.getString("username_from");
			// replyTo = replyTo.substring(6);

			try {
				if (!replyTitle.substring(0, 3).equals("Re:")) {

					replyTitle = "Re: " + replyTitle;
				}
			} catch (Exception e) {
				Log.e("Error in substring", e.toString());
			}
			message.requestFocus();
		}

		header.setTypeface(myTypefacebold);

		title.setTypeface(myTypeface);
		message.setTypeface(myTypeface);
		address.setTypeface(myTypeface);
		title.setText(replyTitle);
		address.setText(replyTo);

		TV1.setTypeface(myTypeface);
		TV2.setTypeface(myTypeface);
		TV3.setTypeface(myTypeface);
		TV4.setTypeface(myTypeface);
		TV5.setTypeface(myTypefacebold);
		radiobutton3.setTypeface(myTypefacebold);
		radiobutton0.setTypeface(myTypefacebold);
		radiobutton1.setTypeface(myTypefacebold);
		radiobutton2.setTypeface(myTypefacebold);

		// mSubmit = (Button) findViewById(R.id.submit);
		// backButton = (Button) findViewById(R.id.back);
		// addImage = (Button)findViewById(R.id.back);
		camera = (Button) findViewById(R.id.camera);
		gallery = (Button) findViewById(R.id.gallery);
		messageFL = (FrameLayout) findViewById(R.id.messageFL);
		titleFL = (FrameLayout) findViewById(R.id.titleFL);

		// mSubmit.setTypeface(myTypefacebold);
		// backButton.setTypeface(myTypefacebold);
		camera.setTypeface(myTypefacebold);
		gallery.setTypeface(myTypefacebold);

		dialog = new ProgressDialog(getBaseContext());

		/*
		 * final View activityRootView = findViewById(R.id.rootView);
		 * activityRootView.getViewTreeObserver().addOnGlobalLayoutListener( new
		 * OnGlobalLayoutListener() {
		 * 
		 * @Override public void onGlobalLayout() { int heightDiff =
		 * activityRootView.getRootView() .getHeight() -
		 * activityRootView.getHeight(); if (heightDiff > 100) { // if more than
		 * 100 pixels, its // probably a keyboard... goBack = false; } else {
		 * goBack = true; } } });
		 */

		setclear(cleartitle, title);
		setclear(clearmessage, message);
		setclearautocomplete(clearaddress, address);

		address.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				scrollView.smoothScrollTo(0, 0);
				if (hasFocus) {
					if (address.getText().length() > 0)
						clearaddress.setVisibility(View.VISIBLE);
				} else
					clearaddress.setVisibility(View.GONE);
			}
		});

		title.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				scrollView.smoothScrollTo(0, (int) titleFL.getY());
				if (hasFocus) {
					if (title.getText().length() > 0)
						cleartitle.setVisibility(View.VISIBLE);
				} else
					cleartitle.setVisibility(View.GONE);
			}
		});

		message.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				scrollView.smoothScrollTo(0,
						((int) messageFL.getY() + (int) titleFL.getY()) / 2);
				if (hasFocus) {
					if (message.getText().length() > 0)
						clearmessage.setVisibility(View.VISIBLE);
				} else
					clearmessage.setVisibility(View.GONE);
			}
		});

		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					// define the file-name to save photo taken by Camera
					// activity
					String fileName = "new-photo-name.jpg";
					// create parameters for Intent with filename
					ContentValues values = new ContentValues();
					values.put(MediaStore.Images.Media.TITLE, fileName);
					values.put(MediaStore.Images.Media.DESCRIPTION,
							"Image captured by camera");
					// imageUri is the current activity attribute, define and
					// save it for later usage (also in onSaveInstanceState)
					imageUri = getContentResolver().insert(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							values);
					// create new Intent
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
					startActivityForResult(intent, PICK_Camera_IMAGE);

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), e.getMessage(),
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), "pesare" + e.getMessage());
				}
			}
		});

		gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent gintent;
					if (Build.VERSION.SDK_INT < 19) {
						gintent = new Intent();
						gintent.setType("image/*");
						gintent.setAction(Intent.ACTION_GET_CONTENT);
					} else {
						gintent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					}

					startActivityForResult(
							Intent.createChooser(gintent, "Select Picture"),
							PICK_IMAGE);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), e.getMessage(),
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), "salam" + e.getMessage());
				}

			}
		});

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (radiobutton0.isChecked()) {

					horizontalScrollView.smoothScrollTo(0, 0);
				}
					
				if (radiobutton1.isChecked()) {

					horizontalScrollView.smoothScrollTo(radiobutton0.getRight()
							- radiobutton1.getWidth() / 2, 0);

				}
				if (radiobutton2.isChecked()) {

					horizontalScrollView.smoothScrollTo(radiobutton1.getRight()
							- radiobutton1.getWidth() / 2, 0);

				}
				

				if (radiobutton3.isChecked()) {

					horizontalScrollView.smoothScrollTo(
							radiobutton2.getRight(), 0);

				}

			}
		});

		// adapter = new
		// ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,new
		// ArrayList<String>());
		address.setThreshold(1);
		// address.setDropDownHeight(600);
		address.setAdapter(Login.GetContacts());
		address.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		SpannableStringBuilder SS = new SpannableStringBuilder("Back");
		SS.setSpan(new CustomTypefaceSpan("", myTypefacebold), 0, 4,
				Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		ab.setTitle(SS);
		ab.setBackgroundDrawable(new ColorDrawable(0xff006b49));
		ab.setDisplayShowHomeEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_delete:
			onBackPressed();
			// new DeleteEmail(postID,ivyInbox).execute();
			return true;
		case R.id.action_send:
			if ((title.getText().toString()).matches("")) {
				Toast.makeText(getApplicationContext(),
						"Please enter a title for your email",
						Toast.LENGTH_LONG).show();
			} else {
				if ((title.getText().toString()).length() > 30) {
					Toast.makeText(getApplicationContext(),
							"Please shorten email title", Toast.LENGTH_LONG)
							.show();
				} else {
					if ((message.getText().toString()).matches("")) {
						Toast.makeText(getApplicationContext(),
								"Please enter your message", Toast.LENGTH_LONG)
								.show();
					}

					else {
						String addressesstring = address.getText().toString();
						String[] addresses = addressesstring.replaceAll(
								"^[,\\s]+", "").split("[,\\s]+");
						boolean allvalid = true;
						for (int x = 0; x < addresses.length; x++) {
							Log.v("address", addresses[x]);
							Log.v("address", Boolean
									.toString(isEmailValid(addresses[x])));
							if (!isEmailValid(addresses[x])) {
								allvalid = false;
							}
						}
						if (allvalid) {

							SharedPreferences sp = PreferenceManager
									.getDefaultSharedPreferences(AddEmail.this);
							post_username_from = sp.getString("username_from",
									"doesnt exist");
							serviceProvider = sp.getString("service",
									"mail.mailivy.com");
							fullname = sp.getString("fullname", "null");
							gcm_regID = sp.getString("regId", "doesnt exist");
							password = sp.getString("password", "doesnt exist");
							mailivy = sp.getBoolean("mailivy", false);
							emailpassword = sp.getString("emailpassword",
									"doesnt exist");

							// AlertDialog.Builder alertDialogBuilder = new
							// AlertDialog.Builder(AddEmail.this);

							// dialog =
							// ProgressDialog.show(AddEmail.this,"Sending","Please wait...",true);

							String themechange = ((RadioButton) findViewById(radioGroup
									.getCheckedRadioButtonId())).getText()
									.toString();

							if (themechange.equals("Masterpiece"))
								theme = "masterpiece";
							if (themechange.equals("Plain"))
								theme = "Plain";
							if (themechange.equals("Whiteboard"))
								theme = "whiteboard";
							if (themechange.equals("Paper"))
								theme = "oldpaper";

							// dialog.dismiss();
							pDialog = new ProgressDialog(AddEmail.this);
							

							new PostEmail().execute();

							if (bitmap != null) {
								new ImageGalleryTask().execute();
							}

						}

						else {
							Toast.makeText(getApplicationContext(),
									"Please enter valid email addresses",
									Toast.LENGTH_LONG).show();
						}

					}
				}
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		/*
		 * if (goBack == false) { // keyboard is open InputMethodManager
		 * inputMethodManager = (InputMethodManager)
		 * getSystemService(Activity.INPUT_METHOD_SERVICE);
		 * inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
		 * .getWindowToken(), 0); scrollView.smoothScrollTo(0, 0); goBack =
		 * true;
		 * 
		 * } else {
		 */
		// keyboard is closed
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
				.getWindowToken(), 0);

		super.onBackPressed();
		overridePendingTransition(R.anim.transition_right_to_left,
				R.anim.transition_top_to_down_out);
		// }

	}

	class PostEmail extends AsyncTask<String, String, String> {
		int success;
		String[] email_addresses;
		String email_title;
		String email_message;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("Creating Message...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {

			String Addresses = address.getText().toString();
			email_addresses = Addresses.replaceAll("^[,\\s]+", "").split(
					"[,\\s]+");
			Addresses = Addresses.replaceAll("[,\\s]+$", "");
			email_title = title.getText().toString();
			email_message = message.getText().toString()
					.replaceAll("\\n", "<br />");

			// Retrieving Saved Username Data:

			// We need to change this:
			// String post_username_from = "tempfrom";
			String post_username_to = "tempto";

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			String strDate = df.format(new Date());
			JSONObject json = null;

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username_from",
						post_username_from));
				params.add(new BasicNameValuePair("username_to", Addresses));
				params.add(new BasicNameValuePair("title", email_title));
				params.add(new BasicNameValuePair("message", email_message));
				params.add(new BasicNameValuePair("sendmethod", "new"));

				params.add(new BasicNameValuePair("url_code", randomURL));
				params.add(new BasicNameValuePair("theme", theme));
				params.add(new BasicNameValuePair("created", strDate));
				params.add(new BasicNameValuePair("gcmregid", gcm_regID));
				params.add(new BasicNameValuePair("fullname", fullname));
				//EmailsFragment.newEmail = new Email(email_title, postID, email_message, userName, ivyinbox)

				if (bitmap != null)
					params.add(new BasicNameValuePair("photo_url", "uploads/"
							+ randomURL + "_image.png"));
				params.add(new BasicNameValuePair("opened", "unread"));
				json = jsonParser.makeHttpRequest(POST_EMAIL_URL,
						"POST", params);
				Log.v("sending","before success");
				
				success = json.getInt(TAG_SUCCESS);
				Log.v("sending","success= "+Integer.toString(success));
				if (success == 1) {
					Log.v("sending","1");
					Log.v("postid", ""+json.getString(TAG_MESSAGE));
					endActivity = true;
					Log.v("sending","calling send mail task");
					new SendMailTask(serviceProvider, post_username_from, emailpassword, email_title, email_message, fullname, randomURL, email_addresses).execute();
					return json.getString(TAG_MESSAGE);
				} else {
					success = 0;
					
					return json.getString(TAG_MESSAGE);
				}
				
			} catch (JSONException e) {
				try {
					Log.v("sending","WTF: "+ json.getString(TAG_MESSAGE));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			//if (bitmap == null)
				pDialog.dismiss();

			if (success == 1) {
				//Toast.makeText(AddEmail.this, "Email sent", Toast.LENGTH_LONG).show();
				EmailsFragment.refreshAfterSent=true;
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(AddEmail.this);
				if (sp.getBoolean("sendsound", true)) {
					MediaPlayer mp = MediaPlayer.create(
							getApplicationContext(), R.raw.sent);
					mp.start();
				}
			} else {
				Toast.makeText(AddEmail.this, "Email was not sent.",
						Toast.LENGTH_LONG).show();
			}

			onBackPressed();
			//overridePendingTransition(R.anim.transition_right_to_left,R.anim.transition_top_to_down_out);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri selectedImageUri = null;
		String filePath = null;
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				selectedImageUri = data.getData();

			}
			break;
		case PICK_Camera_IMAGE:
			if (resultCode == RESULT_OK) {
				// use imageUri here to access the image

				removeImage(getLastImageId());
				selectedImageUri = imageUri;
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Picture was not taken",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Picture was not taken",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		if (selectedImageUri != null) {
			try {
				// OI FILE Manager
				String filemanagerstring = selectedImageUri.getPath();

				// MEDIA GALLERY
				String selectedImagePath = getPath(selectedImageUri);

				if (selectedImagePath != null) {
					filePath = selectedImagePath;

				} else if (filemanagerstring != null) {
					filePath = filemanagerstring;

				} else {
					Toast.makeText(getApplicationContext(), "Unknown path",
							Toast.LENGTH_LONG).show();
					Log.e("Bitmap", "Unknown path");
				}

				if (filePath != null) {
					decodeFile(filePath);

				} else {
					bitmap = null;
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Internal error",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), "sosis" + e.getMessage());
			}
		}

	}

	class ImageGalleryTask extends AsyncTask<Void, Void, String> {
		@SuppressWarnings("unused")
		@Override
		protected String doInBackground(Void... unsued) {

			InputStream is;
			BitmapFactory.Options bfo;
			Bitmap bitmapOrg;
			ByteArrayOutputStream bao;
			Log.v("hello", "dude3");
			bfo = new BitmapFactory.Options();
			bfo.inSampleSize = 2;
			// bitmapOrg =
			// BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
			// + "/" + customImage, bfo);

			bao = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
			byte[] ba = bao.toByteArray();
			String ba1 = com.smartikyapps.smartmail.Base64.encodeBytes(ba);

			// String credentials = "smart3li" + ":" + "Alireza88";
			// String base64EncodedCredentials =
			// android.util.Base64.encodeToString(credentials.getBytes(),
			// (int)android.util.Base64.NO_WRAP);

			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("image", ba1));
			nameValuePairs.add(new BasicNameValuePair("cmd", "uploads/"
					+ randomURL + "_image.png"));
			// nameValuePairs.add(new BasicNameValuePair("Authorization",
			// "Basic " + base64EncodedCredentials));

			Log.v("log_tag", System.currentTimeMillis() + ".jpg");
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new
				// Here you need to put your server file address
				HttpPost(
						"http://www.smartikymail.com/webservice/UploadToServer.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				Log.v("log_tag", "In the try Loop");
			} catch (Exception e) {
				Log.v("log_tag", "Error in http connection " + e.toString());
			}
			return "Success";
			// (null);
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (pDialog.isShowing())
					pDialog.dismiss();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), "avazi" + e.getMessage());
			}
		}

	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);
		/*
		 * // The new size we want to scale to final int REQUIRED_SIZE = 1024;
		 * 
		 * // Find the correct scale value. It should be the power of 2. int
		 * width_tmp = o.outWidth, height_tmp = o.outHeight; int scale = 1;
		 * while (true) { if (width_tmp < REQUIRED_SIZE && height_tmp <
		 * REQUIRED_SIZE) break; width_tmp /= 2; height_tmp /= 2; scale *= 2; }
		 * 
		 * // Decode with inSampleSize BitmapFactory.Options o2 = new
		 * BitmapFactory.Options(); o2.inSampleSize = scale; bitmap =
		 * BitmapFactory.decodeFile(filePath, o2);
		 */
		Bitmap oldbitmap = null;
		Bitmap croppedbitmap;
		try {
			oldbitmap = BitmapFactory.decodeFile(filePath);

			if (oldbitmap.getWidth() >= oldbitmap.getHeight()) {

				croppedbitmap = Bitmap.createBitmap(oldbitmap,
						oldbitmap.getWidth() / 2 - oldbitmap.getHeight() / 2,
						0, (int) (oldbitmap.getHeight() * 0.916),
						(int) (oldbitmap.getHeight() * 1));

			} else {

				croppedbitmap = Bitmap.createBitmap(oldbitmap, 0,
						oldbitmap.getHeight() / 2 - oldbitmap.getWidth() / 2,
						(int) (oldbitmap.getWidth() * 0.916),
						(int) (oldbitmap.getWidth() * 1));

			}
			bitmap = Bitmap.createScaledBitmap(croppedbitmap, 300, 327, true);
			imgView.setImageBitmap(bitmap);
		} catch (OutOfMemoryError e) {
			Toast.makeText(getApplicationContext(), "picture is too large",
					Toast.LENGTH_LONG).show();
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

	@Override
	protected void onPause() {
		try {
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();

			if (alertDialog != null && alertDialog.isShowing())
				alertDialog.dismiss();
		} catch (Exception e) {
			Log.e("onpauseproblem", e.toString());
		}

		super.onPause();
	}

	private int getLastImageId() {
		final String[] imageColumns = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA };
		final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
		Cursor imageCursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns,
				null, null, imageOrderBy);
		if (imageCursor.moveToFirst()) {
			int id = imageCursor.getInt(imageCursor
					.getColumnIndex(MediaStore.Images.Media._ID));
			String fullPath = imageCursor.getString(imageCursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			return id;
		} else {
			return 0;
		}
	}

	private void removeImage(int id) {
		ContentResolver cr = getContentResolver();
		cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Media._ID + "=?", 
				new String[] { Long.toString(id) });
	}

	boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private String setemailbody(String ranURL, String username_from) {
		return "<html><head>"
				+ "	<title></title>"
				+ "</head>"
				+ "<body>"

				
				+ "<table style=\"border-collapse: separate!important; border-radius: 3px;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"
				+ "<tbody>"
				+ "<tr>"
				+ "<td style=\"background-color: #009a3d; color: #ffffff; font-family: Helvetica,Arial,sans-serif; font-size: 15px; font-weight: bold; line-height: 100%; padding-top: 18px; padding-right: 15px; padding-bottom: 15px; padding-left: 15px;\"><a style=\"color: white; text-decoration: none;\" href=\"http://smartikymail.com/webservice/openemail.php/"
				+ randomURL+"\" target=\"_blank\">Open Message</a></td>"
				+ "<td><a href=\"https://play.google.com/store/apps/details?id=com.smartikyapps.smartmail\" target=\"_blank\"><img style=\"width: 100px; min-height: 100px; padding-bottom: 10px;\" src=\"https://ci4.googleusercontent.com/proxy/6lZXq2jxAMgwlBoNhFkmivT0v4AjFPAl0cAF0dOKXGbsBIS15iYnLlZx-1rBxh0wmiI4UAIfrM6eHn3hq_BBLNBhOpe_PzhpsE9AfUs=s0-d-e1-ft#http://www.smartikymail.com/webservice/ic_launcher3.png\" alt=\"Smart Mail Logo\" /></a></td>"
				+ "</tr>"
				+ "</tbody>"
				+ "</table>"
				
				+ "<p><img src=\"http://api.screenshotmachine.com/?key=7c3592&amp;size=f&amp;format=gif&amp;url=http://smartikymail.com/webservice/screenshot.php/"
				+randomURL+"\" alt=\"message\" /></p>"
				
				
				
				
				+ "</body></html>";
		/*return "<html><head>"
				+ "	<title></title>"
				+ "</head>"
				+ "<body>"
				+ "	<table align=\"center\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%; background-color: rgb(242, 242, 242);\">"
				+ "		<tbody>"
				+ "			<tr>"
				+ "				<td style=\"text-align: center;\">"
				+ "					<p>"
				+ "						<a href=\"https://play.google.com/store/apps/details?id=com.smartikyapps.smartmail\"><img alt=\"Smart Mail Logo\" src=\"http://www.smartikymail.com/webservice/ic_launcher3.png\" style=\"width: 100px; height: 100px; padding-bottom:10px\" /></a></p>"
				+ "				</td>"
				+ "			</tr>"
				+ "			<tr>"
				+ "				<td style=\"text-align: center;\">"
				+ "					<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 80%; background-color:#ffffff;border-collapse:separate!important;border-radius:4px;padding-top:20px;padding-bottom:30px\">"
				+ "						<tbody>"
				+ "							<tr>"
				+ "								<td style=\"color:#606060!important;font-family:Helvetica,Arial,sans-serif;font-size:40px;font-weight:bold;letter-spacing:-1px;line-height:115%;margin:0;padding:0;text-align:center\">"
				+ "									You&#39;ve Received A Message</td>"
				+ "							</tr>"
				+ "							<tr>"
				+ "								<td style=\"color:#606060!important;font-family:Helvetica,Arial,sans-serif;font-size:18px;letter-spacing:-.5px;line-height:115%;margin:0;padding-bottom:5px;text-align:center\">"
				+ "									<p>"
				+ "										<span style=\"line-height: 115%;\">Click the button below to read your message from:</span></p>"
				+ "									<p>"
				+ "										<span style=\"font-weight: bold;\">"
				+ username_from
				+ "</span></p>"
				+ "								</td>"
				+ "							</tr>"
				+ "							<tr>"
				+ "								<td>"
				+ "									<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#009a3d;border-collapse:separate!important;border-radius:3px\">"
				+ "										<tbody>"
				+ "											<tr>"
				+ "												<td style=\"color:#ffffff;font-family:Helvetica,Arial,sans-serif;font-size:15px;font-weight:bold;line-height:100%;padding-top:18px;padding-right:15px;padding-bottom:15px;padding-left:15px\">"
				+ "													<a c=\"\" href=\"http://smartikymail.com/webservice/openemail.php/"
				+ ranURL
				+ "\" style=\"color:white;text-decoration: none\">Open Message</a></td>"
				+ "											</tr>"
				+ "										</tbody>"
				+ "									</table>"
				+ "								</td>"
				+ "							</tr>"
				+ "						</tbody>"
				+ "					</table>"
				+ "				</td>"
				+ "			</tr>"
				+ "			<tr>"
				+ "				<td style=\"text-align: center; color:#606060;font-family:Helvetica,Arial,sans-serif;font-size:13px;line-height:125%\">"
				+ "					<p>"
				+ "						&nbsp;</p>"
				+ "					<p>"
				+ "						Sent by</p>"
				+ "					<p>"
				+ "						<a href=\"https://play.google.com/store/apps/details?id=com.smartikyapps.smartmail\"><img alt=\"\" src=\"http://www.smartikymail.com/webservice/small-logo.png\" style=\"width: 100px; height: 25px;\" /></a></p>"
				+ "				</td>"
				+ "			</tr>"
				+ "		</tbody>"
				+ "	</table></body></html>";*/

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

	private void setclearautocomplete(final ImageView imageView,
			final AutoCompleteTextView edittext) {
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edittext.setText("");

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
