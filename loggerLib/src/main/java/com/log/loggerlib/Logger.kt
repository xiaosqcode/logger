package com.log.loggerlib

/**
 * @author xiaosq
 * 版本：1.0
 * 创建日期：2020/11/18
 * 描述：
 *
 */
abstract class Logger {
    fun log(type: Int, tag: String?, msg: String) {
        var index = 0
        val length = msg.length
        val countOfSub = length / MAX_LENGTH
        if (countOfSub > 0) {
            for (i in 0 until countOfSub) {
                val sub = msg.substring(index, index + MAX_LENGTH)
                logType(type, tag, sub)
                index += MAX_LENGTH
            }
            logType(type, tag, msg.substring(index, length))
        } else {
            logType(type, tag, msg)
        }
    }

    abstract fun logType(type: Int, tag: String?, msg: String?)

    companion object {
        private const val MAX_LENGTH = 4000
    }
}