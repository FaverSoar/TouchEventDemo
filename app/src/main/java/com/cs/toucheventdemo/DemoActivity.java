package com.cs.toucheventdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by charmingsoft on 2016/3/15.
 */
public class DemoActivity  extends Activity {
    private static final String TAG = "DemoActivity_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        Log.d(TAG, "onCreate");
        initView();
    }

    private void initView() {
    }

}
