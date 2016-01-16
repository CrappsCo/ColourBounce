package com.colourbounce.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by C on 2015-05-20.
 */
public class GameObject {
    public int x, y, centerx, centery, dx, dy, playerSpeed, width, height, radius, currentColor, numFrames;
    public int gravity;
    public double angle;
    public boolean isEnemy,isPlatform,isPlayer;
    public Bitmap image;

    public int cap(int var, int min, int max) {
        if (var < min)
            return min;
        else if (var > max)
            return max;
        else
            return var;
    }

    public Rect getBounds() {
        return new Rect(x, y, x + width, y + height);
    }


}
