package com.abhishek.internships.identifier.skysnap

import android.app.ActivityManager
import android.app.Application
import android.util.Log
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat

class SkySnapApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("EmojiTestApp", "EmojiCompat initialized = ${EmojiCompat.isConfigured()}")


        if (isMainProcess()) {
            val config = BundledEmojiCompatConfig(this)
            EmojiCompat.init(config)
        }
    }

    private fun isMainProcess(): Boolean {
        val pid = android.os.Process.myPid()
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val processName = am.runningAppProcesses
            ?.firstOrNull { it.pid == pid }
            ?.processName

        return processName == packageName
    }
}
