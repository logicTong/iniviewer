package com.tianhe.iniviewer.utils;

/**
 * Created by tianhe on 2023/4/9
 */
public class Log {

    public static void e(String tag, String message){
        System.err.println("["+tag+"] "+message);
    }

    public static void e(String tag, String message, Throwable e){
        System.err.println("["+tag+"] "+message+" "+e.getMessage());
        e.printStackTrace();
    }

    public static void d(String tag, String message){
        System.out.println("["+tag+"] "+message);
    }
}
