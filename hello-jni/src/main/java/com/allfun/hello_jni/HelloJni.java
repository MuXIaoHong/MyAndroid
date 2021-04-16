/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.allfun.hello_jni;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HelloJni extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView tv = (TextView)findViewById(R.id.hello_textview);
        tv.setText( stringFromJNI() );
        Toast.makeText(this, inputStringReturnString("input"), Toast.LENGTH_SHORT).show();
    }
    //相应的c或者cpp文件中有JNIEXPORT jstring JNICALLJava_com_example_xx(JNIEnv *env, jobject thiz) {}时，alt+enter就会自动出现创建Native方法的提示
    public native String  stringFromJNI();

    //传入String返回String
    public native String  inputStringReturnString(String input);



    static {
        System.loadLibrary("hello-jni");
    }
}
