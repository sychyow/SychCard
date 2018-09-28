package org.supremus.sych.sychnews;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class MailSender {
    private final Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
    private ComponentName mailName = null;
    private static String MAIL_TYPE = "text/plain";

    MailSender(Context context) {
        mailIntent.setType(MAIL_TYPE);
        mailIntent.setData(Uri.parse(String.format("mailto:%s", context.getString(R.string.mail_recipient))));
        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {context.getString(R.string.mail_recipient)});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.mail_subject));
        mailName = mailIntent.resolveActivity(context.getPackageManager());
    }

    public boolean hasActivity() {
        return mailName != null;
    }

    public Intent getSendIntent(String text) {
        mailIntent.putExtra(Intent.EXTRA_TEXT, text);
        return mailIntent;
    }
}