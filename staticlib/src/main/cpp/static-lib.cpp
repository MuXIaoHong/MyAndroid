#include <jni.h>
#include <android/log.h>
#include <Add.h>
#include <Bdd.h>

static const char *kTAG = "static_lib";
#define LOGI(...)\
    ((void)__android_log_print(ANDROID_LOG_INFO, kTAG, __VA_ARGS__))
#define LOGW(...) \
  ((void)__android_log_print(ANDROID_LOG_WARN, kTAG, __VA_ARGS__))1
#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, kTAG, __VA_ARGS__))


extern "C" JNIEXPORT void JNICALL
Java_com_allfun_staticlib_NativeLib_callMethod(JNIEnv *env, jobject thiz) {
    int a = add(2, 2);
    int b = bdd(2, 2);
    if (a == 4 && b == 4) {
        LOGE("Java_com_allfun_staticlib_NativeLib_callMethod");
    }
}

int addBdd(int a, int b) {
    int ab = add(a, b) + bdd(a, b);
    return ab;
}