package com.tianhe.iniviewer.ui.main.tree.node

import com.tianhe.iniviewer.data.model.MultiSection
import com.tianhe.iniviewer.data.model.Section

/**
 * Created by tianhe on 2023/4/9
 */
class MultiSectionTreeNode(private val multiSection: MultiSection) : TreeNode() {

    private val firstSection: Section = multiSection.getSingleSection()!!

    /**
     * 存在同名的section，认为是冲突节点
     */
    fun isConflictNode(): Boolean {
        return multiSection.size > 1
    }

    fun getSections(): List<Section> {
        return multiSection.sections
    }


    override val section: Section
        get() = multiSection.getSingleSection() ?: firstSection

    override val lineNum: Int
        get() = section.lineNum

    override fun toString(): String {
        return multiSection.sectionName
    }
}