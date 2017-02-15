package com.smartikyapps.smartmail;

public class Contact {
	private String ContactName;
    private String ContactEmail;

    public Contact(String ContactName, String ContactEmail) {
    	super();
       this.ContactName = ContactName;
       this.ContactEmail = ContactEmail;
    }
    
    public String getName(){
    	return this.ContactName;
    }
    public String getEmail(){
    	return this.ContactEmail;
    }
    
}
