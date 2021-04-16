//
// Created by 93289 on 2020/12/2.
//


#include <jni.h>
#include "android/log.h"
#include <a_extern.h>
static const char *kTAG = "exercise";

#define LOGI(...)\
    ((void)__android_log_print(ANDROID_LOG_INFO, kTAG, __VA_ARGS__))
#define LOGW(...) \
  ((void)__android_log_print(ANDROID_LOG_WARN, kTAG, __VA_ARGS__))
#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, kTAG, __VA_ARGS__))

typedef int new_type;

extern "C" JNIEXPORT void JNICALL
Java_com_allfun_cpp_1exercise_MainActivity_callMethod(JNIEnv *env, jobject thiz) {

#if 0
    code1
#else
    LOGI("使用 if else endif 注释实现注释代码块 ");
#endif


    /**
     * d 以十进制形式输bai出带符号整du数(正数不输出符号zhi)
     * o 以八进制形式dao输zhuan出无符号整数shu(不输出前缀0)
     * x,X 以十六进制形式输出无符号整数(不输出前缀Ox)
     * u 以十进制形式输出无符号整数
     * f 以小数形式输出单、双精度实数
     * e,E 以指数形式输出单、双精度实数
     * g,G 以%f或%e中较短的输出宽度输出单、双精度实数
     * c 输出单个字符
     * s 输出字符串
     * p 输出指针地址
     * lu 32位无符号整数
     * llu 64位无符号整数
     */

    LOGI("使用 sizeof()获取数据类型大小使用 %d", sizeof(int));

    LOGI("使用 typedef 为一个已有的类型取一个新的名字 %d", sizeof(new_type));


    enum color {
        red, yellow, green = 1, blue
    } c;
    c = blue;
    color a = red;
    color b = yellow;
    LOGI("使用枚举 %d %d %d", c, a, b);
    //输出为2 0 1 枚举不能打印red yellow 只能是0 1 2 要想打印yellow要自己判断0 1 2 输出相应的字符


    extern int num;//num定义子次函数之后，正常逻辑或报错的，使用extern可解决。
    LOGI("使用extern引用自己文件中的变量，起到先使用后定义的作用。num %d",num );
    extern int num_ex;
    LOGI("使用extern引用别的文件中的变量。num_ex %d",num_ex );



}


int num = 3;