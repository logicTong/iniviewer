package com.tianhe.iniviewer.ui.manage.reference

import com.tianhe.iniviewer.data.model.Section
import com.tianhe.iniviewer.data.model.Value

/**
 * Created by tianhe on 2023/4/27
 */
data class ReferenceItem(val section: Section, val key: String, val value: Value){

    fun getIniFile():String{
        return section.iniFileName
    }


    override fun toString(): String {
        return "[${section.name}]  ->  $key=${value.value}"
    }
}
