package com.tianhe.iniviewer.ui.main

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.impl.ProjectViewImpl
import com.intellij.notification.NotificationType
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDocumentManager
import com.intellij.ui.TextFieldWithStoredHistory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.components.BorderLayoutPanel
import com.tianhe.iniviewer.data.ImageDir
import com.tianhe.iniviewer.data.model.SectionTree
import com.tianhe.iniviewer.logic.Consts
import com.tianhe.iniviewer.logic.IniViewer
import com.tianhe.iniviewer.ui.main.tree.node.*
import com.tianhe.iniviewer.ui.manage.ini.PathManageDialog
import com.tianhe.iniviewer.ui.manage.reference.ReferencePanel
import com.tianhe.iniviewer.utils.Log
import com.tianhe.iniviewer.utils.Navigation
import com.tianhe.iniviewer.utils.NotificationUtils
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
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
    val iniManageBtn = JButton(AllIcons.Actions.Colors).apply { toolTipText = "manage ini file" }
    val imageManageBtn = JButton(AllIcons.Actions.Dump).apply { toolTipText = "set image dir" }

    /**
     * 文本输入框
     */
    val inputPanel = JPanel(VerticalFlowLayout())
    val treeRootLabel = JBLabel("please input the root section: ")
    val treeRootText = TextFieldWithStoredHistory("${Consts.getProjectKey()}.RootNode").apply { setHistorySize(20) }

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
            add(iniManageBtn)
            add(imageManageBtn)
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
        val treeModel = SectionTree(text).buildTreeModel()
        tree.model = DefaultTreeModel(treeModel)
        treeModel?.let {
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
            saveEditorsChange()
            IniViewer.loadAllIniFile{
                refreshTree(treeRootText.text)
            }
        }

        iniManageBtn.addActionListener {
            PathManageDialog(project, PathManageDialog.PathType.INI_PATH) {
                refreshTree(treeRootText.text)
            }.apply {
                title = "manage ini file"
            }.showAndGet()
        }

        imageManageBtn.addActionListener {
            PathManageDialog(project, PathManageDialog.PathType.IMAGE_DIR).apply {
                title = "set image directory"
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
                    } else if (clickCount == 2) {
                        selectNode?.let {
                            Navigation(project).navigationToEditor(it.section, it.lineNum)
                            selectNode = null
                        }
                    }
                }
            }
        })

        tree.addTreeSelectionListener {
            selectNode = tree.lastSelectedPathComponent as TreeNode?
            Log.d(TAG, "lastNode=$selectNode")
        }
    }

    private fun saveEditorsChange(){
        for (e in FileEditorManagerEx.getInstanceEx(project).allEditors) {
            if (e.isModified && e.file != null) {
                if (e.file!!.path.endsWith(".ini")&&e is TextEditor){
                    Log.d(TAG, "saveEditorsChange: save file=${e.file}")
                    e.editor.document.let {
                        FileDocumentManager.getInstance().saveDocument(it)
                    }
                }
            }
        }

        FileEditorManagerEx.getInstanceEx(project).selectedTextEditor?.let { editor ->
            editor.document.let {
                FileDocumentManager.getInstance().saveDocument(it)
            }
        }
    }

    private fun isImageNode(): Boolean {
        if (selectNode is KVTreeNode) {
            return (selectNode as KVTreeNode).isImageFile()
        }
        return false
    }

    fun showMenu(tree: Tree, x: Int, y: Int) {
        JPopupMenu().apply {
            add(JMenuItem("edit ini file").apply {
                addActionListener {
                    selectNode?.let {
                        Navigation(project).navigationToEditor(it.section, it.lineNum)
                        selectNode = null
                    }
                }
            })
            add(JMenuItem("locate to ${if (isImageNode()) "image" else "file"}").apply {
                addActionListener {
                    selectNode?.let {
                        navigationToFile(it)
                        selectNode = null
                    }
                }
            })
            if (isSectionObj()) {
                add(JMenuItem("refer to me").apply {
                    addActionListener {
                        selectNode?.let {
                            showReferencesDialog(it)
                            selectNode = null
                        }
                    }
                })
            }
        }.show(tree, x, y)
    }

    private fun isSectionObj(): Boolean {
        return selectNode != null && (selectNode is SectionTreeNode || selectNode is MultiSectionTreeNode)
    }


    private fun showReferencesDialog(node: TreeNode) {
        if (node is SectionTreeNode || node is MultiSectionTreeNode) {
            node.section.let {
                val dialog = object : DialogWrapper(project, null, false, IdeModalityType.PROJECT, false) {

                    init {
                        init()
                    }

                    override fun createCenterPanel(): JComponent {
                        return ReferencePanel(project, it.name)
                    }
                }
                dialog.showAndGet()
            }
        }
    }

    private fun navigationToFile(node: TreeNode) {
        if (node is KVTreeNode && node.isImageFile()) {
            if (!ImageDir.hasImageDir()) {
                NotificationUtils.showNotification(
                    project,
                    "not set image directory: \n please set image directory first",
                    NotificationType.ERROR
                )
                return
            }
            val imageFile = File(ImageDir.directory, node.getImageFile())
            Log.d(TAG, "navigationToFile: imageFile = ${imageFile.absolutePath}")
            if (!imageFile.exists()) {
                NotificationUtils.showNotification(
                    project,
                    "image file not exist: \n ${imageFile.absolutePath}",
                    NotificationType.ERROR
                )
                return
            }
            VfsUtil.findFileByIoFile(imageFile, false)?.let { vf ->
                val projectView = ProjectViewImpl.getInstance(project) as ProjectViewImpl
                projectView.select(vf, vf, true)
            }
        } else {
            node.section.let { section ->
                VfsUtil.findFileByIoFile(File(section.intFilePath), false)?.let { vf ->
                    val projectView = ProjectViewImpl.getInstance(project) as ProjectViewImpl
                    projectView.select(vf, vf, true)
                }
            }
        }
    }


}

