package com.yndongyong.androiddevhelper.copynameactions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.yndongyong.androiddevhelper.utils.Logger.debug
import com.yndongyong.androiddevhelper.utils.PlatformUtils
import com.yndongyong.androiddevhelper.utils.StringUtils
import com.yndongyong.androiddevhelper.utils.UIUtils
import java.awt.datatransfer.StringSelection

abstract class BaseCopyAndroidReferenceAction : AnAction() {

    override fun update(e: AnActionEvent) {
        super.update(e)
        val presentation = e.presentation
        val project = e.getData(PlatformDataKeys.PROJECT)
        val psiFile = e.getData(LangDataKeys.PSI_FILE)
        presentation.isEnabled = !(project == null || psiFile == null || psiFile.isDirectory)
        presentation.isVisible = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val toCopy: String?
        val project = e.getData(PlatformDataKeys.PROJECT) ?: return
        val editor = e.getData(PlatformDataKeys.EDITOR)
        val psiFile = e.getData(LangDataKeys.PSI_FILE)!!
        debug("Current Psi File: $psiFile")
        toCopy = if (psiFile is PsiJavaFile) {
            PlatformUtils.getClassReferenceFromJavaFile(
                psiFile,
                editor
            )
        } else {
            getAndroidResourceReference(psiFile, editor)
        }
        if (StringUtils.isBlank(toCopy)) {
            UIUtils.showErrorNotification(project, "copy fail")
        } else {
            CopyPasteManager.getInstance().setContents(StringSelection(toCopy))
            val toCopyMsg = "copy success: $toCopy"
            UIUtils.showNotification(project, toCopyMsg)
        }
    }

    protected abstract fun getAndroidResourceReference(paramPsiFile: PsiFile, paramEditor: Editor?): String?
}