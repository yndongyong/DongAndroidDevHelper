package com.yndongyong.androiddevhelper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.yndongyong.androiddevhelper.AdbHelper
import com.yndongyong.androiddevhelper.utils.UIUtils
import org.jetbrains.android.sdk.AndroidSdkUtils

/**
 *  打开App 设置界面
 */
class StartAppSettingAction : AnAction() {


    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val adbPath = AndroidSdkUtils.getAdb(project)?.path.toString()

        //1. 获取当前App 包名
        val shell1 = "dumpsys window | grep mCurrentFocus"


        val message = AdbHelper.execCMD("$adbPath shell $shell1")
        println(message)

        val array = message.split("/")
        if (array.size == 2) {
            val packageName = array[0].split(" ").last()
            val activityName = array[1].replace("}", "")


            if (packageName.isBlank()) {
                return UIUtils.showErrorNotification(project, "Please check your phone is properly connected!")
            }

            //2. 启动App设置画面
            val shell2 = "am start -a android.settings.APPLICATION_DETAILS_SETTINGS -d package:${packageName}"

            val result = AdbHelper.execCMD("$adbPath shell $shell2")
            println(result)

            if (result.isBlank()) {
                return UIUtils.showErrorNotification(project, "Please check your phone is properly connected!")
            }
            UIUtils.showNotification(project, "Start App Setting Success!")
        }


    }
}