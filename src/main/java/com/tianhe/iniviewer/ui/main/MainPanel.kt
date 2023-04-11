package com.tianhe.iniviewer.ui.main

import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiManager
import com.intellij.ui.PopupMenuListenerAdapter
import com.intellij.ui.TextFieldWithStoredHistory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.components.BorderLayoutPanel
import com.tianhe.iniviewer.data.model.MultiSection
import com.tianhe.iniviewer.data.model.Section
import com.tianhe.iniviewer.data.model.SectionTree
import com.tianhe.iniviewer.logic.IniViewer
import com.tianhe.iniviewer.ui.main.tree.node.*
import com.tianhe.iniviewer.ui.path.PathManageDialog
import com.tianhe.iniviewer.utils.Log
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.swing.*
import javax.swing.event.MenuKeyEvent
import javax.swing.event.MenuKeyListener
import javax.swing.event.PopupMenuEvent
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

    private var selectNode: TreeNode? = null

    /**
     * 树上右键弹窗
     */
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

    private fun refreshTree(text: String) {
        Log.d(TAG, "refreshTree: text=$text")
        SectionTree(text).buildTreeModel()?.let {
            tree.model = DefaultTreeModel(it)
            treeRootText.addCurrentTextToHistory()
        }
    }

    private fun initListener() {
        treeRootText.textEditor.document.addDocumentListener(object :
            DelayDocumentListener(treeRootText.textEditor, 1300) {
            override fun onTextChange(text: String) {
                Log.d(TAG, "onTextChange: text=$text")
                refreshTree(text)
            }
        })

        syncBtn.addActionListener {
            IniViewer.loadAllIniFile(true)
        }

        manageBtn.addActionListener {
            PathManageDialog(project) {
                refreshTree(treeRootText.text)
            }.showAndGet()
        }

        tree.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                e?.run {
                    if (button == MouseEvent.BUTTON3) {
                        //右键单击
                        val selectPath = tree.getPathForLocation(x, y)
                        tree.selectionPath = selectPath
                        selectNode = selectPath?.lastPathComponent as TreeNode?
                        showMenu(tree, x, y)
                    }
                }
            }
        })

        tree.addTreeSelectionListener {
            selectNode = tree.lastSelectedPathComponent as TreeNode?
            Log.d(TAG, "lastNode=$selectNode")
        }
    }


    fun showMenu(tree: Tree, x: Int, y: Int) {
        JPopupMenu().apply {
            add(JMenuItem("edit ini file").apply {
                addActionListener {
                    selectNode?.let {
                        navigationToFile(it)
                    }
                    selectNode = null
                }
            })
        }.show(tree, x, y)
    }

    private fun navigationToFile(node: TreeNode) {
        node.section.let { section ->
            val lineNum = node.lineNum
            FileEditorManagerEx.getInstanceEx(project).selectedTextEditor?.let { editor ->
                val editingFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)?.virtualFile?.path
                Log.d(TAG, "navigationToFile: editingFile = $editingFile")
                if (section.intFilePath == editingFile) {
                    scrollToLineNum(editor, lineNum)
                    return
                }
            }
            openEditor(section.intFilePath, lineNum)
        }
    }


    private fun openEditor(file: String, lineNum: Int) {
        VfsUtil.findFileByIoFile(File(file), false)?.let { vf ->
            FileEditorManagerEx.getInstanceEx(project).openTextEditor(OpenFileDescriptor(project, vf, 0), true)
                ?.let { editor ->
                    scrollToLineNum(editor, lineNum)
                }
        }
    }


    private fun scrollToLineNum(editor: Editor, lineNum: Int) {
        val position = LogicalPosition(lineNum, 0)
        editor.caretModel.moveToLogicalPosition(position)
        editor.scrollingModel.scrollToCaret(ScrollType.CENTER)
    }


}

