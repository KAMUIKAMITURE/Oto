package com.lifeistech.android.oto;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    private static final String TAG = "Ch0703";
    // 再生
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private String mFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);

        ToggleButton recSwitch = (ToggleButton) findViewById(R.id.toggleButton);
        recSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    // 録音の開始
                    startRecord();
                } else {
                    // 録音の停止
                    if (mMediaRecorder != null) {
                        mMediaRecorder.stop();
                        mMediaRecorder.reset();
                    }
                }
            }
        });

        // 再生ボタンのイベント登録
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 録音した音声を再生
                startPlay();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaPlayerの解放処理
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        // MediaRecorderの解放処理
        if (mMediaRecorder != null) {
            if (mMediaRecorder != null) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        }
    }

    /**
     * MediaRecorderを初期化し録音を開始
     */
    private void startRecord() {
        // 再生中であれば再生を停止
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }

        // MediaRecorderの初期化
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        // 入力ソースをマイクに設定
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 保存フォーマットを3gpに設定
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // Audioエンコードをデフォルトに設定
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        // 外部ストレージ（microSDなど）に「hoge.3gp」という名前で保存する
        String fileName = "hoge.3gp";
        mFilePath = Environment.getExternalStorageDirectory() + "/" + fileName;
        mMediaRecorder.setOutputFile(mFilePath);

        // 録音準備が完了したら録音開始
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        }
    }

    /**
     * MediaPlayerを初期化し音声を再生
     */
    private void startPlay() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        } else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setDataSource(mFilePath);
            // 音声の再生準備が完了した際に呼び出されるリスナー
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 音声を再生
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(MainActivity.this,
                            R.string.app_name,
                            Toast.LENGTH_SHORT).show();
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        }
    }
}

