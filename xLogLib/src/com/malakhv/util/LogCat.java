/**
 * Copyright (C) 2013 Mikhail Malakhov <malakhv@live.ru>
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
 * Class extends standard Android API for sending a log output.
 *
 * <p>The {@link LogCat} class allows you to create log messages (similarly {@link android.util.Log}
 * class). Generally, you should use the following {@link LogCat} methods, listed in order from the
 * highest to lowest priority (or, least to most verbose):
 * <ul>
 *  <li>{@link LogCat#e(String)} (ERROR)</li>
 *  <li>{@link LogCat#w(String)} (WARN)</li>
 *  <li>{@link LogCat#i(String)} (INFO)</li>
 *  <li>{@link LogCat#d(String)} (DEBUG)</li>
 *  <li>{@link LogCat#v(String)} (VERBOSE)</li>
 * </ul></p>
 *
 * <p>You should never compile VERBOSE logs into your app, except during development. DEBUG logs are
 * compiled in but stripped at runtime, while ERROR, WARN and INFO logs are always kept.</p>
 *
 * <p>Before using this class for write logs, you should initialize it and specify the main log
 * tag for your app (further {@code APP_TAG}). This tag will be used into all methods
 * for print logs as unique log tag. This is the main difference between the class {@link LogCat}
 * and Android {@link android.util.Log}.</p>
 *
 * <p><b>Tip:</b> A good conventions are to use the app name as the main log tag and to declare
 * an appropriate constant for this tag in your app class, for example:
 * <br>&nbsp;<br>
 * &#09;{@code private static final String APP_TAG = "MyApp";}
 * <br>&nbsp;<br>
 * then you could use this constant in method {@link LogCat#init(String)}.</p>
 *
 * <p>The {@link LogCat} class allows you to use different log tags for various components in your
 * code (classes, methods and etc.) in addition to the main log tag for your app (as example
 * {@code APP_TAG}).A good convention is to declare a <code>TAG</code> constant in your class:
 * <br>&nbsp;<br>
 * &#09;{@code private static final String TAG = "MyActivity";}
 * <br>&nbsp;<br>
 * and use that in subsequent calls to the log methods. This tag will be added to LogCat message:
 * <br>&nbsp;<br>
 * &#09;{@code LogCat.d(TAG, "Any message.");}<br>
 * &#09;{@code Output: "D/MyApp: MyActivity: Any message."}
 * <br>&nbsp;<br></p>
 *
 * <p>You could change log level for your app using main app log tag. The default level of any tag
 * is set to INFO. This means that any level above and including INFO will be logged. You can
 * change the default level by setting a system property:
 * <br>&nbsp;<br>
 * &#09;{@code adb shell setprop log.tag.[APP_TAG] [LEVEL]}
 * <br>&nbsp;<br>
 * Where level is either VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS. SUPPRESS will turn
 * off all logging for your tag. You can also create a local.prop file that with the following
 * in it:
 * <br>&nbsp;<br>
 * &#09;{@code log.tag.[APP_TAG]=[LEVEL]}
 * <br>&nbsp;<br>
 * and place that in {@code /data/local.prop}.</p>
 *
 * <p><b>Tip:</b> Don't forget to change log level for main app log tag to DEBUG via shell command,
 * or you could specify debug flag during initialization, for example:
 * <br>&nbsp;<br>
 * &#09;{@code LogCat.init(APP_TAG, BuildConfig.DEBUG);}
 * <br>&nbsp;<br>
 * </p>
 *
 * <p><b>Tip:</b>You could use this class for printing a stack trace. The stack trace information
 * printed as usual app log, and you could specify tag and priority. For more details, please, see
 * {@link #printStackTrace(String, int, Thread)}</p>
 *
 * @author Mikhail.Malakhov [malakhv@live.ru|https://github.com/malakhv]
 *
 * @see android.util.Log
 * @see LogCat#init(String)
 * */
@SuppressWarnings("unused")
public final class LogCat {

    /** Tag for LogCat. */
    private static final String LOG_TAG = LogCat.class.getSimpleName();

    /*----------------------------------------------------------------------------------------*/
    /* General constants
    /*----------------------------------------------------------------------------------------*/

    /** String that will use as delimiter for tags in LogCat message. */
    private static final String TAG_DELIMITER = ": ";

    /** The maximum length of tag. */
    private static final int TAG_MAX_LENGTH = 23;

    /*----------------------------------------------------------------------------------------*/
    /* Grabbing the native values from Android's native logging facilities,
    /* to make for easy migration, interaction and accessibility.
    /*----------------------------------------------------------------------------------------*/

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

    /*----------------------------------------------------------------------------------------*/
    /* General static fields
    /*----------------------------------------------------------------------------------------*/

    /** The main LogCat tag for app. */
    private static String sAppTag = null;

    /**
     * The debug flag. If it equals {@code true}, all log levels will be enabled for
     * {@link LogCat#sAppTag}. Use {@code true} for debug mode only.
     * */
    private static boolean sDebug = false;

    /** State of initialisation {@link LogCat} in app. */
    private static boolean sWasInit = false;

    /*----------------------------------------------------------------------------------------*/
    /* Constructor and static initialization methods
    /*----------------------------------------------------------------------------------------*/

    /**
     * This class has only static data, not need to create instance.
     * */
    private LogCat() {}

    /**
     * Before using this class for write logs, you should initialize it and specify the main log
     * tag for your app. This tag will be used into all methods for print logs as unique log tag.
     *
     * @param tag The main app LogCat tag that will use into all methods for print logs.
     * @throws IllegalArgumentException If the appTag is null or empty, or the appTag.length() > 23.
     * */
    public static void init(String tag) { init(tag, false); }

    /**
     * Before using this class for write logs, you should initialize it and specify the main log
     * tag for your app. This tag will be used into all methods for print logs as unique log tag.
     *
     * @param tag The main app LogCat tag that will use into all methods for print logs.
     * @param debug If it equals {@code true}, all log levels will be enabled for specified
     *              {@code tag}. Use {@code true} for debug mode only.
     * @throws IllegalArgumentException If the appTag is null or empty, or the appTag.length() > 23.
     * */
    public static void init(String tag, boolean debug) {
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
        sDebug = debug;
        sWasInit = true;
        LogCat.d(LOG_TAG, "Init with app tag - " + sAppTag);
    }

    /**
     * Returns {@code true} if the tag is null or empty.
     * @param tag The tag to be examined.
     * */
    private static boolean isTagEmpty(String tag) {
        return TextUtils.isEmpty(tag) || TextUtils.isEmpty(tag.trim());
    }

    /**
     * Checks that this class was been initialized or not.
     * @throws IllegalStateException if this class was not been initialized.
     * */
    private static void checkInit() {
        if (!sWasInit) throw new IllegalStateException(LOG_TAG + TAG_DELIMITER +
                "You should init LogCat before use it.");
    }

    /*----------------------------------------------------------------------------------------*/
    /* Methods for checking current log level
    /*----------------------------------------------------------------------------------------*/

    /**
     * Checks to see whether or not a log for the specified tag is loggable at the specified level.
     *
     * @param level The level to check.
     * @return Whether or not that this is allowed to be logged.
     * @throws IllegalArgumentException is thrown if the tag.length() > 23.
     * */
    public static boolean isLoggable(int level) {
        checkInit();
        return sDebug || android.util.Log.isLoggable(sAppTag, level);
    }

    /**
     * Returns {@code true} if log level for tag is {@link LogCat#DEBUG} or debug flag in
     * {@link LogCat} is {@code true}.
     * */
    public static boolean isDebug() { return isLoggable(DEBUG); }

    /*----------------------------------------------------------------------------------------*/
    /* Log level - VERBOSE
    /*----------------------------------------------------------------------------------------*/

    /**
     * Send a {@link #VERBOSE} log message.
     * @param msg The message you would like logged.
     * */
    public static int v(String msg) { return LogCat.v(null, msg); }

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * */
    public static int v(String tag, String msg) { return LogCat.println(VERBOSE, tag, msg); }

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param format The format string (see {@link java.util.Formatter#format}).
     * @param args The list of arguments passed to the formatter. If there are more arguments than
     *             required by {@code format}, additional arguments are ignored.
     *
     * @throws NullPointerException if {@code format == null}.
     * @throws java.util.IllegalFormatException if the format is invalid.
     * */
    public static int v(String tag, String format, Object... args) {
        return LogCat.println(VERBOSE, tag, format, args);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     * */
    public static int v(String msg, Throwable tr) { return LogCat.v(null, msg, tr); }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     * */
    public static int v(String tag, String msg, Throwable tr) {
        return LogCat.println(VERBOSE, tag, msg, tr);
    }

    /*----------------------------------------------------------------------------------------*/
    /* Log level - DEBUG
    /*----------------------------------------------------------------------------------------*/

    /**
     * Send a {@link #DEBUG} log message.
     * @param msg The message you would like logged.
     * */
    public static int d(String msg) { return LogCat.d(null, msg); }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * */
    public static int d(String tag, String msg) { return LogCat.println(DEBUG, tag, msg); }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param format The format string (see {@link java.util.Formatter#format}).
     * @param args The list of arguments passed to the formatter. If there are more arguments than
     *             required by {@code format}, additional arguments are ignored.
     *
     * @throws NullPointerException if {@code format == null}.
     * @throws java.util.IllegalFormatException if the format is invalid.
     * */
    public static int d(String tag, String format, Object... args) {
        return LogCat.println(DEBUG, tag, format, args);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     * */
    public static int d(String msg, Throwable tr) { return LogCat.d(null, msg, tr); }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     * */
    public static int d(String tag, String msg, Throwable tr) {
        return LogCat.println(DEBUG, tag, msg, tr);
    }

    /*----------------------------------------------------------------------------------------*/
    /* Log level - INFO
    /*----------------------------------------------------------------------------------------*/

    /**
     * Send an {@link #INFO} log message.
     *
     * @param msg The message you would like logged.
     * */
    public static int i(String msg) { return LogCat.i(null, msg); }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * */
    public static int i(String tag, String msg) { return LogCat.println(INFO, tag, msg); }

    /**
     * Send a {@link #INFO} log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param format The format string (see {@link java.util.Formatter#format}).
     * @param args The list of arguments passed to the formatter. If there are more arguments than
     *             required by {@code format}, additional arguments are ignored.
     *
     * @throws NullPointerException if {@code format == null}.
     * @throws java.util.IllegalFormatException if the format is invalid.
     * */
    public static int i(String tag, String format, Object... args) {
        return LogCat.println(INFO, tag, format, args);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     * */
    public static int i(String msg, Throwable tr) { return LogCat.i(null, msg, tr); }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     * */
    public static int i(String tag, String msg, Throwable tr) {
        return LogCat.println(INFO, tag, msg, tr);
    }

    /*----------------------------------------------------------------------------------------*/
    /* Log level - WARN
    /*----------------------------------------------------------------------------------------*/

    /**
     * Send a {@link #WARN} log message.
     * @param msg The message you would like logged.
     * */
    public static int w(String msg) { return LogCat.w(null, msg); }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * */
    public static int w(String tag, String msg) { return LogCat.println(WARN, tag, msg); }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param format The format string (see {@link java.util.Formatter#format}).
     * @param args The list of arguments passed to the formatter. If there are more arguments than
     *             required by {@code format}, additional arguments are ignored.
     *
     * @throws NullPointerException if {@code format == null}.
     * @throws java.util.IllegalFormatException if the format is invalid.
     * */
    public static int w(String tag, String format, Object... args) {
        return LogCat.println(WARN, tag, format, args);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     * */
    public static int w(String tag, String msg, Throwable tr) {
        return LogCat.println(WARN, tag, msg, tr);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tr An exception to log.
     * */
    public static int w(Throwable tr) { return LogCat.w(null, tr); }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param tr  An exception to log.
     * */
    public static int w(String tag, Throwable tr) {
        return LogCat.println(WARN, tag, android.util.Log.getStackTraceString(tr));
    }

    /*----------------------------------------------------------------------------------------*/
    /* Log level - ERROR
    /*----------------------------------------------------------------------------------------*/

    /**
     * Send an {@link #ERROR} log message.
     * @param msg The message you would like logged.
     */
    public static int e(String msg) { return LogCat.e(null, msg); }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * */
    public static int e(String tag, String msg) { return LogCat.println(ERROR, tag, msg); }

    /**
     * Send a {@link #ERROR} log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param format The format string (see {@link java.util.Formatter#format}).
     * @param args The list of arguments passed to the formatter. If there are more arguments than
     *             required by {@code format}, additional arguments are ignored.
     *
     * @throws NullPointerException if {@code format == null}.
     * @throws java.util.IllegalFormatException if the format is invalid.
     * */
    public static int e(String tag, String format, Object... args) {
        return LogCat.println(ERROR, tag, format, args);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     * */
    public static int e(String msg, Throwable tr) { return LogCat.e(null, msg, tr); }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     * */
    public static int e(String tag, String msg, Throwable tr) {
        return LogCat.println(ERROR, tag, msg, tr);
    }

    /*----------------------------------------------------------------------------------------*/
    /* Stacktrace
    /*----------------------------------------------------------------------------------------*/

    /**
     * Send a {@link #DEBUG} log message that contains a printable representation of current
     * {@link Thread}'s stack trace.
     * */
    public static void printStackTrace() {
        printStackTrace(null, DEBUG, Thread.currentThread());
    }

    /**
     * Send a log message that contains a printable representation of current {@link Thread}'s
     * stack trace.
     * @param priority The priority/type of this log message.
     * */
    public static void printStackTrace(int priority) {
        printStackTrace(null, priority, Thread.currentThread());
    }

    /**
     * Send a log message that contains a printable representation of current {@link Thread}'s
     * stack trace.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param priority The priority/type of this log message.
     * */
    public static void printStackTrace(String tag, int priority) {
        printStackTrace(tag, priority, Thread.currentThread());
    }

    /**
     * Send a log message that contains a printable representation of specified {@link Thread}'s
     * stack trace.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param priority The priority/type of this log message.
     * @param thread The {@link Thread}, for which a stack trace will be sent.
     * */
    public static void printStackTrace(String tag, int priority, Thread thread) {
        checkInit();
        if (thread == null) {
            LogCat.w("Cannot print stack trace for thread - thread is null"); return;
        }
        final String NEW_LINE = "\n";
        StackTraceElement[] elements = thread.getStackTrace();
        final StringBuilder builder = new StringBuilder(elements.length * 2);
        //TODO Could we remove from output following elements?:
        //TODO  - dalvik.system.VMStack.getThreadStackTrace(Native Method)
        //TODO  - java.lang.Thread.getStackTrace(Thread.java:580)
        //TODO  - com.malakhv.util.LogCat.printStackTrace(LogCat.java:534)
        //TODO  - com.malakhv.util.LogCat.printStackTrace(LogCat.java:520)
        for (StackTraceElement element: elements) {
            builder.append(element.toString()).append(NEW_LINE);
        }
        LogCat.println(priority, tag, builder.toString());
    }

    /*----------------------------------------------------------------------------------------*/
    /* Threads
    /*----------------------------------------------------------------------------------------*/

    /**
     * Send a {@link #DEBUG} log message that contains information (name, priority, group) about
     * all active threads in current {@link ThreadGroup}.
     * */
    public static void printThreads() {
        printThreads(null, DEBUG, Thread.currentThread().getThreadGroup());
    }

    /**
     * Send a log message that contains information (name, priority, group) about all active
     * threads in current {@link ThreadGroup}.
     * @param priority The priority/type of this log message.
     * */
    public static void printThreads(int priority) {
        printThreads(null, priority, Thread.currentThread().getThreadGroup());
    }

    /**
     * Send a log message that contains information (name, priority, group) about all active
     * threads in current {@link ThreadGroup}.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param priority The priority/type of this log message.
     * */
    public static void printThreads(String tag, int priority) {
        printThreads(tag, priority, Thread.currentThread().getThreadGroup());
    }

    /**
     * Send a log message that contains information (name, priority, group) about all active
     * threads in specified {@link ThreadGroup}.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param priority The priority/type of this log message.
     * @param group The group of threads.
     * */
    public static void printThreads(String tag, int priority, ThreadGroup group) {
        final String NEW_LINE = "\n";
        int count = group.activeCount();
        final Thread[] threads = new Thread[count];
        int copied = group.enumerate(threads, true);
        final StringBuilder builder = new StringBuilder(copied * 2);
        builder.append(NEW_LINE);
        for (int i = 0; i < copied; i++) {
            builder.append(threads[i].toString()).append(NEW_LINE);
        }
        LogCat.println(priority, tag, builder.toString());
    }

    /*----------------------------------------------------------------------------------------*/
    /* Memory
    /*----------------------------------------------------------------------------------------*/

    /**
     * Send a log message that contains information about memory usage.
     * @param priority The priority/type of this log message.
     * */
    public static void printMemoryInfo(int priority) {
        checkInit();
        if (LogCat.isLoggable(priority)) printMemoryInfo_native(sAppTag, priority);
    }

    /*----------------------------------------------------------------------------------------*/
    /* Low-level logging calls
    /*----------------------------------------------------------------------------------------*/

    /**
     * Low-level logging call.
     *
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
     * Low-level logging call.
     *
     * @param priority The priority/type of this log message.
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param format The format string (see {@link java.util.Formatter#format}).
     * @param args The list of arguments passed to the formatter. If there are more arguments than
     *             required by {@code format}, additional arguments are ignored.
     * @return The number of bytes written.
     * @throws NullPointerException if {@code format == null}.
     * @throws java.util.IllegalFormatException if the format is invalid.
     * */
    @SuppressLint("LogTagMismatch")
    private static int println(int priority, String tag, String format, Object... args) {
        // This is a double check, but it is faster than String.format()
        if (LogCat.isLoggable(priority)) {
            final String msg = String.format(format, args);
            return LogCat.println(priority, tag, msg);
        } else {
            return -1;
        }
    }

    /**
     * Low-level logging call.
     * @param priority The priority/type of this log message.
     * @param tag Used to identify the source of a log message.  It usually identifies the class or
     *            activity where the log call occurs. Maybe {@code null}.
     * @param msg The message you would like logged.
     * @param tr An exception to log.
     * @return The number of bytes written.
     * */
    private static int println(int priority, String tag, String msg, Throwable tr) {
        return LogCat.println(priority, tag, msg + "\n" +
                android.util.Log.getStackTraceString(tr));
    }

    /*----------------------------------------------------------------------------------------*/
    /* Native
    /*----------------------------------------------------------------------------------------*/

    /** Send a log message that contains information about memory usage. */
    private static native void printMemoryInfo_native(String tag, int priority);

    /** Load a native library */
    static {
        System.loadLibrary("logcat-native-lib");
    }
}