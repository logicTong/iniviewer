package com.tianhe.iniviewer.logic;

import com.intellij.openapi.application.ApplicationInfo;
import com.tianhe.iniviewer.utils.Log;

/**
 * Created by tianhe on 2023/4/10
 */
public class Consts {
    public static final String TAG = "Consts";

    public static final String PACKAGE_NAME = "com.tianhe.iniviewer";
    public static final String NOTIFICATION_ID = "com.tianhe.iniviewer.notification.id";
    public static String PROJECT_NAME = "";

    public static final int MAJOR_211 = 211;
    public static final int MAJOR_201 = 201;

    public static String getProjectKey() {
        String key = PACKAGE_NAME + "." + PROJECT_NAME;
        Log.d(TAG, "getProjectKey: key = " + key);
        return key;
    }

    public static int getIDEAVersion() {
        int v = ApplicationInfo.getInstance().getBuild().getBaselineVersion();
        Log.d(TAG, "getIDEAVersion: v=" + v);
        return v;
    }

}
