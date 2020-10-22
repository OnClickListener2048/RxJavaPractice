package com.example.rxjavapractice;

public class MyJni {

    static {
        System.loadLibrary("MyJni");
    }

    public static native String getString();
}
