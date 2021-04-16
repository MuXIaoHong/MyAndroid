package com.allfun.cpp_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preCallMethod();
        callMethod();
    }

    public native void callMethod();

    public native void preCallMethod();

    static {
        System.loadLibrary("exercise");
        System.loadLibrary("pre-exercise");
    }
}
