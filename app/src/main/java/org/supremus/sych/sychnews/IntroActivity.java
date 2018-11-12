package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.printservice.PrintJob;

import java.util.concurrent.TimeUnit;

public class IntroActivity extends AppCompatActivity {

    private static final String INTRO_PREF = "INTRO";
    private static final Boolean INTRO_DEF = true;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (needIntro()) {
            setContentView(R.layout.activity_intro);
            Disposable disposable = Completable.complete()
                    .delay(3, TimeUnit.SECONDS)
                    .subscribe(this::startSecondActivity);
            compositeDisposable.add(disposable);
        } else {
            startSecondActivity();
        }
        updatePref();
    }

    private boolean needIntro() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean introState = sharedPref.getBoolean(INTRO_PREF, INTRO_DEF);
        return introState;
    }

    private void updatePref() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sharedPref.edit();
        Boolean introState = sharedPref.getBoolean(INTRO_PREF, INTRO_DEF);
        ed.putBoolean(INTRO_PREF, !introState);
        ed.commit();
    }

    private void startSecondActivity() {
        startActivity(new Intent(this, NewsListActivity.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }

}
