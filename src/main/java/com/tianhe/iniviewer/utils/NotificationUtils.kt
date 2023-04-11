package com.tianhe.iniviewer.utils

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.tianhe.iniviewer.logic.Consts

/**
 * Created by tianhe on 2023/4/10
 */
object NotificationUtils {


    fun showNotification(project: Project, content: String, type: NotificationType) {
        NotificationGroupManager.getInstance().getNotificationGroup(Consts.NOTIFICATION_ID)
            .createNotification(content, type)
            .notify(project)
    }
}