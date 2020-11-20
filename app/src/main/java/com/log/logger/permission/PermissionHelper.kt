package com.log.logger.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * 检查权限的工具类
 */
object PermissionHelper {

    private val REQUEST_PERMISSION = 4 //权限请求
    private val PERMISSIONS_RECORD_AUDIO = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.MODIFY_AUDIO_SETTINGS
    )

    fun isHasAudioPermissions(activity: Activity, array: Array<String>): Boolean {
        // 检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (lacksPermissions(activity, *array)) {
                PermissionsActivity.startActivityForResult(activity, REQUEST_PERMISSION, *array)
                return false
            }
        }
        return true
    }

    // 判断权限集合
    fun lacksPermissions(activity: Activity, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (lacksPermission(activity, permission)) {
                return true
            }
        }
        return false
    }

    // 判断是否缺少权限
    private fun lacksPermission(activity: Activity, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) ==
                PackageManager.PERMISSION_DENIED
    }

}
