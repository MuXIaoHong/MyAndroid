cmake_minimum_required(VERSION 3.4.1)
add_library(pre-exercise
        SHARED
        a_extern.cpp)

add_library(exercise
        SHARED
        exercise01.cpp)
target_link_libraries(exercise
        android
        log)

target_link_libraries(exercise
        android
        log)
include_directories(${CMAKE_SOURCE_DIR}/include)