package com.tianhe.iniviewer.ui.main.tree.node

import com.tianhe.iniviewer.data.model.Section
import com.tianhe.iniviewer.data.model.Value

/**
 * Created by tianhe on 2023/4/9
 */
class KVTreeNode(val key: String, val value: Value, override val section: Section) : TreeNode() {

    override val lineNum: Int
        get() = value.lineNum

    override fun toString(): String {
        return "$key = $value"
    }

    fun isConflictNode(): Boolean {
        section.parentSection?.let {
            return it.containsConflictKey(key)
        }
        return false
    }

    fun isImageFile(): Boolean {
        return value.isImage()
    }


    fun getImageFile(): String {
        return value.getImageName()
    }
}