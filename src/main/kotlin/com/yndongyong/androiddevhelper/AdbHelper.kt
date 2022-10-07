package com.yndongyong.androiddevhelper

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.InputStreamReader

object AdbHelper {

    /**
     * Execute command in terminal
     * @param command String
     * @return String
     */
    fun execCMD(command: String): String {
        val sb = StringBuilder()
        try {
            val process = Runtime.getRuntime().exec(command)
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
        } catch (e: Exception) {
            return e.toString()
        }
        return sb.toString()
    }


}