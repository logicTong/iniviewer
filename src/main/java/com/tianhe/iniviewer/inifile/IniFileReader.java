package com.tianhe.iniviewer.inifile;


import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责ini文件读取
 */
public class IniFileReader {
    /**
     * @param path
     * @return 返回结构如下：
     * key: section
     * value {
     * properties1=xxx,
     * properties2=xxx
     * }
     */
    public static Map<String, HashMap<String, String>> readIniFile(String path) throws Exception {
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = new FileInputStream(path);
        try {
            inputStreamReader = new InputStreamReader(inputStream, Charset.defaultCharset());
            bufferedReader = new BufferedReader(inputStreamReader);
            return IniUtils.parseIni(bufferedReader);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
