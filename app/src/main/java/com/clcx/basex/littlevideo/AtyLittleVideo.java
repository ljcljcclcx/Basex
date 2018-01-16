package com.clcx.basex.littlevideo;

import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.clcx.basex.R;
import com.clcx.basex.utils.FileHandle;
import com.clcx.basex.utils.SizeClcx;

import java.util.List;

/**
 * Created by ljc123 on 2018/1/15.
 */

public class AtyLittleVideo extends Activity {
    private SurfaceView littlevideo_surface;
    private SurfaceView littlevideo_surface_play;
    private PressRecordView littlevideo_btn;
    private TextView littlevideo_btn_ok;
    private TextView littlevideo_btn_cancel;

    private Camera mCamera;
    private SurfaceHolder mSfholder;
    private SurfaceHolder mPlayerSfholder;
    private MediaRecorder mMediaRecorder = new MediaRecorder();
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    private static final int STEP_RECORD = 0;//录制步骤
    private static final int STEP_PLAY = 1;//播放步骤
    private int step = STEP_RECORD;

    private static final int VID_WID = 640;
    private static final int VID_HEI = 480;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_littlevideo);
        littlevideo_surface = (SurfaceView) findViewById(R.id.littlevideo_surface);
        littlevideo_surface_play = (SurfaceView) findViewById(R.id.littlevideo_surface_play);
        littlevideo_btn = (PressRecordView) findViewById(R.id.littlevideo_btn);
        littlevideo_btn_ok = (TextView) findViewById(R.id.littlevideo_btn_ok);
        littlevideo_btn_cancel = (TextView) findViewById(R.id.littlevideo_btn_cancel);

        /**
         * 双向进度条到时回调（有时间限制，暂时为10秒）
         */
        littlevideo_btn.setOnRecordListener(new PressRecordView.OnRecordListener() {
            @Override
            public void onTimeUp() {
                stopRecord();
            }

            @Override
            public void onStart(PressRecordView v) {
                startRecord();
                new Thread(v).start();
            }

            @Override
            public void onStop() {
                stopRecord();
            }
        });

        //设置录制的SurfaceView
        mSfholder = littlevideo_surface.getHolder();
        mSfholder.setFixedSize(VID_WID, VID_HEI);//屏幕分辨率
        mSfholder.addCallback(new RecordHolderCallback());

        //设置播放的SurfaceView
        mPlayerSfholder = littlevideo_surface_play.getHolder();
        mPlayerSfholder.setFixedSize(VID_WID, VID_HEI);//屏幕分辨率
        mPlayerSfholder.addCallback(new PlayHolderCallback());
    }

    private void initPlayer() {
        littlevideo_surface.setVisibility(View.GONE);
        littlevideo_surface_play.setVisibility(View.VISIBLE);
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(this, Uri.parse(targetDir));
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    mMediaPlayer.setLooping(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 开启录制预览
     */
    private void startPreview(SurfaceHolder holder) {
        if (mCamera == null) mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        if (mMediaRecorder == null) mMediaRecorder = new MediaRecorder();
        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(holder);
                Camera.Parameters pms = mCamera.getParameters();
                //自动对焦
                List<String> focusMode = pms.getSupportedFocusModes();
                if (focusMode != null) {
                    for (String mode : focusMode) {
                        mode.contains("continuous-video");
                        pms.setFocusMode("continuous-video");
                    }
                }
                mCamera.setParameters(pms);
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private boolean isRecording = false;
    //录制保存路径
    private String targetDir = FileHandle.getSDFilepath("_tmpclcx", "vid_" + FileHandle.timeStamp(null) + ".mp4");

    /**
     * 开始录制
     */
    private void startRecord() {
        if (mMediaRecorder != null) {
            //没有SD卡直接返回
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return;
            }
            try {
                mCamera.unlock();
                mMediaRecorder.setCamera(mCamera);
                //从相机采集视频
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                //从麦克风采集音频
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                //设置视频格式
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mMediaRecorder.setVideoSize(VID_WID, VID_HEI);
                //每秒帧数
                mMediaRecorder.setVideoFrameRate(24);
                //编码格式
                mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                //设置帧频率，据说因此会清晰
                mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024 * 100);
                // 保存
                mMediaRecorder.setOutputFile(targetDir);
                mMediaRecorder.setPreviewDisplay(mSfholder.getSurface());
                //解决录制视频, 播放器横向问题
                mMediaRecorder.setOrientationHint(90);

                mMediaRecorder.prepare();
                //正式录制
                mMediaRecorder.start();
                isRecording = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 停止录制
     */
    private void stopRecord() {
        if (isRecording) {
            isRecording = false;
            mMediaRecorder.stop();
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 开始自动播放
                 */
                initPlayer();
                //停止录制之后的一些按钮动画
                littlevideo_btn_ok.setVisibility(View.VISIBLE);
                littlevideo_btn_cancel.setVisibility(View.VISIBLE);
                littlevideo_btn_ok.setAlpha(0.0f);
                littlevideo_btn_cancel.setAlpha(0.0f);
                ViewCompat.animate(littlevideo_btn_ok).alpha(1.0f).translationX(SizeClcx.windowSize
                        (AtyLittleVideo.this)[0] / 4).setDuration(200).start();
                ViewCompat.animate(littlevideo_btn_cancel).alpha(1.0f).translationX(-1 * SizeClcx.windowSize
                        (AtyLittleVideo.this)[0] / 4).setDuration(200).start();
                ViewCompat.animate(littlevideo_btn).alpha(0.0f).setDuration(200)
                        .setListener(new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationStart(View view) {

                            }

                            @Override
                            public void onAnimationEnd(View view) {
                                littlevideo_btn.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(View view) {

                            }
                        }).start();
            }
        });
    }

    /**
     * 播放视频的SurfaceHolder回调
     */
    private class PlayHolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (mMediaPlayer != null)
                mMediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }
    }

    /**
     * 录制视频的SurfaceHolder回调
     */
    private class RecordHolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mSfholder = holder;
            startPreview(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
            if (mMediaRecorder != null) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        }
    }
}
