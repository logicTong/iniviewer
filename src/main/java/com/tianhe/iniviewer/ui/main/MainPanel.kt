package com.tianhe.iniviewer.ui.main

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBScrollPane
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.TextFieldWithStoredHistory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.components.BorderLayoutPanel
import com.tianhe.iniviewer.data.model.SectionTree
import com.tianhe.iniviewer.inifile.IniDataLoader
import com.tianhe.iniviewer.logic.IniViewer
import com.tianhe.iniviewer.ui.main.tree.node.TreeNodeRender
import com.tianhe.iniviewer.ui.path.PathManageDialog
import com.tianhe.iniviewer.utils.Log
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.tree.DefaultTreeModel

/**
 * Created by tianhe on 2023/4/8
 * ini viewer主页面
 */
class MainPanel(val project: Project) : BorderLayoutPanel() {

    val TAG = "MainPanel"


    /**
     * 顶部操作按钮
     */
    val buttonPanel = JPanel(FlowLayout(FlowLayout.LEFT))
    val syncBtn = JButton(AllIcons.Actions.BuildLoadChanges).apply { toolTipText = "reload all ini file" }
    val manageBtn = JButton(AllIcons.Actions.Colors).apply { toolTipText = "manage ini file" }

    /**
     * 文本输入框
     */
    val inputPanel = JPanel(VerticalFlowLayout())
    val treeRootLabel = JBLabel("please input the root section: ")
    val treeRootText = TextFieldWithStoredHistory("RootNode")

    val topPanel = JPanel(VerticalFlowLayout())

    /**
     * 树形控件
     */
    val tree = Tree()
    val treePanel = JBScrollPane()

    init {
        layout = BorderLayout()
        initComponents()
        initListener()
    }


    private fun initComponents() {
        buttonPanel.apply {
            add(syncBtn)
            add(manageBtn)
        }.let {
            topPanel.add(it)
        }
        inputPanel.apply {
            add(treeRootLabel)
            add(treeRootText)
        }.let {
            topPanel.add(it)
        }
        addToTop(topPanel)
        initTree()
        treePanel.apply {
            setViewportView(tree)
        }.let {
            add(it)
        }
        addToCenter(treePanel)

    }

    private fun initTree() {
        tree.apply {
            cellRenderer = TreeNodeRender()
        }
    }

    private fun initListener() {
        treeRootText.textEditor.document.addDocumentListener(object :
            DelayDocumentListener(treeRootText.textEditor, 1300) {
            override fun onTextChange(text: String) {
                Log.d(TAG, "onTextChange: text=$text")
                SectionTree(text).buildTreeModel()?.let {
                    tree.model = DefaultTreeModel(it)
                    treeRootText.addCurrentTextToHistory()
                }
            }
        })

        syncBtn.addActionListener {
            IniViewer.loadAllIniFile(true)
        }

        manageBtn.addActionListener {
            PathManageDialog(project).showAndGet()
        }


    }


}