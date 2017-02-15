package com.smartikyapps.smartmail;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.InternetAddress;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class EmailPullService extends IntentService {
	String username, password, registrationID;
	private static final String NOTIFY_EMAIL_URL = "http://www.smartikymail.com/webservice/ivy/newemail.php";
	JSONParser jsonParser;

	public EmailPullService() {
		super("EmailPullService");

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		// Gets data from the incoming Intent
		// String dataString = workIntent.getDataString();
		// username = workIntent.getExtras().getString("username");
		// password = workIntent.getExtras().getString("password");
		registrationID = workIntent.getExtras().getString("regid");
		jsonParser = new JSONParser();

		try {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			username = sp.getString("username_from", "doesnt exist");
			password = sp.getString("emailpassword", "doesnt exist");
			Log.v("loing", username);
			Log.v("loing", password);
		} catch (Exception e) {
			Log.v("sp error", e.toString());
		}

		// Do work here, based on the contents of dataString
		try {

			// Session mailSession =
			// Session.getInstance(System.getProperties(),null);
			// IMAPStore iMAPStore = (IMAPStore) mailSession.getStore("imap");
			// iMAPStore.connect("mail.mailivy.com", 143, username, password);
			// final IMAPFolder f = (IMAPFolder) iMAPStore.getFolder("INBOX");
			// Login.f.open(Folder.READ_WRITE);
			openEmailFolder();
			Login.f.addMessageCountListener(new MessageCountListener() {

				public void messagesAdded(MessageCountEvent messageCountEvent) {

					Message[] messages = messageCountEvent.getMessages();
					for (Message message : messages) {
						try {
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("regID",
									registrationID));
							params.add(new BasicNameValuePair("from",
									InternetAddress.toString(message.getFrom())));
							jsonParser.makeHttpRequest(NOTIFY_EMAIL_URL,
									"POST", params);

						} catch (MessagingException ex) {
							ex.printStackTrace();
						}
					}

				}

				public void messagesRemoved(MessageCountEvent e) {
				}
			});

			Log.v("salam", "before while: "
					+ DateFormat.getDateTimeInstance().format(new Date()));
			while (true) {
				Log.v("salam", "before idle: "
						+ DateFormat.getDateTimeInstance().format(new Date()));
				if (!Login.f.isOpen()) {
					Login.f.open(Folder.READ_WRITE);
				}
				Login.f.idle();

				Log.v("salam", "after idle: "
						+ DateFormat.getDateTimeInstance().format(new Date()));
			}

		} catch (MessagingException ex) {
			ex.printStackTrace();

		}

	}

	public void openEmailFolder() {
		try {
			Session mailSession = Session.getInstance(System.getProperties(),
					null);
			IMAPStore iMAPStore = (IMAPStore) mailSession.getStore("imaps");
			if (username.contains("@gmail.com")) {
				iMAPStore.connect("imap.gmail.com", username, password);
			} else if (username.contains("@yahoo.com")) {
				iMAPStore.connect("imap.mail.yahoo.com", username, password);

			} else {
				iMAPStore.connect("mail.mailivy.com",
						username + "@mailivy.com", password);
			}

			Login.f = (IMAPFolder) iMAPStore.getFolder("INBOX");
			Login.f.open(Folder.READ_WRITE);

		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			Log.e("error", "Messaging Exception");
		}

	}

}
