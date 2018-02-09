package com.example.jakera.smartchat.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.SmartChatConstant;
import com.example.jakera.smartchat.Utils.SharePreferenceUtils;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;

/**
 * Created by jakera on 18-2-9.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_exit_login;
    private TextView tv_title_bar_center;
    private ImageView iv_user_info_back;
    private RelativeLayout relate_userinfo_portrait;

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
        setContentView(R.layout.activity_userinfo);
        getSupportActionBar().hide();

        initView();

    }

    public void initView() {
        btn_exit_login = (Button) findViewById(R.id.btn_exit_login);
        btn_exit_login.setOnClickListener(this);
        tv_title_bar_center = (TextView) findViewById(R.id.tv_title_bar_center);
        tv_title_bar_center.setText(getString(R.string.person_info));
        iv_user_info_back = (ImageView) findViewById(R.id.iv_title_bar_back);
        iv_user_info_back.setVisibility(View.VISIBLE);
        iv_user_info_back.setOnClickListener(this);
        relate_userinfo_portrait = (RelativeLayout) findViewById(R.id.relate_userinfo_portrait);
        relate_userinfo_portrait.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_login:
                JMessageClient.logout();
                SharePreferenceUtils.put(UserInfoActivity.this, SmartChatConstant.SPISLOGINKEY, false);
                Intent intent = new Intent(UserInfoActivity.this, RegisterLoginActivity.class);
                startActivity(intent);
                UserInfoActivity.this.finish();
                break;
            case R.id.iv_title_bar_back:
                UserInfoActivity.this.onBackPressed();
                break;
            case R.id.relate_userinfo_portrait:
                Intent intent1 = new Intent(UserInfoActivity.this, ModifyPortraitActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
