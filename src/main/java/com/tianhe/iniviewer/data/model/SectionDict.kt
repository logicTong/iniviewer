package com.tianhe.iniviewer.data.model

import com.tianhe.iniviewer.utils.Log

/**
 * Created by tianhe on 2023/4/9
 */
object SectionDict {

    const val TAG = "SectionDict"


    private val dict = mutableMapOf<String, MultiSection>()


    @Synchronized
    fun addIniFile(iniFile: IniFile) {
        iniFile.sectionMap.forEach {
            val section = it.value
            var multiSection = dict[section.name]
            if (multiSection == null) {
                multiSection = MultiSection(section)
                dict[section.name] = multiSection
            } else {
                multiSection.addSection(section)
            }
        }
    }

    @Synchronized
    fun clear() {
        dict.clear()
    }

    @Synchronized
    fun removeIniFile(iniFilePath: String) {
        val tr = dict.iterator()
        while (tr.hasNext()) {
            tr.next().let {
                val multi = it.value
                multi.removeSectionByPath(iniFilePath)
                if (multi.size == 0) {
                    tr.remove()
                }
            }
        }
    }

    @Synchronized
    fun get(name: String): MultiSection? {
        return dict[name]
    }

    @Synchronized
    fun findIgnoreCase(name: String): MultiSection? {
        dict.forEach {
            if (name.equals(it.key, true)) {
                return it.value
            }
        }
        return null
    }

    @Synchronized
    fun dump() {
        dumpSize()
//        dict.forEach {
//            Log.d(TAG, it.value.toString())
//        }
    }

    private fun dumpSize() {
        Log.d(TAG, "dict size =${dict.size}")
    }
}