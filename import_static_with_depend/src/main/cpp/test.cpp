#include <android/log.h>
#include <jni.h>
#include <static-lib.h>

//
// Created by 93289 on 2020/11/30.
//
static const char *kTAG = "test";
#define LOGI(...)\
    ((void)__android_log_print(ANDROID_LOG_INFO, kTAG, __VA_ARGS__))
#define LOGW(...) \
  ((void)__android_log_print(ANDROID_LOG_WARN, kTAG, __VA_ARGS__))
#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, kTAG, __VA_ARGS__))

extern "C" JNIEXPORT void JNICALL
Java_com_allfun_import_1static_1with_1depend_NativeLib_calMethod(JNIEnv *env, jobject thiz) {
    int c = addBdd(1, 2);
    if (c==6) {
        LOGE("Java_com_allfun_staticlib_NativeLib_callMethod");
    }
}
