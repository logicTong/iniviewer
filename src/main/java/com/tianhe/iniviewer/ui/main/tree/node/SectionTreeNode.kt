package com.tianhe.iniviewer.ui.main.tree.node

import com.tianhe.iniviewer.data.model.Section

/**
 * Created by tianhe on 2023/4/9
 */
class SectionTreeNode(override val section: Section) : TreeNode() {


    override val lineNum: Int
        get() = section.lineNum


    override fun toString(): String {
        return section.name
    }

}