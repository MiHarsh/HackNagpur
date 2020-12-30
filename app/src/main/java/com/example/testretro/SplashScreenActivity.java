package com.example.testretro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    //private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        //getSupportActionBar().hide();
        Splash ob=new Splash();
        ob.start();
        //mAuth= FirebaseAuth.getInstance();
        //mAuth.signInWithEmailAndPassword("withoutloginuser@gmail.com","LoginFast");


    }
    private class Splash extends Thread
    {
        public void run()
        {
            try
            {
                sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
            SplashScreenActivity.this.finish();
        }
    }
}

