package com.allfun.staticlib;

/**
 * author：93289
 * date:2020/11/27
 * dsc:
 */
public class NativeLib {
    static {
        System.loadLibrary("static-lib");
    }
    native void callMethod();
}
