package com.tianhe.iniviewer.ui.manage.ini

import com.intellij.icons.AllIcons
import com.intellij.ide.util.TreeFileChooser
import com.intellij.ide.util.TreeFileChooserFactory
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
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
import com.tianhe.iniviewer.utils.FileChooser
import com.tianhe.iniviewer.utils.NotificationUtils
import java.awt.Dimension
import java.awt.FlowLayout
import java.io.File
import javax.swing.*
import javax.swing.border.EmptyBorder

/**
 * Created by tianhe on 2023/4/8
 */
class IniManagePanel(val project: Project) : BorderLayoutPanel() {

    val TAG = "PathManagePanel"

    /**
     * 顶部路径选择
     */
    val inputLabel = JLabel("add ini files by click \"add file\" or \"add directory\" button:")
    val addFileButton = JButton(AllIcons.Actions.AddMulticaret).apply { toolTipText = "add a ini file" }
//    val addFileButton = JButton(AllIcons.Actions.AddFile).apply { toolTipText = "add a ini file" }
    val addDirectoryButton =
        JButton(AllIcons.Actions.NewFolder).apply { toolTipText = "add ini files in directory" }
    val deletePathButton = JButton(AllIcons.Actions.GC).apply { toolTipText = "remove selected ini file" }


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
            add(JPanel(FlowLayout(FlowLayout.LEFT)).apply {
                add("add_file", addFileButton)
                add("add_dir", addDirectoryButton)
                add(deletePathButton)
            })
        }
        addToTop(topPanel)
        addFileButton.addActionListener {
            showIniFileChooser()?.let { addPath(it) }
        }
        addDirectoryButton.addActionListener {
            FileChooser(project).chooseDirectory(PathList.getLatestPath())?.let {
                val dir = File(it)
                if (dir.exists()) {
                    if (dir.isFile) {
                        addPath(it)
                    } else if (dir.isDirectory) {
                        Log.d(TAG, "add directory =${dir.absolutePath}")
                        dir.listFiles { _, name ->
                            return@listFiles name.endsWith(".ini")
                        }?.forEach { file ->
                            addPath(file.absolutePath)
                        }
                    }
                }
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


    private fun addPath(path: String) {
        if (path.isEmpty() || !path.endsWith("ini", true)) {
            Log.e(TAG, "addPathButton action: path=$path")
            NotificationUtils.showNotification(
                project,
                "not a ini file, please check input content:\n $path",
                NotificationType.ERROR
            )
            return
        }
        if (PathList.containPath(path)) {
            NotificationUtils.showNotification(project, "path has exist! \n${path} ", NotificationType.ERROR)
            return
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

    private fun showIniFileChooser(): String? {
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
        return fileChooser.selectedFile?.virtualFile?.path
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