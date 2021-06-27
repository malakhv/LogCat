package com.malakhv.libs.logcat.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.malakhv.util.LogCat;

public class MainActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "LogTest";

    private LogCat.LogObfuscator mObfuscator = null;

    /* *
     * setprop log.tag.xLogLib ERROR
     * setprop log.tag.xLogLib INFO
     * setprop log.tag.xLogLib DEBUG
     * setprop log.tag.xLogLib VERBOSE
     * */
    static {
        LogCat.init("  xLogLib ", BuildConfig.DEBUG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mObfuscator = new LogCat.LogObfuscator() {
            @Override
            public String obfuscate(String msg) {
                return msg;
            }
        };
    }

    /**
     * Called when a view has been clicked.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        final int id = v != null ? v.getId() : 0;

        if (id == R.id.btn_e) {
            LogCat.e(TAG, "Log level - ERROR");
            LogCat.printStackTrace(LogCat.ERROR);
            LogCat.printThreads(LogCat.ERROR);
        }

        if (id == R.id.btn_w) {
            LogCat.w(TAG, "Log level - WARN");
            LogCat.printStackTrace(LogCat.WARN);
            LogCat.printThreads(LogCat.WARN);
        }

        if (id == R.id.btn_i) {
            LogCat.i(TAG, "Log level - INFO");
            LogCat.printStackTrace(LogCat.INFO);
            LogCat.printThreads(LogCat.INFO);
        }

        if (id == R.id.btn_d) {
            LogCat.d(TAG, "Log level - DEBUG", true);

            final String message = "Ivanov: +71234567890";
            LogCat.d(TAG, message); // 0 - not obfuscated
            LogCat.d(TAG, message, true); // 1 - obfuscated
            LogCat.setObfuscator(mObfuscator); // Empty obfuscator
            LogCat.d(TAG, message); // 0 - Empty obfuscator
            LogCat.d(TAG, message, true); // 0 - Empty obfuscator

            LogCat.d(TAG, "===============");
            LogCat.setObfuscator(LogCat.SIMPLE_NUMBER_OBFUSCATOR);
            LogCat.setObfuscateByDefault(true);

            LogCat.d(TAG, message); // 1 - by default
            LogCat.d(TAG, message, false); // 0
            LogCat.setObfuscator(mObfuscator); // Empty obfuscator
            LogCat.d(TAG, message); // 0 - Empty obfuscator
            LogCat.d(TAG, message, false); // 0 - Empty obfuscator
        }

        if (id == R.id.btn_v) {
            LogCat.v(TAG, "Log level - VERBOSE");
            LogCat.printStackTrace(LogCat.VERBOSE);
            makeThreads(7);
            LogCat.printThreads(TAG, LogCat.VERBOSE);
        }

    }

    /** Make empty threads */
    private void makeThreads(int count) {
        for (int i = 0; i < count; i++) {
            new Thread(mEmptyRunnable,"TEST - " + i).start();
        }
    }

    /** Empty runnable */
    private final Runnable mEmptyRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(35 * 1000);
            } catch (InterruptedException ignored) {}
        }
    };
}