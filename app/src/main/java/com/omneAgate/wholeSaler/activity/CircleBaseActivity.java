package com.omneAgate.wholeSaler.activity;

import android.app.Activity;
import android.os.Bundle;

import com.cardinalsolutions.android.arch.autowire.AndroidAutowire;

/**
 * Created by root on 6/10/16.
 */
public class CircleBaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutId = AndroidAutowire.getLayoutResourceByAnnotation(this,
                this, CircleBaseActivity.class);
        // If this activity is not annotated with AndroidLayout, do nothing
        if (layoutId == 0) {
            return;
        }
        setContentView(layoutId);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        AndroidAutowire.autowire(this, CircleBaseActivity.class);
    }
}
