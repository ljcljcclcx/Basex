package com.clcx.basex.utils;

/**
 * Created by ljc123 on 2018/1/15.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 权限管理器，所有权限获取
 * 回调接口AuthorCallback是指，如果有该权限则进行某项操作，可以为null
 */
public class AuthorityGet {


    private static final int REQUSET_CODE_READ_EXTERNAL_STORAGE = 1001;
    private static final int REQUSET_CODE_WRITE_EXTERNAL_STORAGE = 1003;
    private static final int REQUSET_CODE_CAMERA = 1002;

    //检查SD卡读权限授予情况
    public static void SDcardRead(Activity c, AuthorCallback call) {

        if (ContextCompat.checkSelfPermission(c, Manifest.permission
                .READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (call != null) {
                call.callback();
            }
        } else {
            ActivityCompat.requestPermissions(c,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUSET_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    //检查SD卡写权限授予情况
    public static void SDcardWrite(Activity c, AuthorCallback call) {

        if (ContextCompat.checkSelfPermission(c, Manifest.permission
                .WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (call != null) {
                call.callback();
            }
        } else {
            ActivityCompat.requestPermissions(c,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUSET_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }

    //摄像头
    public static void camera(Activity c, AuthorCallback call) {
        if (ContextCompat.checkSelfPermission(c, Manifest.permission
                .CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            if (call != null) {
                call.callback();
            }
        } else {
            ActivityCompat.requestPermissions(c,
                    new String[]{Manifest.permission.CAMERA},
                    REQUSET_CODE_CAMERA);
        }
    }

    //麦克风
    public static void mic(Activity c, AuthorCallback call) {
        if (ContextCompat.checkSelfPermission(c, Manifest.permission
                .RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            if (call != null) {
                call.callback();
            }
        } else {
            ActivityCompat.requestPermissions(c,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUSET_CODE_CAMERA);
        }
    }


    public interface AuthorCallback {
        void callback();
    }


}
