package com.colourbounce;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;


/**
 * Created by C on 2015-12-30.
 */
public class DeathScreen {
    public Bitmap image;
    public Context context;
    public int x, y, height, width;

    public DeathScreen(){
    }

    public DeathScreen(Context context) {
        this.context = context;
        width = 1080;
        height = 1920;
        x = 0; //(GamePanel.WIDTH - width) / 2;
        y = 0; //(GamePanel.HEIGHT - height) / 2;
    }


    public void draw(Canvas canvas) {
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.transparentblack);
        canvas.drawBitmap(image, 0, 0, null);
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.deathscreen);
        canvas.drawBitmap(image, x, y, null);
    }

    public Rect replayButton(){
        return new Rect(0, 950, 1080, 1200);
    }

    public Rect mainmenuButton(){
        return new Rect(0, 1300, 1080, 1550);
    }
}

