/**
 * Copyright (C) 2013 Mikhail Malakhov (malakhv@live.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */
package com.malakhv.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

/**
 * API for sending log output.
 *
 * <p>Generally, use the LogCat.v() LogCat.d() LogCat.i() LogCat.w() and LogCat.e() methods.</p>
 *
 * <p>The order in terms of verbosity, from least to most is ERROR, WARN, INFO, DEBUG, VERBOSE.
 * Verbose should never be compiled into an application except during development. Debug logs are
 * compiled in but stripped at runtime. Error, warning and info logs are always kept. </p>
 *
 * <p>Before using this class for write logs, you should initialize it and specify the main LogCat
 * tag for your app. This tag will be used into all methods for print logs as LogCat tag.</p>
 *
 * <p><b>Tip:</b> You could change LogCat level for your app using main app LogCat tag</p>
 *
 * <p><b>Tip:</b> The default level of any tag is set to INFO. This means that any level above and
 * including INFO will be logged. You can change the default level by setting a system property:
 * <pre>
 *     {@code adb shell setprop log.tag.YOUR_MAIN_APP_LOG_TAG LEVEL}
 * </pre>
 * Where level is either VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS. SUPPRESS will turn
 * off all logging for your tag.</p>
 *
 * <p><b>Tip:</b> A good conventions are to use the app name as the main LogCat tag and to declare
 * an appropriate constant for this tag in your app class, for example:
 * <pre>
 *     public static final String APP_TAG = "MyApp";
 * </pre>
 * then you could use this constant in method {@link LogCat#init(String)}.
 * </p>
 *
 * <p><b>Tip:</b> A good convention is to declare a <code>TAG</code> constant in your class:
 * <pre>
 *     private static final String TAG = "MyActivity";
 * </pre>
 * and use that in subsequent calls to the log methods. This tag will be added to LogCat message:
 * <pre>
 *     LogCat.d(TAG, "Any message.");
 *     Output: "D/MyApp: MyActivity: Any message."
 * </pre></p>
 *
 * @author Mikhail.Malakhov <malakhv@live.ru>
 *
 * @see android.util.Log
 * @see LogCat#init(String)
 * */
@SuppressWarnings("unused")
public final class LogCat {

    /** Tag for LogCat. */
    private static final String LOG_TAG = LogCat.class.getSimpleName();

    /** String that will use as delimiter for tags in LogCat message. */
    private static final String TAG_DELIMITER = ": ";

    /** The maximum length of tag. */
    private static final int TAG_MAX_LENGTH = 23;

    // Grabbing the native values from Android's native logging facilities, to make for easy
    // migration, interaction and accessibility.

    /** Priority constant for the println method; use LogCat.v. */
    public static final int VERBOSE = android.util.Log.VERBOSE;

    /** Priority constant for the println method; use LogCat.d. */
    public static final int DEBUG = android.util.Log.DEBUG;

    /** Priority constant for the println method; use LogCat.i. */
    public static final int INFO = android.util.Log.INFO;

    /** Priority constant for the println method; use LogCat.w. */
    public static final int WARN = android.util.Log.WARN;

    /** Priority constant for the println method; use LogCat.e. */
    public static final int ERROR = android.util.Log.ERROR;

    /** Priority constant for the println method. */
    public static final int ASSERT = android.util.Log.ASSERT;

    /** The main LogCat tag for app. */
    private static String sAppTag = null;

    /** State of initialisation {@link LogCat} in app. */
    private static boolean sWasInit = false;

    /**
     * This class has only static data, not need to create instance.
     */
    private LogCat() {}

    /**
     * Returns {@code true} if the tag is null or empty.
     * @param tag The tag to be examined.
     * */
    private static boolean isTagEmpty(String tag) {
        return TextUtils.isEmpty(tag) || TextUtils.isEmpty(tag.trim());
    }

    /**
     * Before using this class for write logs, you should initialise it and specify the main LogCat
     * tag for your app. This tag will be used into all methods for print logs as LogCat tag. You
     * can change the default level by setting a system property:
     * <pre>
     *     {@code adb shell setprop log.tag.YOUR_MAIN_APP_LOG_TAG LEVEL}
     * </pre>
     * Where level is either VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS. SUPPRESS will
     * turn off all logging for your tag.
     *
     * @param tag The main app LogCat tag that will use into all methods for print logs.
     * @throws IllegalArgumentException If the appTag is null or empty, or the appTag.length() > 23.
     */
    public static void init(String tag) {

        // Check initial tag is null or empty
        if (isTagEmpty(tag)) {
            throw new IllegalArgumentException(LOG_TAG + TAG_DELIMITER +
                    "The tag is null or empty");
        }

        // Check initial tag is not very long
        if (tag.length() > TAG_MAX_LENGTH) {
            throw new IllegalArgumentException(LOG_TAG + TAG_DELIMITER + "The tag is too long");
        }

        sAppTag = tag.trim();
        sWasInit = true;
        LogCat.d(LOG_TAG, "Init with app tag - " + sAppTag);
    }

    /**
     * Send a {@link #VERBOSE} log message.
     * @param msg The message you would like logged.
     */
    public static int v(String msg) { return LogCat.v(null, msg); }

    /**
     * Send a {@link #VERBOSE} log message.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
        return LogCat.println(VERBOSE, tag, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static int v(String msg, Throwable tr) {
        return LogCat.v(null, msg, tr);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static int v(String tag, String msg, Throwable tr) {
        return LogCat.println(VERBOSE, tag, msg, tr);
    }

    /**
     * Send a {@link #DEBUG} log message.
     * @param msg The message you would like logged.
     */
    public static int d(String msg) { return LogCat.d(null, msg); }

    /**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
        return LogCat.println(DEBUG, tag, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static int d(String msg, Throwable tr) { return LogCat.d(null, msg, tr); }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static int d(String tag, String msg, Throwable tr) {
        return LogCat.println(DEBUG, tag, msg, tr);
    }

    /**
     * Send an {@link #INFO} log message.
     * @param msg The message you would like logged.
     */
    public static int i(String msg) { return LogCat.i(null, msg); }

    /**
     * Send an {@link #INFO} log message.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) { return LogCat.println(INFO, tag, msg); }

    /**
     * Send a {@link #INFO} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static int i(String msg, Throwable tr) {
        return LogCat.i(null, msg, tr);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static int i(String tag, String msg, Throwable tr) {
        return LogCat.println(INFO, tag, msg, tr);
    }

    /**
     * Send a {@link #WARN} log message.
     * @param msg The message you would like logged.
     */
    public static int w(String msg) {
        return LogCat.w(null, msg);
    }

    /**
     * Send a {@link #WARN} log message.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
        return LogCat.println(WARN, tag, msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static int w(String tag, String msg, Throwable tr) {
        return LogCat.println(WARN, tag, msg, tr);
    }

    /**
     * Checks to see whether or not a log for the specified tag is loggable at the specified level.
     *
     * <p>The default level of any tag is set to INFO. This means that any level above and
     * including INFO will be logged. Before you make any direct calls to a logging method you
     * should check to see if your tag should be logged. You can change the default level by
     * setting a system property:
     * <pre>
     *     {@code adb shell setprop log.tag.YOUR_MAIN_APP_LOG_TAG LEVEL}
     * </pre>
     * Where level is either VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS. SUPPRESS will
     * turn off all logging for your tag. You can also create a local.prop file that with the
     * following in it:
     * <pre>
     *     {@code log.tag.YOUR_MAIN_APP_LOG_TAG=LEVEL}
     * </pre>
     * and place that in /data/local.prop.</p>
     *
     * @param level The level to check.
     * @return Whether or not that this is allowed to be logged.
     * @throws IllegalArgumentException is thrown if the tag.length() > 23.
     */
    public static boolean isLoggable(int level) {
        checkInit();
        return android.util.Log.isLoggable(sAppTag, level);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tr An exception to log.
     */
    public static int w(Throwable tr) {
        return LogCat.w(null, tr);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param tr  An exception to log.
     */
    public static int w(String tag, Throwable tr) {
        return LogCat.println(WARN, tag, android.util.Log.getStackTraceString(tr));
    }

    /**
     * Send an {@link #ERROR} log message.
     * @param msg The message you would like logged.
     */
    public static int e(String msg) {
        return LogCat.e(null, msg);
    }

    /**
     * Send an {@link #ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
        return LogCat.println(ERROR, tag, msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static int e(String msg, Throwable tr) {
        return LogCat.e(null, msg, tr);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static int e(String tag, String msg, Throwable tr) {
        return LogCat.println(ERROR, tag, msg, tr);
    }

    /**
     * Low-level logging call.
     * @param priority The priority/type of this log message.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     * */
    @SuppressLint("LogTagMismatch")
    private static int println(int priority, String tag, String msg) {
        checkInit();
        if (LogCat.isLoggable(priority)) {
            final String m = (!isTagEmpty(tag)) ? tag + TAG_DELIMITER + msg : msg;
            return android.util.Log.println(priority, sAppTag, m);
        } else {
            return -1;
        }
    }

    /**
     * Checks that this class was been initialized or not.
     * @throws IllegalStateException if this class was not been initialized.
     */
    private static void checkInit() {
        if (!sWasInit) throw new IllegalStateException(LOG_TAG + TAG_DELIMITER +
                "You should init LogCat before use it.");
    }

    /**
     * Low-level logging call.
     * @param priority The priority/type of this log message.
     * @param tag Used to identify the source of a log message.  It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr An exception to log.
     * @return The number of bytes written.
     */
    private static int println(int priority, String tag, String msg, Throwable tr) {
        return LogCat.println(priority, tag, msg + "\n" +
                android.util.Log.getStackTraceString(tr));
    }
}