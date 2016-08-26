package com.example.johnnie.ottawainfo.welcome;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.johnnie.ottawainfo.R;
import com.example.johnnie.ottawainfo.ottawainfo.MainActivity;

public class SplashScreen extends AppCompatActivity {

    Thread splashThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        startAnim();

    }


    public void startAnim() {
        RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.welcome_layout);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        anim.reset();
        linearLayout.clearAnimation();
        linearLayout.setAnimation(anim);


        ImageView imageView = (ImageView) findViewById(R.id.welcome_imageView);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_anim);
        anim.reset();
        imageView.clearAnimation();
        imageView.setAnimation(anim);

        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;

                    while (waited < 3000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashScreen.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SplashScreen.this.finish();
                }
            }
        };
             splashThread.start();

//        anim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
    }
}
