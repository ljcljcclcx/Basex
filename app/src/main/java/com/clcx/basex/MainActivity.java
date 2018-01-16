package com.clcx.basex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.clcx.basex.littlevideo.AtyLittleVideo;
import com.clcx.basex.utils.AuthorityGet;
import com.clcx.basex.utils.FileHandle;
import com.clcx.basex.utils.LogCLCXUtils;
import com.clcx.basex.utils.PicHandle;
import com.clcx.basex.littlevideo.PressRecordView;

public class MainActivity extends AppCompatActivity {

    private ImageView yulantu;
    private String takePicFile;
    private PressRecordView wwwww;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yulantu = (ImageView) findViewById(R.id.yulantu);
        wwwww= (PressRecordView) findViewById(R.id.wwwww);
        new Thread(wwwww).start();

        yulantu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] sel = {"拍照", "相册", "小视频", "取消"};
                new AlertDialog.Builder(MainActivity.this)
                        .setItems(sel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    AuthorityGet.camera(MainActivity.this, new AuthorityGet.AuthorCallback() {
                                        @Override
                                        public void callback() {
                                            takePicFile = PicHandle.takePhoto(MainActivity.this, FileHandle
                                                    .getSDFilepath("_aax",
                                                            "testimg_" + FileHandle.timeStamp(null) + ".jpg"));
                                        }
                                    });
                                } else if (which == 1) {
                                    AuthorityGet.SDcardRead(MainActivity.this, new AuthorityGet.AuthorCallback() {
                                        @Override
                                        public void callback() {
                                            PicHandle.album(MainActivity.this);
                                        }
                                    });
                                } else if (which == 2) {
                                    AuthorityGet.mic(MainActivity
                                            .this, new AuthorityGet.AuthorCallback() {
                                        @Override
                                        public void callback() {
                                            startActivity(new Intent(MainActivity.this, AtyLittleVideo.class));
                                        }
                                    });
                                }
                            }
                        })
                        .create().show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PicHandle.REQUEST_ALBUM_OK) {
            //相册回调
            PicHandle.ActivityResultAlbum(this, data, new PicHandle.AlbumCallback() {
                @Override
                public void callback(String path) {
                    yulantu.setImageBitmap(BitmapFactory.decodeFile(path));
                }
            });
        } else if (requestCode == PicHandle.REQUEST_TAKEPHOTO_OK) {
            yulantu.setImageBitmap(BitmapFactory.decodeFile(takePicFile));
        }
    }
}
