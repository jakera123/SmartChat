package com.example.jakera.smartchat.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.SmartChatApp;
import com.example.jakera.smartchat.SmartChatConstant;
import com.example.jakera.smartchat.Utils.SharePreferenceUtils;
import com.example.jakera.smartchat.Views.LoadingDialog;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by jakera on 18-2-7.
 */

public class RegisterLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_tab_register, ll_tab_login;
    private TextView tv_tab_register, tv_tab_login;
    private View indicate_register, indicate_login;
    private EditText et_username, et_password, et_check_password;
    private TextView tv_register_announce;
    private SpannableString registerSpannableString, loginSpannableString;
    private Button btn_regist_login_commit;
    private boolean isRegister = true;
    private LoadingDialog loadingDialog;

    private String TAG = "RegisterLosin";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(0xc93688f2);

        setContentView(R.layout.activity_register_login);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();


    }

    private void initView() {
        ll_tab_register = (LinearLayout) findViewById(R.id.ll_tab_register);
        ll_tab_register.setOnClickListener(this);
        ll_tab_login = (LinearLayout) findViewById(R.id.ll_tab_login);
        ll_tab_login.setOnClickListener(this);
        tv_tab_register = (TextView) findViewById(R.id.tv_tab_register);
        tv_tab_login = (TextView) findViewById(R.id.tv_tab_login);
        indicate_register = findViewById(R.id.indicate_register);
        indicate_login = findViewById(R.id.indicate_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_check_password = (EditText) findViewById(R.id.et_check_password);
        tv_register_announce = (TextView) findViewById(R.id.tv_register_announce);

        //使用占位符，及设置一个textView不同的颜色
        registerSpannableString = new SpannableString(getString(R.string.announce, getString(R.string.register)));
        registerSpannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#c93688f2")), 10, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerSpannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#c93688f2")), 17, registerSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_register_announce.setText(registerSpannableString);

        loginSpannableString = new SpannableString(getString(R.string.announce, getString(R.string.login)));
        loginSpannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#c93688f2")), 10, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginSpannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#c93688f2")), 17, registerSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        btn_regist_login_commit = (Button) findViewById(R.id.btn_regist_login_commit);
        btn_regist_login_commit.setOnClickListener(this);

        loadingDialog = new LoadingDialog(RegisterLoginActivity.this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setTextContent(getString(R.string.login_now));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tab_register:
                tv_tab_register.setTextColor(0xc93688f2);
                indicate_register.setBackgroundColor(0xc93688f2);
                tv_tab_login.setTextColor(0x79434951);
                indicate_login.setBackgroundColor(0x79434951);
                et_check_password.setVisibility(View.VISIBLE);
                tv_register_announce.setText(registerSpannableString);
                isRegister = true;
                break;
            case R.id.ll_tab_login:
                tv_tab_register.setTextColor(0x79434951);
                indicate_register.setBackgroundColor(0x79434951);
                tv_tab_login.setTextColor(0xc93688f2);
                indicate_login.setBackgroundColor(0xc93688f2);
                et_check_password.setVisibility(View.GONE);
                tv_register_announce.setText(loginSpannableString);
                isRegister = false;
                break;
            case R.id.btn_regist_login_commit:
                if (et_username.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.tip_username_null), Toast.LENGTH_SHORT).show();
                    break;
                }
                if (et_password.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.tip_password_null), Toast.LENGTH_SHORT).show();
                    break;
                }
                if (isRegister) {
                    if (et_check_password.getText().toString().isEmpty()) {
                        Toast.makeText(this, getString(R.string.tip_password_null), Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        if (et_password.getText().toString().equals(et_check_password.getText().toString())) {

                            loadingDialog.show();
                            //执行注册逻辑
                            JMessageClient.register(et_username.getText().toString(), et_password.getText().toString(), new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    loadingDialog.dismiss();
                                    if (i == 0) {
                                        Toast.makeText(RegisterLoginActivity.this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                                        JMessageClient.login(et_username.getText().toString(), et_password.getText().toString(), new BasicCallback() {
                                            @Override
                                            public void gotResult(int i, String s) {
                                                loadingDialog.dismiss();
                                                if (i == 801004) {
                                                    Toast.makeText(RegisterLoginActivity.this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
                                                } else if (i == 0) {
                                                    SharePreferenceUtils.put(RegisterLoginActivity.this, SmartChatConstant.SPISLOGINKEY, true);
                                                    SharePreferenceUtils.put(RegisterLoginActivity.this, SmartChatConstant.SPUSERNAME, JMessageClient.getMyInfo().getUserName());
                                                    SmartChatApp.USERNAME = JMessageClient.getMyInfo().getUserName();
                                                    Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    RegisterLoginActivity.this.finish();
                                                } else {
                                                    Toast.makeText(RegisterLoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else if (i == 898001) {
                                        Toast.makeText(RegisterLoginActivity.this, getString(R.string.register_already), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterLoginActivity.this, getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            break;
                        } else {
                            Toast.makeText(this, getString(R.string.tip_password_not_consistent), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } else {
                    loadingDialog.show();
                    JMessageClient.login(et_username.getText().toString(), et_password.getText().toString(), new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            loadingDialog.dismiss();
                            if (i == 801004) {
                                Toast.makeText(RegisterLoginActivity.this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
                            } else if (i == 0) {
                                SharePreferenceUtils.put(RegisterLoginActivity.this, SmartChatConstant.SPISLOGINKEY, true);
                                SharePreferenceUtils.put(RegisterLoginActivity.this, SmartChatConstant.SPUSERNAME, JMessageClient.getMyInfo().getUserName());
                                Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                RegisterLoginActivity.this.finish();
                            } else {
                                Toast.makeText(RegisterLoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    break;
                }
        }
    }
}
