
package com.e.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button playBtn;
    private SeekBar positionBar,volumeBar;
    private TextView elapsedTimeLabel,remainingTimeLabel;
    private MediaPlayer mediaPlayer;
    private int totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn=(Button)findViewById(R.id.playBtn);
        elapsedTimeLabel=(TextView)findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel=(TextView)findViewById(R.id.remainingTimeLabel);
        positionBar=(SeekBar)findViewById(R.id.positionBar);
        volumeBar=(SeekBar)findViewById(R.id.volumeBar);

        mediaPlayer=MediaPlayer.create(MainActivity.this,R.raw.files);
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f,0.5f);
        totalTime= mediaPlayer.getDuration();


        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTheUser) {

                if(fromTheUser)
                {
                    mediaPlayer.seekTo(progress);
                    positionBar.setProgress(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTheUser) {
                float volumeNumber=progress/100f;
                mediaPlayer.setVolume(volumeNumber,volumeNumber);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {

                while (mediaPlayer!=null)
                {



                    try {

                        Message message = new Message();
                        message.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }




            }
        });


    }


    private final Handler handler = new Handler(Looper.myLooper())
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            int currentPosition = obtainMessage().what;
            positionBar.setProgress(currentPosition);

            String elapsedTime =createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText(remainingTime);
        }
    };

    public String createTimeLabel(int time )
    {
        String timeLabel ="";
        int min = time/1000/60;
        int sec=time/1000%60;

        timeLabel = min + "";
        if(sec<10)
            timeLabel+="0";
        timeLabel+=sec;
        return timeLabel;
    }


    public  void playBtnClick(View view)
    {
        if(!mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
            playBtn.setBackgroundResource(R.drawable.pause);
        }
        else
        {
            mediaPlayer.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }
}




