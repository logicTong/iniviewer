package com.tianhe.iniviewer.data.model

import com.tianhe.iniviewer.inifile.SectionInfo
import com.tianhe.iniviewer.utils.Strings

/**
 * Created by tianhe on 2023/4/9
 */
class IniFile(val path: String, data: List<SectionInfo>) {

    val fileName: String
    val sectionMap: MutableMap<String, Section> = mutableMapOf()

    init {
        fileName = Strings.getFileNameFromPath(path)
        assembleSections(data)
    }


    private fun assembleSections(data: List<SectionInfo>) {
        data.forEach {
            val section = Section(it, this)
            sectionMap[section.name] = section
        }
    }


}