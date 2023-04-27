package com.tianhe.iniviewer.data.model

/**
 * Created by tianhe on 2023/4/9
 */
class MultiSection(section: Section) {
    val sections = mutableListOf<Section>()
    private val conflictKeys = mutableSetOf<String>()

    init {
        sections.add(section)
        section.parentSection = this
    }


    fun addSection(section: Section) {
        recordConflictKeys(section)
        sections.add(section)
        section.parentSection = this
    }

    fun containsConflictKey(key: String): Boolean {
        return conflictKeys.contains(key)
    }

    private fun recordConflictKeys(section: Section) {
        section.properties.forEach {
            if (conflictKeys.contains(it.key)) {
                return@forEach
            }
            for (e in sections) {
                if (e.properties.containsKey(it.key)) {
                    conflictKeys.add(it.key)
                    return@forEach
                }
            }
        }
    }

    override fun toString(): String {
        return "MultiSection(size=$size, sections=${sections})"
    }

    val size: Int
        get() = sections.size

    val sectionName: String
        get() {
            return getSingleSection()?.name ?: ""
        }

    fun getSingleSection(): Section? {
        return if (size > 0) sections[0] else null
    }


    fun removeSectionByPath(path: String) {
        val tr = sections.iterator()
        while (tr.hasNext()) {
            tr.next().let {
                if (path.equals(it.intFilePath)) {
                    tr.remove()
                }
            }
        }
    }


}