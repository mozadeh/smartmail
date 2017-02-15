package com.smartikyapps.smartmail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class CustomSimpleAdapter extends ArrayAdapter<Contact>{

    Context context; 
    int layoutResourceId;    
    private ArrayList<Contact> data = null;
    private ArrayList<Contact> itemsAll;
    private ArrayList<Contact> suggestions;
    Typeface myTypeface;
	Typeface myTypefaceitalic;
    
    public CustomSimpleAdapter(Context context, int layoutResourceId, ArrayList<Contact> contactList) {
        super(context, layoutResourceId, contactList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = contactList;
        this.itemsAll = (ArrayList<Contact>) data.clone();
        this.suggestions = new ArrayList<Contact>();
        myTypeface = Typeface.createFromAsset(context.getAssets(),"Roboto-Condensed.ttf");
		myTypefaceitalic = Typeface.createFromAsset(context.getAssets(),"Roboto-CondensedItalic.ttf");
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ContactHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ContactHolder();
            holder.cEmail = (TextView)row.findViewById(R.id.contactemail);
            holder.cName = (TextView)row.findViewById(R.id.contactname);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ContactHolder)row.getTag();
        }
        
        Contact contact = (Contact) data.get(position);
        holder.cEmail.setText(contact.getEmail());
        holder.cEmail.setTypeface(myTypefaceitalic);
        holder.cName.setText(contact.getName());
        holder.cName.setTypeface(myTypeface);
        
        return row;
    }
    
    
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        public String convertResultToString(Object resultValue) {
            String str = ((Contact)(resultValue)).getEmail(); 
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Contact contact : itemsAll) {
                	String cst= constraint.toString().toLowerCase(Locale.ENGLISH);
                    if(contact.getName().toLowerCase(Locale.ENGLISH).startsWith(cst)  || contact.getEmail().toLowerCase(Locale.ENGLISH).startsWith(cst)){
                        suggestions.add(contact);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        	ArrayList<Contact> filteredList = (ArrayList<Contact>) results.values;
            ArrayList<Contact> contactList=new ArrayList<Contact>();
            if (results != null && results.count > 0) {
                clear();
                for (Contact c : filteredList) {
                    contactList.add(c);
                }
                Iterator<Contact> contactIterator=contactList.iterator();
                while (contactIterator.hasNext()) {
                	Contact contactIterator2=contactIterator.next();
                    add(contactIterator2);
                }
                notifyDataSetChanged();
            }
        	
        	/*ArrayList<Contact> filteredList = (ArrayList<Contact>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Contact c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }*/
        }
    };
    
    
    
    
    static class ContactHolder
    {
        TextView cName;
        TextView cEmail;
    }
}
