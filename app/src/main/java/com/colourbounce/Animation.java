package com.colourbounce;

import android.graphics.Bitmap;

/**
 * Created by C on 2015-05-20.
 */
public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    public boolean playedOnce = false;

    public void setFrames(Bitmap[] frames){
        this.frames = frames;
        startTime = System.nanoTime();
    }

    public void setDelay(long d){
        delay = d;
    }

    public void update(){
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length){
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public Bitmap getImage(){
        return frames[currentFrame];
    }

    public boolean isPlayedOnce() {
        return playedOnce;
    }


}
