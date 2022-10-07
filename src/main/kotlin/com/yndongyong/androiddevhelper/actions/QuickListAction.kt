package com.yndongyong.androiddevhelper.actions

import com.intellij.ide.actions.QuickSwitchSchemeAction
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project

/**
 * adb Operations Popup list
 */
class QuickListAction : QuickSwitchSchemeAction() {

    override fun isEnabled() = true
    override fun getPopupTitle(e: AnActionEvent) = "ADB Operations Popup"

    override fun fillActions(project: Project?, group: DefaultActionGroup, dataContext: DataContext) {
        addAction("com.yndongyong.androiddevhelper.actions.FindActivity", group)
        addAction("com.yndongyong.androiddevhelper.actions.UrlSchemeTestAction", group)
        addAction("com.yndongyong.androiddevhelper.actions.StartAppSettingAction", group)
    }

    private fun addAction(actionId: String, toGroup: DefaultActionGroup) {
        // add action to group if it is available
        ActionManager.getInstance().getAction(actionId)?.let {
            toGroup.add(it)
        }
    }

}