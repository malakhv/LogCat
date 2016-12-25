# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)

# Added logcat-native-lib library
# com_malakhv_util_LogCat.cpp
add_library(logcat-native-lib SHARED logcat-native-lib.c)

# Include libraries needed for logcat-native-lib lib
target_link_libraries(logcat-native-lib android log)