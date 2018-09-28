package org.supremus.sych.sychcard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private MailSender ms;
    private static Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView cp = new TextView(this);
        cp.setText(R.string.copyright);
        cp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        cp.setTextColor(Color.LTGRAY);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        cp.setLayoutParams(lp);

        LinearLayout root = findViewById(R.id.ll_root);
        root.addView(cp);

        ms = new MailSender(this);
        Button btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText etMessage = findViewById(R.id.et_message);
        String message = etMessage.getText().toString();
        if (message.length()==0) {
            Snackbar.make(v,getString(R.string.empty_message), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }
        if (!ms.hasActivity()) {
            Snackbar.make(v,getString(R.string.no_mail), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }

        Intent mailIntent = ms.getSendIntent(message);
        startActivity(mailIntent);
    }

    public static void launch(Activity parent) {
        if (intent == null) intent = new Intent(parent, AboutActivity.class);
        parent.startActivity(intent);
    }
}