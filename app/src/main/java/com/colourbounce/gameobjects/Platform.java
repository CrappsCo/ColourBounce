package com.colourbounce.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.colourbounce.Animation;
import com.colourbounce.GamePanel;

/**
 * Created by C on 2015-05-24.
 */
public class Platform extends GameObject {

    public Platform() {
    }

    public Platform(Bitmap res, int w, int h, int xloc, int yloc, int xspeed, int yspeed, int color) {
        image = res;
        width = w;
        height = h;
        x = xloc;
        y = yloc;
        centerx = x + width / 2;
        centery = y + height / 2;
        dx = xspeed;
        dy = yspeed;
        playerSpeed = 0;
        currentColor = color;
        isEnemy = false;
        isPlatform = true;
        isPlayer = false;
        angle = Math.toDegrees(Math.atan2(height, width));
    }

    public void update(boolean scroll, int playerdy) {
        // Vertical Movment
        if (scroll)
            playerSpeed = -playerdy;
        else
            playerSpeed = 0;
        y += playerSpeed;
        y += dy;

        //Horizontal Movement
        x += dx;
        if (x + width >= GamePanel.WIDTH || x <= 0) {
            dx *= -1;
            dy *= -1;
        }

        // Update center
        centerx = x + width / 2;
        centery = y + height / 2;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }
}
