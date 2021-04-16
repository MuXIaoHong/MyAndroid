1. 在使用native方法前都会先加载该native方法的so文件，通常在一个类的静态代码块中进行加载，当然也可以在构造函数，或者调用前加载。
jvm在加载so时都会先调用so中的JNI_OnLoad函数，如果你没有重写该方法，那么系统会给你自动生成一个。[原生库](https://developer.android.com/training/articles/perf-jni.html?spm=a2c6h.12873639.0.0.2d0e497aM1C5BK#native-libraries)
2. static在C和CPP中它被用来控制变量的存储方式和可见性。 [菜鸟教程](https://www.runoob.com/w3cnote/cpp-static-usage.html)
3. 代码块太长换行使用\
4. 多参数形参使用...,使用多参数的时候使用宏：`__VA_ARGS__`
5. typedef给结构体定义一个别称，在声明结构体实例的时候就可以直接使用别称从而省略struct关键字了：
```
typedef struct tick_context {
    JavaVM *javaVM;
} TickContext;

//使用的typedef之后声明可以简写成这样
TickContext g_ctx;
//否则是这样
struct tick_context g_ctx;
```
6.C中： native获取java的方法字段等需要使用JNIEnv的指针的方法,参数2使用java类的JNI jclass对象，参数3表示java方法的参数和返回值。
7. JNI表示java方法的参数和返回值:
```
Type Signatures
The JNI uses the Java VM’s representation of type signatures. Table 3-2 shows these type signatures.

Table 3-2 Java VM Type Signatures
Type Signature  Java Type
Z  boolean  B
byte  C
char  S
short  I
int  J
long  F
float  D
double  L 
fully-qualified-class ;  fully-qualified-class
[ type  type[]
( arg-types ) ret-type  method type
```
8. GetStringUTFChars与ReleaseStringUTFChars要成对出现
9. 在JNI方法中声明的jstring局部引用要及时释放掉`(*env)->DeleteLocalRef(env, xxx);`
10. 线程的使用[pthread](https://pubs.opengroup.org/onlinepubs/7908799/xsh/pthread.h.html)
