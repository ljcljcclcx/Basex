package com.clcx.basex.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by ljc123 on 2018/1/16.
 */

public class SizeClcx {
    /**
     * @param aty
     * @return 0-屏幕宽度，1-屏幕高度
     */
    public static int[] windowSize(Activity aty) {
        DisplayMetrics metrics = new DisplayMetrics();
        aty.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }
}
