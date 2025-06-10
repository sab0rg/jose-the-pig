package com.publish.samuelhosovsky.josethepig;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;


/**
 * Created by SamuelHosovsky on 19. 5. 2015.
 */


public class Prasa {
    private Bitmap bitmap;
    private Bitmap bitmap2;
    private int x;
    private int y;
    private int Speed;
    private int inc;
    private int ScreenWidth;
    private int ScreenWeight;
    private int ScreenHeight;

    private int i = 0;

    //explostion sequence
    public ArrayList<Bitmap> Booms = null;
    boolean death;
    boolean up;
    float VertSpeed;

    //playing time of the animation
    float animTime = 0;
    //time of all the playing time animations
    float totalAnimTime = 1;
    //number of frames in a animation
    float numFrames;

    public Prasa(Bitmap decodeResource, Bitmap decodeResource2, int x, int y, int screenWidth, int screenheight) {
        this.bitmap = decodeResource;
        this.bitmap2 = decodeResource2;

        this.x = x;
        this.y = y;
        Speed = 1;
        inc = 0;
        death = false;
        ScreenWidth = screenWidth;
        ScreenHeight = screenheight;
        ScreenWeight = screenWidth;
        VertSpeed = 0;
    }

    public void setBoomAnimation(ArrayList<Bitmap> animation) {
        Booms = new ArrayList<Bitmap>(animation);
        numFrames = Booms.size();
    }


    public void draw(Canvas canvas) {
        if (!death && !up) {
            canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, null);
        } else if (!death && up) {
            canvas.drawBitmap(bitmap2, x - bitmap2.getWidth() / 2, y - bitmap2.getHeight() / 2, null);
        } else {
            //index of the animation frame shown at this time
            int index = (int) (animTime / totalAnimTime * numFrames);
            if (index < numFrames) {
                canvas.drawBitmap(Booms.get(index), x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, null);
            }
        }
    }

    public void update(float dt) {

        if (i < 30) {
            y = 500 - (i * 10);
            i++;
        } else {
            if (death) {
                //setting the time for frames of the explosion to execute
                animTime += dt * 5;

            } else {
                //original  VertSpeed+=ScreenHeight/0.7*dt;
                //modifying the gravity acceleration
                VertSpeed += ScreenHeight / 0.9 * dt;
                if (up)
                    //original  VertSpeed-=ScreenHeight*dt*4;
                    //modifying the normal acceleration
                    VertSpeed -= ScreenHeight * dt * 3;

                y += VertSpeed * dt;

                if (y - (bitmap.getHeight() / 2) > ScreenWidth)
                    y = 0 - (bitmap.getHeight() / 2);
                //looking if the pig left the screen. If yes then it will die.
                if (y > ScreenHeight - 30 || y < 0)
                   death = true;
            }
        }
    }

    //point takes only 2 parameter - x & y coorinate. With that I can calculate the location of the obstructions
    public boolean bump(Point OTL, Point OTR, Point OBR, Point OBL) {
        Point TL = new Point(), TR = new Point(), BL = new Point(), BR = new Point();

        ArrayList<Point> PointList = new ArrayList<Point>();
        PointList.add(OTL);
        PointList.add(OTR);
        PointList.add(OBR);
        PointList.add(OBL);
        getPoint(TL, TR, BL, BR);

        for (int i = 0; i < PointList.size(); i++) {
            if (BR.x >= PointList.get(i).x)
                if (TL.x <= PointList.get(i).x)
                    if (PointList.get(i).y >= TL.y)
                        if (PointList.get(i).y <= BR.y)
                            return true;
        }

        PointList.clear();
        PointList.add(TL);
        PointList.add(TR);
        PointList.add(BR);
        PointList.add(BL);
        for (int i = 0; i < PointList.size(); i++) {
            if (OBR.x >= PointList.get(i).x)
                if (OTL.x <= PointList.get(i).x)
                    if (PointList.get(i).y >= OTL.y)
                        if (PointList.get(i).y <= OBR.y)
                            return true;
        }

        return false;
    }

    //setting the points of the pig so that it colides with the barrier only when absolutely necessary (visual correction)
    public void getPoint(Point TL, Point TR, Point BL, Point BR) {
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
