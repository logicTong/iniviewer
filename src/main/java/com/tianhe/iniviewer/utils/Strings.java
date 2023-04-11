package com.tianhe.iniviewer.utils;

import java.io.File;

/**
 * Created by tianhe on 2023/4/9
 */
public class Strings {


    /**
     * <p>清空字符串左右的空白字符。</p>
     *
     * <pre>
     * SStrings.trim(null)          = null
     * SStrings.trim("")            = ""
     * SStrings.trim("     ")       = ""
     * SStrings.trim("abc")         = "abc"
     * SStrings.trim("    abc    ") = "abc"
     * </pre>
     *
     * @param str the String to be trimmed, may be null
     * @return the trimmed string, <code>null</code> if null String input
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }


    /**
     * <p>清空字符串左右的空白字符，返回值为空时，返回空字符串""</p>
     *
     * <pre>
     * SStrings.trimToEmpty(null)          = ""
     * SStrings.trimToEmpty("")            = ""
     * SStrings.trimToEmpty("     ")       = ""
     * SStrings.trimToEmpty("abc")         = "abc"
     * SStrings.trimToEmpty("    abc    ") = "abc"
     * </pre>
     *
     * @param str the String to be trimmed, may be null
     * @return the trimmed String, or an empty String if <code>null</code> input
     */
    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }


    public static String getFileNameFromPath(String path) {
        int start = path.lastIndexOf(File.separator);
        return path.substring(start + 1);
    }
}
