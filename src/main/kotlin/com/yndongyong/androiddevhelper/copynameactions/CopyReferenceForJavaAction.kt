package com.yndongyong.androiddevhelper.copynameactions

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.yndongyong.androiddevhelper.utils.AndroidUtils

class CopyReferenceForJavaAction : BaseCopyAndroidReferenceAction() {
    override fun getAndroidResourceReference(file: PsiFile, editor: Editor?): String? {
        if (file == null) checkNotNull(file)
        return AndroidUtils.getResRefForJava(file, editor)
    }
}