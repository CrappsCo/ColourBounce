package com.colourbounce.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.colourbounce.GamePanel;

/**
 * Created by C on 2015-05-31.
 */
public class Enemy extends GameObject {

    public Enemy() {
    }

    public Enemy(Bitmap res, int w, int h, int xloc, int yloc, int xSpeed, int ySpeed, int color) {
        image = res;
        width = w;
        height = h;
        x = xloc;
        y = yloc;
        dx = xSpeed;
        dy = ySpeed;
        playerSpeed = 0;
        centerx = x + width / 2;
        centery = y + height / 2;
        currentColor = color;
        isEnemy = true;
        isPlatform = false;
        isPlayer = false;
    }

    public void update(boolean scroll, int playerdy) {
        // Vertical Movment
        if (scroll)
            playerSpeed = -playerdy;
        else
            playerSpeed = 0;
        y += playerSpeed;
        y += dy;

        // Horizontal Movment
        x += dx;
        if (x + width >= GamePanel.WIDTH || x <= 0)
            dx *= -1;

        centerx = x + width / 2;
        centery = y + height / 2;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }
}
