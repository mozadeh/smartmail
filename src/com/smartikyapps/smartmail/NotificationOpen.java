package com.smartikyapps.smartmail;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class NotificationOpen extends Activity{
	
	JSONParser jsonParser = new JSONParser();
	private static final String LOGIN_URL = "http://www.smartikymail.com/webservice/login.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		new AttemptLogin().execute();
	}
	
	
	
	
	class AttemptLogin extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		String username,password;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			username = sp.getString("username_from",
					"doesnt exist");
			password = sp.getString("password",
					"doesnt exist");
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			try {
				// Building Parameters
				Log.v("here", "1");
				
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));
				Log.v("here", password);

				// Log.d("request!", "starting");
				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
						params);

				// check your log for json response
				// Log.d("Login attempt", json.toString());

				// json success tag
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// Log.d("Login Successful!", json.toString());
					// save user data
					Intent i = new Intent(NotificationOpen.this, ReadEmails.class);
					finish();
					startActivity(i);
					Log.v("message in notification",json.getString(TAG_MESSAGE));
				} else {
					// Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					Log.v("message in notification",json.getString(TAG_MESSAGE));

				}
			} catch (Exception e) {
				// e.printStackTrace();
				Log.e("Error", e.toString());
			}

			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			

		}

	}
	
	

}
