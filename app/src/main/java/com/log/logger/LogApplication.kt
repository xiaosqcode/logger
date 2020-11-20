package com.log.logger

import android.app.Application
import com.log.logger.util.FileUtil
import com.log.loggerlib.LogCache
import com.log.loggerlib.LogManger
import java.io.File

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
        LogManger.init(true, "慧控", File(FileUtil.getLoggerPath(this)))
    }

}