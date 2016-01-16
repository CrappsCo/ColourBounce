package com.colourbounce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


/**
 * Created by C on 2016-01-02.
 */
public class HowToPlay extends Activity {
    RelativeLayout Screen;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_play);
    }

    public void Back(View v){
        Button button = (Button) v;
        Intent myIntent = new Intent(HowToPlay.this, MainMenu.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        this.finish();
    }
}

