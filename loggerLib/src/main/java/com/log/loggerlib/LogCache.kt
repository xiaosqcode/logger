package com.log.loggerlib

import android.content.Context
import android.util.Log
import com.log.loggerlib.util.FileUtil
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.ref.WeakReference
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
    private var mWeakReference: WeakReference<Context>? = null
    private const val DEFAULT_MESSAGE = "execute"
    private const val ARGUMENTS = "argument"
    private const val NULL = "null"
    private const val SUFFIX_JAVA = ".java"
    const val DEFAULT_TAG = "LogCache"
    private const val TRACE_CLASS_END = "at com.log.loggerlib.LogCache"
    const val DEFAULT_FILE_PREFIX = "LogCache"
    //保存日志的主目录
    private const val LOG_FILE_SAVE_DIR = "log/"
    //设备信息、系统版本、app版本日志目录
    const val LOG_DEVICE_DIR = LOG_FILE_SAVE_DIR + "deviceLog"
    //核心功能Debug信息（操作记录、界面记录）、崩溃信息
    const val LOG_KERNEL_DIR = LOG_FILE_SAVE_DIR + "kernelLog/"
    //网络强度日志、请求日志
    const val LOG_NET_DIR = LOG_FILE_SAVE_DIR + "netLog"
    //崩溃信息、使用行为统计
    const val LOG_MSG_STATISTICS_DIR = LOG_FILE_SAVE_DIR + "msgStatisticsLog"
    //日志格式
    private const val FILE_FORMAT = ".log"
    //是否开启log日志的收集
    private var mShowLog = true
    const val V = 0x1
    const val D = 0x2
    const val I = 0x3
    const val W = 0x4
    const val E = 0x5
    const val A = 0x6
    //线程的栈层级
    private var STACK_TRACE_INDEX_WRAP = 4
    private const val JSON_INDENT = 4
    private var TAG = DEFAULT_TAG
    //当前写的目录
    private var mLogFileDir: File? = null
    //当前写的日志文件
    private var mCurLogFile: File? = null
    //是否缓存日志
    private var mSaveLog = false
    //日志后缀
    private var FILE_PREFIX: String? = null
    private var logger: Logger? = null
    //正在写的文件名后戳
    private const val WRITE_FILE_NAME = "-1605920500573"

    var fileMaxSize = 1//默认一个文件最大大小 MB
    var maxSaveCount = 20//默认最大创建文件数目
    var clearBeforeDate = 2//重新打开app默认清除前2天记录

    init {
        logger = AndroidLogger()
        STACK_TRACE_INDEX_WRAP++
    }

    @JvmOverloads
    fun init(showLog: Boolean, tag: String = DEFAULT_TAG) {
        mCurLogFile = null
        mShowLog = showLog
        TAG = tag
    }

    @JvmOverloads
    fun init(
        showLog: Boolean,
        saveLog: Boolean,
        tag: String,
        context: Context?,
        logFilePrefix: String? = DEFAULT_FILE_PREFIX
    ) {
        mCurLogFile = null
        mShowLog = showLog
        TAG = tag
        mSaveLog = saveLog
        FILE_PREFIX = logFilePrefix

        mWeakReference = WeakReference<Context>(context)

        initFile()
    }

    /**
     * 初始化文件
     */
    private fun initFile() {
        try {//开启app默认转到核心文件目录
            mLogFileDir = File(FileUtil.getLoggerPath(mWeakReference!!.get(), LOG_KERNEL_DIR))
            if (clearBeforeDate > 0) {

            }
        } catch (e: Exception) {
            logger!!.log(E, TAG, "log printFile failed :" + LINE_SEPARATOR + getStackTraceString(e))
        }
    }

    fun v() {
        printLog(V, DEFAULT_MESSAGE)
    }

    fun v(msg: Any?, type: String) {
        initType(type)
        printLog(V, msg!!)
    }

    fun v(vararg objects: Any?) {
        printLog(V, objects as Array<out Any>)
    }

    fun d() {
        printLog(D, DEFAULT_MESSAGE)
    }

    fun d(msg: Any?, type: String) {
        initType(type)
        printLog(D, msg!!)
    }

    fun d(vararg objects: Any?) {
        printLog(D, objects)
    }

    fun i() {
        printLog(I, DEFAULT_MESSAGE)
    }

    fun i(msg: Any?, type: String) {
        initType(type)
        printLog(I, msg!!)
    }

    fun i(vararg objects: Any?) {
        printLog(I, objects)
    }

    fun w() {
        printLog(W, DEFAULT_MESSAGE)
    }

    fun w(msg: Any?, type: String) {
        initType(type)
        printLog(W, msg!!)
    }

    fun w(vararg objects: Any?) {
        printLog(W, objects)
    }

    fun e() {
        printLog(E, DEFAULT_MESSAGE)
    }

    fun e(msg: Any?, vararg type: String) {
        for (i in type) {
            initType(i)
            printLog(E, msg!!)
        }
    }

    fun e(vararg objects: Any?) {
        printLog(E, objects)
    }

    fun a() {
        printLog(A, DEFAULT_MESSAGE)
    }

    fun a(msg: Any?, type: String) {
        initType(type)
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

    private fun initType(type: String) {
        if (mLogFileDir != null) {
            mLogFileDir = File(FileUtil.getLoggerPath(mWeakReference!!.get(), type))
            Log.i("jfioesjtuespiojfgefegh","====="+mLogFileDir!!.path)
        }
    }

    private fun initCurLogFile() {
        Log.i("jifojesotuysodsfsdd","=11111==="+(mLogFileDir != null && mLogFileDir!!.isDirectory)+"==="+System.currentTimeMillis())
        if (mLogFileDir != null && mLogFileDir!!.isDirectory) {
            //当前写的文件名
            val writeName = "$FILE_PREFIX&$WRITE_FILE_NAME&$FILE_FORMAT"
            val curLogFile = File(mLogFileDir, writeName)

            mCurLogFile = if (curLogFile.exists()) {
                val fileLen = curLogFile.length() / 1048576f
                Log.i("jifojesotuysodsfsdd","=22222==="+fileLen+"==="+mLogFileDir)
                if (fileLen < fileMaxSize) {
                    mCurLogFile = curLogFile
                    return
                } else {
                    createFile()
                }
            } else {
                createFile()
            }
            val allFiles: Array<File> = mLogFileDir!!.listFiles()!!
            if (allFiles.isNullOrEmpty()) {
                mCurLogFile = createFile()
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
                    mCurLogFile = fileList.first()

                    if (fileList.size <= maxSaveCount) {
                        return
                    }
                    if (fileList.size == maxSaveCount.plus(1)) {
                        fileList[1].delete()
                    }
                } else {
                    mCurLogFile = createFile()
                }
                Log.i("jifojesotuysodsfsdd","=33333==="+fileList.size)
            }
        }
    }

    /**
     * 创建文件
     */
    private fun createFile(): File {
        //时间戳
        val timeMillis = System.currentTimeMillis()
        //当前写的文件名
        val writeName = "$FILE_PREFIX&$WRITE_FILE_NAME&$FILE_FORMAT"
        var writeFile = File(mLogFileDir, writeName)

        if (writeFile.exists()) {
            val isReName = writeFile.renameTo(File(mLogFileDir, "$FILE_PREFIX&$timeMillis&$FILE_FORMAT"))
            if (isReName) {
                writeFile = File(mLogFileDir, writeName)
                return writeFile
            }
        }

        return makeLogFileWithTime(mLogFileDir, WRITE_FILE_NAME.toLong())
    }

    /**
     * 打印json字符串
     */
    private fun printJson(`object`: Any) {
        if (!mShowLog) {
            return
        }
        val headString = wrapperContent(STACK_TRACE_INDEX_WRAP)
        var message: String? = null
        try {
            message = when (`object`) {
                is JSONObject -> {
                    `object`.toString(JSON_INDENT)
                }
                is JSONArray -> {
                    `object`.toString(JSON_INDENT)
                }
                else -> {
                    `object`.toString()
                }
            }
        } catch (e: JSONException) {
            logger!!.log(E, TAG, getStackTraceString(e))
        }
        if (message != null) {
            message = headString + LINE_SEPARATOR + message
            logger!!.log(D, TAG, message)
        }
    }

    /**
     * 开始日志收集
     */
    @Synchronized
    private fun printLog(type: Int, vararg objects: Any) {
        if (!mShowLog) {
            return
        }
        Log.i("jfioesjfesfefefefesfes","===="+ mLogFileDir!!.path)
        val headString = wrapperContent(STACK_TRACE_INDEX_WRAP)
        val msg = getObjectsString(objects as Array<Any>)
        if (mSaveLog) {
            printFile(headString, msg)
        }
        logger!!.log(type, TAG, headString + msg)
    }

    /**
     * 保存日志至本地
     */
    private fun printFile(headString: String, msg: String) {
        //赋值mCurLogFile，获取当前写的文件
        Log.i("jfioesjifoespotguesto","==111111=="+ mCurLogFile?.path)
        initCurLogFile()
        Log.i("jfioesjifoespotguesto","==222222=="+ mCurLogFile!!.path)

        val timeMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.CHINA)
        val timeFormat = dateFormat.format(timeMillis)
        try {
            FileLog.printFile(mCurLogFile, timeFormat, headString, msg)
        } catch (e: IOException) {
            logger!!.log(E, TAG, "log printFile failed :" + LINE_SEPARATOR + getStackTraceString(e))
        }
    }

    /**
     * 创建日志文件
     */
    private fun makeLogFileWithTime(LogFileDir: File?, timeMillis: Long): File {
        val file = File(LogFileDir, "$FILE_PREFIX&$timeMillis&$FILE_FORMAT")
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

    fun getFileLastModifiedTime(file: File): String? {
        val cal: Calendar = Calendar.getInstance()
        val time = file.lastModified()
        val formatter = SimpleDateFormat("mformatType")
        cal.timeInMillis = time

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

    /**
     * 获取栈的信息
     */
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
                builder.append(`object`.toString())
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
}