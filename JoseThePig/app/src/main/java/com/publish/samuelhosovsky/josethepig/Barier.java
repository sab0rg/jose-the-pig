package com.publish.samuelhosovsky.josethepig;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by SamuelHosovsky on 19. 5. 2015.
 */
public class Barier {
    private Bitmap bitmap;
    private int x;
    private int y;

    Barriermanager BM;

    public Barier(Bitmap center, int x, int y) {
        bitmap = center;
        this.x = x;
        this.y = y;
    }

    public void setManager(Barriermanager barriermanager) {
        BM = barriermanager;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }

    //problem! needs to be discussed
    public void update(float dt, boolean b, int zreb) {

        //checking if the barrier is still there
        if (x < -bitmap.getWidth()) {
            //'b'is passed from the Barriermanager - 'update' method, determining if we have to use monkey-true or farmer-false
            int raz = 200;
            int vyska = BM.dpos;
            //'zreb' help me to randomly determine if monkey or ballard is going to appear

            //tu sa urci ci dalsi obrazok bude opica alebo ballard
            if (zreb == 1) {
                vyska = BM.dpos - raz;
            } else {
                vyska = BM.dpos + raz;
            }
            //koniec toho triedenia

            if (b) {
                //setting the y value for the top wall
                y = vyska - BM.dl / 2 - bitmap.getHeight() / 2;
            } else {
                //setting the y value for bottom wall
                y = vyska + BM.dl / 2 + bitmap.getHeight() / 2;
            }
            //setting x-value
            x = x + bitmap.getWidth() * (BM.TopWalls.size() - 1);
        }

        x = (int) (x - BM.game_panel.ShipSpeed * dt);

    }

    //setting the points for the barriers so that the pig colides with it only when absolutely necessary! (visual correction)
    public ArrayList<Point> getArray() {
        Point TL = new Point(), TR = new Point(), BL = new Point(), BR = new Point();
        TL.x = x - bitmap.getWidth() / 4;
        TL.y = y - bitmap.getHeight() / 2 + 10;

        TR.x = x + bitmap.getWidth() / 6;
        TR.y = y - bitmap.getHeight() / 2 + 10;

        BL.x = x - bitmap.getWidth() / 5;
        BL.y = y + bitmap.getHeight() / 2 - 9;

        BR.x = x + bitmap.getWidth() / 7;
        BR.y = y + bitmap.getHeight() / 2 - 9;

        ArrayList<Point> temp = new ArrayList<Point>();
        temp.add(TL);
        temp.add(TR);
        temp.add(BR);
        temp.add(BL);

        return temp;
    }
}
