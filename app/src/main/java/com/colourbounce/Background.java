package com.colourbounce;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by C on 2015-05-20.
 */
public class Background {
    private Bitmap image;

    public Background(Bitmap res){
        image = res;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, 0, 0, null);
    }
}
