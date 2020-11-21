package com.log.loggerlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
//    private static CrashHandle myCrashHandler;
    private Context context;
//    private boolean isShowDialog;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//    private CommonDialog dialogBaiban;
//    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //1.私有化构造方法
    private CrashHandler() {
    }

    private static class Singleton {
        //静态初始化器，由JVM来保证线程安全
        private static CrashHandler instance = new CrashHandler();
    }

    public static CrashHandler getInstance() {
        return Singleton.instance;
    }

    public void init(Context context) {
        this.context = context;
//        this.isShowDialog = isShowDialog;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
            // 1.获取当前程序的版本号. 版本的id
            String versioninfo = getVersionInfo();

            // 2.获取手机的硬件信息.
            String mobileInfo = getMobileInfo();

            // 3.把错误的堆栈信息 获取出来
            String errorinfo = getErrorInfo(e);
            try {
//                long timestamp = System.currentTimeMillis();
//                String time = dataFormat.format(new Date());
//                String fileName = "crash" + time + "-" + timestamp + ".txt";
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    FileOutputStream fos = new FileOutputStream(FileUtil.getDiskCacheDir(context, fileName));
//                    Log.e("--异常文件创建--", FileUtil.getDiskCacheDir(context, fileName));
//                    fos.write((versioninfo + mobileInfo + errorinfo).getBytes());
//                    fos.close();
//                }
                LogCache.INSTANCE.e(errorinfo, LogCache.LOG_MSG_STATISTICS_DIR, LogCache.LOG_KERNEL_DIR);
            } catch (Exception ex) {
                Log.e("pds", "...", ex);
            }
            System.exit(0);
    }

    /**
     * 获取错误的信息
     */
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        return writer.toString();
    }

    /**
     * 获取手机的硬件信息
     */
    private String getMobileInfo() {
        StringBuilder sb = new StringBuilder();
        //通过反射获取系统的硬件信息
        try {

            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                //暴力反射 ,获取私有的信息
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name).append("=").append(value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取手机的版本信息
     */
    private String getVersionInfo() {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }
}
