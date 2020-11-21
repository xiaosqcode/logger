package com.log.logger

import android.app.Application
import com.log.loggerlib.LogCache

/**
 * @author xiaosq
 * 版本：1.0
 * 创建日期：2020/11/18
 * 描述：
 *
 */
class LogApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        LogCache.init(showLog = true, saveLog = true, tag = "慧控", context = this)
    }

}