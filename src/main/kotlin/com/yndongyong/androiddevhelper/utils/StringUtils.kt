package com.yndongyong.androiddevhelper.utils

import org.apache.http.util.TextUtils
import java.util.*
import java.util.regex.Pattern
import kotlin.jvm.internal.Intrinsics

object StringUtils {
    fun emptyIfNull(text: String?): String {
        if (text ?: "" == null) Intrinsics.checkNotNull(text)
        return text ?: ""
    }

    fun isEmpty(text: String?): Boolean {
        return text == null || text.isEmpty()
    }

    fun isBlank(str: String?): Boolean {
        if (!isEmpty(str)) {
            val strLen = str!!.length
            for (i in 0 until strLen) {
                if (!Character.isWhitespace(str[i])) {
                    return false
                }
            }
            return true
        }
        return true
    }

    fun removeBlanksInString(s: String): String {
        if (s == null) Intrinsics.checkNotNull(s)
        if (s.replace(" ", "") == null) Intrinsics.checkNotNull(s)
        return s.replace(" ", "")
    }

    fun capitalize(word: String?): String {
        if (word == null || word.trim { it <= ' ' }.length < 1) {
            if ("" == null) Intrinsics.checkNotNull(word)
            return ""
        }
        if (word.substring(0, 1)
                .uppercase(Locale.getDefault()) + word.substring(1) == null
        ) throw NullPointerException()
        return word.substring(0, 1).uppercase(Locale.getDefault()) + word.substring(1)
    }

    fun transformUnderscore2Camel(underscoreText: String): String {
        if (underscoreText == null) throw NullPointerException()
        if (TextUtils.isBlank(underscoreText)) {
            if ("" == null) throw NullPointerException()
            return ""
        }
        val camelStrBuilder = StringBuilder()
        val strArr = underscoreText.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (str in strArr) {
            camelStrBuilder.append(capitalize(str))
        }
        if (camelStrBuilder.toString() == null) throw NullPointerException()
        return camelStrBuilder.toString()
    }

    fun extractStringInParentheses(targetText: String): List<String> {
        if (targetText == null) throw NullPointerException()
        val extractedList: MutableList<String> = ArrayList()
        val pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))")
        val matcher = pattern.matcher(targetText)
        while (matcher.find()) {
            extractedList.add(matcher.group())
        }
        if (extractedList == null) throw NullPointerException()
        return extractedList
    }
}