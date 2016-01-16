package com.colourbounce.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.colourbounce.GamePanel;

/**
 * Created by C on 2015-05-20.
 */
public class Player extends GameObject {

    public boolean playing, neutralZone, scroll;
    public float tiltAngle;




    public Player(Bitmap res, int xloc, int yloc) {
        image = res;
        width = image.getWidth();
        height = image.getHeight();
        radius = width / 2;
        x = xloc;
        y = yloc;
        centerx = x + radius;
        centery = y + radius;
        dx = 0;
        gravity = -37;
        playerSpeed = gravity;
        isEnemy = false;
        isPlatform = false;
        isPlayer = true;

        neutralZone = true;
        tiltAngle = 0;


    }

    public void update() {

        dx = (int) (4.0 * -1.0 * tiltAngle);
        x += dx;
        x = cap(x, 0, GamePanel.WIDTH - width);


        if (y <= GamePanel.HEIGHT / 3 && playerSpeed < 0)
            scroll = true;
        else
            scroll = false;

        centerx = x + radius;
        centery = y + radius;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void setNeutral(boolean n) {
        this.neutralZone = n;
    }

    public void setTiltAngle(float a) {
        this.tiltAngle = a;
    }

    public void setGravity(int g){ this.gravity = g;}

}
