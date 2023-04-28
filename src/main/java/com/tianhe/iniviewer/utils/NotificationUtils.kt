package com.tianhe.iniviewer.utils

import com.intellij.notification.*
import com.intellij.openapi.project.Project
import com.tianhe.iniviewer.logic.Consts

/**
 * Created by tianhe on 2023/4/10
 */
object NotificationUtils {

    const val TAG = "NotificationUtils"

    fun showNotification(project: Project, content: String, type: NotificationType) {
        if (!notificationByGroup(project, content, type)) {
            notificationByBus(content)
        }
    }


    private fun notificationByGroup(project: Project, content: String, type: NotificationType): Boolean {
        try {
            NotificationGroupManager.getInstance().getNotificationGroup(Consts.NOTIFICATION_ID)
                .createNotification(content, type)
                .notify(project)
            return true
        } catch (e: Exception) {
            Log.e(TAG, "notificationByGroup: error", e)
            return false
        }
    }


    private fun notificationByBus(content: String): Boolean {
        try {
            Notifications.Bus.notify(
                Notification(
                    Consts.NOTIFICATION_ID,
                    "Ini Viewer",
                    content,
                    NotificationType.INFORMATION
                )
            )
            return true
        } catch (e: Exception) {
            Log.e(TAG, "notificationByBus: error", e)
            return false
        }
    }
}