package com.tianhe.iniviewer.data.model

/**
 * Created by tianhe on 2023/4/9
 */
class Section(val name: String, values: Map<String, String>, private val iniFile: IniFile) {

    val properties = mutableMapOf<String, Value>()

    val iniFileName: String
        get() {
            return iniFile.fileName
        }

    val intFilePath: String
        get() {
            return iniFile.path
        }

    init {
        values.forEach {
            properties[it.key] = Value(it.value)
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