
#include <jni.h>
#include <string.h>
#include <pthread.h>
#include <assert.h>
#include <android/log.h>
#include <inttypes.h>
static  const char *kTAG = "hello-jniCallback";

#define LOGI(...)\
    ((void)__android_log_print(ANDROID_LOG_INFO, kTAG, __VA_ARGS__))
#define LOGW(...) \
  ((void)__android_log_print(ANDROID_LOG_WARN, kTAG, __VA_ARGS__))
#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, kTAG, __VA_ARGS__))

typedef struct tick_context {
    JavaVM *javaVM;
    jclass jniHelperClz;
    jobject jniHelperObj;
    jclass mainActivityClz;
    jobject mainActivityObj;
    pthread_mutex_t lock;
    int done;
} TickContext;
TickContext g_ctx;



void queryRuntimeInfo(JNIEnv *env, jobject instance) {
    jmethodID versionFunc =(*env)->GetStaticMethodID( env, g_ctx.jniHelperClz,"getBuildVersion","()Ljava/lang/String;");
    if(!versionFunc){
        LOGE("Failed to retrieve getBuildVersion() methodID @ line %d",__LINE__);
        return;
    }
    jstring buildVersion =(*env)->CallStaticObjectMethod(env,
                                                         g_ctx.jniHelperClz, versionFunc);
    const char *version =(*env)->GetStringUTFChars(env,buildVersion,NULL);
    if (!version) {
        LOGE("Unable to get version string @ line %d", __LINE__);
        return;
    }

    LOGI("Android Version - %s", version);
    //GetStringUTFChars与ReleaseStringUTFChars要成对出现,参数2为生成char*的jstring,参数3为生成的char*
    (*env)->ReleaseStringUTFChars(env, buildVersion, version);

    // we are called from JNI_OnLoad, so got to release LocalRef to avoid leaking
    (*env)->DeleteLocalRef(env, buildVersion);

    // Query available memory size from a non-static public function
    // we need use an instance of JniHelper class to call JNI
    jmethodID memFunc = (*env)->GetMethodID(env, g_ctx.jniHelperClz,
                                            "getRuntimeMemorySize", "()J");
    //关于函数的参数和返回值的类型https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/types.html#wp1064
    jlong result = (*env)->CallLongMethod(env, instance, memFunc);
    LOGI("Runtime free memory size: %" PRId64, result);
    (void) result;  // silence the compiler warning

}

jint JNICALL JNI_OnLoad(JavaVM *vm, void *unused) {
    JNIEnv *env;
    //初始化g_ctx
    memset(&g_ctx, 0, (sizeof(g_ctx)));
    g_ctx.javaVM = vm;
    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // JNI version not supported.
    }
    //获取JniHandler的jclass
    jclass clz = (*env)->FindClass(env, "com/allfun/hello_jni_callback/JniHandler");
    //长期持有JniHandler的class引用
    g_ctx.jniHelperClz = (*env)->NewGlobalRef(env, clz);
    //找到JniHandler的class中的构造方法,构造方法用<init>表示，参数为无参 ()V
    jmethodID jniHelperCtor = (*env)->GetMethodID(env, g_ctx.jniHelperClz, "<init>", "()V");
    jobject handler = (*env)->NewObject(env, g_ctx.jniHelperClz, jniHelperCtor);
    g_ctx.jniHelperObj = (*env)->NewGlobalRef(env, handler);
    queryRuntimeInfo(env, g_ctx.jniHelperObj);

    g_ctx.done = 0;
    g_ctx.mainActivityObj = NULL;
    return JNI_VERSION_1_6;
}


JNIEXPORT jstring JNICALL
Java_com_allfun_hello_1jni_1callback_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
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
    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");

}

void sendJavaMsg(JNIEnv *env, jobject instance,jmethodID func, const char *msg) {
    jstring javaMsg = (*env)->NewStringUTF(env, msg);
    //调用java方法
    (*env)->CallVoidMethod(env, instance, func, javaMsg);
    //删除javaMsg局部引用
    (*env)->DeleteLocalRef(env, javaMsg);
}

void *UpdateTicks(void *context) {
    TickContext *pctx = (TickContext *) context;
    JavaVM *javaVm = pctx->javaVM;
    JNIEnv *env;
    jint res = (*javaVm)->GetEnv(javaVm, (void **) &env, JNI_VERSION_1_6);
    if (res != JNI_OK) {
        res = (*javaVm)->AttachCurrentThread(javaVm, &env, NULL);
        if (res != JNI_OK) {
            LOGE("Failed to AttachCurrentThread, ErrorCode = %d", res);
            return NULL;
        }
    }
    jmethodID statusId = (*env)->GetMethodID(env, pctx->jniHelperClz,
                                             "updateStatus",
                                             "(Ljava/lang/String;)V");
    sendJavaMsg(env, pctx->jniHelperObj, statusId,
                "TickerThread status: initializing...");
    jmethodID timerId = (*env)->GetMethodID(env, pctx->mainActivityClz,
                                            "updateTimer", "()V");
    struct timeval beginTime, curTime, usedTime, leftTime;
    const struct timeval kOneSecond = {
            (__kernel_time_t) 1,
            (__kernel_suseconds_t) 0
    };
    sendJavaMsg(env, pctx->jniHelperObj, statusId,
                "TickerThread status: start ticking ...");

    while (1) {
        //获得当前时间，填入结构体中，tz是时区结构体，这里填MULL
        gettimeofday(&beginTime, NULL);
        pthread_mutex_lock(&pctx->lock);
        int done = pctx->done;
        if (pctx->done) {
            pctx->done = 0;
        }
        pthread_mutex_unlock(&pctx->lock);
        if (done) {
            break;
        }

        (*env)->CallVoidMethod(env, pctx->mainActivityObj, timerId);
        gettimeofday(&curTime, NULL);
        timersub(&curTime, &beginTime, &usedTime);
        timersub(&kOneSecond, &usedTime, &leftTime);
        struct timespec sleepTime;
        sleepTime.tv_sec = leftTime.tv_sec;
        sleepTime.tv_nsec = leftTime.tv_usec * 1000;

        if (sleepTime.tv_sec <= 1) {
            nanosleep(&sleepTime, NULL);
        } else {
            sendJavaMsg(env, pctx->jniHelperObj, statusId,
                        "TickerThread error: processing too long!");
        }

    }
    sendJavaMsg(env, pctx->jniHelperObj, statusId,
                "TickerThread status: ticking stopped");
    (*javaVm)->DetachCurrentThread(javaVm);
    return context;
}

JNIEXPORT void JNICALL
Java_com_allfun_hello_1jni_1callback_MainActivity_startTicks(JNIEnv *env, jobject thiz) {
    //用于标识线程
    pthread_t threadInfo_;
    //线程的属性
    pthread_attr_t threadAttr_;
    //初始化属性
    pthread_attr_init(&threadAttr_);
    //设置分离状态,PTHREAD_CREATE_DETACHED标识线程停止之后其他线程可以使用此线程的ID和资源
    pthread_attr_setdetachstate(&threadAttr_, PTHREAD_CREATE_DETACHED);
    //初始化互斥锁
    pthread_mutex_init(&g_ctx.lock, NULL);

    //获取MainActivity的引用
    jclass clz = (*env)->GetObjectClass(env, thiz);
    g_ctx.mainActivityClz = (*env)->NewGlobalRef(env, clz);
    g_ctx.mainActivityObj = (*env)->NewGlobalRef(env, thiz);

    //创建一个线程
    int result = pthread_create(&threadInfo_, &threadAttr_, UpdateTicks, &g_ctx);
    //ASSERT() 是一个调试程序时经常使用的宏，在程序运行时它计算括号内的表达式，如果表达式为 FALSE (0), 程序将报告错误，并终止执行。如果表达式不为 0，
    // 则继续执行后面的语句。这个宏通常原来判断程序中是否出现了明显非法的数据，如果出现了终止程序以免导致严重后果，同时也便于查找错误。
    assert(result==JNI_OK);

    //去除初始化
    pthread_attr_destroy(&threadAttr_);
    (void) result;
}

JNIEXPORT void JNICALL
Java_com_allfun_hello_1jni_1callback_MainActivity_StopTicks(JNIEnv *env, jobject thiz) {
    pthread_mutex_lock(&g_ctx.lock);
    g_ctx.done = 1;
    pthread_mutex_unlock(&g_ctx.lock);

    // waiting for ticking thread to flip the done flag
    struct timespec sleepTime;
    memset(&sleepTime, 0, sizeof(sleepTime));
    sleepTime.tv_nsec = 100000000;
    while (g_ctx.done) {
        nanosleep(&sleepTime, NULL);
    }

    // release object we allocated from StartTicks() function
    (*env)->DeleteGlobalRef(env, g_ctx.mainActivityClz);
    (*env)->DeleteGlobalRef(env, g_ctx.mainActivityObj);
    g_ctx.mainActivityObj = NULL;
    g_ctx.mainActivityClz = NULL;

    pthread_mutex_destroy(&g_ctx.lock);

}