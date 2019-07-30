package br.com.icaro.agende.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import br.com.icaro.agende.R;


public class SplashScreenActivity extends AppCompatActivity {


    public static final int TIME_SPLASH_SCREEN = 2000;
    private ImageView logoSplashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logoSplashScreen = findViewById(R.id.logo_splash_screen);

        Animation animationTransition = AnimationUtils.loadAnimation(this, R.anim.animation_transition);
        logoSplashScreen.startAnimation(animationTransition);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_SPLASH_SCREEN);

    }

}
