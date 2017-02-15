package com.smartikyapps.smartmail;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EmailHolder {
	
    private String postID="";
    private TextView opened;
    private TextView title;
    private TextView username;
    private String message="";
    private String imgURL="";
    private String URL="";
    private String theme="";
    private String ivyInbox="";
    private String openStatus="";
    private LinearLayout box;
    private LinearLayout postborder;
    private ImageView expand;

	 public EmailHolder(String postID, TextView opened, TextView title, TextView username, String message, String imgURL, String URL, String theme, String ivyInbox) {
		this.postID=postID;
		this.opened=opened;
		this.title=title;
		this.username=username;
		this.message=message;
		this.imgURL=imgURL;
		this.URL=URL;
		this.theme=theme;
		this.setIvyInbox(ivyInbox);
	}
	 
	 public EmailHolder() {
		// TODO Auto-generated constructor stub
	}
    
    public TextView getTitle(){
    	return this.title;
    }
    public String getpostID(){
    	return this.postID;
    }
    public TextView getRead(){
    	return this.opened;
    }
    public TextView getUserName(){
    	return this.username;
    }
    
    public void setTitle(TextView input){
    	this.title= input;
    }
    public void setpostID(String input){
    	this.postID= input;
    }
    public void setRead(TextView input){
    	this.opened= input;
    }
    public void setUserName(TextView input){
    	this.username= input;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getIvyInbox() {
		return ivyInbox;
	}

	public void setIvyInbox(String ivyInbox) {
		this.ivyInbox = ivyInbox;
	}

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	public LinearLayout getBox() {
		return box;
	}

	public void setBox(LinearLayout box) {
		this.box = box;
	}

	public LinearLayout getPostborder() {
		return postborder;
	}

	public void setPostborder(LinearLayout postborder) {
		this.postborder = postborder;
	}

	public ImageView getExpand() {
		return expand;
	}

	public void setExpand(ImageView expand) {
		this.expand = expand;
	}


}
