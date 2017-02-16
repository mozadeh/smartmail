#Smart Mail

## App store listing

link to Google Play listing: ```https://play.google.com/store/apps/details?id=com.smartikyapps.smartmail```

## Description

Take control of your emails!

Have you ever sent an Email to a colleague, girlfriend, boyfriend, boss or family member that you wish you could delete (or change before it's read). Well now you can! Smart Mail solves three key problems existing email services have:

- Smart Mail allows you to delete sent messages and edit unread messages

- Smart Mail notifies you when your message have been read

- Smart Mail makes emails look beautiful

You can either create a new email account or login using your Gmail or Yahoo Mail account.

Note: We do not store Gmail or Yahoo email passwords on our servers.


## Libraries / Tutorials / APIs used

- Google Analytics Android SDK ```https://developers.google.com/analytics/devguides/collection/android/v4/```

- Android Pull to Refresh Loadmore listview ```https://github.com/incube8/android-pullToRefresh-loadMore```

- IMAP Store (Javamail) ```https://javamail.java.net/nonav/docs/api/com/sun/mail/imap/IMAPStore.html```

- Android Push Notification Tutorial ```https://www.tutorialspoint.com/android/android_push_notification.htm```

- Navigation Drawer ```http://www.androidhive.info/2013/11/android-sliding-menu-using-navigation-drawer/```

- Screenshot Machine ```https://www.screenshotmachine.com```


## Key Features

- Push notification on email open

- Ability to create an email account

- Ability to login via your Gmail or Yahoo email account


## Key classes

link to key classes used in app: ```https://github.com/mozadeh/smartmail/tree/master/bin/classes/com/smartikyapps/smartmail```

## Class structure

```
/
├── AndroidManifest.xml
├── README.md
├── assets
│   ├── Roboto-Bold.ttf
│   ├── Roboto-BoldCondensed.ttf
│   ├── Roboto-BoldCondensedItalic.ttf
│   ├── Roboto-Condensed.ttf
│   ├── Roboto-CondensedItalic.ttf
│   ├── masterpiecedefaultblur.png
│   ├── oldlinedpaper.jpg
│   ├── oldlinedpaper_burned.jpg
│   ├── oldlinedpaper_photo_mobile.jpg
│   ├── plain.png
│   ├── whiteboard.jpg
│   ├── whiteboard_erased.jpg
│   └── whiteboard_photo_mobile.jpg
├── bin
│   ├── AndroidManifest.xml
│   ├── R.txt
│   ├── Smart\ Mail.apk
│   ├── classes
│   │   └── com
│   │       ├── android
│   │       │   └── widget
│   │       │       ├── R$color.class
│   │       │       ├── R$drawable.class
│   │       │       ├── R$id.class
│   │       │       ├── R$layout.class
│   │       │       ├── R$string.class
│   │       │       └── R.class
│   │       └── smartikyapps
│   │           └── smartmail
│   │               ├── AddEmail$1.class
│   │               ├── AddEmail$10.class
│   │               ├── AddEmail$11.class
│   │               ├── AddEmail$2.class
│   │               ├── AddEmail$3.class
│   │               ├── AddEmail$4.class
│   │               ├── AddEmail$5.class
│   │               ├── AddEmail$6.class
│   │               ├── AddEmail$7.class
│   │               ├── AddEmail$8.class
│   │               ├── AddEmail$9.class
│   │               ├── AddEmail$ImageGalleryTask.class
│   │               ├── AddEmail$PostEmail.class
│   │               ├── AddEmail.class
│   │               ├── Base64$1.class
│   │               ├── Base64$InputStream.class
│   │               ├── Base64$OutputStream.class
│   │               ├── Base64.class
│   │               ├── BuildConfig.class
│   │               ├── Contact.class
│   │               ├── ContactAdapter.class
│   │               ├── CustomSimpleAdapter$1.class
│   │               ├── CustomSimpleAdapter$ContactHolder.class
│   │               ├── CustomSimpleAdapter.class
│   │               ├── CustomTypefaceSpan.class
│   │               ├── Email.class
│   │               ├── EmailAdapter.class
│   │               ├── EmailHolder.class
│   │               ├── EmailPullService$1.class
│   │               ├── EmailPullService.class
│   │               ├── EmailReceiver.class
│   │               ├── EmailsFragment$1.class
│   │               ├── EmailsFragment$2.class
│   │               ├── EmailsFragment$3.class
│   │               ├── EmailsFragment$4$1.class
│   │               ├── EmailsFragment$4$2.class
│   │               ├── EmailsFragment$4$3.class
│   │               ├── EmailsFragment$4.class
│   │               ├── EmailsFragment$5.class
│   │               ├── EmailsFragment$6.class
│   │               ├── EmailsFragment$7.class
│   │               ├── EmailsFragment$8.class
│   │               ├── EmailsFragment$DeleteEmail.class
│   │               ├── EmailsFragment$DeleteEmailInbox.class
│   │               ├── EmailsFragment$LoadEmails.class
│   │               ├── EmailsFragment$MakeEdits.class
│   │               ├── EmailsFragment$OpenEmail.class
│   │               ├── EmailsFragment.class
│   │               ├── GCMIntentService.class
│   │               ├── JSONParser.class
│   │               ├── Log.class
│   │               ├── Login$1.class
│   │               ├── Login$2.class
│   │               ├── Login$3.class
│   │               ├── Login$4.class
│   │               ├── Login$5.class
│   │               ├── Login$6.class
│   │               ├── Login$AttemptLogin.class
│   │               ├── Login$Sendpassword.class
│   │               ├── Login$readContacts.class
│   │               ├── Login.class
│   │               ├── MailSender.class
│   │               ├── Manifest$permission.class
│   │               ├── Manifest.class
│   │               ├── MyWebView.class
│   │               ├── NavDrawerItem.class
│   │               ├── NavDrawerListAdapter.class
│   │               ├── NotificationOpen$AttemptLogin.class
│   │               ├── NotificationOpen.class
│   │               ├── PreviewEmail$1.class
│   │               ├── PreviewEmail$2.class
│   │               ├── PreviewEmail$3.class
│   │               ├── PreviewEmail$4.class
│   │               ├── PreviewEmail$DeleteEmail.class
│   │               ├── PreviewEmail$DownloadTask.class
│   │               ├── PreviewEmail$MakeEdits.class
│   │               ├── PreviewEmail.class
│   │               ├── R$anim.class
│   │               ├── R$array.class
│   │               ├── R$attr.class
│   │               ├── R$bool.class
│   │               ├── R$color.class
│   │               ├── R$dimen.class
│   │               ├── R$drawable.class
│   │               ├── R$id.class
│   │               ├── R$layout.class
│   │               ├── R$menu.class
│   │               ├── R$raw.class
│   │               ├── R$string.class
│   │               ├── R$style.class
│   │               ├── R.class
│   │               ├── ReadEmails$1.class
│   │               ├── ReadEmails$2.class
│   │               ├── ReadEmails$SectionsPagerAdapter.class
│   │               ├── ReadEmails$SlideMenuClickListener.class
│   │               ├── ReadEmails.class
│   │               ├── Register$1.class
│   │               ├── Register$2.class
│   │               ├── Register$3.class
│   │               ├── Register$4.class
│   │               ├── Register$5.class
│   │               ├── Register$6.class
│   │               ├── Register$CreateUser.class
│   │               ├── Register$ItemFragment$CheckUserName.class
│   │               ├── Register$ItemFragment.class
│   │               ├── Register$SectionsPagerAdapter.class
│   │               ├── Register.class
│   │               ├── SendMailTask.class
│   │               ├── SingleEmail$1.class
│   │               ├── SingleEmail$2.class
│   │               ├── SingleEmail$DeleteEmail.class
│   │               ├── SingleEmail$LoadMail.class
│   │               ├── SingleEmail.class
│   │               ├── SpaceTokenizer.class
│   │               ├── fragments
│   │               │   ├── AboutFragment$1.class
│   │               │   ├── AboutFragment.class
│   │               │   ├── ChangepasswordFragment$1.class
│   │               │   ├── ChangepasswordFragment$2.class
│   │               │   ├── ChangepasswordFragment$3.class
│   │               │   ├── ChangepasswordFragment$4.class
│   │               │   ├── ChangepasswordFragment$5.class
│   │               │   ├── ChangepasswordFragment$ChangePassword.class
│   │               │   ├── ChangepasswordFragment.class
│   │               │   ├── FeedbackFragment$1.class
│   │               │   ├── FeedbackFragment$2.class
│   │               │   ├── FeedbackFragment$SendFeedback.class
│   │               │   ├── FeedbackFragment.class
│   │               │   ├── HomeFragment.class
│   │               │   ├── PhotosFragment.class
│   │               │   ├── SettingsFragment$1.class
│   │               │   ├── SettingsFragment$2.class
│   │               │   ├── SettingsFragment$3.class
│   │               │   └── SettingsFragment.class
│   │               └── gcm
│   │                   ├── AlertDialogManager$1.class
│   │                   ├── AlertDialogManager.class
│   │                   ├── CommonUtilities.class
│   │                   ├── ConnectionDetector.class
│   │                   ├── IMAPWakeLocker.class
│   │                   ├── ServerUtilities.class
│   │                   └── WakeLocker.class
│   ├── classes.dex
│   ├── dexedLibs
│   │   ├── activation-9d37b7deb7db5b48188c4a1efdf207e8.jar
│   │   ├── additionnal-2dc7932cd1bb204c231870d598bbb89d.jar
│   │   ├── android-support-v4-2c42c1c1e07643431ab32d645ee23511.jar
│   │   ├── gcm-01edb3f086eb1515b26c82ffd7a5d31b.jar
│   │   ├── imapstore-79fda5e817ef3a3c1969ee26c3ffea2e.jar
│   │   ├── libGoogleAnalyticsServices-eecca436ea29a4bd1bd837d52adc08ee.jar
│   │   ├── loadmorelistview-03398085e3c48a6268b1fb3bacfa27b2.jar
│   │   └── mail-350247bc093cc3b3ff672e21a252fa57.jar
│   ├── jarlist.cache
│   ├── res
│   │   └── crunch
│   │       ├── drawable
│   │       │   ├── camerabutton.png
│   │       │   ├── ...
│   │       │   └── topmenugradient.png
│   │       ├── drawable-hdpi
│   │       │   ├── apptheme_textfield_activated_holo_light.9.png
│   │       │   ├── ...
│   │       │   └── ic_whats_hot.png
│   │       ├── drawable-ldpi
│   │       │   ├── ic_about.png
│   │       │   ├── ic_compose.png
│   │       │   ├── ic_launcher.png
│   │       │   ├── ic_logout.png
│   │       │   └── ic_stat_logonoti.png
│   │       ├── drawable-mdpi
│   │       │   ├── apptheme_textfield_activated_holo_light.9.png
│   │       │   ├── ...
│   │       │   └── ic_stat_logonoti.png
│   │       ├── drawable-xhdpi
│   │       │   ├── apptheme_textfield_activated_holo_light.9.png
│   │       │   ├── apptheme_textfield_default_holo_light.9.png
│   │       │   ├── ...
│   │       │   └── ic_stat_logonoti.png
│   │       ├── drawable-xxhdpi
│   │       │   ├── apptheme_textfield_activated_holo_light.9.png
│   │       │   ├── apptheme_textfield_default_holo_light.9.png
│   │       │   ├── ...
│   │       │   └── ic_whats_hot.png
│   │       └── drawable-xxxhdpi
│   │           └── ic_launcher.png
│   └── resources.ap_
├── gen
│   └── com
│       ├── android
│       │   └── widget
│       │       └── R.java
│       └── smartikyapps
│           └── smartmail
│               ├── BuildConfig.java
│               ├── Manifest.java
│               └── R.java
├── ic_launcher-web.png
├── libs
│   ├── activation.jar
│   ├── additionnal.jar
│   ├── android-support-v4.jar
│   ├── gcm.jar
│   ├── imapstore.jar
│   ├── libGoogleAnalyticsServices.jar
│   └── mail.jar
├── lint.xml
├── proguard-project.txt
├── project.properties
├── res
│   ├── anim
│   │   ├── list_layout_controller.xml
│   │   ├── scale.xml
│   │   ├── transition_down_to_up.xml
│   │   ├── transition_left_to_right.xml
│   │   ├── transition_left_to_right_in.xml
│   │   ├── transition_left_to_right_out.xml
│   │   ├── transition_right_to_left.xml
│   │   ├── transition_right_to_left_out.xml
│   │   ├── transition_tohomefragment.xml
│   │   └── transition_top_to_down_out.xml
│   ├── drawable
│   │   ├── a.xml
│   │   ├── ...
│   │   └── whiteboardb.jpg
│   ├── drawable-hdpi
│   │   ├── apptheme_textfield_activated_holo_light.9.png
│   │   ├── ...
│   │   └── ic_whats_hot.png
│   ├── drawable-ldpi
│   │   ├── ic_about.png
│   │   ├── ...
│   │   └── ic_stat_logonoti.png
│   ├── drawable-mdpi
│   │   ├── apptheme_textfield_activated_holo_light.9.png
│   │   ├── ...
│   │   └── ic_stat_logonoti.png
│   ├── drawable-xhdpi
│   │   ├── apptheme_textfield_activated_holo_light.9.png
│   │   ├── ...
│   │   └── ic_stat_logonoti.png
│   ├── drawable-xxhdpi
│   │   ├── apptheme_textfield_activated_holo_light.9.png
│   │   ├── ...
│   │   └── ic_whats_hot.png
│   ├── drawable-xxxhdpi
│   │   └── ic_launcher.png
│   ├── layout
│   │   ├── add_email.xml
│   │   ├── drawer_list_item.xml
│   │   ├── emails_fragment.xml
│   │   ├── fragment_about.xml
│   │   ├── fragment_changepassword.xml
│   │   ├── fragment_feedback.xml
│   │   ├── fragment_home.xml
│   │   ├── fragment_photos.xml
│   │   ├── fragment_settings.xml
│   │   ├── image_upload.xml
│   │   ├── listview_item_row.xml
│   │   ├── login.xml
│   │   ├── preview_email.xml
│   │   ├── read_emails.xml
│   │   ├── register.xml
│   │   ├── register_fullname.xml
│   │   ├── register_mailivy.xml
│   │   ├── register_password.xml
│   │   ├── register_recovery.xml
│   │   ├── register_username.xml
│   │   ├── simple_dropdown_item.xml
│   │   ├── single_email.xml
│   │   └── single_post.xml
│   ├── menu
│   │   ├── activity_image_gallery.xml
│   │   ├── compose.xml
│   │   ├── login.xml
│   │   ├── main.xml
│   │   ├── openmail.xml
│   │   └── preview.xml
│   ├── raw
│   │   ├── delete.mp3
│   │   ├── sent.mp3
│   │   └── soundeffect.mp3
│   ├── values
│   │   ├── analytics.xml
│   │   ├── color.xml
│   │   ├── dimens.xml
│   │   ├── strings.xml
│   │   └── styles.xml
│   ├── values-sw600dp
│   │   └── dimens.xml
│   ├── values-sw720dp-land
│   │   └── dimens.xml
│   ├── values-v11
│   │   └── styles.xml
│   └── values-v14
│       └── styles.xml
└── src
    └── com
        └── smartikyapps
            └── smartmail
                ├── AddEmail.java
                ├── Base64.java
                ├── Contact.java
                ├── ContactAdapter.java
                ├── CustomSimpleAdapter.java
                ├── CustomTypefaceSpan.java
                ├── Email.java
                ├── EmailAdapter.java
                ├── EmailHolder.java
                ├── EmailPullService.java
                ├── EmailReceiver.java
                ├── EmailsFragment.java
                ├── GCMIntentService.java
                ├── JSONParser.java
                ├── Log.java
                ├── Login.java
                ├── MailSender.java
                ├── MyWebView.java
                ├── NavDrawerItem.java
                ├── NavDrawerListAdapter.java
                ├── NotificationOpen.java
                ├── PreviewEmail.java
                ├── ReadEmails.java
                ├── Register.java
                ├── SendMailTask.java
                ├── SingleEmail.java
                ├── SpaceTokenizer.java
                ├── fragments
                │   ├── AboutFragment.java
                │   ├── ChangepasswordFragment.java
                │   ├── FeedbackFragment.java
                │   ├── HomeFragment.java
                │   ├── PhotosFragment.java
                │   └── SettingsFragment.java
                └── gcm
                    ├── AlertDialogManager.java
                    ├── CommonUtilities.java
                    ├── ConnectionDetector.java
                    ├── IMAPWakeLocker.java
                    ├── ServerUtilities.java
                    └── WakeLocker.java
```

