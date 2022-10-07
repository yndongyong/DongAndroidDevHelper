package com.yndongyong.androiddevhelper.utils

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile

object AndroidUtils {
    private const val FOLDER_NAME_RES = "res"
    private const val FILE_SUFFIX_NINE_PATCH_DRAWABLE = ".9.png"
    private const val RES_TYPE_ANIMATOR = "animator"
    private const val RES_TYPE_ANIM = "anim"
    private const val RES_TYPE_COLOR = "color"
    private const val RES_TYPE_DRAWABLE = "drawable"
    private const val RES_TYPE_MIPMAP = "mipmap"
    private const val RES_TYPE_LAYOUT = "layout"
    private const val RES_TYPE_MENU = "menu"
    private const val RES_TYPE_RAW = "raw"
    private const val RES_TYPE_STRING = "string"
    private const val RES_TYPE_STYLE = "style"
    private const val RES_TYPE_FAKE_VALUES = "values"
    private const val RES_TYPE_BOOL = "bool"
    private const val RES_TYPE_DIMEN = "dimen"
    private const val RES_TYPE_ID = "id"
    private const val RES_TYPE_INTEGER = "integer"
    private const val RES_TYPE_INTEGER_ARRAY = "integer-array"
    private const val RES_TYPE_TYPED_ARRAY = "array"
    private const val RES_TYPE_STRING_ARRAY = "string-array"
    private const val RES_TYPE_PLURALS = "plurals"
    private const val RES_TYPE_STYLEABLE = "declare-styleable"
    private const val RES_TYPE_XML = "xml"
    private const val RES_TYPE_FONT = "font"
    private val RES_REF_PREFIX_FOR_JAVA_MAP: MutableMap<String?, String> = HashMap(32)

    init {
        RES_REF_PREFIX_FOR_JAVA_MAP["animator"] = "R.animator."
        RES_REF_PREFIX_FOR_JAVA_MAP["anim"] = "R.anim."
        RES_REF_PREFIX_FOR_JAVA_MAP["color"] = "R.color."
        RES_REF_PREFIX_FOR_JAVA_MAP["drawable"] = "R.drawable."
        RES_REF_PREFIX_FOR_JAVA_MAP["mipmap"] = "R.drawable."
        RES_REF_PREFIX_FOR_JAVA_MAP["layout"] = "R.layout."
        RES_REF_PREFIX_FOR_JAVA_MAP["menu"] = "R.menu."
        RES_REF_PREFIX_FOR_JAVA_MAP["raw"] = "R.raw."
        RES_REF_PREFIX_FOR_JAVA_MAP["style"] = "R.style."
        RES_REF_PREFIX_FOR_JAVA_MAP["string"] = "R.string."
        RES_REF_PREFIX_FOR_JAVA_MAP["values"] = "values"
        RES_REF_PREFIX_FOR_JAVA_MAP["bool"] = "R.bool."
        RES_REF_PREFIX_FOR_JAVA_MAP["dimen"] = "R.dimen."
        RES_REF_PREFIX_FOR_JAVA_MAP["id"] = "R.id."
        RES_REF_PREFIX_FOR_JAVA_MAP["integer"] = "R.integer."
        RES_REF_PREFIX_FOR_JAVA_MAP["integer-array"] = "R.array."
        RES_REF_PREFIX_FOR_JAVA_MAP["array"] = "R.array."
        RES_REF_PREFIX_FOR_JAVA_MAP["string-array"] = "R.array."
        RES_REF_PREFIX_FOR_JAVA_MAP["plurals"] = "R.plurals."
        RES_REF_PREFIX_FOR_JAVA_MAP["declare-styleable"] = "R.styleable."
        RES_REF_PREFIX_FOR_JAVA_MAP["xml"] = "R.xml."
    }

    private val RES_REF_PREFIX_FOR_XML_MAP: MutableMap<String?, String> = HashMap(32)

    init {
        RES_REF_PREFIX_FOR_XML_MAP["animator"] = "@animator/"
        RES_REF_PREFIX_FOR_XML_MAP["anim"] = "@anim/"
        RES_REF_PREFIX_FOR_XML_MAP["color"] = "@color/"
        RES_REF_PREFIX_FOR_XML_MAP["drawable"] = "@drawable/"
        RES_REF_PREFIX_FOR_XML_MAP["mipmap"] = "@drawable/"
        RES_REF_PREFIX_FOR_XML_MAP["layout"] = "@layout/"
        RES_REF_PREFIX_FOR_XML_MAP["menu"] = "@menu."
        RES_REF_PREFIX_FOR_XML_MAP["raw"] = "@raw/"
        RES_REF_PREFIX_FOR_XML_MAP["string"] = "@string/"
        RES_REF_PREFIX_FOR_XML_MAP["style"] = "@style/"
        RES_REF_PREFIX_FOR_XML_MAP["values"] = "values"
        RES_REF_PREFIX_FOR_XML_MAP["bool"] = "@bool/"
        RES_REF_PREFIX_FOR_XML_MAP["dimen"] = "@dimen/"
        RES_REF_PREFIX_FOR_XML_MAP["id"] = "@id/"
        RES_REF_PREFIX_FOR_XML_MAP["integer"] = "@integer/"
        RES_REF_PREFIX_FOR_XML_MAP["integer-array"] = "@array."
        RES_REF_PREFIX_FOR_XML_MAP["array"] = "@array."
        RES_REF_PREFIX_FOR_XML_MAP["xml"] = "@xml/"
        RES_REF_PREFIX_FOR_XML_MAP["font"] = "@font/"
    }

    private const val XML_ATTR_NAME = "name"
    private const val XML_ATTR_TYPE = "type"
    fun getResRefForJava(file: PsiFile, editor: Editor?): String? {
        if (file == null) checkNotNull(file)
        return getResRef(file, editor, RES_REF_PREFIX_FOR_JAVA_MAP)
    }

    @JvmStatic
    fun getResRefForXml(file: PsiFile, editor: Editor?): String? {
        if (file == null) checkNotNull(file)
        return getResRef(file, editor, RES_REF_PREFIX_FOR_XML_MAP)
    }

    private fun getResRef(file: PsiFile, editor: Editor?, resRefPrefixMap: Map<String?, String>): String? {
        if (file == null) checkNotNull(file)
        if (resRefPrefixMap == null) checkNotNull(resRefPrefixMap)
        if (!isProbablyResFile(file)) {
            return null
        }
        val resRefPrefixFromFile = getResRefPrefixFromFile(file, resRefPrefixMap)
            ?: return null
        if (resRefPrefixFromFile == "values") {
            return if (editor != null && file is XmlFile) {
                getResRefInValuesXml(editor, resRefPrefixMap)
            } else null
        }
        val fileName = file.name
        val resName = getResName(fileName)
        return resRefPrefixFromFile + resName
    }

    private fun getResRefInValuesXml(editor: Editor, resRefPrefixMap: Map<String?, String>): String? {
        if (editor == null) checkNotNull(editor)
        if (resRefPrefixMap == null) checkNotNull(resRefPrefixMap)
        val xmlTag = PlatformUtils.getXmlTagAtCaretLine(editor) ?: return null
        val tagName = xmlTag.name
        var refResPrefix = resRefPrefixMap[tagName]
        if (refResPrefix == null) {
            val typeAttr = xmlTag.getAttribute("type")
            if (typeAttr != null) {
                val resType = typeAttr.value
                refResPrefix = resRefPrefixMap[resType]
            }
        }
        if (refResPrefix != null) {
            val nameAttr = xmlTag.getAttribute("name")
            if (nameAttr != null) {
                val resName = nameAttr.value
                return refResPrefix + resName
            }
        }
        return null
    }

    private fun getResName(fileName: String): String {
        val resName: String
        if (fileName == null) checkNotNull(fileName)
        resName = if (isProbablyNinePatchFile(fileName)) {
            fileName.substring(0, fileName.length - ".9.png".length)
        } else {
            val fileExtIndex = fileName.lastIndexOf(".")
            fileName.substring(0, fileExtIndex)
        }
        if (resName == null) checkNotNull(resName)
        return resName
    }

    private fun isProbablyNinePatchFile(fileName: String): Boolean {
        if (fileName == null) checkNotNull(fileName)
        val beginIndex = fileName.length - ".9.png".length
        return beginIndex > 0 && fileName.substring(beginIndex) == ".9.png"
    }

    private fun getResRefPrefixFromFile(file: PsiFile, resRefPrefixMap: Map<String?, String>): String? {
        if (file == null) checkNotNull(file)
        if (resRefPrefixMap == null) checkNotNull(resRefPrefixMap)
        val parentDir = file.parent ?: return null
        val dirName = parentDir.name
        if (dirName.contains("animator")) {
            return resRefPrefixMap["animator"]
        }
        for (resType in resRefPrefixMap.keys) {
            if (dirName.contains(resType!!)) {
                return resRefPrefixMap[resType]
            }
        }
        return null
    }

    private fun isProbablyResFile(file: PsiFile): Boolean {
        if (file == null) checkNotNull(file)
        var parent = file.parent
        parent = parent?.parent
        return if (parent == null) {
            false
        } else "res" == parent.name
    }

    private fun isProbablyResFile(file: VirtualFile): Boolean {
        if (file == null) checkNotNull(file)
        var parent: VirtualFile = file.parent ?: return false
        parent = parent.parent
        return if (parent == null) {
            false
        } else "res" == parent.name
    }
}