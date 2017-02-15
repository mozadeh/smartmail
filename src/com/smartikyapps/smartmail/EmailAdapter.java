package com.smartikyapps.smartmail;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.smartikyapps.smartmail.Log;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EmailAdapter extends ArrayAdapter<Email>{

    Context context; 
    int layoutResourceId;    
    private ArrayList<Email> data = null;
    Typeface myTypefacebolditalic,myTypefaceitalic,myTypefacebold;
    
    public EmailAdapter(Context context, int layoutResourceId, ArrayList<Email> Emails) {
        super(context, layoutResourceId, Emails);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = Emails;
        myTypefacebolditalic = Typeface.createFromAsset(context.getAssets(),"Roboto-BoldCondensedItalic.ttf");
		myTypefaceitalic = Typeface.createFromAsset(context.getAssets(),"Roboto-CondensedItalic.ttf");
		myTypefacebold = Typeface.createFromAsset(context.getAssets(),"Roboto-BoldCondensed.ttf");
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EmailHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new EmailHolder();
            holder.setTitle((TextView)row.findViewById(R.id.title));
            holder.setRead((TextView)row.findViewById(R.id.opened));
            holder.setUserName((TextView)row.findViewById(R.id.username));
            holder.setBox((LinearLayout)row.findViewById(R.id.box));
            holder.setPostborder((LinearLayout)row.findViewById(R.id.postborder));
            holder.setExpand((ImageView)row.findViewById(R.id.expand));
            row.setTag(holder);
        }
        else
        {
            holder = (EmailHolder)row.getTag();
        }
        
        Email email = (Email) data.get(position);
        holder.getUserName().setText(email.getUserName());
        holder.getUserName().setTypeface(myTypefacebold);
        holder.getTitle().setText(email.getTitle());
        if(email.getOpenstatus().equals("unread")) {
        	holder.getBox().setBackgroundResource((R.drawable.post_background_style_unread));
        	holder.getPostborder().setBackgroundResource((R.drawable.post_border_style_unread));
        	holder.getExpand().setImageResource((R.drawable.expand_unread));
        	holder.getRead().setTextColor(Color.parseColor("#009f4c"));
        	holder.getTitle().setTextColor(Color.parseColor("#333333"));
        	holder.getUserName().setTextColor(Color.parseColor("#707070"));
        }
        else{
        	holder.getBox().setBackgroundResource((R.drawable.post_background_style));
        	holder.getPostborder().setBackgroundResource((R.drawable.post_border_style));
        	holder.getExpand().setImageResource((R.drawable.expand));
        	holder.getRead().setTextColor(Color.parseColor("#5c5c5c"));
        	holder.getTitle().setTextColor(Color.parseColor("#5c5c5c"));
        	holder.getUserName().setTextColor(Color.parseColor("#707070"));
        }
        //Log.v("readornot", email.getOpenstatus());
        //holder.getTitle().setTypeface(myTypefacebold);
        holder.getRead().setText(email.getRead());
        holder.getRead().setTypeface(myTypefaceitalic);
        holder.setOpenStatus(email.getOpenstatus());
        holder.setImgURL(email.getImgURL());
        holder.setMessage(email.getMessage());
        holder.setURL(email.getURL());
        holder.setTheme(email.getTheme());
        holder.setpostID(email.getpostID());
        holder.setIvyInbox(email.getIvyInbox());
       
        
        return row;
    }

}
