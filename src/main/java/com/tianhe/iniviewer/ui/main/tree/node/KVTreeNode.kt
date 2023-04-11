package com.tianhe.iniviewer.ui.main.tree.node

import com.tianhe.iniviewer.data.model.Value

/**
 * Created by tianhe on 2023/4/9
 */
class KVTreeNode(val key:String, val value:Value): TreeNode() {


    override fun toString(): String {
        return "$key = $value"
    }

}