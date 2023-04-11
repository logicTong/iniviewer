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
    public static List<SectionInfo> parseIni(BufferedReader bufferedReader) throws Exception {
        if (bufferedReader == null) {
            return null;
        }

        ArrayList<SectionInfo> result = new ArrayList<>(64);
        SectionInfo info = null;
        int lineNum = 0;
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
                                info = new SectionInfo(Strings.trimToEmpty(substr), lineNum);
                                result.add(info);
                            }
                        } else if (trim.contains("=")) {
                            int indexOf = trim.indexOf("=");
                            String k = Strings.trim(trim.substring(0, indexOf));
                            String v = Strings.trim(trim.substring(indexOf + 1));
                            if (trim.length() > 0 && info != null) {
                                info.properties.put(k, new LineValue(v, lineNum));
                            }
                        }
                    }
                } else {
                    break;
                }
                lineNum++;
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
