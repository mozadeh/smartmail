package com.smartikyapps.smartmail.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartikyapps.smartmail.JSONParser;
import com.smartikyapps.smartmail.Log;
import com.smartikyapps.smartmail.R;
import com.smartikyapps.smartmail.ReadEmails;

public class ChangepasswordFragment extends Fragment {
	private Button changeButton, backButton;
	private TextView TV1, TV2, TV3;
	private EditText oldpassword, newpassword, confirmpassword;
	String username, password;
	private ProgressDialog pDialog;
	private static final String CHANGE_PASSWORD_URL = "http://www.smartikymail.com/webservice/changepassword.php";
	private static final String TAG_SUCCESS = "success";
	JSONParser jsonParser = new JSONParser();
	SharedPreferences sp;
	boolean goBack = false;
	ScrollView scrollView;

	public ChangepasswordFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final Typeface myTypefacebold = Typeface.createFromAsset(getActivity()
				.getAssets(), "Roboto-BoldCondensed.ttf");
		final Typeface myTypeface = Typeface.createFromAsset(getActivity()
				.getAssets(), "Roboto-Condensed.ttf");

		View rootView = inflater.inflate(R.layout.fragment_changepassword,
				container, false);

		changeButton = (Button) rootView.findViewById(R.id.change);
		backButton = (Button) rootView.findViewById(R.id.back);
		TV1 = (TextView) rootView.findViewById(R.id.textView1cp);
		TV2 = (TextView) rootView.findViewById(R.id.textView2cp);
		TV3 = (TextView) rootView.findViewById(R.id.textView3cp);
		oldpassword = (EditText) rootView.findViewById(R.id.oldpassword);
		newpassword = (EditText) rootView.findViewById(R.id.newpassword);
		confirmpassword = (EditText) rootView
				.findViewById(R.id.confirmpassword);
		scrollView = (ScrollView) rootView.findViewById(R.id.scrollViewpassword);

		changeButton.setTypeface(myTypefacebold);
		backButton.setTypeface(myTypefacebold);
		TV1.setTypeface(myTypeface);
		TV2.setTypeface(myTypeface);
		TV3.setTypeface(myTypeface);
		oldpassword.setTypeface(myTypeface);
		newpassword.setTypeface(myTypeface);
		confirmpassword.setTypeface(myTypeface);

		try {
			sp = PreferenceManager.getDefaultSharedPreferences(getActivity()
					.getBaseContext());
			username = sp.getString("username_from", "doesnt exist");
			password = sp.getString("password", "doesnt exist");
		} catch (Exception e) {
			Log.v("sp error", e.toString());
		}
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		

		oldpassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				scrollView.smoothScrollTo(0, 0);
			}
		});

		newpassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				scrollView.smoothScrollTo(0, (int) newpassword.getY());
			}
		});

		confirmpassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				scrollView.smoothScrollTo(0,
						((int) confirmpassword.getY() + (int) newpassword.getY()) / 2);
			}
		});
		
		

		changeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (oldpassword.getText().toString().equals(password)) {

					if (newpassword.getText().toString()
							.equals(confirmpassword.getText().toString())) {

						new ChangePassword(username, newpassword.getText()
								.toString()).execute();
						Editor edit = sp.edit();
						edit.putString("password", newpassword.getText()
								.toString());
						edit.commit();

					} else {
						Toast.makeText(getActivity(),
								"New passwords don't match", Toast.LENGTH_LONG)
								.show();
					}
				} else {
					Toast.makeText(getActivity(), "Old password is incorrect",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (goBack == false) {
					// keyboard is open
					InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(getActivity()
							.getCurrentFocus().getWindowToken(), 0);
					goBack = true;

				} else {
					// keyboard is closed
					ReadEmails.setBackFromOther(true);
					((ReadEmails) getActivity()).displayView(0);
				}
			}

		});

		return rootView;
	}

	class ChangePassword extends AsyncTask<String, String, String> {
		String username, newpassword;
		int success;

		public ChangePassword(String iusername, String inewpasword) {
			username = iusername;
			newpassword = inewpasword;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Changing Password...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("newpassword", newpassword));
				JSONObject json = jsonParser.makeHttpRequest(
						CHANGE_PASSWORD_URL, "POST", params);
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
				Toast.makeText(getActivity(), "Password has been changed",
						Toast.LENGTH_LONG).show();
			}

			if (file_url != null) {
				Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG)
						.show();
			}
			ReadEmails.setBackFromOther(true);
			((ReadEmails) getActivity()).displayView(0);

		}

	}

}
