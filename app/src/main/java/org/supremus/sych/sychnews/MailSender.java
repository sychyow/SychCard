package org.supremus.sych.sychnews;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class MailSender {
    private final Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
    private final ComponentName mailName;
    private static final String MAIL_TYPE = "message/rfc822";

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