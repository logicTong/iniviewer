package com.tianhe.iniviewer.logic

import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.tianhe.iniviewer.data.PathList
import com.tianhe.iniviewer.data.model.SectionDict
import com.tianhe.iniviewer.inifile.IniDataLoader
import com.tianhe.iniviewer.utils.Log
import com.tianhe.iniviewer.utils.NotificationUtils

/**
 * Created by tianhe on 2023/4/9
 */
object IniViewer {

    const val TAG = "IniViewer"

    private lateinit var project: Project
    private lateinit var toolWindow: ToolWindow


    fun init(project: Project, toolWindow: ToolWindow) {
        this.project = project
        this.toolWindow = toolWindow
        initComponents()
    }


    private fun initComponents() {
        PathList.init(project)
        loadAllIniFile(false)
    }

    fun loadAllIniFile(notifyFinish: Boolean) {
        if (PathList.pathList.isNotEmpty()) {
            SectionDict.clear()
            IniDataLoader.loadAllIniFile(PathList.pathList, success = {
                SectionDict.addIniFile(it)
            }, fail = { path, e ->
                Log.e(TAG, "loadAllInitFile fail, path = $path", e)
                NotificationUtils.showNotification(
                    project,
                    "read ini file fail\n $path",
                    NotificationType.ERROR
                )
            }, allSuccess = {
                Log.d(TAG, "loadAllInitFile success")
                if (notifyFinish) {
                    NotificationUtils.showNotification(
                        project,
                        "read all ini file success",
                        NotificationType.INFORMATION
                    )
                }

            })
        }
    }
}