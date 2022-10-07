package com.yndongyong.androiddevhelper.utils

import com.intellij.notification.*
import kotlin.jvm.internal.Intrinsics

object Logger {
    private var sName: String? = null

    private var sLevel = 0
    const val DEBUG = 3
    const val INFO = 2
    const val WARN = 1
    const val ERROR = 0

    @JvmStatic
    fun init(name: String, level: Int) {
        if (name == null) Intrinsics.checkNotNull(name)
        sName = name
        sLevel = level
        NotificationsConfiguration.getNotificationsConfiguration().register(sName!!, NotificationDisplayType.NONE)
    }

    @JvmStatic
    fun debug(text: String) {
        if (text == null) Intrinsics.checkNotNull(text)
        if (sLevel >= 3) {
            Notifications.Bus.notify(Notification(sName!!, sName + " [DEBUG]", text, NotificationType.INFORMATION))
        }
    }

    @JvmStatic
    fun info(text: String) {
        if (text == null) Intrinsics.checkNotNull(text)
        if (sLevel > 2) {
            Notifications.Bus.notify(Notification(sName!!, sName + " [INFO]", text, NotificationType.INFORMATION))
        }
    }

    fun warn(text: String) {
        if (text == null) Intrinsics.checkNotNull(text)
        if (sLevel > 1) {
            Notifications.Bus.notify(Notification(sName!!, sName + " [WARN]", text, NotificationType.WARNING))
        }
    }

    fun error(text: String) {
        if (text == null) Intrinsics.checkNotNull(text)
        if (sLevel > 0) Notifications.Bus.notify(
            Notification(
                sName!!, sName + " [ERROR]", text, NotificationType.ERROR
            )
        )
    }
}