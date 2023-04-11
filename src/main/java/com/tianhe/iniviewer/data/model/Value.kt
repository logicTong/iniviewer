package com.tianhe.iniviewer.data.model

import com.tianhe.iniviewer.logic.Config

/**
 * Created by tianhe on 2023/4/9
 */
class Value(val value: String, val lineNum: Int) {

    //值是否引用了多个section
    var isMultiReference: Boolean = false

    //是否引用类型
    var isReference: Boolean = false
    var values: List<String>? = null

    init {
        if (value.contains(Config.VALUE_SEPARATOR)) {
            val ret = value.split(Config.VALUE_SEPARATOR)
            isMultiReference = true
            ret.forEach {
                if (isDigit(it)) {
                    isMultiReference = false
                    return@forEach
                }
            }
            if (isMultiReference) {
                values = ret
            }
        } else if (!isDigit(value)) {
            isReference = true
        }
    }

    override fun toString(): String {
        return value
    }


    fun isDigit(value: String): Boolean {
        return value.all { Character.isDigit(it) || it == '.' }
    }
}