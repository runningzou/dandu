package com.runningzou.dandu.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.runningzou.dandu.app.DanduApp;


public class DimenUtils {
    public static int dp2px(float dipValue) {
        final float scale = DanduApp.getApplication().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = DanduApp.getApplication().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getWindowWidth()
    {
        return getWindowManager(DanduApp.getApplication()).getDefaultDisplay().getWidth();
    }
    public static WindowManager getWindowManager(Context paramContext)
    {
        return (WindowManager)paramContext.getSystemService(Context.WINDOW_SERVICE);
    }
}