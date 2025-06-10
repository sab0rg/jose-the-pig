package com.publish.samuelhosovsky.josethepig;

/**
 * Created by SamuelHosovsky on 18. 5. 2015.
 */


import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public MainThread thread;
    private Background background;
    private Prasa ship;
    private Barriermanager BM;
    private Bonus bacon;
    public float ShipSpeed;
    public int ScreenWidth;
    public int Screenheigt;
    public Game game;
    public boolean Pause_game;
    private Bitmap[] pigFrames = new Bitmap[2];
    int f = 0;


    public GamePanel(Context context, Game game, int ScreenWidth, int Screenheigt) {
        super(context);
        getHolder().addCallback(this);
        this.game = game;
        //so that thread can turn into the game panel
        thread = new MainThread(getHolder(), this);
        pigFrames[0] = BitmapFactory.decodeResource(getResources(), R.drawable.pig1);
        pigFrames[1] = BitmapFactory.decodeResource(getResources(), R.drawable.pig2);
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.game_background), ScreenWidth, this);
        BM = new Barriermanager(BitmapFactory.decodeResource(getResources(), R.drawable.monkey), BitmapFactory.decodeResource(getResources(), R.drawable.farmer), this);
        BM.setScreen(ScreenWidth, Screenheigt);
        ship = new Prasa(pigFrames[0], pigFrames[1],100, 0, ScreenWidth, Screenheigt);
        bacon = new Bonus(BitmapFactory.decodeResource(getResources(), R.drawable.bonus_bacon), -200, -200);

        ArrayList<Bitmap> animation = new ArrayList<Bitmap>();
        animation.add(BitmapFactory.decodeResource(getResources(), R.drawable.explosion1));
        animation.add(BitmapFactory.decodeResource(getResources(), R.drawable.explosion2));
        animation.add(BitmapFactory.decodeResource(getResources(), R.drawable.explosion3));
        animation.add(BitmapFactory.decodeResource(getResources(), R.drawable.explosion4));
        ship.setBoomAnimation(animation);

        bacon.setBarrierManager(BM);
        setFocusable(true);
        ShipSpeed = ScreenWidth / 2.f;
        this.ScreenWidth = ScreenWidth;
        this.Screenheigt = Screenheigt;

    }

    //help with clicking when playing the game
    //if there is a touch then the pig should flap
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //if you hold it, it goes up
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ship.up = true;

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            ship.up = false;
        }
        return true;
    }


    //this will keep all of our drawings
    void Draw(Canvas canvas) {
        if (!Pause_game) {
            if (canvas != null) {
                //canvas.drawColor(Color.BLACK);
                background.draw(canvas);
                bacon.draw(canvas);
                ship.draw(canvas);
                BM.draw(canvas);

            }
        }
    }

    public void inverted(){
        background.invert();
    }

    void Update(float dt){
        ship.update(dt);
        //bumbing
        if (!ship.death) {
            background.update(dt);
            BM.update(dt);

            for (int i = 0; i % 5 == 0; i++) {

                bacon.update(dt);
            }
        }

        ArrayList<Point> bacon_point = new ArrayList<Point>(bacon.getArray());
        if (ship.bump(bacon_point.get(0), bacon_point.get(1), bacon_point.get(2), bacon_point.get(3))) {
            bacon.setX(-200);
            bacon.setY(-200);
            Message msg = BM.game_panel.game.handler.obtainMessage();
            msg.what = 0;
            BM.game_panel.game.handler.sendMessage(msg);

        }

        for (int i = 0; i < BM.TopWalls.size(); i++) {
            ArrayList<Point> temp = new ArrayList<Point>(BM.TopWalls.get(i).getArray());
            //after we have accest the TopWalls arraylist we can call bump that check TopWalls object's points position with the pig's points
            ArrayList<Point> temp2 = new ArrayList<Point>(BM.BottomWalls.get(i).getArray());
            //after we have accest the TopWalls arraylist we can call bump that check BottomWalls object's points position with the pig's points

            if ((ship.bump(temp.get(0), temp.get(1), temp.get(2), temp.get(3))) || (ship.bump(temp2.get(0), temp2.get(1), temp2.get(2), temp2.get(3))) || ship.death) {

                ship.death = true;
                game.onStop();
                while(f==0) {
                    MediaPlayer mp = MediaPlayer.create(game, R.raw.explosion_fart);
                    mp.start();
                    f++;
                }
                f++;
                Message msg = BM.game_panel.game.handler.obtainMessage();
                msg.what = 1;
                BM.game_panel.game.handler.sendMessage(msg);
                //ship.update(dt);
                //ship.draw(Canvas canvas);
                //thread.setRunning(false);
                i = BM.TopWalls.size()-1;
                if(f == 10){
                    thread.setRunning(false);
                }
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }

        }
    }
}