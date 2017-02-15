package com.smartikyapps.smartmail.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.smartikyapps.smartmail.Log;
import com.smartikyapps.smartmail.R;
import com.smartikyapps.smartmail.ReadEmails;

public class AboutFragment extends Fragment {
	private Button backButton;
	private TextView About;
	String versionName = "";

	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final Typeface myTypefacebold = Typeface.createFromAsset(getActivity()
				.getAssets(), "Roboto-BoldCondensed.ttf");
		final Typeface myTypeface = Typeface.createFromAsset(getActivity()
				.getAssets(), "Roboto-Condensed.ttf");
		

		View rootView = inflater.inflate(R.layout.fragment_about, container,
				false);

		backButton = (Button) rootView.findViewById(R.id.back);
		About = (TextView) rootView.findViewById(R.id.about);
		backButton.setTypeface(myTypefacebold);
		About.setTypeface(myTypeface);

		try {
			versionName = (getActivity().getPackageManager()).getPackageInfo(
					(getActivity().getPackageName()), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e("version name error", e.getMessage().toString());
		}
		
		
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getActivity()
				.getCurrentFocus().getWindowToken(), 0);
			
			

		About.setText("Typical email services are built based on centuries old mailing"
				+ " systems where a letter was sent using horses and a sender had no way"
				+ " to retrieve what they sent."
				+ "\n\nWe always make mistakes when we send emails"
				+ " and we should be able to fix or delete them without having to send more emails or apologizing."
				+ " \n\nSmart Mail is designed to provide users a smarter way of sending emails. "
				+ "You can delete sent messages and modify unread sent messages. Simple as that!");

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ReadEmails.setBackFromOther(true);
				((ReadEmails) getActivity()).displayView(0);
			}

		});
		

		return rootView;
	}
	
	
}
