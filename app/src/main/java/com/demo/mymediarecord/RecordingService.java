package com.demo.mymediarecord;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimerTask;

public class RecordingService extends Service {

    private static final String TAG = "RecordingService";

    private String mFileName = null;
    private String mFilePath = null;

    private MediaRecorder mMediaRecorder = null;

    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private TimerTask mIncrementTimerTask = null;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onActivityCreated === ");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onActivityCreated === ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand === ");
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy === ");
        if (mMediaRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }

    public void startRecording() {
        Log.d(TAG, "startRecording === 开始录音");
        setFileNameAndPath(); // 设置保存路径

        // 实例化MediaRecorder对象
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置麦克风录音
        /*
         * 设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
         * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
         */
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setOutputFile(mFilePath);
        try {
            mMediaRecorder.prepare(); // 准备
            mMediaRecorder.start(); // 开始
            mStartingTimeMillis = System.currentTimeMillis(); // 开始时间
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFileNameAndPath() {
        Log.d(TAG, "setFileNameAndPath === 设置保存路径");
        mFileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".m4a";
        File saveDir = new File(Environment.getExternalStorageDirectory() + "/audiokit/");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        String audioSaveDir = Environment.getExternalStorageDirectory() + "/audiokit/";
        mFilePath = audioSaveDir + mFileName;
    }

    public void stopRecording() {
        Log.d(TAG, "stopRecording === 停止录音");
        try {
            mMediaRecorder.stop();
            mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
            mMediaRecorder.release();

            getSharedPreferences("sp_name_audio", MODE_PRIVATE)
                    .edit()
                    .putString("audio_path", mFilePath)
                    .putLong("elpased", mElapsedMillis)
                    .apply();

            if (mIncrementTimerTask != null) {
                mIncrementTimerTask.cancel();
                mIncrementTimerTask = null;
            }

            mMediaRecorder = null;
            mFilePath = "";
        } catch (RuntimeException e) {
            e.printStackTrace();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file =  new File(mFilePath);
            if (file.exists()) {
                file.delete();
            }

            mFilePath = "";
        }
    }
}
