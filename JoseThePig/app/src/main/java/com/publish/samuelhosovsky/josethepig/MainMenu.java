package com.publish.samuelhosovsky.josethepig;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainMenu extends Activity {
    /**
     * Called when the activity is first created.
     */
    MediaPlayer MainMenuMusic;
    RelativeLayout Btn;
    ImageView ImageButton;
    TextView txt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Btn = (RelativeLayout) findViewById(R.id.btn_start);
        ImageButton = (ImageView) findViewById(R.id.img_btn);
        txt = (TextView) findViewById(R.id.text_start);
        Typeface Custom = Typeface.createFromAsset(getAssets(), "Jose_font.ttf");
        txt.setTypeface(Custom);
        txt.setShadowLayer(25, 0, 0, Color.WHITE);


        Btn.setOnTouchListener(new TochButton(ImageButton));
        Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, Game.class);
                startActivity(myIntent);
            }
        });
    }


    @Override
    protected void onStart() {
        MainMenuMusic = MediaPlayer.create(MainMenu.this, R.raw.welcome);
        MainMenuMusic.setVolume(2f, 2f);
        MainMenuMusic.start();
        super.onStart();

    }

    @Override
    protected void onStop() {
        if (MainMenuMusic.isPlaying()) {
            MainMenuMusic.stop();
        }
        super.onStop();
    }

}