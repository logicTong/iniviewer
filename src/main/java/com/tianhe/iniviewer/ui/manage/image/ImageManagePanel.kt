package com.tianhe.iniviewer.ui.manage.image

import com.intellij.icons.AllIcons
import com.intellij.ide.util.TreeFileChooser
import com.intellij.ide.util.TreeFileChooserDialog
import com.intellij.ide.util.TreeFileChooserFactory
import com.intellij.notification.NotificationType
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.components.BorderLayoutPanel
import com.tianhe.iniviewer.data.ImageDir
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
class ImageManagePanel(val project: Project) : JPanel() {

    val TAG = "PathManagePanel"

    /**
     * 顶部路径选择
     */
    val imageLabel = JLabel("current image directory:")
    val imageTextFiled = JTextField()
    val searchButton = JButton(AllIcons.Actions.Search).apply { toolTipText = "set image directory" }

    val bottomPanel = BorderLayoutPanel()

    init {
        minimumSize = Dimension(600, 80)
        layout = VerticalFlowLayout()
        add(imageLabel)
        bottomPanel.apply {
            addToCenter(imageTextFiled)
            addToRight(searchButton)
        }.let {
            add(it)
        }
        searchButton.addActionListener {
            chooseImageDir()
        }
        imageTextFiled.text = ImageDir.directory
    }


    private fun chooseImageDir() {
        FileChooser(project).chooseDirectory(ImageDir.directory)?.let {
            imageTextFiled.text = it
            ImageDir.directory = it
        }
    }

}