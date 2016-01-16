package com.colourbounce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.colourbounce.gameobjects.Enemy;
import com.colourbounce.gameobjects.GameObject;
import com.colourbounce.gameobjects.Platform;
import com.colourbounce.gameobjects.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C on 2015-05-20.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 1920;
    public static boolean neutralZone = true;
    public static float tiltAngle = 0;
    public boolean[] color;
    public boolean gameOver, scroll;
    public int currentColor, scrollSpeed, maxColor, maxEnemies, maxPlatforms, Highscore;
    public Bitmap platformBase, enemyBase;


    private MainThread thread;
    public Background bg;
    public Context context;
    public Player player;
    public DeathScreen deathScreen;

    public SoundPool jump;
    public int jumpsound;

    //List Of Game Objects
    public List<Platform> platforms;
    public List<Enemy> enemies;
    //Score related stuff.
    public Paint paint = new Paint();  //For printing out the score
    public int score;
    public int HeightReached;

    public int PreviousX, PreviousY;

    public GamePanel(Context context) {
        super(context);
        this.context = context;
        //adds the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupGame();
    }

    // ********** Create Highscore **********

    public int getHighscore() {
        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        return prefs.getInt("key", 0);   //0 is the default value
    }

    public void CheckScore() {
        if (score > Highscore) {
            SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("key", score);
            editor.commit();
        }
    }

    public void setupGame() {
        score = 0;
        //Typeface face=Typeface.createFromAsset(context.getAssets(),"fonts/manteka.ttf");
        //paint.setTypeface(face);
        paint.setColor(Color.BLACK);
        paint.setTextSize(80);
        HeightReached = 1920;
        Highscore = getHighscore();

        PreviousX = 0;
        PreviousY = 0;
        maxEnemies = 0;
        maxPlatforms = 5;
        currentColor = 1;
        maxColor = 3;
        color = new boolean[4];
        gameOver = false;
        color[1] = true;  // Blue
        color[2] = false; // Green
        color[3] = false; // Red

        jump = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        jumpsound = jump.load(context, R.raw.jump, 1);

        // board setup
        deathScreen = new DeathScreen(context);
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg1));
        player = new Player(BitmapFactory.decodeResource(context.getResources(), R.drawable.player), 0, 0);
        player = new Player(BitmapFactory.decodeResource(context.getResources(), R.drawable.player), GamePanel.WIDTH / 2 - player.width / 2, GamePanel.HEIGHT - player.height);

        platforms = new ArrayList<>();
        enemies = new ArrayList<>();

        // THE FLOOR
        platforms.add(createPlatform(context, 1080, 30, 0, 1920, 0, 0, 0, 1));
        // GHOST
        platforms.add(createPlatform(context, 1, 1, -5000, 1620, 0, 0, 0, 1));
        //Starting Platforms
        platforms.add(createPlatform(context, 350, 30, 365, GamePanel.HEIGHT - 600, 0, 0, 2, 3));
        platforms.add(createPlatform(context, 350, 30, 365, GamePanel.HEIGHT - 1000, 0, 0, 3, 3));
        platforms.add(createPlatform(context, 350, 30, 365, GamePanel.HEIGHT - 1400, 0, 0, 1, 3));
        platforms.add(createPlatform(context, 350, 30, 365, GamePanel.HEIGHT - 1800, 0, 0, 2, 3));

        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X = (int) event.getX();
        int Y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!player.isPlaying()) {
                player.setPlaying(true);  //THIS MAKES IT SO THE GAME DOESN'T START UNTIL IT IS CLICKED.
            } else {
                colorChange(X); // Changes Background Color
            }
            return true;
        }

        if (gameOver) {
            if (deathScreen.replayButton().contains(X, Y)) {
                gameOver = false;
                setupGame();
            } else if (deathScreen.mainmenuButton().contains(X, Y)) {
                gameOver = false;
                Intent intent = new Intent(context, MainMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void colorChange(int X) {
        if (X <= WIDTH / 2) {
            color[currentColor] = false;
            currentColor--;
            if (currentColor < 1) {
                currentColor = maxColor;
                color[maxColor] = true;
            } else {
                color[currentColor] = true;
            }
        } else {
            color[currentColor] = false;
            currentColor++;
            if (currentColor > maxColor) {
                currentColor = 1;
                color[1] = true;
            } else {
                color[currentColor] = true;
            }
        }
        if (color[1]) {
            bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg1));
        } else if (color[2]) {
            bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg2));
        } else if (color[3]) {
            bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg3));
        }
    }

    // ********** GENERATE FUNCTIONS **********

    public Platform generateRandomPlatform() {
        int w, h, xp, yp, xspeed, yspeed, color, size;

        if (score <= 750) {
            color = (int) (Math.random() * 4);
            w = 350;
            h = 30;
            size = 3;
            xp = (int) (Math.random() * 730);
            while (Math.abs(xp - PreviousX) < 200) {  // So that platforms are not in line with each other
                xp = (int) (Math.random() * 730);
            }
            yp = -50;
            xspeed = 0;
            yspeed = 0;
        } else if (750 < score && score <= 1500) {
            color = (int) (Math.random() * 4);
            w = 300;
            h = 30;
            size = 2;
            xp = (int) (Math.random() * 730);
            while (Math.abs(xp - PreviousX) < 150) {
                xp = (int) (Math.random() * 730);
            }
            yp = -50;
            if (xp % 2 == 0) {
                xspeed = 0;
            } else {
                xspeed = 2;
            }
            yspeed = 0;
        } else if (1500 < score && score <= 2250) {
            maxEnemies = 1;
            color = (int) (Math.random() * 4);
            w = 250;
            h = 30;
            size = 1;
            xp = (int) (Math.random() * 730);
            yp = (int) (Math.random() * -25);
            if (xp % 3 == 0) {
                xspeed = 0;
            } else {
                xspeed = (int) (Math.random() * 4) + 2;
            }
            yspeed = 0;
        } else {
            maxEnemies = 2;
            color = (int) (Math.random() * 4);
            w = 250;
            h = 30;
            size = 1;
            xp = (int) (Math.random() * 730);
            yp = (int) (Math.random() * -25);
            xspeed = (int) (Math.random() * 8);
            yspeed = (int) (Math.random() * 6);
        }

        PreviousX = xp;
        PreviousY = yp;
        return createPlatform(context, w, h, xp, yp, xspeed, yspeed, color, size);
    }

    public Enemy generateRandomEnemy() {
        int xspeed = (int) (Math.random() * 3 + 3);
        int yspeed = (int) (Math.random() * 3 + 3);
        int xp = (int) (Math.random() * 500);
        int yp = (int) (Math.random() * -1000);
        if (xspeed % 2 == 0)
            xspeed *= -1;
        return createEnemy(context, 50, 50, xp, yp, xspeed, yspeed, 0);
    }

    // ********** CREATE FUNCTIONS **********

    public Platform createPlatform(Context context, int width, int height, int x, int y, int xspeed, int yspeed, int color, int size) {
        if (color == 1) {
            if (size == 1)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.smallplatform1);
            if (size == 2)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.mediumplatform1);
            if (size == 3)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.largeplatform1);
        } else if (color == 2) {
            if (size == 1)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.smallplatform2);
            if (size == 2)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.mediumplatform2);
            if (size == 3)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.largeplatform2);
        } else if (color == 3) {
            if (size == 1)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.smallplatform3);
            if (size == 2)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.mediumplatform3);
            if (size == 3)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.largeplatform3);
        } else {
            if (size == 1)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.smallplatform0);
            if (size == 2)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.mediumplatform0);
            if (size == 3)
                platformBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.largeplatform0);
        }
        return new Platform(platformBase, width, height, x, y, xspeed, yspeed, color);
    }

    public Enemy createEnemy(Context context, int width, int height, int x, int y, int xSpeed, int ySpeed, int color) {
        enemyBase = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        return new Enemy(enemyBase, width, height, x, y, xSpeed, ySpeed, color);
    }


    // ********** Collision **********

    public boolean Collision(GameObject object1, GameObject object2) {
        if (object2.getBounds().intersects(object1.x, object1.y, object1.x + object1.width, object1.y + object1.height)) {
            double angle = Math.toDegrees(Math.atan2(object2.centery - object1.centery, object2.centerx - object1.centerx));
            if (angle < 0)
                angle += 360;
            if (object2.angle <= angle && angle <= 180 - object2.angle) { // Top Collision
                object1.y = object2.y - object1.height - 1;
                if (object1.isEnemy)
                    object1.dy *= -1;
                else
                    object1.playerSpeed = object1.gravity;
            } else if (180 - object2.angle < angle && angle < 180 + object2.angle) { // Right Collision
                object1.x = object2.x + object2.width + 1;
                if (object1.isEnemy)
                    object1.dx *= -1;
                else
                    object1.dx = 0;
            } else if (180 + object2.angle <= angle && angle <= 360 - object2.angle) { // Bot Collision
                object1.y = object2.y + object2.height + 1;
                if (object1.isEnemy)
                    object1.dy *= -1;
                else
                    object1.playerSpeed = 5;
            } else { // Left Collision
                object1.x = object2.x - object1.width - 1;
                if (object1.isEnemy)
                    object1.dx *= -1;
                else
                    object1.dx = 0;
            }
            return true;
        } else
            return false;
    }

    public void update() {
        if (player.isPlaying()) {
            scroll = player.scroll;
            scrollSpeed = player.playerSpeed;
            player.setNeutral(neutralZone);
            player.setTiltAngle(tiltAngle);

            // Player update
            player.update();
            if (!scroll)
                player.y += player.playerSpeed;
            player.playerSpeed += 1;
            player.setGravity(-37);

            //Enemy update
            if (enemies.size() < maxEnemies) {
                enemies.add(generateRandomEnemy());
            }
            for (int k = 0; k < enemies.size(); k++) {
                Enemy enemy = enemies.get(k);
                enemy.update(scroll, scrollSpeed);
                for (int j = 0; j < platforms.size(); j++) {
                    Platform platform = platforms.get(j);
                    if (platform.currentColor != currentColor) {
                        Collision(enemy, platform);
                    }
                }

                if (Collision(player, enemy)) {
                    player.setPlaying(false);
                    CheckScore();
                    gameOver = true;
                }
                if (enemy.y > HEIGHT * 2 || enemy.y < -HEIGHT) {
                    enemies.remove(enemy);
                }
            }

            // Platform update
            for (int k = 0; k < platforms.size(); k++) {
                Platform platform = platforms.get(k);

                platform.update(scroll, scrollSpeed);
                if (platform.currentColor != currentColor) {
                    if (Collision(player, platform) && player.y < platform.y) {
                        jump.play(jumpsound, 1.0f, 1.0f, 0, 0, 1.5f);
                    }
                }

                // If platform moves off bottom of screen, replace it
                if (platform.y > HEIGHT) {
                    platforms.remove(platform);
                }
                if (platforms.size() < maxPlatforms) {
                    platforms.add(generateRandomPlatform());
                }
            }

            //Score calculations

            if (player.y < HeightReached) {
                score++;
                HeightReached = player.y;
            }
            if (scroll) {
                score++;
            }

            //FOR GAME OVER.
            if (player.y > HEIGHT) {
                player.setPlaying(false);
                CheckScore();
                gameOver = true;
            }
        }
    }


    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            canvas.scale(scaleFactorX, scaleFactorY);

            final int savedState = canvas.save();
            bg.draw(canvas);

            //Draw Enemies
            for (int k = 0; k < enemies.size(); k++) {
                Enemy enemy = enemies.get(k);

                if (enemy.currentColor != currentColor)
                    enemy.draw(canvas);
            }

            //Draw Platforms
            for (int k = 0; k < platforms.size(); k++) {
                Platform platform = platforms.get(k);

                if (platform.currentColor != currentColor)
                    platform.draw(canvas);
            }

            player.draw(canvas);
            canvas.restoreToCount(savedState);

            //Draw Score
            String s = Integer.toString(score);
            String hs = Integer.toString(Highscore);
            canvas.drawText(s, 50, 100, paint);

            if (gameOver) {
                thread.setRunning(false);
                deathScreen.draw(canvas);
                paint.setColor(Color.WHITE);
                paint.setTextSize(105);
                canvas.drawText(s, 600, 555, paint);
                canvas.drawText(hs, 600, 705, paint);
            }
        }
    }
}
