package com.yndongyong.androiddevhelper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.yndongyong.androiddevhelper.AdbHelper
import com.yndongyong.androiddevhelper.ui.UrlSchemeInputDialog
import com.yndongyong.androiddevhelper.utils.UIUtils
import org.jetbrains.android.sdk.AndroidSdkUtils

const val ERROR_TITLE = "Error Message"

/**
 * url scheme test
 */
class UrlSchemeTestAction : AnAction() {


    override fun actionPerformed(e: AnActionEvent) {
        e.project?.run {
            val testUrlScheme: (urlScheme: String) -> Unit = {
                val urlScheme = if (!it.contains("://")) "https://$it" else it
                val shell = "am start -a android.intent.action.VIEW -d $urlScheme"

                val project = e.project!!
                val adbPath = AndroidSdkUtils.getAdb(project)?.path.toString()

                val result = AdbHelper.execCMD("$adbPath shell $shell")
                println(result)

                if (result.isBlank()) {
                    UIUtils.showErrorNotification(project, "Please check your phone is properly connected!")
                } else {
                    UIUtils.showNotification(project, result)
                }
            }

            UrlSchemeInputDialog(
                "Click the test button after entering the url scheme.",
                "URL Scheme Test",
                testUrlScheme
            ).show()
        } ?: Messages.showErrorDialog("project is error", ERROR_TITLE)
    }
}