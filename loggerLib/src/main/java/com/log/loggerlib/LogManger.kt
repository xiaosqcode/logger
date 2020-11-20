package com.log.loggerlib

import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

/**
 * @author xiaosq
 * 版本：1.0
 * 创建日期：2020/11/18
 * 描述：
 *
 */
object LogManger {

    private val TAG = this.javaClass.simpleName
    //每個文件固定大小，1M
    private const val LOG_FILE_MAX_SIZE = 1 * 1024 * 1024

    private var mLogFileDir: File? = null

    //内存中的日志文件大小监控时间间隔，10分钟
    private const val MEMORY_LOG_FILE_MONITOR_INTERVAL = 10 * 60 * 1000

    //sd卡中日志文件的最多保存天数
    private const val SDCARD_LOG_FILE_SAVE_DAYS = 7

    //日志文件在内存中的路径(日志文件在安装目录中的路径)
    private val LOG_PATH_MEMORY_DIR: String? = null

    //日志文件在sdcard中的路径【主要这个很重要】
    private val LOG_PATH_SDCARD_DIR: String? = null

    //当前的日志记录类型为存储在SD卡下面
    private const val SDCARD_TYPE = 0

    //当前的日志记录类型为存储在内存中
    private const val MEMORY_TYPE = 1

    //当前的日志记录类型，默认是SD卡
    private const val CURR_LOG_TYPE = SDCARD_TYPE

    //如果当前的日志写在内存中，记录当前的日志文件名称
    private val CURR_INSTALL_LOG_NAME: String? = null

    //本服务输出的日志文件名称，也可以是txt格式，或者后缀名是.log
    private const val logServiceLogName = "Log.log"

    @JvmOverloads
    fun init(showLog: Boolean, tag: String = LogCache.DEFAULT_TAG) {
        LogCache.init(showLog, tag)
    }

    @JvmOverloads
    fun init(
        showLog: Boolean,
        tag: String,
        logFileDir: File?,
        logFilePrefix: String? = LogCache.DEFAULT_FILE_PREFIX
    ) {
        mLogFileDir = logFileDir
        LogCache.init(showLog, tag, logFileDir, logFilePrefix)
    }


}