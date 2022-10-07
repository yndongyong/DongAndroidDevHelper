package com.yndongyong.androiddevhelper.iconfont

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.*

abstract class IconFontViewer : FileEditor {
    var rootPanel: JPanel? = null
    var jLabelFontName: JLabel? = null
    var jTextSearch: JTextField? = null
    var jButtonSearch: JButton? = null
    var jListIcons: JList<IconVo>? = null
    var jScrollPanel: JScrollPane? = null
    val codeString: JLabel? = null
    private fun createUIComponents() {
        rootPanel = JPanel()
    }

    override fun getFile(): VirtualFile? {
        return FileEditor.FILE_KEY[this] as VirtualFile
    }
}