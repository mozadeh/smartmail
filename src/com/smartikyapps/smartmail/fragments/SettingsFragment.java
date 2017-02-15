package com.smartikyapps.smartmail.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smartikyapps.smartmail.Log;
import com.smartikyapps.smartmail.Login;
import com.smartikyapps.smartmail.R;
import com.smartikyapps.smartmail.ReadEmails;

public class SettingsFragment extends Fragment {
	
	private Button backButton;
	private ImageButton IB1,IB3;
	//private ImageButton IB2,IB4;
	private TextView TV1,TV3;
	//private TextView TV2,TV4;
	private Boolean notification=true;
	private Boolean sendsound=true;
	private Boolean readsound=true;
	private Boolean receivesound=true;

	public SettingsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

		final Typeface myTypefacebold = Typeface.createFromAsset(getActivity()
				.getAssets(), "Roboto-BoldCondensed.ttf");
		final Typeface myTypeface = Typeface.createFromAsset(getActivity()
				.getAssets(), "Roboto-Condensed.ttf");
 
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        
        backButton = (Button) rootView.findViewById(R.id.back);
        
        backButton.setTypeface(myTypefacebold);
        
		TV1 = (TextView) rootView.findViewById(R.id.sendnotificationtext);
		//TV2 = (TextView) rootView.findViewById(R.id.readsoundtext);
		TV3 = (TextView) rootView.findViewById(R.id.sendsoundtext);
		//TV4 = (TextView) rootView.findViewById(R.id.receivesoundtext);
		
		TV1.setTypeface(myTypeface);
		//TV2.setTypeface(myTypeface);
		TV3.setTypeface(myTypeface);
		//TV4.setTypeface(myTypeface);
		
		IB1 = (ImageButton) rootView.findViewById(R.id.sendnotificationbutton);
		//IB2 = (ImageButton) rootView.findViewById(R.id.readsoundbutton);
		IB3 = (ImageButton) rootView.findViewById(R.id.sendsoundbutton);
		//IB4 = (ImageButton) rootView.findViewById(R.id.receivesoundbutton);
		
		
		final SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		if (!sp.getBoolean("notification", true)) 
			notification=false;
		if (!sp.getBoolean("sendsound", true)) 
			sendsound=false;
		if (!sp.getBoolean("readsound", true)) 
			readsound=false;
		if (!sp.getBoolean("receivesound", true)) 
			receivesound=false;

			//sp.edit().putBoolean("notification", false).commit();
		
		
		
		if(notification) IB1.setImageResource(R.drawable.onbutton); else IB1.setImageResource(R.drawable.offbutton);
		//if(readsound) IB2.setImageResource(R.drawable.onbutton); else IB2.setImageResource(R.drawable.offbutton);
		if(sendsound) IB3.setImageResource(R.drawable.onbutton); else IB3.setImageResource(R.drawable.offbutton);
		//if(receivesound) IB4.setImageResource(R.drawable.onbutton); else IB4.setImageResource(R.drawable.offbutton);
		
		IB1.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(notification==true) {
					notification=false;
					IB1.setImageResource(R.drawable.offbutton);
					sp.edit().putBoolean("notification", false).commit();
				}
				else {
					notification=true;
					IB1.setImageResource(R.drawable.onbutton);
					sp.edit().putBoolean("notification", true).commit();
				}
				
			}
		});
		
		
		/*IB2.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(readsound==true) {
					readsound=false;
					IB2.setImageResource(R.drawable.offbutton);
					sp.edit().putBoolean("readsound", false).commit();
				}
				else {
					readsound=true;
					IB2.setImageResource(R.drawable.onbutton);
					sp.edit().putBoolean("readsound", true).commit();
				}
				
			}
		});*/
		
		IB3.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(sendsound==true) {
					sendsound=false;
					IB3.setImageResource(R.drawable.offbutton);
					sp.edit().putBoolean("sendsound", false).commit();
				}
				else {
					sendsound=true;
					IB3.setImageResource(R.drawable.onbutton);
					sp.edit().putBoolean("sendsound", true).commit();
				}
				
			}
		});
		
		/*IB4.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(receivesound==true) {
					receivesound=false; 
					IB4.setImageResource(R.drawable.offbutton);
					sp.edit().putBoolean("receivesound", false).commit();
				}
				else {
					receivesound=true;
					IB4.setImageResource(R.drawable.onbutton);
					sp.edit().putBoolean("receivesound", true).commit();
				}
				
			}
		});*/
		
        try{
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getActivity()
				.getCurrentFocus().getWindowToken(), 0);
        }
        catch(Exception e){
        	Log.e("error", "in hiding keyboard");
        }
		
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
