package com.tianhe.iniviewer.ui.main.tree.node

import com.intellij.icons.AllIcons
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.components.BorderLayoutPanel
import com.tianhe.iniviewer.ui.main.tree.node.MultiSectionTreeNode
import com.tianhe.iniviewer.ui.main.tree.node.SectionTreeNode
import com.tianhe.iniviewer.ui.main.tree.node.TreeNode
import java.awt.Color
import java.awt.Component
import javax.swing.Icon
import javax.swing.JTree
import javax.swing.tree.DefaultTreeCellRenderer

/**
 * Created by tianhe on 2023/4/9
 */
class TreeNodeRender : DefaultTreeCellRenderer() {

    val container = BorderLayoutPanel()
    val iniFileName = JBLabel()

    val leafIcon0 = AllIcons.General.Information
    var openIcon0: Icon = AllIcons.Actions.Collapseall
    val closeIcon0 = AllIcons.Actions.PrettyPrint

//    val leafIcon0 = AllIcons.General.InspectionsOK
//    var openIcon0:Icon = AllIcons.Actions.Expandall
//    val closeIcon0 = AllIcons.Json.Object


    init {
        setOpenIcon(openIcon0)
        setClosedIcon(closeIcon0)
        setLeafIcon(leafIcon0)
        container.addToCenter(this)
        container.addToRight(iniFileName)
        iniFileName.foreground = Color.orange
        iniFileName.componentStyle = UIUtil.ComponentStyle.SMALL
    }


    override fun getTreeCellRendererComponent(
        tree: JTree?,
        value: Any?,
        sel: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ): Component {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)
        if (value is TreeNode) {
            renderTreeNode(value)
        }

        return container

    }


    private fun renderTreeNode(model: TreeNode) {
        if (model is SectionTreeNode) {
            iniFileName.text = "${model.section.iniFileName}    "
        } else if (model is MultiSectionTreeNode) {
            iniFileName.text = "${model.section.iniFileName}    "
        } else {
            iniFileName.text = ""
        }

    }
}