package com.example.i_sangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playsong extends AppCompatActivity {
    protected void onDestroy(){
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }
    TextView songname;
    ImageView play, previous, next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String s_name;
    int pos;
    SeekBar seekBar;
    Thread updateseek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        songname = findViewById(R.id.songname);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);
        previous = findViewById(R.id.previous);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("SongList");
        s_name = intent.getStringExtra("Current Song");
        songname.setText(s_name);
        songname.setSelected(true);
        pos = intent.getIntExtra("Position", 0);
        Uri uri = Uri.parse(songs.get(pos).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateseek = new Thread() {
            @Override
            public void run() {
                int currpos = 0;
                try {
                        while(currpos< mediaPlayer.getDuration())
                        {
                            currpos=mediaPlayer.getCurrentPosition();
                             seekBar.setProgress(currpos);
                             sleep(800);
                        }
                } catch (Exception e) {

                                e.printStackTrace();
                }


            }
        };
        updateseek.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(pos!=0){
                    pos=pos-1;
                }
                else{
                    pos=songs.size()-1;     //circularly play your song
                }
                Uri uri = Uri.parse(songs.get(pos).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                s_name=songs.get(pos).getName().toString();
                songname.setText(s_name);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(pos!=songs.size()-1){
                    pos=pos+1;
                }
                else{
                    pos=0;     //circularly play your song
                }
                Uri uri = Uri.parse(songs.get(pos).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                s_name=songs.get(pos).getName().toString();
                songname.setText(s_name);

            }
        });
    }
}