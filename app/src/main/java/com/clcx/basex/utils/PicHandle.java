package com.clcx.basex.utils;

/**
 * Created by ljc123 on 2018/1/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import java.io.File;

/**
 * 图片处理
 */
public class PicHandle {
    public static final int REQUEST_ALBUM_OK = 100;
    public static final int REQUEST_TAKEPHOTO_OK = 102;
    public static final int REQUEST_RECORD_VIDEO_OK = 101;
    public static final int REQUEST_SELECT_VIDEO_OK = 103;

    /**
     * 拍照逻辑：传入缓存路径，拍照之后会把文件存入该路径，并返回路径
     *
     * @param cachePath
     */
    public static String takePhoto(Activity c, String cachePath) {
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cachePath == null || "".equals(cachePath)) {
            cachePath = FileHandle.getSDFilepath("demoClcx", FileHandle.timeStamp(null) + ".jpg");
        }
        File tempFile = new File(cachePath);
        Uri uri = Uri.fromFile(tempFile);
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        c.startActivityForResult(intent1,
                REQUEST_TAKEPHOTO_OK);
        return cachePath;
    }

    /**
     * 相册
     */
    public static void album(Activity c) {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        c.startActivityForResult(albumIntent, REQUEST_ALBUM_OK);
    }


    //=============回调方法：都在OnActivityResult方法中执行，判断requestCode===
    /*
    //拍照回调很简单，就是获取takePic方法返回的tempFile进行取值
    //之前需要判断requestCode=REQUEST_TAKEPHOTO_OK
    public static void takephoto(){
        if (tempFile == null) {
            return;
        }
        final String filPath = tempFile.getPath();
    }
    */

    /**
     * 相册回调
     *
     * @param c
     * @param data
     * @param call
     */
    public static void ActivityResultAlbum(Activity c, Intent data, AlbumCallback call) {
        try {
            Uri originalUri = data.getData();        //获得图片的uri
            String[] proj = {MediaStore.Images.Media.DATA};
            //好像是Android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = c.managedQuery(originalUri, proj, null, null, null);
            //按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径
            final String path = cursor.getString(column_index);
            if (call != null) {
                call.callback(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AlbumCallback {
        void callback(String path);//相册取到的图片路径
    }
}
