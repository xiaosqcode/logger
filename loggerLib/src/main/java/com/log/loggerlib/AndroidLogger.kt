package com.log.loggerlib

import android.util.Log

/**
 * @author xiaosq
 * 版本：1.0
 * 创建日期：2020/11/18
 * 描述：
 *
 */
class AndroidLogger : Logger() {
    companion object {
        var isAndroidLogAvailable = false

        init {
            var android = false
            try {
                android = Class.forName("android.util.Log") != null
            } catch (e: ClassNotFoundException) {
                //java environment
            }
            if (android) {
                //Test环境 有android的mock
                try {
                    android = Log.i(
                        LogCache.DEFAULT_TAG,
                        "LLogger in android environment"
                    ) > 0
                } catch (e: RuntimeException) {
                    android = false
                }
            }
            isAndroidLogAvailable = android
        }
    }

    override fun logType(type: Int, tag: String?, msg: String?) {
        when (type) {
            LogCache.V -> Log.v(tag, msg!!)
            LogCache.D -> Log.d(tag, msg!!)
            LogCache.I -> Log.i(tag, msg!!)
            LogCache.W -> Log.w(tag, msg!!)
            LogCache.E -> Log.e(tag, msg!!)
            LogCache.A -> Log.wtf(tag, msg)
        }
    }
}