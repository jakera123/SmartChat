package com.example.jakera.smartchat.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.SmartChatConstant;
import com.example.jakera.smartchat.Utils.SharePreferenceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by jakera on 18-2-9.
 */

public class ModifyPortraitActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_title_bar_center;
    private ImageView iv_user_info_back, iv_title_bar_more;
    private TextView tv_bottom_bar_take_photo, tv_bottom_bar_select_gallery, tv_bottom_bar_save_photo, tv_bottom_bar_cacel;

    private Dialog bottomDialog;
    private String TAG = "ModifyPortraitActivity";
    private int REQUEST_IMAGE_CAPTURE = 1;
    private int REQUEST_IMAGE_CAMERA = 2;

    private ImageView iv_modify_user_portrait;

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
        setContentView(R.layout.acitivity_modify_portrait);
        initView();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }



        Intent intent = getIntent();
        String state = intent.getStringExtra("portrait");
        if (state != null && state.equals("sucess")) {

            //一开始报null对象，原来是imageview没有初始化.....
            iv_modify_user_portrait.setImageURI(Uri.fromFile(new File(getCacheDir(), "user_portrait.jpg")));
            JMessageClient.updateUserAvatar(new File(getCacheDir(), "user_portrait.jpg"), new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        Toast.makeText(ModifyPortraitActivity.this, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    public void initView() {
        tv_title_bar_center = (TextView) findViewById(R.id.tv_title_bar_center);
        tv_title_bar_center.setText(getString(R.string.modify_portrait));
        iv_user_info_back = (ImageView) findViewById(R.id.iv_title_bar_back);
        iv_user_info_back.setVisibility(View.VISIBLE);
        iv_user_info_back.setOnClickListener(this);
        iv_title_bar_more = (ImageView) findViewById(R.id.iv_title_bar_more);
        iv_title_bar_more.setVisibility(View.VISIBLE);
        iv_title_bar_more.setOnClickListener(this);
        iv_modify_user_portrait = (ImageView) findViewById(R.id.iv_modify_user_portrait);
        JMessageClient.getMyInfo().getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                iv_modify_user_portrait.setImageBitmap(bitmap);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_bar_back:
                ModifyPortraitActivity.this.onBackPressed();
                break;
            case R.id.iv_title_bar_more:
                showBottonBar();
                break;
            case R.id.tv_bottom_bar_take_photo:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //记得注册调用摄像头权限，打log可以看出原因
                //运行时权限
                startActivityForResult(intent1, REQUEST_IMAGE_CAMERA);
                bottomDialog.dismiss();
                break;
            case R.id.tv_bottom_bar_select_gallery:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                bottomDialog.dismiss();
                break;
            case R.id.tv_bottom_bar_save_photo:
                break;
            case R.id.tv_bottom_bar_cacel:
                bottomDialog.dismiss();
                break;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Log.e(TAG, "ActivityResult resultCode error");
            return;
        }

        Bitmap bm = null;

        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();

        //此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Uri originalUri = data.getData();        //获得图片的uri
            //文件过大，不便传输,传输其uri即可
            Bundle bundle = new Bundle();
            bundle.putParcelable("bitmapUri", originalUri);

            Intent intent = new Intent(ModifyPortraitActivity.this, CropPortraitAcitivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (requestCode == REQUEST_IMAGE_CAMERA) {
            //从相册获取uri
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            //将获取的图片暂时保存起来
            Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "user_portrait_temp.jpg"));
            if (mSaveUri != null) {
                OutputStream outputStream = null;
                try {
                    outputStream = getContentResolver().openOutputStream(mSaveUri);
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    }
                } catch (IOException ex) {
                    Log.e("android", "Cannot open file: " + mSaveUri, ex);
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //文件过大，不便传输,传输其uri即可
                Bundle bundle1 = new Bundle();
                bundle.putParcelable("bitmapUri", mSaveUri);

                Intent intent = new Intent(ModifyPortraitActivity.this, CropPortraitAcitivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);


            }

        }
        //防止创建过多的Activity
        finish();

    }


    private void showBottonBar() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.portrait_bottom_bar, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        tv_bottom_bar_take_photo = (TextView) contentView.findViewById(R.id.tv_bottom_bar_take_photo);
        tv_bottom_bar_take_photo.setOnClickListener(this);
        tv_bottom_bar_select_gallery = (TextView) contentView.findViewById(R.id.tv_bottom_bar_select_gallery);
        tv_bottom_bar_select_gallery.setOnClickListener(this);
        tv_bottom_bar_save_photo = (TextView) contentView.findViewById(R.id.tv_bottom_bar_save_photo);
        tv_bottom_bar_save_photo.setOnClickListener(this);
        tv_bottom_bar_cacel = (TextView) contentView.findViewById(R.id.tv_bottom_bar_cacel);
        tv_bottom_bar_cacel.setOnClickListener(this);

        bottomDialog.show();
    }
}
