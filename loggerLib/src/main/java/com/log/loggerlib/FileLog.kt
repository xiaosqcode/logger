package com.log.loggerlib

import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * @author xiaosq
 * 版本：1.0
 * 创建日期：2020/11/18
 * 描述：缓存日志
 *
 */
class FileLog {

    companion object{
        @Throws(IOException::class)
        fun printFile(
            logFile: File?,
            time: String,
            headString: String,
            msg: String) {
            var fileWriter: FileWriter? = null
            try {
                fileWriter = FileWriter(logFile, true)
                fileWriter.write("$time: $headString $msg\n")
                fileWriter.flush()
            } finally {
                fileWriter?.close()
            }
        }
    }

}