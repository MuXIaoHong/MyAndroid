package com.allfun.import_static_with_depend;

/**
 * author：93289
 * date:2020/11/27
 * dsc:
 */
public class NativeLib {
    static {
        System.loadLibrary("test-lib");
    }
    native void calMethod();
}
