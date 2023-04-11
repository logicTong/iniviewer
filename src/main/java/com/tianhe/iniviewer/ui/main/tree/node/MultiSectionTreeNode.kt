package com.tianhe.iniviewer.ui.main.tree.node

import com.tianhe.iniviewer.data.model.MultiSection
import com.tianhe.iniviewer.data.model.Section

/**
 * Created by tianhe on 2023/4/9
 */
class MultiSectionTreeNode(val multiSection: MultiSection) : TreeNode() {



    /**
     * 存在同名的section，认为是冲突节点
     */
    fun isConflictNode(): Boolean {
        return multiSection.size > 1
    }

    fun getSection(): Section {
        return multiSection.getSingleSection()
    }

    fun getSections(): List<Section> {
        return multiSection.sections
    }

    override fun toString(): String {
        return multiSection.sectionName
    }
}