package com.example.jakera.smartchat.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jakera.smartchat.Activity.MainActivity;
import com.example.jakera.smartchat.Activity.UserInfoActivity;
import com.example.jakera.smartchat.R;

/**
 * Created by jakera on 18-1-25.
 */

public class UserInfoFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_modify_user_info;
    public UserInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.user_info_fragment,container,false);
        iv_modify_user_info = (ImageView) view.findViewById(R.id.iv_modify_user_info);
        iv_modify_user_info.setOnClickListener(this);
        return view;
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
