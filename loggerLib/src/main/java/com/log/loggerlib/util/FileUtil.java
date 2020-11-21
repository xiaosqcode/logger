package com.log.loggerlib.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.File;

public class FileUtil {
    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
    private String string;
    private static String cur_path = null;

    /**
     * 这个路径被Android系统认定为应用程序的缓存路径，当程序被卸载的时候，这里的数据也会一起被清除掉
     *
     * @return 路径例如： /SD卡/Android/data/程序的包名/cache/uniqueName
     */
    public static String getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath();
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + uniqueName;
    }

    /**
     * 获取文件后缀名
     */
    public static String getsuffix(String str) {
        return str.substring(str.lastIndexOf("/") + 1);
    }

    /**
     * 删除文件夹
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) { // 文件
                file.delete();
            } else { // 文件夹
                String[] filePaths = file.list();
                for (String path : filePaths) {
                    deleteFile(filePath + File.separator + path);
                }
                file.delete();
            }
        }
    }

    /**
     * 删除文件夹
     */
    public static void deleteDirFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.isFile()) { // 文件夹
                String[] filePaths = file.list();
                for (String path : filePaths) {
                    deleteFile(filePath + File.separator + path);
                }
            }
        }
    }

    /**
     * 获取特定文件夹的文件
     */
    public static String getFileSearchPaths(Context context, File currentFilePath, String keyName) {
        if (currentFilePath == null) return null;
        File[] fList = currentFilePath.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (f.isDirectory()){
//                    if (f.getName().contains(keyName)) {
//                        break;
//                    }
                    cur_path = f.getAbsolutePath() + "/" + keyName;
                    if(FileUtil.isVersion7()){
                        cur_path = FileUtil.pathAndroid7(context, cur_path);
                    }
                    break;
                }
//                if (f.isDirectory()) { // 目录就递归遍历
//                    getFileSearchPaths(f, keyName);
//                }
            }
        }
        return cur_path;
    }

    private static String getSDPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); // 获取跟目录
            return sdDir.toString();
        }
        return null;
    }

    public static boolean isVersion7 (){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static String pathAndroid7(Context context, String path){
        String fileAbosolutePath;
        Uri contenuri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", new File(path));
        fileAbosolutePath = contenuri.getPath();
        return fileAbosolutePath;
    }

    /**
     * 获取存储log日志的文件目录
     * @return 路径
     */
    public static String getLoggerPath(Context context, String dir) {
        String path;

        if(FileUtil.isVersion7()){
            path = FileUtil.pathAndroid7(context, FileUtil.getDiskCacheDir(context, dir));
        }else {
            path = FileUtil.getDiskCacheDir(context, dir);
        }

        // 新建一个File，传入文件夹目录
        File file = new File(path);
        // 判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            // 通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            file.mkdirs();
        }
        return path;
    }

}