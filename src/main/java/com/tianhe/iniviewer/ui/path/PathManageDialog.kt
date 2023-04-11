package com.tianhe.iniviewer.ui.path

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.*

/**
 * Created by tianhe on 2023/4/8
 */
class PathManageDialog(private val project: Project, refreshCallback: () -> Unit) :
    DialogWrapper(project, null, false, IdeModalityType.IDE, false) {

    val contentPanel = PathManagePanel(project)

    init {
        title = "manage ini file"
        contentPanel.refreshCallback = refreshCallback
        init()
    }

    override fun createCenterPanel(): JComponent {
        return contentPanel
    }

}