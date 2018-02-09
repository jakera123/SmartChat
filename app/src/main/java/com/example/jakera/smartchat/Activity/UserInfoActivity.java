package com.example.jakera.smartchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.SmartChatConstant;
import com.example.jakera.smartchat.Utils.SharePreferenceUtils;

/**
 * Created by jakera on 18-2-9.
 */

public class UserInfoActivity extends AppCompatActivity {
    private Button btn_exit_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        getSupportActionBar().hide();
        btn_exit_login = (Button) findViewById(R.id.btn_exit_login);
        btn_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtils.put(UserInfoActivity.this, SmartChatConstant.SPISLOGINKEY, false);
                Intent intent = new Intent(UserInfoActivity.this, RegisterLoginActivity.class);
                startActivity(intent);
                UserInfoActivity.this.finish();
            }
        });


    }
}
