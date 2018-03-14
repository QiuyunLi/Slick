package com.mrezanasirloo.slick.sample.activity.dagger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mrezanasirloo.slick.Presenter;
import com.mrezanasirloo.slick.sample.App;
import com.mrezanasirloo.slick.sample.R;
import com.mrezanasirloo.slick.sample.activity.ViewTestable;
import com.mrezanasirloo.slick.test.SlickPresenterTestable;

import javax.inject.Inject;
import javax.inject.Provider;

public class ActivitySimpleDagger extends AppCompatActivity implements ViewSimpleDagger {

    @Inject
    Provider<PresenterSimpleDagger> provider;
    @Presenter
    PresenterSimpleDagger presenter;

    private static final String TAG = ActivitySimpleDagger.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getDaggerComponent(this).inject(this);
        PresenterSimpleDagger_Slick.bind(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            Log.d(TAG, "onDestroy() called disposing");
            App.disposeDaggerComponent(this);
        }
    }

    @Override
    public SlickPresenterTestable<? extends ViewTestable> presenter() {
        return presenter;
    }
}