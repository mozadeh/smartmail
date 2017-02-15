package com.smartikyapps.smartmail.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartikyapps.smartmail.Log;
import com.smartikyapps.smartmail.MailSender;
import com.smartikyapps.smartmail.R;
import com.smartikyapps.smartmail.ReadEmails;

public class FeedbackFragment extends Fragment {

	private Button mSubmit, backButton;
	private TextView thanks;
	private EditText feedback;
	String post_username_from = "";
	private String emailpassword, serviceProvider;
	boolean goBack = false;

	public FeedbackFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_feedback, container,
				false);

		final Typeface myTypefacebold = Typeface.createFromAsset(getActivity()
				.getAssets(), "Roboto-BoldCondensed.ttf");
		final Typeface myTypeface = Typeface.createFromAsset(getActivity()
				.getAssets(), "Roboto-Condensed.ttf");

		mSubmit = (Button) rootView.findViewById(R.id.submit);
		backButton = (Button) rootView.findViewById(R.id.back);
		thanks = (TextView) rootView.findViewById(R.id.thanks);
		feedback = (EditText) rootView.findViewById(R.id.feedback);

		mSubmit.setTypeface(myTypefacebold);
		backButton.setTypeface(myTypefacebold);
		thanks.setTypeface(myTypeface);
		feedback.setTypeface(myTypeface);
		try {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getActivity().getBaseContext());
			post_username_from = sp.getString("username_from", "doesnt exist");
			emailpassword = sp.getString("emailpassword", "doesnt exist");
			serviceProvider = sp.getString("service", "mail.mailivy.com");

		} catch (Exception e) {
			Log.v("sp error", e.toString());
		}
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (feedback.getText().toString().length() > 2) {

					/*
					 * new SendMailTask(getActivity()).execute(
					 * "hossein@mailivy.com", toArr, "MailIvy Feedback",
					 * feedback.getText().toString(), "salam");
					 */
					new SendFeedback().execute();

				} else {
					Toast.makeText(getActivity(), "Please enter your feedback",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		backButton.setOnClickListener(new OnClickListener() {

			@Override
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

	class SendFeedback extends AsyncTask<String, String, String> {
		int success;
		String[] email_addresses;
		String email_title;
		String email_message;
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Sending Feedback...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			String[] toArr = { "hosein88@gmail.com" };
			MailSender sender;
			if (serviceProvider.equals("gmail")) {
				sender = new MailSender(post_username_from, emailpassword,
						"465", "smtp.gmail.com");
				sender.setFrom(post_username_from);
				Log.v("sending...", post_username_from + "  " + emailpassword);
			} else if (serviceProvider.equals("yahoo")) {
				sender = new MailSender(post_username_from, emailpassword,
						"465", "smtp.mail.yahoo.com");
				sender.setFrom(post_username_from);
			} else {
				sender = new MailSender(post_username_from + "@mailivy.com",
						emailpassword);
				if (isEmailValid(post_username_from)) {
					sender.setFrom(post_username_from);
				} else {
					sender.setFrom(post_username_from + "@mailivy.com");
				}
			}
			sender.setTo(toArr);
			sender.setBody(feedback.getText().toString());

			sender.setSubject("feedback");

			try {
				sender.send();
				success = 1;
			} catch (Exception e) {
				Log.e("SendMail", e.getLocalizedMessage());
			}

			return null;

		}

		protected void onPostExecute(String file_url) {

			pDialog.dismiss();

			if (success == 1) {
				Toast.makeText(getActivity(), "Thank You", Toast.LENGTH_LONG)
						.show();

			} else {
				Toast.makeText(getActivity(), "Feedback was not sent",
						Toast.LENGTH_LONG).show();
			}
			ReadEmails.setBackFromOther(true);
			((ReadEmails) getActivity()).displayView(0);

		}
	}

	boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

}
