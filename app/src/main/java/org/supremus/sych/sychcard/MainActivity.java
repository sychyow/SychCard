package org.supremus.sych.sychcard;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView cp = new TextView(this);
        cp.setText("© Dmitri Sychyow, 2018");
        cp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        cp.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        cp.setLayoutParams(lp);

        LinearLayout root = findViewById(R.id.ll_root);
        root.addView(cp);
    }
}
