package com.tianhe.iniviewer.ui.path

import com.intellij.ide.util.TreeFileChooserFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.io.FileUtil
import javax.swing.*

/**
 * Created by tianhe on 2023/4/8
 */
class PathManageDialog(private val project: Project) : DialogWrapper(project, null, false, IdeModalityType.IDE, false) {

    val contentPanel = PathManagePanel(project)

    init {
        title = "manage ini file"
        init()
    }

    override fun createCenterPanel(): JComponent {
        return contentPanel
    }


}