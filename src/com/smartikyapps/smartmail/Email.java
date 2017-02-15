package com.smartikyapps.smartmail;

public class Email {
	private String Title;
    private String postID;
    private String read;
    private String userName;
    private String message;
    private String imgURL;
    private String URL;
    private String theme;
    private String ivyInbox;
    private String openstatus;

    public Email(String Title, String postID, String message, String userName, String ivyinbox) {
    	super();
       this.Title = Title;
       this.postID = postID;
       this.message = message;
       this.userName = userName; 
       this.ivyInbox = ivyinbox;
    }
    
    public String getTitle(){
    	return this.Title;
    }
    public String getpostID(){
    	return this.postID;
    }
    public String getRead(){
    	return this.read;
    }
    public String getUserName(){
    	return this.userName;
    }
    
    public void setTitle(String input){
    	this.Title= input;
    }
    public void setpostID(String input){
    	this.postID= input;
    }
    public void setRead(String input){
    	this.read= input;
    }
    public void setUserName(String input){
    	this.userName= input;
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

	public String getOpenstatus() {
		if (openstatus!=null) return openstatus;
		else return "not set";
	}

	public void setOpenstatus(String openstatus) {
		this.openstatus = openstatus;
	}


    
}
