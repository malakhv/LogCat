# xLogCat
The library project includes classes for writing an app log in Android. Is essentially a wrapper for the standard Android [Log](https://developer.android.com/reference/android/util/Log.html) class. The main difference is the availability check on the current level logs while trying to log. Also, there is a possibility to change the log level for the application at runtime via adb.

### LogCat class
This class extends standard Android API for sending a log output. This class allows you to create log messages (similarly `android.util.Log` class). Generally, you should use the following `LogCat` methods, listed in order from the highest to lowest priority (or, least to most verbose):
* `LogCat.e()` (ERROR)
* `LogCat.w()` (WARN)
* `LogCat.i()` (INFO)
* `LogCat.d()` (DEBUG)
* `LogCat.v()` (VERBOSE)

You should never compile `VERBOSE` logs into your app, except during development. `DEBUG` logs are compiled in but stripped at runtime, while `ERROR`, `WARN` and `INFO` logs are always kept.

Before using this class for write logs, you should initialize it and specify the main log tag for your app (further `APP_TAG`). This tag will be used into all methods for print logs as unique log tag. This is the one of main difference between the class `LogCat` and Android `android.util.Log`.

__Tip:__ A good conventions are to use the app name as the main log tag and to declare an appropriate constant for this tag in your app class, for example:
```Java
private static final String APP_TAG = "MyApp";
```
Then you could use this constant in method `LogCat.init(String)`.

The `LogCat` class allows you to use different log tags for various components in your code (classes, methods and etc.) in addition to the main log tag for your app (as example `APP_TAG`).A good convention is to declare a `TAG` constant in your class:
```Java
private static final String TAG = "MyActivity";
```
and use that in subsequent calls to the log methods. This tag will be added to LogCat message:
```Text
 LogCat.d(TAG, "Any message.");
 Output: "D/MyApp: MyActivity: Any message."
 ```
You could change log level for your app using main app log tag. The default level of any tag is set to `INFO`. This means that any level above and including `INFO` will be logged. You can change the default level by setting a system property:
```Text
adb shell setprop log.tag.[APP_TAG] [LEVEL]
```
Where level is either `VERBOSE`, `DEBUG`, `INFO`, `WARN`, `ERROR`, `ASSERT`, or `SUPPRESS`. `SUPPRESS` will turn off all logging for your tag. You can also create a local.prop file that with the following in it:
```Text
log.tag.[APP_TAG]=[LEVEL]
```
and place that in /data/local.prop.

 __Tip:__ Don't forget to change log level for main app log tag to `DEBUG` via shell command, or you could specify debug flag during initialization, for example:
```Java
LogCat.init(APP_TAG, BuildConfig.DEBUG);
```
### License
```Text
Copyright (C) 2013 Mikhail Malakhov <malakhv@live.ru>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```