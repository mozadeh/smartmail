package com.smartikyapps.smartmail;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<String>{

	Context context; 
    int layoutResourceId;    
    ArrayList<String> data = null;
    Typeface myTypefaceItalic;
    
    public ContactAdapter(Context context, int layoutResourceId, ArrayList<String> data) {
    	super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        myTypefaceItalic = Typeface.createFromAsset(context.getAssets(),
				"Roboto-CondensedItalic.ttf");
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TextView email = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            //holder = new WeatherHolder();
            //holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            email = (TextView)row.findViewById(R.id.txtTitle);
            email.setTypeface(myTypefaceItalic);
            row.setTag(email);
        }
        else
        {
            email = (TextView)row.getTag();
        }
        
       //TextView contact = data[position];
       email.setText(data.get(position));
        //holder.txtTitle.setText(weather.title);
        //holder.imgIcon.setImageResource(weather.icon);
        
        return row;
    }
    
    /*static class WeatherHolder
    {
        //ImageView imgIcon;
        TextView txtTitle;
    }*/
}