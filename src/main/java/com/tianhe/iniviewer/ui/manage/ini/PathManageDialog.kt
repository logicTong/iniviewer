package com.tianhe.iniviewer.ui.manage.ini

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.tianhe.iniviewer.ui.manage.image.ImageManagePanel
import javax.swing.*

/**
 * Created by tianhe on 2023/4/8
 */
class PathManageDialog(val project: Project, val type: PathType, val refreshCallback: (() -> Unit)? = null) :
    DialogWrapper(project, null, false, IdeModalityType.PROJECT, false) {


    init {
        init()
    }

    override fun createCenterPanel(): JComponent {
        if (type == PathType.INI_PATH) {
            val panel = IniManagePanel(project)
            panel.refreshCallback = refreshCallback
            return panel
        } else {
            return ImageManagePanel(project)
        }
    }

    enum class PathType {
        INI_PATH,
        IMAGE_DIR,
    }
}