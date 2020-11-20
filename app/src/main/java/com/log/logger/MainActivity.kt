package com.log.logger

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.log.logger.permission.PermissionHelper
import com.log.logger.util.FileUtil
import com.log.loggerlib.LogCache
import com.log.loggerlib.LogManger
import java.io.File
import java.util.*
import kotlin.Comparator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            var log = "\"jfieosijfosehfoehfeval fileList = arrayListOval fileList = arrayListOf<String>()\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&5\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&1\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&2\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&4\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&3\\\")\\n\" +\n" +
                    "                    \"            Log.i(\\\"jfioesouesptesiofs\\\",\\\"==11111===\\\"+fileList)\\n\" +\n" +
                    "                    \"            Collections.sort(fileList, object : Comparator<String> {\\n\" +\n" +
                    "                    \"                override fun compare(o1: String, o2: String): Int {\\n\" +\n" +
                    "                    \"                    Log.i(\\\"jifoesjfeofefdserefee\\\",\\\"====\\\"+o1+\\\"===\\\"+o2)\\n\" +\n" +
                    "                    \"                    return o1.compareTo(o2)\\n\" +\n" +
                    "                    \"                }\\n\" +\n" +
                    "                    \"\\n\" +\n" +
                    "                    \"            })val fileList = arrayListOf<String>()\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&5\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&1\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&2\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&4\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&3\\\")\\n\" +\n" +
                    "                    \"            Log.i(\\\"jfioesouesptesiofs\\\",\\\"==11111===\\\"+fileList)\\n\" +\n" +
                    "                    \"            Collections.sort(fileList, object : Comparator<String> {\\n\" +\n" +
                    "                    \"                override fun compare(o1: String, o2: String): Int {\\n\" +\n" +
                    "                    \"                    Log.i(\\\"jifoesjfeofefdserefee\\\",\\\"====\\\"+o1+\\\"===\\\"+o2)\\n\" +\n" +
                    "                    \"                    return o1.compareTo(o2)\\n\" +\n" +
                    "                    \"                }\\n\" +\n" +
                    "                    \"\\n\" +\n" +
                    "                    \"            })val fileList = arrayListOf<String>()\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&5\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&1\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&2\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&4\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&3\\\")\\n\" +\n" +
                    "                    \"            Log.i(\\\"jfioesouesptesiofs\\\",\\\"==11111===\\\"+fileList)\\n\" +\n" +
                    "                    \"            Collections.sort(fileList, object : Comparator<String> {\\n\" +\n" +
                    "                    \"                override fun compare(o1: String, o2: String): Int {\\n\" +\n" +
                    "                    \"                    Log.i(\\\"jifoesjfeofefdserefee\\\",\\\"====\\\"+o1+\\\"===\\\"+o2)\\n\" +\n" +
                    "                    \"                    return o1.compareTo(o2)\\n\" +\n" +
                    "                    \"                }\\n\" +\n" +
                    "                    \"\\n\" +\n" +
                    "                    \"            })f<String>()\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&5\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&1\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&2\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&4\\\")\\n\" +\n" +
                    "                    \"            fileList.add(\\\"LogCache&3\\\")\\n\" +\n" +
                    "                    \"            Log.i(\\\"jfioesouesptesiofs\\\",\\\"==11111===\\\"+fileList)\\n\" +\n" +
                    "                    \"            Collections.sort(fileList, object : Comparator<String> {\\n\" +\n" +
                    "                    \"                override fun compare(o1: String, o2: String): Int {\\n\" +\n" +
                    "                    \"                    Log.i(\\\"jifoesjfeofefdserefee\\\",\\\"====\\\"+o1+\\\"===\\\"+o2)\\n\" +\n" +
                    "                    \"                    return o1.compareTo(o2)\\n\" +\n" +
                    "                    \"                }\\n\" +\n" +
                    "                    \"\\n\" +\n" +
                    "                    \"            })\""
            var sp = StringBuffer()
            for (i in 0 until 100) {
                sp.append(log)
            }
            LogCache.i(sp)
        }
        findViewById<TextView>(R.id.tvfab).setOnClickListener { view ->
//            LogManger.checkLogSize()
        }
        PermissionHelper.isHasAudioPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //参数1：控制台是否显示log 参数2：tag标签 参数3：保存log日志的目录
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}