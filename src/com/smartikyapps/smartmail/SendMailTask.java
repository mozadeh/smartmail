package com.smartikyapps.smartmail;

import java.util.Locale;

import android.os.AsyncTask;

public class SendMailTask extends AsyncTask {

	String serviceProvider, post_username_from, emailpassword, email_title,
			email_message, fullname, randomURL;
	int success;
	String[] email_addresses;

	public SendMailTask(String CserviceProvider, String Cpost_username_from,
			String Cemailpassword, String Cemail_title, String Cemail_message,
			String Cfullname, String CrandomURL, String[] Cemail_addresses) {
		serviceProvider = CserviceProvider;
		post_username_from = Cpost_username_from;
		emailpassword = Cemailpassword;
		email_title = Cemail_title;
		email_message = Cemail_message;
		fullname = Cfullname;
		randomURL = CrandomURL;

		email_addresses = Cemail_addresses;

	}

	@Override
	protected Object doInBackground(Object... args) {
		MailSender sender;
		Log.v("sending","in background");
		if (serviceProvider.equals("gmail")) {
			sender = new MailSender(post_username_from, emailpassword, "465",
					"smtp.gmail.com");
			sender.setFrom(post_username_from);
			Log.v("sending...", post_username_from + "  " + emailpassword);
		} else if (serviceProvider.equals("yahoo")) {
			sender = new MailSender(post_username_from, emailpassword, "465",
					"smtp.mail.yahoo.com");
			sender.setFrom(post_username_from);
		} else {
			sender = new MailSender(post_username_from + "@mailivy.com",
					emailpassword);
			if (isEmailValid(post_username_from)) {
				sender.setFrom(post_username_from);
			} else {
				sender.setFrom(post_username_from + "@mailivy.com");
			}
		}
		sender.setTo(email_addresses);
		if (fullname.equals("null")) {
			sender.setBody(setemailbody(randomURL, post_username_from));
		} else {
			sender.setBody(setemailbody(randomURL, fullname));
		}
		sender.setSubject(email_title.toLowerCase(Locale.ENGLISH));

		try {
			sender.send();
			EmailsFragment.refreshAfterSent = true;
			success = 1;
			Log.v("sending","sent");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("SendMail", e.getLocalizedMessage());
			Log.v("sending","not sent");
			success = 0;
		}
		return success;
	}

	/*@Override
	public void onPostExecute(Object result) {

		if (success == 1) {
			Toast.makeText(, "Email sent", Toast.LENGTH_LONG)
					.show();
	}*/

	boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private String setemailbody(String ranURL, String username_from) {

		
		return "<html><head>"
				+ "	<title></title>"
				+ "</head>"
				+ "<body>"

				
				
				+ "<table align=\"center\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:100%;background-color:rgb(242,242,242)\">"
				+ "<tbody>"
				+ "<tr>"
				+ "<td style=\"text-align:center\">"
				+ "<p><span style=\"color: rgb(96, 96, 96); font-family: Helvetica, Arial, sans-serif; font-size: 40px; font-weight: bold; letter-spacing: -1px; line-height: 46px; text-align: center;\">You&#39;ve Received A Message</span></p>"
				+ "		</td>"
				+ "	</tr>"
				+ "	<tr>"
				+ "		<td  style=\"text-align:center\">"
				+ "		<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:80%;background-color:#ffffff;border-collapse:separate!important;border-radius:4px;padding-top:20px;padding-bottom:30px\">"
				+ "			<tbody>"
				+ "				<tr>"
				+ "					<td style=\"color:#606060!important;font-family:Helvetica,Arial,sans-serif;font-size:40px;font-weight:bold;letter-spacing:-1px;line-height:115%;margin:0;padding:0;text-align:center\">"
				+ "					<p style=\"color: rgb(96, 96, 96); font-family: Helvetica, Arial, sans-serif; font-size: 18px; letter-spacing: -0.5px; line-height: 20.7000007629395px; text-align: center;\">Please wait a few seconds for your message preview to load</p>"
				+ "					</td>"
				+ "				</tr>"
				+ "				<tr>"
				+ "					<td align=\"center\" style=\"color:#606060!important;font-family:Helvetica,Arial,sans-serif;font-size:18px;letter-spacing:-.5px;line-height:115%;margin:0;padding-bottom:5px;text-align:center\">"
				+ "					<p><a href=\"http://smartikymail.com/webservice/openemail.php/"
				+ ranURL+"\" target=\"_blank\">"
				+ "<img alt=\"Loading message preview\" src=\"http://api.screenshotmachine.com/?key=7c3592&amp;cacheLimit=0&amp;timeout=0&amp;size=e&amp;format=gif&amp;url=http://smartikymail.com/webservice/screenshot.php/"
				+ ranURL+"\" style=\"width: 320px; height: 240px; border-width: 1px; border-style: solid;\" />"
						+ "</a></p>"
				+ "					</td>"
				+ "				</tr>"
				+ "				<tr>"
				+ "					<td align=\"center\">"
				+ "					<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#a8a8a8;border-collapse:separate!important;border-radius:3px\">"
				+ "						<tbody>"
				+ "							<tr>"
				+ "								<td style=\"color:#a7a7a7;font-family:Helvetica,Arial,sans-serif;font-size:15px;font-weight:bold;line-height:100%;padding-top:18px;padding-right:15px;padding-bottom:15px;padding-left:15px\"><a href=\"http://smartikymail.com/webservice/openemail.php/"
				+ ranURL+"\" style=\"color:white;text-decoration:none\" target=\"_blank\">Open Full Message</a></td>"
				+ "							</tr>"
				+ "						</tbody>"
				+ "					</table>"
				+ "					</td>"
				+ "				</tr>"
				+ "			</tbody>"
				+ "		</table>"
				+ "		</td>"
				+ "	</tr>"
				+ "	<tr>"
				+ "		<td style=\"text-align:center;color:#606060;font-family:Helvetica,Arial,sans-serif;font-size:13px;line-height:125%\">"
				+ "		<p>Sent by</p>"
				+ "		<p><a href=\"https://play.google.com/store/apps/details?id=com.smartikyapps.smartmail\" target=\"_blank\"><img alt=\"\" src=\"https://ci5.googleusercontent.com/proxy/NqgIzdu7TqERrIDX-Y43hYpiM9fUfS23QeBUaUfHKmPDrwIRaORrbk2pad6HdApsmXNMl9azMeCn0qxbbMcI9yhO1eRnFdNSdjml=s0-d-e1-ft#http://www.smartikymail.com/webservice/small-logo.png\" style=\"width:100px;min-height:25px\" /></a></p>"
				+ "		</td>"
				+ "	</tr>"
				+ "</tbody>"
			+ "</table>"

				
				
				
				
				
				
				
				
				
				/*+ "<table style="border-collapse: separate!important; border-radius: 3px;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"
				+ "<tbody>"
				+ "<tr>"
				+ "<td style=\"background-color: #a2a2a2; color: #ffffff; font-family: Helvetica,Arial,sans-serif; font-size: 15px; font-weight: bold; line-height: 100%; padding-top: 18px; padding-right: 15px; padding-bottom: 15px; padding-left: 15px;\"><a style=\"color: white; text-decoration: none;\" href=\"http://smartikymail.com/webservice/openemail.php/"
				+ randomURL+"\" target=\"_blank\">Open Message</a></td>"
				+ "<td><a href=\"https://play.google.com/store/apps/details?id=com.smartikyapps.smartmail\" target=\"_blank\"><img style=\"width: 50px; min-height: 50px; padding-bottom: 10px;\" src=\"https://ci4.googleusercontent.com/proxy/6lZXq2jxAMgwlBoNhFkmivT0v4AjFPAl0cAF0dOKXGbsBIS15iYnLlZx-1rBxh0wmiI4UAIfrM6eHn3hq_BBLNBhOpe_PzhpsE9AfUs=s0-d-e1-ft#http://www.smartikymail.com/webservice/ic_launcher3.png\" alt=\"Smart Mail Logo\" /></a></td>"
				+ "</tr>"
				+ "</tbody>"
				+ "</table>"
				
				+ "<p><img src=\"http://api.screenshotmachine.com/?key=7c3592&amp;size=m&cacheLimit=0&amp;format=gif&amp;url=http://smartikymail.com/webservice/screenshot.php/"
				+randomURL+"\" alt=\"message\" /></p>"*/
				
				
				
				
				+ "</body></html>";
		/*return "<html><head>"
				+ "	<title></title>"
				+ "</head>"
				+ "<body>"
				+ "	<table align=\"center\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%; background-color: rgb(242, 242, 242);\">"
				+ "		<tbody>"
				+ "			<tr>"
				+ "				<td style=\"text-align: center;\">"
				+ "					<p>"
				+ "						<a href=\"https://play.google.com/store/apps/details?id=com.smartikyapps.smartmail\"><img alt=\"Smart Mail Logo\" src=\"http://www.smartikymail.com/webservice/ic_launcher3.png\" style=\"width: 100px; height: 100px; padding-bottom:10px\" /></a></p>"
				+ "				</td>"
				+ "			</tr>"
				+ "			<tr>"
				+ "				<td style=\"text-align: center;\">"
				+ "					<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 80%; background-color:#ffffff;border-collapse:separate!important;border-radius:4px;padding-top:20px;padding-bottom:30px\">"
				+ "						<tbody>"
				+ "							<tr>"
				+ "								<td style=\"color:#606060!important;font-family:Helvetica,Arial,sans-serif;font-size:40px;font-weight:bold;letter-spacing:-1px;line-height:115%;margin:0;padding:0;text-align:center\">"
				+ "									You&#39;ve Received A Message</td>"
				+ "							</tr>"
				+ "							<tr>"
				+ "								<td style=\"color:#606060!important;font-family:Helvetica,Arial,sans-serif;font-size:18px;letter-spacing:-.5px;line-height:115%;margin:0;padding-bottom:5px;text-align:center\">"
				+ "									<p>"
				+ "										<span style=\"line-height: 115%;\">Click the button below to read your message from:</span></p>"
				+ "									<p>"
				+ "										<span style=\"font-weight: bold;\">"
				+ username_from
				+ "</span></p>"
				+ "								</td>"
				+ "							</tr>"
				+ "							<tr>"
				+ "								<td>"
				+ "									<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#009a3d;border-collapse:separate!important;border-radius:3px\">"
				+ "										<tbody>"
				+ "											<tr>"
				+ "												<td style=\"color:#ffffff;font-family:Helvetica,Arial,sans-serif;font-size:15px;font-weight:bold;line-height:100%;padding-top:18px;padding-right:15px;padding-bottom:15px;padding-left:15px\">"
				+ "													<a c=\"\" href=\"http://smartikymail.com/webservice/openemail.php/"
				+ ranURL
				+ "\" style=\"color:white;text-decoration: none\">Open Message</a></td>"
				+ "											</tr>"
				+ "										</tbody>"
				+ "									</table>"
				+ "								</td>"
				+ "							</tr>"
				+ "						</tbody>"
				+ "					</table>"
				+ "				</td>"
				+ "			</tr>"
				+ "			<tr>"
				+ "				<td style=\"text-align: center; color:#606060;font-family:Helvetica,Arial,sans-serif;font-size:13px;line-height:125%\">"
				+ "					<p>"
				+ "						&nbsp;</p>"
				+ "					<p>"
				+ "						Sent by</p>"
				+ "					<p>"
				+ "						<a href=\"https://play.google.com/store/apps/details?id=com.smartikyapps.smartmail\"><img alt=\"\" src=\"http://www.smartikymail.com/webservice/small-logo.png\" style=\"width: 100px; height: 25px;\" /></a></p>"
				+ "				</td>"
				+ "			</tr>"
				+ "		</tbody>"
				+ "	</table></body></html>";*/

	}

}