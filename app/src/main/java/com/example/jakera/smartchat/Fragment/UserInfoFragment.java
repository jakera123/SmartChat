package com.example.jakera.smartchat.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jakera.smartchat.Activity.MainActivity;
import com.example.jakera.smartchat.Activity.UserInfoActivity;
import com.example.jakera.smartchat.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by jakera on 18-1-25.
 */

public class UserInfoFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_modify_user_info;
    private TextView tv_userinfo_username;
    private String TAG = "UserInfoFragment";
    private View layout;
    private UserInfo userInfo;
    public UserInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.user_info_fragment, container, false);
        userInfo = JMessageClient.getMyInfo();
        initView();
        return layout;
    }


    public void initView() {
        iv_modify_user_info = (ImageView) layout.findViewById(R.id.iv_modify_user_info);
        iv_modify_user_info.setOnClickListener(this);
        tv_userinfo_username = (TextView) layout.findViewById(R.id.tv_userinfo_username);
        tv_userinfo_username.setText(userInfo.getUserName());
        // ((MainActivity)getActivity()).setTitlebar(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_modify_user_info:
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;


        }

    }
}
