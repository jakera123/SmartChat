package com.example.jakera.smartchat.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.SmartChatConstant;
import com.example.jakera.smartchat.Utils.SharePreferenceUtils;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by jakera on 18-2-9.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_exit_login;
    private TextView tv_title_bar_center, tv_userinfo_nickname, tv_userinfo_signature;
    private ImageView iv_user_info_back, iv_user_info_portrait;
    private RelativeLayout relate_userinfo_portrait, relate_userinfo_nickname, relate_userinfo_signature;

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
        tv_userinfo_nickname = (TextView) findViewById(R.id.tv_userinfo_nickname);
        tv_userinfo_nickname.setText(JMessageClient.getMyInfo().getNickname());
        tv_userinfo_signature = (TextView) findViewById(R.id.tv_userinfo_signature);
        //将signature设成性别
        tv_userinfo_signature.setText(JMessageClient.getMyInfo().getSignature());
        relate_userinfo_portrait = (RelativeLayout) findViewById(R.id.relate_userinfo_portrait);
        relate_userinfo_portrait.setOnClickListener(this);
        relate_userinfo_nickname = (RelativeLayout) findViewById(R.id.relate_userinfo_nickname);
        relate_userinfo_nickname.setOnClickListener(this);
        relate_userinfo_signature = (RelativeLayout) findViewById(R.id.relate_userinfo_signature);
        relate_userinfo_signature.setOnClickListener(this);
        iv_user_info_portrait = (ImageView) findViewById(R.id.iv_user_info_portrait);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JMessageClient.getMyInfo().getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                iv_user_info_portrait.setImageBitmap(bitmap);
            }
        });
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
            case R.id.relate_userinfo_nickname:
                View view = getLayoutInflater().inflate(R.layout.dialog_item_edittext, null);
                final EditText editText = (EditText) view.findViewById(R.id.et_dialog);
                AlertDialog dialog = new AlertDialog.Builder(UserInfoActivity.this)
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getResources().getString(R.string.modify_nickname))//设置对话框的标题
                        .setView(view)
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nickname = editText.getText().toString();
                                UserInfo userInfo = JMessageClient.getMyInfo();
                                userInfo.setNickname(nickname);
                                JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
                                            tv_userinfo_nickname.setText(JMessageClient.getMyInfo().getNickname());
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                break;
            case R.id.relate_userinfo_signature:
                View view2 = getLayoutInflater().inflate(R.layout.dialog_item_edittext, null);
                final EditText editText2 = (EditText) view2.findViewById(R.id.et_dialog);
                AlertDialog dialog2 = new AlertDialog.Builder(UserInfoActivity.this)
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getResources().getString(R.string.modify_signature))//设置对话框的标题
                        .setView(view2)
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String signature = editText2.getText().toString();
//                                if (!gender.equals(getString(R.string.male))&&!gender.equals(getString(R.string.female))){
//                                    Toast.makeText(UserInfoActivity.this,getString(R.string.check_gender), Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
                                UserInfo userInfo = JMessageClient.getMyInfo();
                                userInfo.setSignature(signature);
                                JMessageClient.updateMyInfo(UserInfo.Field.signature, userInfo, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
                                            tv_userinfo_signature.setText(JMessageClient.getMyInfo().getSignature());
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        }).create();
                dialog2.show();
        }
    }
}
