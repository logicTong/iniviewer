package com.tianhe.iniviewer.inifile;

import com.tianhe.iniviewer.utils.Strings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负责将ini文件解析成ArrayMap
 */
public class IniUtils {
    private static String TAG = "IniUtils";

    /**
     * key: section
     * value {
     * properties1=xxx,
     * properties2=xxx
     * }
     *
     * @param bufferedReader
     * @return
     */
    public static Map<String, HashMap<String, String>> parseIni(BufferedReader bufferedReader) throws Exception {
        if (bufferedReader == null) {
            return null;
        }

        Map<String, HashMap<String, String>> result = new HashMap<>();
        HashMap<String, String> hashMap = null;
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String trim = Strings.trim(readLine);
                    if (trim == null || trim.length() == 0 || trim.charAt(0) == ';') {
                    } else {
                        if (trim.startsWith("[") && trim.endsWith("]")) {
                            String substr = trim.substring(1, trim.length() - 1);
                            if (substr.length() > 0) {
                                hashMap = new HashMap<>();
                                result.put(Strings.trimToEmpty(substr), hashMap);
                            }
                        } else if (trim.contains("=")) {
                            int indexOf = trim.indexOf("=");
                            String sub1 = Strings.trim(trim.substring(0, indexOf));
                            String sub2 = Strings.trim(trim.substring(indexOf + 1));
                            if (trim.length() > 0 && hashMap != null) {
                                hashMap.put(sub1, sub2);
                            }
                        }
                    }
                } else {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    bufferedReader.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                throw e;
            }
        }
        return result;
    }


}
