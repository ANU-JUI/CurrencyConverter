package com.example.currencyconverter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class splash extends AppCompatActivity {
    MediaPlayer sound;
    TextView txtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        txtn=findViewById(R.id.txt);
        Intent  inext=new Intent(splash.this, MainActivity.class);
        //for delaying of splash screen by 4s
        Animation aanim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splash_screen);
        txtn.startAnimation(aanim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sound=MediaPlayer.create(getApplicationContext(),R.raw.entry);
                sound.start();
                startActivity(inext);
                finish();
            }
        }, 6000);
    }
}