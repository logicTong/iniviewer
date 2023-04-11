package com.tianhe.iniviewer.ui.path

import com.intellij.icons.AllIcons
import com.intellij.ide.util.TreeFileChooser
import com.intellij.ide.util.TreeFileChooserFactory
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.components.BorderLayoutPanel
import com.tianhe.iniviewer.utils.Log
import com.tianhe.iniviewer.data.PathList
import com.tianhe.iniviewer.data.model.SectionDict
import com.tianhe.iniviewer.inifile.IniDataLoader
import com.tianhe.iniviewer.utils.NotificationUtils
import java.awt.Dimension
import java.awt.FlowLayout
import java.io.File
import javax.swing.*
import javax.swing.border.EmptyBorder

/**
 * Created by tianhe on 2023/4/8
 */
class PathManagePanel(val project: Project) : BorderLayoutPanel() {

    val TAG = "PathManagePanel"

    /**
     * 顶部路径选择
     */
    val inputLabel = JLabel("please input the path of ini file:")
    val inputTextFiled = JTextField()
    val addPathButton = JButton(AllIcons.Actions.AddFile).apply { toolTipText = "add ini file" }
    val deletePathButton = JButton(AllIcons.Actions.GC).apply { toolTipText = "remove selected ini file" }
    val addPathTF = TextFieldWithBrowseButton(
        inputTextFiled
    ) {
        showIniFileChooser()
    }

    val topPanel = JPanel(VerticalFlowLayout())

    /**
     * 路径列表
     */
    var listModel = DefaultListModel<String>()
    val list = JBList(listModel)
    val listPanel = JBScrollPane(list)
    var refreshCallback: (() -> Unit)? = null


    init {
        minimumSize = Dimension(500, 400)
        initTopPanel()
        listPanel.border = EmptyBorder(5, 10, 5, 10)
        addToCenter(listPanel)
        refreshList()
    }


    private fun initTopPanel() {
        topPanel.add(inputLabel)
        topPanel.run {
            add(inputLabel)
            add(addPathTF)
            add(JPanel(FlowLayout(FlowLayout.LEFT)).apply {
                add(deletePathButton)
                add(addPathButton)
            })
        }
        addToTop(topPanel)
        addPathButton.addActionListener {
            val path = addPathTF.text
            if (path.isEmpty() || !path.endsWith("ini", true)) {
                Log.e(TAG, "addPathButton action: path=$path")
                NotificationUtils.showNotification(
                    project,
                    "not a ini file, please check input content:\n $path",
                    NotificationType.ERROR
                )
                return@addActionListener
            }
            if (PathList.containPath(path)) {
                NotificationUtils.showNotification(project, "path has exist! \n${path} ", NotificationType.ERROR)
                return@addActionListener
            }
            if (PathList.addPath(path)) {
                IniDataLoader.readIniFile(path, success = {
                    SectionDict.addIniFile(it)
                    SectionDict.dump()
                    refreshList()
                    refreshCallback?.let { it() }
                    NotificationUtils.showNotification(
                        project,
                        "read ini file finish \n${path}",
                        NotificationType.INFORMATION
                    )
                }, fail = { p, error ->
                    Log.e(TAG, "readIniFile: error, path = $p", error)
                    NotificationUtils.showNotification(
                        project,
                        "read ini file fail: \n $p",
                        NotificationType.ERROR
                    )
                })
            }
        }
        deletePathButton.addActionListener {
            list.selectedValue?.let {
                PathList.removePath(it)
                SectionDict.removeIniFile(it)
                refreshList()
                refreshCallback?.let { it() }
            }
        }
    }


    private fun refreshList() {
        listModel = buildListModel()
        list.model = listModel
    }


    private fun showIniFileChooser() {
        val fileChooser = TreeFileChooserFactory.getInstance(project)
            .createFileChooser("choose a ini file", getLastIniFile(), null, object : TreeFileChooser.PsiFileFilter {
                override fun accept(file: PsiFile?): Boolean {
                    file?.let {
                        return it.virtualFile.path.endsWith(".ini", true)
                    }
                    return false
                }
            })
        fileChooser.showDialog()
        addPathTF.text = fileChooser.selectedFile?.virtualFile?.path ?: ""
    }

    private fun getLastIniFile(): PsiFile? {
        PathList.getLatestPath()?.let { path ->
            VfsUtil.findFileByIoFile(File(path), true)?.let {
                return PsiManager.getInstance(project).findFile(it)
            }
        }
        return null
    }


    private fun buildListModel(): DefaultListModel<String> {
        listModel.clear()
        PathList.pathList.forEach {
            listModel.addElement(it)
        }
        return listModel
    }

}