package com.evil.webbrowser.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 后台截屏工具类
 */
public class ScreentShotUtil {
    private static final String CLASS1_NAME = "android.view.SurfaceControl";

    private static final String CLASS2_NAME = "android.view.Surface";

    private static final String METHOD_NAME = "screenshot";

    private static ScreentShotUtil instance;

    private Display mDisplay;

    private DisplayMetrics mDisplayMetrics;

    private Matrix mDisplayMatrix;

    private WindowManager wm;

    private SimpleDateFormat format;

    private ScreentShotUtil() {

    }

    public static ScreentShotUtil getInstance() {
        synchronized (ScreentShotUtil.class) {
            if (instance == null) {
                instance = new ScreentShotUtil();
            }
        }
        return instance;
    }

    private Bitmap screenShot(int width, int height) {
        Class<?> surfaceClass;
        Method   method;
        try {
            if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.JELLY_BEAN_MR2) {

                surfaceClass = Class.forName(CLASS1_NAME);
            } else {
                surfaceClass = Class.forName(CLASS2_NAME);
            }
            method = surfaceClass.getDeclaredMethod(METHOD_NAME, int.class, int.class);
            method.setAccessible(true);
            return (Bitmap) method.invoke(null, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ScreenRunnable implements Runnable {
        private String  filePath;
        private Context mContext;

        public ScreenRunnable(String filePath, Context context) {
            this.filePath = filePath;
            mContext = context;
        }

        @Override
        public void run() {
            if (filePath == "") {
                format = new SimpleDateFormat("yyyyMMddHHmmss");
                String fileName =
                        format.format(new Date(System.currentTimeMillis())) +
                        ".png";
                if (SDCardUtils.sdCardExist()) {
                    filePath = new File(SDCardUtils.getSdRoot(), fileName).getAbsolutePath();
                }
            }

            if (ShellUtils.checkRootPermission()) {
                if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    //使用shell命令截屏
                    ShellUtils.execCommand(
                            "/system/bin/screencap -p " + filePath, true);
                }
            } else {
                if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    mDisplay = wm.getDefaultDisplay();
                    mDisplayMatrix = new Matrix();
                    mDisplayMetrics = new DisplayMetrics();

                    mDisplay.getRealMetrics(mDisplayMetrics);

                    float[] dims = {mDisplayMetrics.widthPixels,
                                    mDisplayMetrics.heightPixels};
                    float   degrees          = getDegreesForRotation(mDisplay.getRotation());
                    boolean requiresRotation = (degrees > 0);
                    if (requiresRotation) {
                        mDisplayMatrix.reset();
                        mDisplayMatrix.preRotate(-degrees);
                        mDisplayMatrix.mapPoints(dims);
                        dims[0] = Math.abs(dims[0]);
                        dims[1] = Math.abs(dims[1]);
                    }

                    Bitmap mScreenBitmap = screenShot((int) dims[0], (int) dims[1]);
                    if (requiresRotation) {
                        Bitmap ss = Bitmap.createBitmap(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, Bitmap.Config.ARGB_8888);
                        Canvas c  = new Canvas(ss);
                        c.translate(ss.getWidth() / 2, ss.getHeight() / 2);
                        c.rotate(degrees);
                        c.translate(-dims[0] / 2, -dims[1] / 2);
                        c.drawBitmap(mScreenBitmap, 0, 0, null);
                        c.setBitmap(null);
                        mScreenBitmap = ss;
                        if (ss != null && !ss.isRecycled()) {
                            ss.recycle();
                        }
                    }

                    if (mScreenBitmap == null) {
                        Log.e("screen", "截屏失败");
                    }
                    mScreenBitmap.setHasAlpha(false);
                    mScreenBitmap.prepareToDraw();

                    saveBitmap2File(mScreenBitmap, filePath);
                }
            }
        }
    }

    /**
     * Takes a screenshot of the current display and shows an animation.
     */
    @SuppressLint("NewApi")
    public void takeScreenshot(Context context, String fileFullPath) {
        new Thread(new ScreenRunnable(fileFullPath, context)).start();
    }

    /**
     * 保存文件
     */
    public void saveBitmap2File(Bitmap bm, String filePath) {
        BufferedOutputStream bos = null;

        try {
            File myCaptureFile = new File(filePath);
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
        } catch (Exception e) {
            TestUtils.log(e.getMessage());
        } finally {
            FileUtils.close(bos);
            if (!bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    /**
     * @return the current display rotation in degrees
     */
    private float getDegreesForRotation(int value) {
        switch (value) {
            case Surface.ROTATION_90:
                return 360f - 90f;
            case Surface.ROTATION_180:
                return 360f - 180f;
            case Surface.ROTATION_270:
                return 360f - 270f;
        }
        return 0f;
    }
}
