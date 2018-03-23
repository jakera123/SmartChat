package com.example.jakera.smartchat.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakera.smartchat.Activity.ContactAuthorActivity;
import com.example.jakera.smartchat.Activity.RecognizerActivity;
import com.example.jakera.smartchat.Activity.UserInfoActivity;
import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.Utils.RecognizerHelper;
import com.example.jakera.smartchat.Views.CircleImageView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by jakera on 18-1-25.
 */

public class UserInfoFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_modify_user_info;
    private TextView tv_userinfo_username, tv_fregment_userinfo_signature, tv_recognize_car, tv_recognize_animal, tv_recognize_plant, tv_recognize_food, tv_contact_author;
    private CircleImageView civ_user_portrait;
    private String TAG = "UserInfoFragment";
    private View layout;
    private UserInfo userInfo;
    private LoadUserInfo loadUserInfo;
    public UserInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.user_info_fragment, container, false);
        initView();
        return layout;
    }


    public void initView() {
        iv_modify_user_info = (ImageView) layout.findViewById(R.id.iv_modify_user_info);
        iv_modify_user_info.setOnClickListener(this);
        tv_userinfo_username = (TextView) layout.findViewById(R.id.tv_userinfo_username);
        civ_user_portrait = (CircleImageView) layout.findViewById(R.id.civ_user_portrait);
        loadUserInfo = new LoadUserInfo();
        loadUserInfo.start();
        tv_fregment_userinfo_signature = (TextView) layout.findViewById(R.id.tv_fregment_userinfo_signature);
        tv_recognize_car = (TextView) layout.findViewById(R.id.tv_recognize_car);
        tv_recognize_car.setOnClickListener(this);
        tv_recognize_animal = (TextView) layout.findViewById(R.id.tv_recognize_animal);
        tv_recognize_animal.setOnClickListener(this);
        tv_recognize_plant = (TextView) layout.findViewById(R.id.tv_recognize_plant);
        tv_recognize_plant.setOnClickListener(this);
        tv_recognize_food = (TextView) layout.findViewById(R.id.tv_recognize_food);
        tv_recognize_food.setOnClickListener(this);
        tv_contact_author = (TextView) layout.findViewById(R.id.tv_contact_author);
        tv_contact_author.setOnClickListener(this);
//        if (userInfo!=null){
//            tv_userinfo_username.setText(userInfo.getUserName());
//        }
        // ((MainActivity)getActivity()).setTitlebar(this);
    }


    @Override
    public void onClick(View v) {
        Bundle data = new Bundle();
        switch (v.getId()) {
            case R.id.iv_modify_user_info:
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_recognize_car:
                Intent intent1 = new Intent(getActivity(), RecognizerActivity.class);
                data.putInt(RecognizerHelper.type, RecognizerHelper.CAR);
                intent1.putExtra("data", data);
                startActivity(intent1);
                break;
            case R.id.tv_recognize_animal:
                Intent intent2 = new Intent(getActivity(), RecognizerActivity.class);
                data.putInt(RecognizerHelper.type, RecognizerHelper.ANIMAL);
                intent2.putExtra("data", data);
                startActivity(intent2);
                break;
            case R.id.tv_recognize_plant:
                Intent intent3 = new Intent(getActivity(), RecognizerActivity.class);
                data.putInt(RecognizerHelper.type, RecognizerHelper.PLANT);
                intent3.putExtra("data", data);
                startActivity(intent3);
                break;
            case R.id.tv_recognize_food:
                Intent intent4 = new Intent(getActivity(), RecognizerActivity.class);
                data.putInt(RecognizerHelper.type, RecognizerHelper.FOOD);
                intent4.putExtra("data", data);
                startActivity(intent4);
                break;
            case R.id.tv_contact_author:
                Intent intent5 = new Intent(getActivity(), ContactAuthorActivity.class);
                startActivity(intent5);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    class LoadUserInfo extends Thread {
        @Override
        public void run() {
            while (userInfo == null) {
                userInfo = JMessageClient.getMyInfo();
                try {
                    Thread.sleep(1000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (userInfo == null) {
                                Toast.makeText(getContext(), getResources().getString(R.string.fail_get_userinfo), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (userInfo != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_userinfo_username.setText(userInfo.getUserName() + ":" + userInfo.getNickname());
                        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                            @Override
                            public void gotResult(int i, String s, Bitmap bitmap) {
                                civ_user_portrait.setImageBitmap(bitmap);
                            }
                        });
                        tv_fregment_userinfo_signature.setText(userInfo.getSignature());

                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
