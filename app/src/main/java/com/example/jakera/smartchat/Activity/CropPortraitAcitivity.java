package com.example.jakera.smartchat.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.Views.ClipViewLayout;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jakera on 18-2-24.
 */

public class CropPortraitAcitivity extends AppCompatActivity {

    //  private ImageView iv_crop_portrait;
    private ClipViewLayout clipViewLayout;
    private TextView tv_cro_portrait_ok, tv_cro_portrait_cancel;

    private String TAG = "CropPortraitActivity";


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
        setContentView(R.layout.activity_crop_portrait);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

        //  iv_crop_portrait=(ImageView)findViewById(R.id.iv_crop_portrait);
        clipViewLayout = (ClipViewLayout) findViewById(R.id.clipView_layout);
        tv_cro_portrait_ok = (TextView) findViewById(R.id.tv_cro_portrait_ok);
        tv_cro_portrait_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateUriAndReturn();
            }
        });
        tv_cro_portrait_cancel = (TextView) findViewById(R.id.tv_cro_portrait_cancel);
        tv_cro_portrait_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {

            //Bitmap过大，不能用Bundle进行传输，故使用Uri来代替
            Bundle bundle = intent.getExtras();
            Uri bmpUri = (Uri) bundle.getParcelable("bitmapUri");
            if (bmpUri == null) {
                //由于保存图片需要时间，所以，直接在新的Activity里进行读取
                bmpUri = Uri.fromFile(new File(getCacheDir(), "user_portrait_temp.jpg"));
            }
            clipViewLayout.setImageSrc(bmpUri);
        }

    }


    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private void generateUriAndReturn() {
        //调用返回剪切图
        Bitmap zoomedCropBitmap;
//        if (type == 1) {
//            zoomedCropBitmap = clipViewLayout1.clip();
//        } else {
        zoomedCropBitmap = clipViewLayout.clip();
//        }
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        // /data/user/0/com.example.jakera.smartchat/cache
        //Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));
        Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "user_portrait.jpg"));
        Log.i(TAG, getCacheDir().getAbsolutePath());
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
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
            Intent intent = new Intent(CropPortraitAcitivity.this, ModifyPortraitActivity.class);
            intent.putExtra("portrait", "sucess");
            startActivity(intent);
            finish();
        }
    }

}
