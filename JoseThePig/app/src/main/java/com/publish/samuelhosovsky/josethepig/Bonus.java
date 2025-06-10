package com.publish.samuelhosovsky.josethepig;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by SamuelHosovsky on 22. 5. 2015.
 */


public class Bonus {
    private Bitmap bitmap;  //The actual bitmap
    private int x;          //the x coordinate
    private int y;          //the y coordinate
    Barriermanager BGG;

    public Bonus(Bitmap decodeResource, int x, int y) {
        this.bitmap = decodeResource;
        this.x = x;
        this.y = y;
    }

    public void setBarrierManager(Barriermanager candidate) {
        BGG = candidate;
    }

    public ArrayList<Point> getArray() {
        Point TL = new Point(), TR = new Point(), BL = new Point(), BR = new Point();
        TL.x = x - bitmap.getWidth() / 2;
        TL.y = y - bitmap.getHeight() / 2;

        TR.x = x + bitmap.getWidth() / 2;
        TR.y = y - bitmap.getHeight() / 2;

        BL.x = x - bitmap.getWidth() / 2;
        BL.y = y + bitmap.getHeight() / 2;

        BR.x = x + bitmap.getWidth() / 2;
        BR.y = y + bitmap.getHeight() / 2;

        ArrayList<Point> temp = new ArrayList<Point>();
        temp.add(TL);
        temp.add(TR);
        temp.add(BR);
        temp.add(BL);

        return temp;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }

    public void update(float dt) {
        if (x < -BGG.game_panel.ScreenWidth / 4) {
            x = BGG.game_panel.ScreenWidth + bitmap.getWidth();
            Random r = new Random();
            y = r.nextInt(BGG.dl) + BGG.dpos - BGG.dl / 2;
        }

        x -= BGG.game_panel.ShipSpeed * dt / 1.8;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    private void getPointBacon(Point TL, Point TR, Point BL, Point BR) {
        TL.x = x - bitmap.getWidth() / 2 + 30;
        TL.y = y - bitmap.getHeight() / 2 + 30;

        TR.x = x + bitmap.getWidth() / 2;
        TR.y = y - bitmap.getHeight() / 2;

        BL.x = x - bitmap.getWidth() / 2 + 30;
        BL.y = y + bitmap.getHeight() / 2 - 30;

        BR.x = x + bitmap.getWidth() / 2;
        BR.y = y + bitmap.getHeight() / 2;
    }
}

