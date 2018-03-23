package com.example.jakera.smartchat.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jakera.smartchat.Adapter.ContactAuthorViewPagerAdapter;
import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.Utils.RecognizerHelper;

import java.util.ArrayList;

/**
 * Created by jakera on 18-3-23.
 */

public class ContactAuthorActivity extends AppCompatActivity {

    int[] imageResIds = new int[]{R.drawable.wyu1, R.drawable.wyu2, R.drawable.wyu3, R.drawable.wyu4};

    private ViewPager viewPager;

    private ImageView iv_user_info_back;

    private TextView tv_title_bar_center;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.BLACK);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_contact_author);

        init();


    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.viewpager_contact_author);
        viewPager.setAdapter(new ContactAuthorViewPagerAdapter(this, imageResIds));

        iv_user_info_back = (ImageView) findViewById(R.id.iv_title_bar_back);
        iv_user_info_back.setVisibility(View.VISIBLE);
        iv_user_info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_title_bar_center = (TextView) findViewById(R.id.tv_title_bar_center);
        tv_title_bar_center.setText(getString(R.string.contact_author));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % imageResIds.length);
                        }
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }


}
