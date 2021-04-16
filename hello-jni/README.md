- 在为库添加NDK自带的API库的时候，官方文档[官方文档](https://developer.android.google.cn/ndk/guides/stable_apis?hl=zh_cn#using_native_apis)
中库的名字前边都有前缀lib，我们使用的时候要去掉，例如:   
```
//其中的android log 在文档中都有前缀lib 
target_link_libraries(hello-jni
                      android
                      log)
```

- 表示当前目录下的文件，可以使用./xxx，也可以直接使用xxx  
- 对于新声明的native方法`public native String  stringFromJNI()`,相应的c或者cpp文件中有`JNIEXPORT jstring JNICALLJava_com_example_xx(JNIEnv *env, jobject thiz) {}`时，alt+enter就会自动出现创建Native方法的提示
- CPP和C文件在实现JNI方法的时候不太一样:  
CPP: `extern "C" JNIEXPORT jstring JNICALL` `env->NewStringUTF("Hello from JNI !  Compiled with A B I 1 " ABI ".")`
C: `(*env)->NewStringUTF(env, "Hello from JNI !  Compiled with A B I " ABI ".")`