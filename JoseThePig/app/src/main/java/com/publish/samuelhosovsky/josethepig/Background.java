package com.publish.samuelhosovsky.josethepig;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by SamuelHosovsky on 18. 5. 2015.
 */
public class Background {

    Bitmap BackBitmap;
    int x, y;
    int ScreenWidth;
    int Count_Background;
    GamePanel root_gamepanel;

    public Background(Bitmap bitmap, int Screen_w, GamePanel Game_panel) {
        this.BackBitmap = bitmap;
        this.x = 0;
        this.y = 0;
        this.ScreenWidth = Screen_w;
        //"Count_Background is a number of how many screen images could be place to the background
        Count_Background = ScreenWidth / BackBitmap.getWidth() + 1;
        root_gamepanel = Game_panel;
        //invert();

    }

    public void invert() {

        int length = BackBitmap.getWidth() * BackBitmap.getHeight();
        int[] array = new int[length];
        int[] array2 = new int[length];
        BackBitmap.getPixels(array2, 0, BackBitmap.getWidth(), 0, 0, BackBitmap.getWidth(), BackBitmap.getHeight());

        for (int i=0;i<length;i++){
            array[i] = 0xFFFFFF - array2[i];
        }
        BackBitmap = BackBitmap.copy(Bitmap.Config.ARGB_8888, true);
        BackBitmap.setPixels(array, 0, BackBitmap.getWidth(), 0, 0, BackBitmap.getWidth(), BackBitmap.getHeight());
    }


    //this is done so that the background appears endless
    public void draw(Canvas canvas) {
        for (int i = 0; i < Count_Background + 1; i++) {
            if (canvas != null)
                //setting up the canvas of the bit background map
                canvas.drawBitmap(BackBitmap, BackBitmap.getWidth() * i + x, y, null);
        }
        if (Math.abs(x) > BackBitmap.getWidth()) {
            x = x + BackBitmap.getWidth();
        }

    }

    public void update(float dt) {
        x = (int) (x - root_gamepanel.ShipSpeed * dt);

    }

}
