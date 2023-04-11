package com.tianhe.iniviewer.inifile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tianhe on 2023/4/11
 */
public class SectionInfo {

    public String name;
    public int lineNum = -1;
    public Map<String,LineValue> properties = new HashMap<>();

    public SectionInfo(String name, int lineNum) {
        this.name = name;
        this.lineNum = lineNum;
    }
}
