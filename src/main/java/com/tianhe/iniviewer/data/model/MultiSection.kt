package com.tianhe.iniviewer.data.model

/**
 * Created by tianhe on 2023/4/9
 */
class MultiSection(section: Section) {
    val sections = mutableListOf<Section>()

    init {
        sections.add(section)
    }

    fun addSection(section: Section) {
        sections.add(section)
    }

    override fun toString(): String {
        return "MultiSection(size=$size, sections=${sections})"
    }

    val size: Int
        get() = sections.size

    val sectionName: String
        get() {
            return getSingleSection()?.name?:""
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