package com.publish.samuelhosovsky.josethepig;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SamuelHosovsky on 19. 5. 2015.
 */
public class Barriermanager {

    Bitmap center;
    Bitmap center2;
    int pigHeight;
    int Num;
    int screenH;
    int dl;
    int TargetY = -1;
    int dpos;
    public GamePanel game_panel;

    ArrayList<Barier> TopWalls = null;
    ArrayList<Barier> BottomWalls = null;

    public Barriermanager(Bitmap decodeResource, Bitmap decodeResource2, GamePanel game_panel) {
        center = decodeResource;
        center2 = decodeResource2;
        this.game_panel = game_panel;
    }


    void setPigHeight(int h) {
        pigHeight = h;
    }

    public void setScreen(int width, int height) {
        screenH = height;
        //determining how many "barriers" fit the screen (verticaly)
        Num = (width) / center.getWidth() + 4;
        TopWalls = new ArrayList<Barier>();
        BottomWalls = new ArrayList<Barier>();
        for (int i = 0; i < Num + 1; i++) {
            //I had to inicialize the barriers ahaed that's why its '*i'
            Barier BB = new Barier(center, width + 200 + center.getWidth() * i, 0);
            BB.setManager(this);
            TopWalls.add(BB);
            Barier BBB = new Barier(center2, width + 200 + center2.getWidth() * i, 0);
            BBB.setManager(this);
            BottomWalls.add(BBB);
        }
        Generate();
    }

    private void Generate() {
        int h = center.getHeight();
        dl = screenH;
        dpos = screenH / 2;
        int new_dl = screenH * 3 / 5;
        int inc = (dl - new_dl) / Num;
        for (int i = 0; i < Num + 1; i++) {
            dl = dl - inc;
            h = TopWalls.get(i).getBitmap().getHeight() / 2;
            TopWalls.get(i).setY(-800);
            BottomWalls.get(i).setY(-800);

        }

    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < Num + 1; i++) {
            TopWalls.get(i).draw(canvas);
            BottomWalls.get(i).draw(canvas);
        }

    }

    public void update(float dt) {
        for (int i = 0; i < Num + 1; i++) {
            //zreb rozhodne o tom ci sa ma vytvorit TopWalls alebo BottomWalls object. Tento parameter sa potom posunie do Barier.update metody kde sa s nim pracuje
            int zreb = new Random().nextInt(2);

            //moj kod na zvacsenie medzeri
            if (i % 10 == 0) {
                TopWalls.get(i).update(dt, true, zreb);
                BottomWalls.get(i).update(dt, false, zreb);
            }

        }
    }


}
