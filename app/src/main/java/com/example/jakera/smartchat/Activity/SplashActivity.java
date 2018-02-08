package com.example.jakera.smartchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.jakera.smartchat.R;

/**
 * Created by jakera on 18-2-8.
 */

public class SplashActivity extends AppCompatActivity {
    private TextView tv_count_time;
    private Handler handler;
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_count_time = (TextView) findViewById(R.id.tv_count_time);
        myCountDownTimer = new MyCountDownTimer(5000, 1000);
        myCountDownTimer.start();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, RegisterLogin.class);
                startActivity(intent);
            }
        }, 5000);


    }

    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_count_time.setText(getString(R.string.jumping));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_count_time.setText("倒计时(" + millisUntilFinished / 1000 + "秒)");
        }
    }


}
