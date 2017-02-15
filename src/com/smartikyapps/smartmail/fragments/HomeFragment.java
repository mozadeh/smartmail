package com.smartikyapps.smartmail.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.smartikyapps.smartmail.R;

public class HomeFragment extends Fragment {
	Typeface myTypefacebold;
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);


         
        return rootView;
    }
	
}
