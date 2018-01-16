package com.clcx.basex.utils;

/**
 * Created by ljc123 on 2018/1/15.
 */

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件处理，主要是SD卡之类的处理
 */
public class FileHandle {

    /**
     * 当前日期的时间戳，默认格式
     * format=yyMMddHHmmss
     *
     * @return
     */
    public static String timeStamp(String format) {
        if (format == null || format.equals("")) {
            format = "yyMMddHHmmss";
        }
        return new SimpleDateFormat(format, Locale
                .getDefault()).format(new Date());
    }

    /**
     * SD卡cacheFileName文件夹路径下的名叫fileName的文件路径
     *
     * @param cacheFileName
     * @param fileName
     * @return
     */
    public static String getSDFilepath(String cacheFileName, String fileName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(),
                    cacheFileName);

            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }

            return file.getPath() + File.separator + fileName;
        } else {
            return null;
        }
    }
}
