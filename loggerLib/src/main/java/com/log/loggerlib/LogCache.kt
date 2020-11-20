package com.log.loggerlib

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator

/**
 * @author xiaosq
 * 版本：1.0
 * 创建日期：2020/11/18
 * 描述：日志缓存
 *
 */
object LogCache {
    val LINE_SEPARATOR = System.getProperty("line.separator")
    private const val DEFAULT_MESSAGE = "execute"
    private const val ARGUMENTS = "argument"
    private const val NULL = "null"
    private const val SUFFIX_JAVA = ".java"
    const val DEFAULT_TAG = "LogCache"
    private const val TRACE_CLASS_END = "at com.log.loggerlib.LogCache"
    const val DEFAULT_FILE_PREFIX = "LogCache"
    private const val FILE_FORMAT = ".log"
    private var mShowLog = true
    const val V = 0x1
    const val D = 0x2
    const val I = 0x3
    const val W = 0x4
    const val E = 0x5
    const val A = 0x6
    private var STACK_TRACE_INDEX_WRAP = 4 //线程的栈层级
    private const val JSON_INDENT = 4
    private var TAG = DEFAULT_TAG
    private var mLogFileDir: File? = null
    private var mCurLogFile: File? = null
    private var mSaveLog = false
    private var FILE_PREFIX: String? = null
    private var logger: Logger? = null

    var fileMaxSize = 1//默认一个文件最大大小 MB
    var maxSaveCount = 20//默认最大创建文件数目
    var clearBeforeDate = 2//重新打开app默认清除前2天记录

    @JvmOverloads
    fun init(showLog: Boolean, tag: String = DEFAULT_TAG) {
        mCurLogFile = null
        mShowLog = showLog
        TAG = tag

        initFile()
    }

    @JvmOverloads
    fun init(
        showLog: Boolean,
        tag: String,
        logFileDir: File?,
        logFilePrefix: String? = DEFAULT_FILE_PREFIX
    ) {
        mCurLogFile = null
        mShowLog = showLog
        TAG = tag
        mSaveLog = logFileDir != null
        mLogFileDir = logFileDir
        FILE_PREFIX = logFilePrefix

        initFile()
    }

    /**
     * 处理文件
     */
    private fun initFile() {
        if (clearBeforeDate > 0) {

        }
    }

    fun v() {
        printLog(V, DEFAULT_MESSAGE)
    }

    fun v(msg: Any?) {
        printLog(V, msg!!)
    }

    fun v(vararg objects: Any?) {
        printLog(V, objects as Array<out Any>)
    }

    fun d() {
        printLog(D, DEFAULT_MESSAGE)
    }

    fun d(msg: Any?) {
        printLog(D, msg!!)
    }

    fun d(vararg objects: Any?) {
        printLog(D, objects)
    }

    fun i() {
        printLog(I, DEFAULT_MESSAGE)
    }

    fun i(msg: Any?) {
        printLog(I, msg!!)
    }

    fun i(vararg objects: Any?) {
        printLog(I, objects)
    }

    fun w() {
        printLog(W, DEFAULT_MESSAGE)
    }

    fun w(msg: Any?) {
        printLog(W, msg!!)
    }

    fun w(vararg objects: Any?) {
        printLog(W, objects)
    }

    fun e() {
        printLog(E, DEFAULT_MESSAGE)
    }

    fun e(msg: Any?) {
        printLog(E, msg!!)
    }

    fun e(vararg objects: Any?) {
        printLog(E, objects)
    }

    fun a() {
        printLog(A, DEFAULT_MESSAGE)
    }

    fun a(msg: Any?) {
        printLog(A, msg!!)
    }

    fun a(vararg objects: Any?) {
        printLog(A, objects)
    }

    fun trace() {
        printStackTrace()
    }

    fun json(jsonObject: JSONObject) {
        printJson(jsonObject)
    }

    fun json(jsonArray: JSONArray) {
        printJson(jsonArray)
    }

    fun json(jsonFormat: String) {
        printJson(jsonFormat)
    }

    fun initCurLogFile() {
        Log.i("jifojesotuysodsfsdd","=11111==="+(mLogFileDir != null && mLogFileDir!!.isDirectory))
        if (mLogFileDir != null && mLogFileDir!!.isDirectory) {
            if (mCurLogFile != null) {
                if (mCurLogFile!!.exists()) {
                    val fl = mCurLogFile!!.length() / 1048576f
                    Log.i("jifojesotuysodsfsdd","=22222==="+fl)
                    if (fl < fileMaxSize) {
                        return
                    } else {
                        val timeMillis = System.currentTimeMillis()
                        mCurLogFile = makeLogFileWithTime(mLogFileDir, timeMillis)
                    }
                }
            }
            val allFiles: Array<File> = mLogFileDir!!.listFiles()!!
            if (allFiles.isNullOrEmpty()) {
                val timeMillis = System.currentTimeMillis()
                mCurLogFile = makeLogFileWithTime(mLogFileDir, timeMillis)
            } else {
                val fileList = arrayListOf<File>()
                for (logFile in allFiles) {
                    val fileName = logFile.name
                    if (fileName.startsWith(DEFAULT_FILE_PREFIX) && logFile.isFile) {
                        fileList.add(logFile)
                    }
                }

                if (fileList.size > 0) {
                    //根据文件时间戳正排序
                    fileList.sortWith(Comparator { o1, o2 ->
                        val fileFlag1 = o1!!.name.split("&")[1].toLong()
                        val fileFlag2 = o2!!.name.split("&")[1].toLong()
                        when {
                            fileFlag1 - fileFlag2 < 0 ->
                                -1
                            fileFlag1 - fileFlag1 > 0 ->
                                1
                            else ->
                                0
                        }
                    })
                    mCurLogFile = fileList.last()

                    if (fileList.size <= maxSaveCount) {
                        return
                    }
                    if (fileList.size == maxSaveCount.plus(1)) {
                        fileList.first().delete()
                    }
                } else {
                    val timeMillis = System.currentTimeMillis()
                    mCurLogFile = makeLogFileWithTime(mLogFileDir, timeMillis)
                }
                Log.i("jifojesotuysodsfsdd","=33333==="+fileList.size)
            }
        }
    }

    private fun printJson(`object`: Any) {
        if (!mShowLog) {
            return
        }
        val headString = wrapperContent(STACK_TRACE_INDEX_WRAP)
        var message: String? = null
        try {
            message = if (`object` is JSONObject) {
                `object`.toString(JSON_INDENT)
            } else if (`object` is JSONArray) {
                `object`.toString(JSON_INDENT)
            } else {
                `object`.toString()
            }
        } catch (e: JSONException) {
            logger!!.log(E, TAG, getStackTraceString(e))
        }
        if (message != null) {
            message = headString + LINE_SEPARATOR + message
            logger!!.log(D, TAG, message)
        }
    }

    @Synchronized
    private fun printLog(type: Int, vararg objects: Any) {
        if (!mShowLog) {
            return
        }
        val headString = wrapperContent(STACK_TRACE_INDEX_WRAP)
        val msg = objects.let { getObjectsString(it as Array<Any>) }
        if (mSaveLog) {
            printFile(headString, msg)
        }
        logger!!.log(type, TAG, headString + msg)
    }

    private fun printFile(headString: String, msg: String) {
        //赋值mCurLogFile，获取当前写的文件
        initCurLogFile()

        val timeMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.CHINA)
        val timeFormat = dateFormat.format(timeMillis)
//        var logFile: File? = makeLogFileWithTime(mLogFileDir, timeMillis, count)
        try {
            FileLog.printFile(mCurLogFile, timeFormat, headString, msg)
        } catch (e: IOException) {
            logger!!.log(E, TAG, "log printFile failed :" + LINE_SEPARATOR + getStackTraceString(e))
        }
    }

    private fun makeLogFileWithTime(LogFileDir: File?, count: Long): File {
        val file = File(LogFileDir, "$FILE_PREFIX&$count&$FILE_FORMAT")
        if (!file.exists()) {
            try {
                file.createNewFile()
                logger!!.log(I, TAG, "create log file local:" + file.absolutePath)
                return file
            } catch (e: IOException) {
                e.printStackTrace()
                logger!!.log(E, TAG, "log create file failed :$LINE_SEPARATOR" + getStackTraceString(e))
            }
        }
        return file
    }

    /**
     * 检查日志文件大小是否超过了规定大小
     */
    public fun checkLogSize() {
//        Log.i("jiofjiesjfesjitsei","===="+ LogManger.mLogFileDir!!.lastModified()+"====="+ LogManger.mLogFileDir!!.length())
//        if (LogManger.mLogFileDir == null || !LogManger.mLogFileDir!!.exists()) {
//            return
//        }
//        Log.d(LogManger.TAG, "checkLog() ==> The size of the log is too big?")
//        //当文件长度>=10M时，重新创建一个新的文件夹
//        if (LogManger.mLogFileDir!!.length() >= LogManger.LOG_FILE_MAX_SIZE) {
//            Log.d(LogManger.TAG, "The log's size is too big!")
//        }
    }

    fun getFileLastModifiedTime(file: File): String? {
        val cal: Calendar = Calendar.getInstance()
        val time = file.lastModified()
        val formatter = SimpleDateFormat("mformatType")
        cal.setTimeInMillis(time)

        // 输出：修改时间[2] 2009-08-17 10:32:38
        return formatter.format(cal.getTime())
    }

    fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) {
            return ""
        }
        var t = tr
        while (t != null) {
            if (t is UnknownHostException) {
                return ""
            }
            t = t.cause
        }
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    private fun printStackTrace() {
        if (!mShowLog) {
            return
        }
        val throwable = Throwable()
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        throwable.printStackTrace(printWriter)
        printWriter.flush()
        val message = stringWriter.toString()
        val traces = message.split("\\n\\t".toRegex()).toTypedArray()
        val builder = StringBuilder()
        builder.append("\n")
        for (trace in traces) {
            if (trace.contains(TRACE_CLASS_END)) {
                continue
            }
            builder.append(trace).append("\n")
        }
        val msg = builder.toString()
        val headString = wrapperContent(STACK_TRACE_INDEX_WRAP)
        logger!!.log(D, TAG, headString + msg)
    }

    private fun wrapperContent(stackTraceIndex: Int): String {
        val stackTraceElements =
            Thread.currentThread().stackTrace
        val targetElement = stackTraceElements[stackTraceIndex]
        val classFileName = targetElement.fileName
        var className = targetElement.className
        val classNameInfo =
            className.split("\\.".toRegex()).toTypedArray()
        if (classNameInfo.isNotEmpty()) {
            className = classNameInfo[classNameInfo.size - 1] + SUFFIX_JAVA
        }
        var innerClassName: String? = null
        if (classFileName != className && className.contains("$")) {
            //内部类
            val index = className.indexOf("$")
            innerClassName = className.substring(index)
        }
        val methodName = targetElement.methodName
        var lineNumber = targetElement.lineNumber
        if (lineNumber < 0) {
            lineNumber = 0
        }
        return ("[ ("
                + classFileName
                + ':'
                + lineNumber
                + ')'
                + (if (innerClassName == null) "#" else "$innerClassName#")
                + methodName
                + " ] ")
    }

    private fun getObjectsString(objects: Array<Any>): String {
        return if (objects.size > 1) {
            val builder = StringBuilder()
            builder.append("\n")
            var i = 0
            val length = objects.size
            while (i < length) {
                val `object` = objects[i]
                builder.append("\t").append(ARGUMENTS).append("[").append(i).append("]")
                    .append("=")
                if (`object` == null) {
                    builder.append(NULL)
                } else {
                    builder.append(`object`.toString())
                }
                if (i != length - 1) {
                    builder.append("\n")
                }
                i++
            }
            builder.toString()
        } else {
            val `object` = objects[0]
            `object`.toString()
        }
    }

    init {
        logger = AndroidLogger()
        STACK_TRACE_INDEX_WRAP++
    }
}