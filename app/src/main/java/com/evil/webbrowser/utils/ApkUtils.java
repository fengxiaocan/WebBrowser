package com.evil.webbrowser.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/**
 *  @项目名： MyComUtils
 *  @包名： com.fxc.lib.utils
 *  @创建者: Noah.冯
 *  @时间: 18:56
 *  @描述： 实现静默安装与卸载的工具 需要权限<uses-permission android:name="android.permission.INSTALL_PACKAGES" />
 */
public class ApkUtils {
    //    public static void installAndStartApk(final Context context, final String apkPath) {
    //        if ((apkPath == null) || (context == null)) {
    //            return;
    //        }
    //
    //        File file = new File(apkPath);
    //        if (file.exists() == false) {
    //            return;
    //        }
    //
    //        new Thread() {
    //            public void run() {
    //                String packageName = getUninstallApkPackageName(context, apkPath);
    //                if (silentInstall(apkPath)) {
    //                    List<ResolveInfo> matches = findActivitiesForPackage(context, packageName);
    //                    if ((matches != null) && (matches.size() > 0)) {
    //                        ResolveInfo  resolveInfo  = matches.get(0);
    //                        ActivityInfo activityInfo = resolveInfo.activityInfo;
    //                        startApk(activityInfo.packageName, activityInfo.name);
    //                    }
    //                }
    //            }
    //
    //            ;
    //        }.start();
    //
    //    }
    //
    //    public static String getUninstallApkPackageName(Context context, String apkPath) {
    //        String packageName = null;
    //        if (apkPath == null) {
    //            return packageName;
    //        }
    //
    //        PackageManager pm = context.getPackageManager();
    //        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
    //        if (info == null) {
    //            return packageName;
    //        }
    //
    //        packageName = info.packageName;
    //        return packageName;
    //    }
    //
    //    public static List<ResolveInfo> findActivitiesForPackage(Context context, String packageName) {
    //        final PackageManager pm = context.getPackageManager();
    //
    //        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
    //        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    //        mainIntent.setPackage(packageName);
    //
    //        final List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);
    //        return apps != null
    //               ? apps
    //               : new ArrayList<ResolveInfo>();
    //    }
    //
    //    public static boolean silentInstall(String apkPath) {
    //        String cmd1 = "chmod 777 " + apkPath + " \n";
    //        String cmd2 = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r " + apkPath + " \n";
    //        return execWithSID(cmd1, cmd2);
    //    }
    //
    //    private static boolean execWithSID(String... args) {
    //        boolean      isSuccess = false;
    //        Process      process   = null;
    //        OutputStream out       = null;
    //        try {
    //            process = Runtime.getRuntime()
    //                             .exec("su");
    //            out = process.getOutputStream();
    //            DataOutputStream dataOutputStream = new DataOutputStream(out);
    //
    //            for (String tmp : args) {
    //                dataOutputStream.writeBytes(tmp);
    //            }
    //
    //            dataOutputStream.flush(); // 提交命令
    //            dataOutputStream.close(); // 关闭流操作
    //            out.close();
    //
    //            isSuccess = waitForProcess(process);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //
    //        return isSuccess;
    //    }
    //
    //    public static boolean startApk(String packageName, String activityName) {
    //        boolean isSuccess = false;
    //
    //        String cmd = "am start -n " + packageName + "/" + activityName + " \n";
    //        try {
    //            Process process = Runtime.getRuntime()
    //                                     .exec(cmd);
    //
    //            isSuccess = waitForProcess(process);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        return isSuccess;
    //    }
    //
    //    private static boolean waitForProcess(Process p) {
    //        boolean isSuccess = false;
    //        int     returnCode;
    //        try {
    //            returnCode = p.waitFor();
    //            switch (returnCode) {
    //                case 0:
    //                    isSuccess = true;
    //                    break;
    //
    //                case 1:
    //                    break;
    //
    //                default:
    //                    break;
    //            }
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        }
    //
    //        return isSuccess;
    //    }

    public static boolean install(String apkPath, Context context) {
        // 先判断手机是否有root权限
        if (hasRootPerssion()) {
            // 有root权限，利用静默安装实现
            return clientInstall(apkPath);
        } else {
            // 没有root权限，利用意图进行安装
            File file = new File(apkPath);
            if (!file.exists()) { return false; }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        }
    }

    /**
     * 描述: 卸载
     */
    public static boolean uninstall(String packageName, Context context) {
        if (hasRootPerssion()) {
            // 有root权限，利用静默卸载实现
            return clientUninstall(packageName);
        } else {
            Uri    packageURI      = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
            return true;
        }
    }

    /**
     * 判断手机是否有root权限
     */
    private static boolean hasRootPerssion() {
        PrintWriter PrintWriter = null;
        Process     process     = null;
        try {
            process = Runtime.getRuntime()
                             .exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 静默安装
     */
    private static boolean clientInstall(String apkPath) {
        PrintWriter PrintWriter = null;
        Process     process     = null;
        try {
            process = Runtime.getRuntime()
                             .exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 " + apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r " + apkPath);
            //          PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 静默卸载
     */
    private static boolean clientUninstall(String packageName) {
        PrintWriter PrintWriter = null;
        Process     process     = null;
        try {
            process = Runtime.getRuntime()
                             .exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
            PrintWriter.println("pm uninstall " + packageName);
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 启动app
     */
    public static boolean startApp(String packageName, String activityName) {
        boolean isSuccess = false;
        String  cmd       = "am start -n " + packageName + "/" + activityName + " \n";
        Process process   = null;
        try {
            process = Runtime.getRuntime()
                             .exec(cmd);
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return isSuccess;
    }


    private static boolean returnResult(int value) {
        // 代表成功
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }


    /**
     * 获取已安装apk的PackageInfo
     *
     * @param context
     * @param packageName
     * @return
     */
    public static PackageInfo getInstalledApkPackageInfo(Context context, String packageName) {
        PackageManager        pm   = context.getPackageManager();
        List<PackageInfo>     apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> it   = apps.iterator();
        while (it.hasNext()) {
            PackageInfo packageinfo = it.next();
            String      thisName    = packageinfo.packageName;
            if (thisName.equals(packageName)) {
                return packageinfo;
            }
        }
        return null;
    }

    /**
     * 判断apk是否已安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager pm        = context.getPackageManager();
        boolean        installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return installed;
    }

    /**
     * 获取已安装Apk文件的源Apk文件
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getSourceApkPath(Context context, String packageName) {
        if (StringUtils.isEmpty(packageName)) { return null; }
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                                             .getApplicationInfo(packageName, 0);
            return appInfo.sourceDir;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
