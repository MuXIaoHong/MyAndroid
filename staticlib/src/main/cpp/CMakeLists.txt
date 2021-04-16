cmake_minimum_required(VERSION 3.4.1)

add_library(static-lib SHARED
        static-lib.cpp)

#引用静态库
add_library(add
        STATIC
        IMPORTED)

#设置静态库路径
set_target_properties( # Specifies the target library.
        add
        # Specifies the parameter you want to define.
        PROPERTIES IMPORTED_LOCATION
        # Provides the path to the library you want to import.
        ${CMAKE_CURRENT_SOURCE_DIR}/lib/${ANDROID_ABI}/libAdd.a)


# 将静态库链接到我们使用该库的库中
target_link_libraries(static-lib
        android
        log
        add)
# 引入该静态库的头文件
include_directories(lib/include/)