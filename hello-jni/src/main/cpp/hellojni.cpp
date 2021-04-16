//
// Created by 93289 on 2020/9/17.
//

//
// Created by 93289 on 2020/9/17.
//

/*
 * Copyright (C) 2016 The Android Open Source Project
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
 *
 */
#include <stdio.h>
#include "android/log.h"

static const char *kTAG = "hello-jni";

#define LOGI(...)\
    ((void)__android_log_print(ANDROID_LOG_INFO, kTAG, __VA_ARGS__))
#define LOGW(...) \
  ((void)__android_log_print(ANDROID_LOG_WARN, kTAG, __VA_ARGS__))
#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, kTAG, __VA_ARGS__))


//使用CPP编写JNI接口时需要 extern "C"
extern "C" JNIEXPORT jstring JNICALL
Java_com_allfun_hello_1jni_HelloJni_stringFromJNI(JNIEnv *env, jobject thiz) {
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a/NEON (hard-float)"
#else
#define ABI "armeabi-v7a/NEON"
#endif
#else
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a (hard-float)"
#else
#define ABI "armeabi-v7a"
#endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
#define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif

    __android_log_write(ANDROID_LOG_DEBUG, "JNI", "Log测试");
//    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with A B I " ABI "."); C的调用方式
    return env->NewStringUTF("Hello from JNI !  Compiled with A B I 1 " ABI ".");
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_allfun_hello_1jni_HelloJni_inputStringReturnString(JNIEnv *env, jobject thiz,
                                                            jstring input) {
    const char *c_str = NULL;
    char buff[128] = {0};
    jboolean isCopy;    // 返回JNI_TRUE表示原字符串的拷贝，返回JNI_FALSE表示返回原字符串的指针
    c_str = env->GetStringUTFChars(input, &isCopy);

    LOGI("isCopy:%d\n", isCopy);

    if (c_str == NULL) {
        return NULL;
    }

    LOGI("C_str: %s \n", c_str);

    sprintf(buff, "return %s", c_str);

    LOGI("buff: %s \n", buff);

    env->ReleaseStringUTFChars(input, c_str);
    return env->NewStringUTF(buff);
}

