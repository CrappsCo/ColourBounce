package com.colourbounce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by C on 2015-05-29.
 */
public class MainMenu extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }

    public void Play(View v){
        Button button = (Button) v;
        Intent myIntent = new Intent(MainMenu.this, Game.class);
        startActivity(myIntent);
    }

    public void HowToPlay(View v){
        Button button = (Button) v;
        Intent myIntent = new Intent(MainMenu.this, HowToPlay.class);
        startActivity(myIntent);
    }
}
