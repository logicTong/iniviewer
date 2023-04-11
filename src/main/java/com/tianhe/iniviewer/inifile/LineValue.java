package com.tianhe.iniviewer.inifile;

/**
 * Created by tianhe on 2023/4/11
 */
public class LineValue {
    public int lineNum = -1;
    public String value;

    public LineValue(String value, int lineNum) {
        this.value = value;
        this.lineNum = lineNum;
    }
}
