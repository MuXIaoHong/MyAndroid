cmake_minimum_required(VERSION 3.4.1)

add_library(test-lib SHARED
        test.cpp)

#引用静态库
add_library(static-lib
        STATIC
        IMPORTED)

#设置静态库路径
set_target_properties( # Specifies the target library.
        static-lib
        # Specifies the parameter you want to define.
        PROPERTIES IMPORTED_LOCATION
        # Provides the path to the library you want to import.
        ${CMAKE_CURRENT_SOURCE_DIR}/lib/${ANDROID_ABI}/libstatic-lib.a)


# 将静态库链接到我们使用该库的库中
target_link_libraries(test-lib
        android
        log
        static-lib)
# 引入该静态库的头文件
include_directories(lib/include/)