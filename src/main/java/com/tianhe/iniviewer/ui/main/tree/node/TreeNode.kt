package com.tianhe.iniviewer.ui.main.tree.node

import com.tianhe.iniviewer.data.model.Section
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Created by tianhe on 2023/4/9
 */
abstract class TreeNode : DefaultMutableTreeNode() {

    abstract val section: Section
    abstract val lineNum:Int


}