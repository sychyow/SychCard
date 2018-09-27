package org.supremus.sych.sychcard;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MailSender ms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView cp = new TextView(this);
        cp.setText("© Dmitri Sychyow, 2018");
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
        if (!ms.hasActivity()) {
            Snackbar.make(v,"Mail app not available", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }
        EditText etMessage = findViewById(R.id.et_message);
        Intent mailIntent = ms.getSendIntent(etMessage.getText().toString());
        startActivity(mailIntent);
    }
}