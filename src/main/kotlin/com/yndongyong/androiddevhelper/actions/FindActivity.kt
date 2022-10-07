package com.yndongyong.androiddevhelper.actions

import com.intellij.codeInsight.navigation.NavigationUtil
import com.intellij.ide.util.TreeClassChooserFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.search.GlobalSearchScope
import com.yndongyong.androiddevhelper.AdbHelper
import com.yndongyong.androiddevhelper.utils.UIUtils
import org.jetbrains.android.sdk.AndroidSdkUtils

/**
 * 查找Activity 以及包含的 Fragment
 */
class FindActivity : AnAction() {

    /** The path of ADB*/
    private var adbPath = ""

    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project!!

        // Must use absolute path if used jvm command line
        adbPath = AndroidSdkUtils.getAdb(project)?.path.toString()

        val result = AdbHelper.execCMD("$adbPath shell dumpsys activity activities | grep mResumedActivity")
        println(result)


        if (result.isBlank()) {
            return UIUtils.showErrorNotification(project, "Please check your phone is properly connected!")
        }
        if (result.contains(".launcher/")) {
            return UIUtils.showErrorNotification(project, "Please open the corresponding app of your project on your mobile phone!")
        }

        val activityPackage = result.split("/")[1].split(" ")[0]
        println(activityPackage)
        val splitResult = activityPackage.split(".")
        val activity = splitResult[splitResult.size - 1]
        println(activity)


        val result2 = AdbHelper.execCMD("$adbPath shell dumpsys activity $activityPackage")
        var flag = 0
        val fragments: MutableList<String?> = ArrayList()
        val lines = result2.split("\n").reversed()
        for (line in lines) {
            if (line.contains("#") && flag == 0) flag++
            if (line.contains("Added")) flag++
            if (flag == 1 && line.trim().startsWith("#")) {
                fragments.add(line.trim().split("{")[0].split(" ")[1])
            }
        }
        fragments.reverse()
        fragments.remove("SupportRequestManagerFragment")
        println(fragments)
        fragments.add(0, activity)


        try {
            val scope: GlobalSearchScope = GlobalSearchScope.allScope(project)
            val chooser = TreeClassChooserFactory.getInstance(project)
                .createNoInnerClassesScopeChooser(
                    "Choose Class to Move",
                    scope,
                    {
                        fragments.contains(it.name)
                    },
                    null
                )
            chooser.showDialog()
            if (chooser.selected != null) NavigationUtil.activateFileWithPsiElement(chooser.selected)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}