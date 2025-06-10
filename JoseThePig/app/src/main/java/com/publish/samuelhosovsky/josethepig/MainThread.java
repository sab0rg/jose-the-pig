package com.publish.samuelhosovsky.josethepig;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by SamuelHosovsky on 18. 5. 2015.
 */


public class MainThread extends Thread {

    private SurfaceHolder surfaceHolder;
    GamePanel gamePanel;
    private boolean running;
    float dt;
    //canvas = graphics
    Canvas canvas;

    public MainThread(SurfaceHolder holder, GamePanel gamePanel) {
        this.surfaceHolder = holder;
        this.gamePanel = gamePanel;
        dt = 0;
    }

    //boolean variable to pass the game state;
    void setRunning(boolean running) {
        this.running = running;
    }

    //creating the infinite game loop
    @Override
    public void run() {


        while (running) {
            if (!gamePanel.Pause_game) {
                //variable "StartDraw" that counts the time since the begining of the game in milliseconds
                long StartDraw = System.currentTimeMillis();
                //assigning canvas to null so can it can be redrawn as the pigs flies
                canvas = null;
                try {
                    //placing the drawing with respect to "StartDraw". Reassing value for canvas. (mrs. Shoemaker it's like a video-effect)
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        //updating position of the surfaceHolder on the screen after the canvas has been reassigned
                        gamePanel.Update(dt);
                        gamePanel.Draw(canvas);
                    }
                } finally {
                    //if the canvas is filled, unlock the canvas for future action in the infinite loop
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
                //variable "EndDraw" that counts the time since the begining of the game in milliseconds but is recorded after the drawing is drawn
                long EndDraw = System.currentTimeMillis();
                //The time difference in milli milli seconds
                dt = (EndDraw - StartDraw) / 1000.f;
            }

        }

    }


}
