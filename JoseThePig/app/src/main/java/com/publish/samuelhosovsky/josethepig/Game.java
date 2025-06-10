package com.publish.samuelhosovsky.josethepig;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Random;

public class Game extends ActionBarActivity {


    final static int UPDATE_SCORE = 0;
    //experiment
    final static int DEATH = 1;
    final static int LOSE = 2;

    Random r = new Random();
    int s = r.nextInt(40);

    View pauseButton;
    View PauseMenu;
    View WinDialog;
    View LoseDialog;
    RelativeLayout Rel_main_game;
    GamePanel game_panel;
    RelativeLayout Hint;


    MediaPlayer MainMusic;
    TextView txt;
    TextView txt_mainmenu;
    TextView txtScore;
    int get_coins = 0;
    int goal = 333;
    public static final int length = 3;


    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_SCORE) {

                i_get_coin();
            }

            if (msg.what == DEATH) {
                postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();
                        msg.what = LOSE;
                        handler.sendMessage(msg);

                    }
                }, 3000);
            }
            if (msg.what == LOSE) {
                i_lose();
            }

            super.handleMessage(msg);
        }
    };


    OnClickListener Continue_list = new OnClickListener() {
        @Override
        public void onClick(View v) {
            PauseMenu.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
            //this resumes the game
            game_panel.Pause_game = false;

        }
    };

    //Pause options listeners
    OnClickListener To_Main_Menu_list = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //after you click main menu the app will start over with the following line of code
            game_panel.thread.setRunning(false);
            Game.this.finish();
        }
    };

    OnClickListener Pausa_click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            pauseButton.setVisibility(View.GONE);
            PauseMenu.setVisibility(View.VISIBLE);
            game_panel.Pause_game = true;


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);
        Rel_main_game = (RelativeLayout) findViewById(R.id.main_game_rl);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);


        final int heightS = dm.heightPixels;
        final int widthS = dm.widthPixels;
        game_panel = new GamePanel(getApplicationContext(), this, widthS, heightS);
        Rel_main_game.addView(game_panel);


        //float dp = 15f;
        //float fpixels = dm.density * dp;
        //int pixels = (int) (fpixels + 0.5f);
        RelativeLayout RR = new RelativeLayout(this);
        RR.setBackgroundResource(R.drawable.button);
        RR.setGravity(Gravity.CENTER);
        RR.setX(0);
        Rel_main_game.addView(RR, 230, 110);
        txt = new TextView(this);
        Typeface Custom = Typeface.createFromAsset(getAssets(), "Jose_font.ttf");
        txt.setTypeface(Custom);
        txt.setTextColor(Color.BLACK);
        //dimension error
        txt.setTextSize(17);
        txt.setText("" + get_coins);

        //
        Hint = new RelativeLayout(this);
        Hint.setBackgroundResource(R.drawable.hint);
        Hint.setGravity(Gravity.CENTER);
        Hint.setY(heightS / 4);
        Hint.setX(widthS / 4);
        Rel_main_game.addView(Hint);

        fadeOutAndHideImage(Hint);

        //Hint.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
/*
        TransitionManager.beginDelayedTransition(Hint);

        RelativeLayout.LayoutParams positionRules = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        positionRules.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        Hint.setLayoutParams(positionRules);


        ViewGroup.LayoutParams sizeRules = Hint.getLayoutParams();
        sizeRules.width = 800;
        sizeRules.height = 500;
        Hint.setLayoutParams(sizeRules); */
        //

        RR.addView(txt);


        MainMusic = MediaPlayer.create(Game.this, R.raw.game_music);
        MainMusic.setVolume(0.6f, 0.6f);
        MainMusic.start();
        MainMusic.setLooping(true);


        LayoutInflater myInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        pauseButton = myInflater.inflate(R.layout.pause, null, false);
        pauseButton.setX(widthS - 170);
        pauseButton.setY(0);
        Rel_main_game.addView(pauseButton);

        ImageView pauseImage = (ImageView) pauseButton.findViewById(R.id.pause_btn);
        pauseButton.setOnTouchListener(new TochButton(pauseImage));

        pauseButton.setOnClickListener(Pausa_click);
        pauseButton.getLayoutParams().height = 150;
        pauseButton.getLayoutParams().width = 170;

        PauseMenu = myInflater.inflate(R.layout.pause_menu, null, false);
        Rel_main_game.addView(PauseMenu);

        /////////////FONT///////////////
        TextView cont = (TextView) findViewById(R.id.continue_g);
        TextView quit = (TextView) findViewById(R.id.go_mainmenu);
        cont.setTypeface(Custom);
        cont.setShadowLayer(25, 0, 0, Color.WHITE);
        quit.setTypeface(Custom);
        quit.setShadowLayer(25, 0, 0, Color.WHITE);

        ////////////END OF MY CODE//////

        PauseMenu.setVisibility(View.GONE);


        ImageView Cont = (ImageView) PauseMenu.findViewById(R.id.imCont);
        ImageView MainMenuTo = (ImageView) PauseMenu.findViewById(R.id.toMain);
        Cont.setOnTouchListener(new TochButton(Cont));
        Cont.setOnClickListener(Continue_list);
        MainMenuTo.setOnTouchListener(new TochButton(MainMenuTo));
        MainMenuTo.setOnClickListener(To_Main_Menu_list);

        WinDialog = myInflater.inflate(R.layout.win, null, false);
        Rel_main_game.addView(WinDialog);
        txtScore = (TextView) findViewById(R.id.txt_win);
        txtScore.setTypeface(Custom);
        txtScore.setShadowLayer(25, 0, 0, Color.WHITE);
        txt_mainmenu = (TextView) findViewById(R.id.txt_mainMenu);
        txt_mainmenu.setTypeface(Custom);
        txt_mainmenu.setShadowLayer(25, 0, 0, Color.WHITE);
        ImageView Win_to_main = (ImageView) WinDialog.findViewById(R.id.toMain);
        Win_to_main.setOnTouchListener(new TochButton(Win_to_main));
        Win_to_main.setOnClickListener(To_Main_Menu_list);
        WinDialog.setVisibility(View.GONE);


        LoseDialog = myInflater.inflate(R.layout.lost, null, false);
        Rel_main_game.addView(LoseDialog);
        txtScore = (TextView) findViewById(R.id.txtLost);


        txtScore.setText("Your highest score is " + (get_coins + "") + " bacon strips");
        txtScore.setTypeface(Custom);
        txtScore.setShadowLayer(25, 0, 0, Color.WHITE);
        txt_mainmenu = (TextView) findViewById(R.id.txt_restart);
        txt_mainmenu.setTypeface(Custom);
        txt_mainmenu.setShadowLayer(25, 0, 0, Color.WHITE);
        ImageView Lose_to_main = (ImageView) LoseDialog.findViewById(R.id.toMain);
        Lose_to_main.setOnTouchListener(new TochButton(Lose_to_main));
        Lose_to_main.setOnClickListener(To_Main_Menu_list);

        LoseDialog.setVisibility(View.GONE);
    }

    private void fadeOutAndHideImage(final RelativeLayout img) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(3000);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeOut);
    }


    @Override
    public void onBackPressed() {
        pauseButton.setVisibility(View.GONE);
        PauseMenu.setVisibility(View.VISIBLE);
        game_panel.Pause_game = true;
    }


    @Override
    protected void onStop() {
        if (MainMusic.isPlaying())
            MainMusic.stop();
        super.onStop();
    }


    protected void i_get_coin() {
        get_coins++;
        txt.setText("" + get_coins);
        MediaPlayer mp = MediaPlayer.create(Game.this, R.raw.bacon_pickup);
        mp.start();

        if (get_coins == goal) {
            i_win();
        }


        if(get_coins == 26 || get_coins == 27 || get_coins == s+50 || get_coins == s+51 || get_coins == s+108 || get_coins == s+109 || get_coins==s+232 || get_coins==s+233 || get_coins == 329){
            game_panel.inverted();
        }else if (get_coins == s+30 || get_coins == s+150 || get_coins==332){
            game_panel.inverted();
            game_panel.inverted();
        }

        SharedPreferences preferences = getSharedPreferences("HighScore", MODE_PRIVATE);
        int score = preferences.getInt("HS", 0);

        if (get_coins - 1 == score)
            Toast.makeText(this, "New High Score!", Toast.LENGTH_SHORT).show();

        switch(get_coins) {
            case 5:
                Toast.makeText(this, "Ok, I think you know what you're doing", Toast.LENGTH_LONG).show();
                break;
            case 20:
                Toast.makeText(this, "Do you think this is too easy?", Toast.LENGTH_LONG).show();
            break;
            case 26:
                Toast.makeText(this, "Life is full of surprises", Toast.LENGTH_LONG).show();
                break;
            case 30:
                Toast.makeText(this, "Not to distract you, but let me tell you a story", Toast.LENGTH_LONG).show();
                break;
            case 32:
                Toast.makeText(this, "You will have to read fast", Toast.LENGTH_SHORT).show();
                break;
            case 33:
                Toast.makeText(this, "Ready?", Toast.LENGTH_SHORT).show();
                break;
            case 34:
                Toast.makeText(getApplicationContext(), "01010111 01111100 01011010 11110001 10101011 01000101 01011010 101011101 01010111 00001100 11010011 01110001", Toast.LENGTH_SHORT).show();
                break;
            case 35:
                Toast.makeText(this, "There. I just told you what is waiting for you at the end of this game", Toast.LENGTH_LONG).show();
                break;
            case 39:
                Toast.makeText(this, "Ok, ok... Joking aside. Now the real story", Toast.LENGTH_LONG).show();
                break;
            case 41:
                Toast.makeText(this, "The day Jose was captured...", Toast.LENGTH_LONG).show();
                break;
            case 42:
                Toast.makeText(this,  "Actually, since you got this far..You deserve a poem!", Toast.LENGTH_LONG).show();
                break;
            case 48:
                Toast.makeText(this, "Do you ever feel,", Toast.LENGTH_SHORT).show();
                break;
            case 49:
                Toast.makeText(this, "like a plastic bag,", Toast.LENGTH_SHORT).show();
                break;
            case 50:
                Toast.makeText(this, "drifting through the wind,", Toast.LENGTH_SHORT).show();
                break;
            case 51:
                Toast.makeText(this, "wanting to start again?", Toast.LENGTH_SHORT).show();
                break;
            case 53:
                Toast.makeText(this, "That's what Jose felt like,", Toast.LENGTH_SHORT).show();
                break;
            case 54:
                Toast.makeText(this, "felt like stepping on a spike...", Toast.LENGTH_LONG).show();
                break;
            case 55:
                Toast.makeText(this, "His life hit a massive bump,", Toast.LENGTH_SHORT).show();
                break;
            case 56:{
                Toast.makeText(this, "when he heard the name Donald Trump.", Toast.LENGTH_SHORT).show();
                game_panel.inverted();
                game_panel.inverted();}
                break;
            case 58:
                Toast.makeText(this, "Jose didn't care about the fact,", Toast.LENGTH_SHORT).show();
                break;
            case 59:
                Toast.makeText(this, "that Evil Farmer his life has wracked.", Toast.LENGTH_SHORT).show();
                break;
            case 60:
                Toast.makeText(this, "It didn't matter to Jose much,", Toast.LENGTH_SHORT).show();
                break;
            case 61:
                Toast.makeText(this, "because his life would end such or such.", Toast.LENGTH_SHORT).show();
                break;
            case 63:
                Toast.makeText(this, "Jose is an immigrant,", Toast.LENGTH_SHORT).show();
                break;
            case 64:
                Toast.makeText(this, "who works hard as an ant.", Toast.LENGTH_SHORT).show();
                break;
            case 65:
                Toast.makeText(this, "Jose is an immigrant,", Toast.LENGTH_SHORT).show();
                break;
            case 66:
                Toast.makeText(this, "who counts his every cent.", Toast.LENGTH_SHORT).show();
                break;
            case 68:
                Toast.makeText(this, "But a bulb lighted above Jose's head,", Toast.LENGTH_SHORT).show();
                break;
            case 69:
                Toast.makeText(this, "much brighter than Donald's hair,", Toast.LENGTH_SHORT).show();
                break;
            case 70:
                Toast.makeText(this, "he doesn't need a billionaire,", Toast.LENGTH_SHORT).show();
                break;
            case 71:
                Toast.makeText(this, "he will build his own wall instead.", Toast.LENGTH_SHORT).show();
                break;
            case 74:
                Toast.makeText(this, "There was only one PROBLEM.", Toast.LENGTH_SHORT).show();
                break;
            case 100:
                Toast.makeText(this, "You're still here?", Toast.LENGTH_SHORT).show();
                break;
            case 107:
                Toast.makeText(this, "Uh...", Toast.LENGTH_SHORT).show();
                break;
            case 110:
                Toast.makeText(this, "Ok, I'll tell you what the problem was", Toast.LENGTH_SHORT).show();
                break;
            case 111:
                Toast.makeText(this, "Jose has no fingers on his paws!", Toast.LENGTH_LONG).show();
                break;
            case 132:
                Toast.makeText(this, "Now, I prepared a test for you", Toast.LENGTH_LONG).show();
                break;
            case 134:
                Toast.makeText(this, "Remember this code: 84Ei2222S7", Toast.LENGTH_LONG).show();
                break;
            case 138:
                Toast.makeText(this, "It is very important you remember it!", Toast.LENGTH_LONG).show();
                break;
            case 155:
                Toast.makeText(this, "84Ei2222S7", Toast.LENGTH_LONG).show();
                break;
            case 172:
                Toast.makeText(this, "One more time: 84Ei2222S7", Toast.LENGTH_LONG).show();
                break;
            case 200:
                Toast.makeText(this, "Still remember the code?", Toast.LENGTH_SHORT).show();
                break;
            case 201:
                Toast.makeText(this, "If you do, you will win the game now!", Toast.LENGTH_LONG).show();
                break;
            case 202:
                Toast.makeText(this, "I'll give you 3 options and you need to *click* on the right code", Toast.LENGTH_LONG).show();
                break;
            case 203:
                Toast.makeText(this, "If you hit the right one you will automatically see the end", Toast.LENGTH_SHORT).show();
                break;
            case 204:
                Toast.makeText(this, "Here it goes", Toast.LENGTH_SHORT).show();
                break;
            case 206:
                Toast.makeText(this, "B431222257", Toast.LENGTH_SHORT).show();
                break;
            case 208:
                Toast.makeText(this, "84Ei2222222S7", Toast.LENGTH_SHORT).show();
                break;
            case 210:
                Toast.makeText(this, "84Ei2222S7", Toast.LENGTH_SHORT).show();
                break;
            case 212:
                Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
                break;
            case 213:
                Toast.makeText(this, "You didn't really think I'd let you win this easily, did you?", Toast.LENGTH_LONG).show();
                break;
            case 214:
                Toast.makeText(this, "But I still want you to rate 5/5 stars", Toast.LENGTH_LONG).show();
                break;
            case 215:
                Toast.makeText(this, "I better make it up to you..", Toast.LENGTH_LONG).show();
                break;
            case 216:
                Toast.makeText(this, "I guess the music is pretty annoying at this point", Toast.LENGTH_LONG).show();
                break;
            case 217:
                Toast.makeText(this, "I'll stop it", Toast.LENGTH_SHORT).show();
                break;
            case 218:
                MainMusic.pause();
                break;
            case 219:
                Toast.makeText(this, "See. I can be nice too", Toast.LENGTH_LONG).show();
                break;
            case 220:
                MainMusic.start();
                Toast.makeText(this, "But not for long", Toast.LENGTH_LONG).show();
                break;
            case 250:
                Toast.makeText(this, "GIVE UP ALREADY !!!", Toast.LENGTH_SHORT).show();
                break;
            case 252:
                Toast.makeText(this, "You're better than 98% of the players anyway", Toast.LENGTH_LONG).show();
                break;
            case 260:
                Toast.makeText(this, "I'll leave you alone, so you can concentrate", Toast.LENGTH_LONG).show();
                break;
            case 263:
                game_panel.inverted();
                game_panel.inverted();
                Toast.makeText(this, "Just kidding!", Toast.LENGTH_SHORT).show();
                game_panel.inverted();
                game_panel.inverted();
                break;
            case 266:
                Toast.makeText(this, "Seriously, I'll leave you now.. See you at the end", Toast.LENGTH_LONG).show();
                break;
            case 294:
                Toast.makeText(this, "You didn't think I would abandon you, did you?", Toast.LENGTH_LONG).show();
                break;
            case 295:
                Toast.makeText(this, "I like you too much for that", Toast.LENGTH_SHORT).show();
                break;
            case 296:
                Toast.makeText(this, "I'm sure you missed me too", Toast.LENGTH_LONG).show();
                break;
            case 300:
                Toast.makeText(this, "You have only 33 left. Start focusing!", Toast.LENGTH_LONG).show();
                break;
            case 301:
                Toast.makeText(this, "Once you get out of the farm. You should grab a drink with me!", Toast.LENGTH_LONG).show();
                break;
            case 302:
                Toast.makeText(this, "Good luck to your final bacon strips", Toast.LENGTH_LONG).show();
                break;
            case 325:
                Toast.makeText(this, "DO NOT GET DISTRACTED", Toast.LENGTH_LONG).show();
                break;
            case 331:
                Toast.makeText(this, "CONCENTRATE!!!", Toast.LENGTH_LONG).show();
                break;
        }

    }


    protected void i_lose() {
        if (MainMusic.isPlaying())
            MainMusic.stop();

        pauseButton.setVisibility(View.GONE);
        MainMusic = MediaPlayer.create(Game.this, R.raw.lost);
        MainMusic.start();

        SharedPreferences sharedPreferences = getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int highS = sharedPreferences.getInt("HS", 0);


        if (highS <= get_coins) {
            editor.putInt("HS", get_coins);
            editor.commit();
        }


        if (get_coins > 4 && get_coins < 281)
            txtScore.setText(Html.fromHtml("<font color = '#e50bc5'> Oink Oink Oink Oooooink... </font> &#128055; \nYou ate <font color = '#EE0000' >" + get_coins + "</font> bacon strips." + "\nHighest Score: <font color = '#EE0000' >" + highS + "</font>"));
        else if (get_coins == 0)
            txtScore.setText(Html.fromHtml("You didn't eat any bacon?! Come on, dude! &#128055; \nEat " + goal + " golden bacon strips to win! "));
        else if (get_coins == 1)
            txtScore.setText(Html.fromHtml("You ate only <font color = '#EE0000' >" + get_coins + "</font> golden bacon strip. &#128055; " + "\nEat " + goal + " golden bacon strips to win! "));
        else if (get_coins > 280)
            txtScore.setText(Html.fromHtml("You were soooo close! You ate <font color = '#EE0000' >" + get_coins + "</font> golden bacon strips. Highest Score: <font color = '#EE0000' >\" + highS + \"</font>"));
        else
            txtScore.setText(Html.fromHtml("You ate only <font color = '#EE0000' >" + get_coins + "</font> golden bacon strips!" + "\nEat " + goal + " golden bacon strips to win! " + "\nHighest Score: <font color = '#EE0000' >" + highS + "</font> &#128055;"));


        game_panel.Pause_game = true;
        LoseDialog.setVisibility(View.VISIBLE);
    }

    private void i_win() {
        if (MainMusic.isPlaying())
            MainMusic.stop();
        pauseButton.setVisibility(View.GONE);
        MainMusic = MediaPlayer.create(Game.this, R.raw.won);
        MainMusic.start();
        game_panel.Pause_game = true;
        WinDialog.setVisibility(View.VISIBLE);
    }
}