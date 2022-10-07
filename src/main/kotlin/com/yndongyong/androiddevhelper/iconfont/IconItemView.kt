package com.yndongyong.androiddevhelper.iconfont

import java.awt.Component
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class IconItemView(private val iconFontGlyph: IconFontGlyph) : JComponent() {
    var rootPanel: JPanel? = null
    var jTextCodePoint: JLabel? = null
    var jTextScript: JLabel? = null
    private fun createUIComponents() {
        rootPanel = JPanel()
    }

    init {
        setUI()
    }

    private fun setUI() {
        val iconVo = iconFontGlyph.iconVo
        //        jTextCodePoint.setText("&#x" + Integer.toHexString(iconVo.getCodePoint()) + ";");
        jTextCodePoint!!.text = "\\u" + Integer.toHexString(iconVo.codePoint.code)
        jTextScript!!.text = iconVo.postScript
    }

    override fun getComponent(n: Int): Component {
        return rootPanel!!
    }
}