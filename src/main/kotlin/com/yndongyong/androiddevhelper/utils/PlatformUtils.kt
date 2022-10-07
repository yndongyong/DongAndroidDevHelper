package com.yndongyong.androiddevhelper.utils

import com.intellij.openapi.editor.Editor
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilBase
import com.intellij.psi.xml.XmlTag
import kotlin.jvm.internal.Intrinsics

object PlatformUtils {
    fun getPsiClassAroundCaret(editor: Editor?): PsiClass? {
        /*  47 */
        if (editor == null) {
            /*  48 */
            return null
        }

        /*  51 */
        val element = PsiUtilBase.getElementAtCaret(editor)
            ?: /*  53 */
            return null
        /*  52 */
        /*  55 */
        val target = PsiTreeUtil.getParentOfType(element, PsiClass::class.java)
        /*  56 */return if (target is SyntheticElement) null else target
    }

    fun getXmlTagAtCaretLine(editor: Editor): XmlTag? {
        /*  63 */
        if (editor == null) Intrinsics.checkNotNull(editor)
        val project = editor.project
            ?: /*  65 */
            return null
        /*  64 */

        /*  68 */
        val document = editor.document
        /*  69 */
        val file = PsiDocumentManager.getInstance(project).getPsiFile(document)
            ?: /*  71 */
            return null
        /*  70 */

        /*  74 */
        val caretModel = editor.caretModel
        /*  75 */
        val caretModelOffset = caretModel.offset

        /*  77 */
        val lineNumber = document.getLineNumber(caretModelOffset)
        /*  78 */
        val lineStartOffset = document.getLineStartOffset(lineNumber)
        /*  79 */
        val lineEndOffset = document.getLineEndOffset(lineNumber)
        /*  80 */
        val lineMiddleOffset = (lineStartOffset + lineEndOffset) / 2

        /*  82 */
        val psiElement = file.findElementAt(lineMiddleOffset)

        /*  84 */return PsiTreeUtil.getParentOfType(psiElement, XmlTag::class.java)
    }

    fun getXmlTagAroundCaret(editor: Editor?): XmlTag? {
        /*  90 */
        if (editor == null) {
            /*  91 */
            return null
        }

        /*  94 */
        val element = PsiUtilBase.getElementAtCaret(editor)
        /*  95 */return PsiTreeUtil.getParentOfType(element, XmlTag::class.java)
    }

    fun getClassReferenceFromJavaFile(javaFile: PsiJavaFile, editor: Editor?): String? {
        /* 101 */
        if (javaFile == null) Intrinsics.checkNotNull(javaFile)
        val psiClassAroundCaret = getPsiClassAroundCaret(editor)


        /* 104 */if (psiClassAroundCaret == null) {
            /* 105 */
            val psiClasses = javaFile.classes
            /* 106 */for (psiClass in psiClasses) {
                /* 107 */
                if (psiClass.hasModifierProperty("public")) {
                    /* 108 */
                    return psiClass.qualifiedName
                }
            }
            /* 111 */return psiClasses[0].qualifiedName
        }


        /* 115 */if (psiClassAroundCaret is PsiAnonymousClass) {
            /* 116 */
            val baseClassType = psiClassAroundCaret.baseClassType
            /* 117 */
            val resolvedClass = baseClassType.resolve()
            /* 118 */if (resolvedClass != null) {
                /* 119 */
                return resolvedClass.qualifiedName
            }
        }

        /* 123 */return psiClassAroundCaret.qualifiedName
    }
}