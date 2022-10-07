package com.yndongyong.androiddevhelper.copynameactions

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.yndongyong.androiddevhelper.utils.AndroidUtils.getResRefForXml
import kotlin.jvm.internal.Intrinsics

/*    */   class CopyReferenceForXmlAction : BaseCopyAndroidReferenceAction() {
    override fun getAndroidResourceReference(file: PsiFile, editor: Editor?): String? {
        if (file == null) Intrinsics.checkNotNull(file)
        return getResRefForXml(file, editor)
    }
}