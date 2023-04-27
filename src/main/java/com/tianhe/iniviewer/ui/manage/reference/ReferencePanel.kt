package com.tianhe.iniviewer.ui.manage.reference

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.components.BorderLayoutPanel
import com.tianhe.iniviewer.data.model.SectionDict
import com.tianhe.iniviewer.utils.Navigation
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.EmptyBorder

/**
 * Created by tianhe on 2023/4/8
 */
class ReferencePanel(val project: Project, val beReferenced: String) : BorderLayoutPanel() {

    val TAG = "ReferencePanel"

    /**
     * 顶部路径选择
     */
    val inputLabel = JLabel("this table show all the section which refer to [${beReferenced}]:")
    val topPanel = JPanel(VerticalFlowLayout())

    /**
     * 路径列表
     */
    var listModel = DefaultListModel<ReferenceItem>()
    val list = JBList(listModel)
    val listPanel = JBScrollPane(list)


    init {
        minimumSize = Dimension(600, 400)
        initComponents()
    }


    private fun initComponents() {
        topPanel.add(inputLabel)
        addToTop(topPanel)
        listPanel.border = EmptyBorder(5, 10, 5, 10)
        addToCenter(listPanel)
        list.cellRenderer = ReferenceItemRender()
        list.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    val item = list.selectedValue
                    Navigation(project).navigationToEditor(item.section, item.value.lineNum)
                }
            }

        })
        refreshList()
    }


    private fun refreshList() {
        listModel = buildListModel()
        list.model = listModel
    }


    private fun buildListModel(): DefaultListModel<ReferenceItem> {
        listModel.clear()
        getAllReferences(beReferenced).let {
            listModel.addAll(it)
        }
        return listModel
    }


    private fun getAllReferences(beReferenced: String): List<ReferenceItem> {
        val dict = SectionDict.dict
        val list = mutableListOf<ReferenceItem>()
        dict.forEach { key, multi ->
            multi.sections.forEach sectionFor@{ section ->
                for (kv in section.properties) {
                    // ，列表中有引用到当前节点
                    if (kv.value.isMultiReference && kv.value.values != null) {
                        for (e in kv.value.values!!) {
                            if (beReferenced == e) {
                                ReferenceItem(section, kv.key, kv.value).let { list.add(it) }
                                return@sectionFor
                            }
                        }
                    } else if (kv.value.isReference) {
                        if (beReferenced == kv.value.value) {
                            ReferenceItem(section, kv.key, kv.value).let { list.add(it) }
                            return@sectionFor
                        }
                    }
                }
            }
        }
        return list
    }

}