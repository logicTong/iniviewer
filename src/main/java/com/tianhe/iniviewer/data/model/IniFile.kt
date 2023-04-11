package com.tianhe.iniviewer.data.model

import com.tianhe.iniviewer.utils.Strings

/**
 * Created by tianhe on 2023/4/9
 */
class IniFile(val path: String, data: Map<String, HashMap<String, String>>) {

    val fileName: String
    val sectionMap: MutableMap<String, Section> = mutableMapOf()

    init {
        fileName = Strings.getFileNameFromPath(path)
        assembleSections(data)
    }


    private fun assembleSections(data: Map<String, HashMap<String, String>>) {
        data.forEach {
            val section = Section(it.key, it.value, this)
            sectionMap[section.name] = section
        }
    }



}