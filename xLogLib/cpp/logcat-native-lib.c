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

/**
 * Author Mikhail.Malakhov [malakhv@live.ru|https://github.com/malakhv]
 * */

#include <jni.h>
#include <stdio.h>
#include <fcntl.h>

#include <android/log.h>

/* The mem info virtual file. */
const char *MEM_INFO_PATH = "/proc/meminfo";

/**
 * Print memory info to a log.
 * */
JNIEXPORT void JNICALL
Java_com_malakhv_util_LogCat_printMemoryInfo_1native(JNIEnv *env, jclass type, jstring tag_,
                                                     jint priority) {
    // Tag
    const char *tag = (*env)->GetStringUTFChars(env, tag_, 0);

    // Open file for read
    int file = open(MEM_INFO_PATH, O_RDONLY);
    if (file < 0) {
        __android_log_write(ANDROID_LOG_ERROR, tag, "Error when opening meminfo file");
        return;
    }

    // Read from file
    const int BUF_SIZE = 2048;
    char buf[BUF_SIZE];
    int len = (int) read(file, buf, sizeof(buf));
    close(file);
    buf[len] = 0;

    // Write a log
    __android_log_write(priority, tag, buf);

    (*env)->ReleaseStringUTFChars(env, tag_, tag);
}