package com.tianhe.iniviewer.data.model

import com.tianhe.iniviewer.inifile.SectionInfo

/**
 * Created by tianhe on 2023/4/9
 */
class Section(info: SectionInfo, private val iniFile: IniFile) {

    val properties = mutableMapOf<String, Value>()
    val name: String
    val lineNum: Int
    var parentSection: MultiSection? = null

    val iniFileName: String
        get() {
            return iniFile.fileName
        }

    val intFilePath: String
        get() {
            return iniFile.path
        }

    init {
        name = info.name
        lineNum = info.lineNum
        info.properties.forEach {
            properties[it.key] = Value(it.value.value, it.value.lineNum)
        }
    }

    override fun toString(): String {
        return prettyName()
    }

    fun dump(): String {
        return "Section(name='$name', properties=$properties, iniFileName='$iniFileName')"
    }

    fun prettyName(): String {
        return "$name : ${iniFileName}"
    }

}