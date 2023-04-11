package com.tianhe.iniviewer.ui.main.tree.node

import com.tianhe.iniviewer.data.model.Section

/**
 * Created by tianhe on 2023/4/9
 */
class SectionTreeNode (val section: Section): TreeNode() {


    override fun toString(): String {
        return section.name
    }

}