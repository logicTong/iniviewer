package com.tianhe.iniviewer.ui.manage.reference

import com.intellij.icons.AllIcons
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.Color
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.Icon
import javax.swing.JList
import javax.swing.JTree
import javax.swing.tree.DefaultTreeCellRenderer

/**
 * Created by tianhe on 2023/4/9
 */
class ReferenceItemRender : DefaultListCellRenderer() {

    val container = BorderLayoutPanel()
    val iniFileName = JBLabel()


    init {
        container.addToCenter(this)
        container.addToRight(iniFileName)
        iniFileName.foreground = Color.orange
        iniFileName.componentStyle = UIUtil.ComponentStyle.SMALL
    }


    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        if (value is ReferenceItem) {
            renderFile(value)
        }
        return container
    }


    private fun renderFile(model: ReferenceItem) {
        iniFileName.text = "${model.getIniFile()}      "
    }


}