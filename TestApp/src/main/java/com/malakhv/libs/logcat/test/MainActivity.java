package com.malakhv.libs.logcat.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.malakhv.util.LogCat;

public class MainActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "LogTest";

    /**
     * setprop log.tag.xLogLib ERROR
     * setprop log.tag.xLogLib INFO
     * setprop log.tag.xLogLib DEBUG
     * setprop log.tag.xLogLib VERBOSE
     * */
    static {
        LogCat.init("xLogLib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        final int id = v != null ? v.getId() : 0;

        if (id == R.id.btn_e) {
            LogCat.e(TAG, "Log level - ERROR");
        }

        if (id == R.id.btn_w) {
            LogCat.w(TAG, "Log level - WARN");
        }

        if (id == R.id.btn_i) {
            LogCat.w(TAG, "Log level - INFO");
        }

        if (id == R.id.btn_d) {
            LogCat.d(TAG, "Log level - DEBUG");
        }

        if (id == R.id.btn_v) {
            LogCat.v(TAG, "Log level - VERBOSE");
        }

    }
}
