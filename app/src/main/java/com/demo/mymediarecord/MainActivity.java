package com.demo.mymediarecord;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button start_record_btn, stop_record_btn, play_audio_btn;
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private String fileName, audioSaveDir, filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initVariables();
        initViews();
    }

//    @Override
    protected void initVariables() {

    }

//    @Override
    protected void initViews() {


        start_record_btn = (Button) findViewById(R.id.start_record_btn);
        start_record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
                fragment.show(getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
                fragment.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                    @Override
                    public void onCancel() {
                        fragment.dismiss();
                    }
                });

            }
        });

        stop_record_btn = (Button) findViewById(R.id.stop_record_btn);
        stop_record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopRecord();
            }
        });

        play_audio_btn = (Button) findViewById(R.id.play_audio_btn);
        play_audio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecordingItem recordingItem = new RecordingItem();
                SharedPreferences sharePreferences = getSharedPreferences("sp_name_audio", MODE_PRIVATE);
                final String filePath = sharePreferences.getString("audio_path", "");
                long elpased = sharePreferences.getLong("elpased", 0);
                recordingItem.setFilePath(filePath);
                recordingItem.setLength((int) elpased);
                PlaybackDialogFragment fragmentPlay = PlaybackDialogFragment.newInstance(recordingItem);
                fragmentPlay.show(getSupportFragmentManager(), PlaybackDialogFragment.class.getSimpleName());


//                if ("".equals(filePath) || filePath == null) {
//                    Toast.makeText(MainActivity.this, "filePath is empty ...", Toast.LENGTH_LONG).show();
//                } else {
//                    play(filePath);
//                }
            }
        });

        mMediaPlayer = new MediaPlayer();
    }

//    @Override
    protected void loadData() {

    }

    public void play(String filePath) {
        try {
            // 如果正在播放，然后在播放其他文件就直接崩溃了
            if (mMediaPlayer.isPlaying()) {
                return;
            }
            // 设置数据源
            mMediaPlayer.setDataSource(filePath);
            // 这个准备工作必须要做
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 播放完毕再重置一下状态，下次播放可以再次使用
                    mp.reset();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
