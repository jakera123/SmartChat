package com.example.jakera.smartchat.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jakera.smartchat.Entry.AnimalOrPlant;
import com.example.jakera.smartchat.Entry.Car;
import com.example.jakera.smartchat.Entry.Dish;
import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.Utils.RecognizerHelper;
import com.example.jakera.smartchat.Views.CircleImageView;
import com.example.jakera.smartchat.Views.LoadingDialog;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jakera on 18-3-9.
 */

public class RecognizerActivity extends AppCompatActivity {

    private String TAG = "RecognizerActivity";

    private TextView tv_title_bar_center, tv_recognize_result;
    private ImageView iv_user_info_back, iv_car_picture;
    private CircleImageView btn_civ_take_photo;

    private Car carResult;
    private AnimalOrPlant animalOrPlantResult;
    private Dish dishResult;

    private int REQUEST_IMAGE_CAMERA = 1;

    private int actitityType;

    private LoadingDialog loadingDialog;


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
        setContentView(R.layout.activity_recognizer);
        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra("data");
        actitityType = data.getInt(RecognizerHelper.type);

        initView();


    }

    public void initView() {
        tv_title_bar_center = (TextView) findViewById(R.id.tv_title_bar_center);
        switch (actitityType) {
            case RecognizerHelper.CAR:
                tv_title_bar_center.setText(getString(R.string.recognize_car));
                break;
            case RecognizerHelper.ANIMAL:
                tv_title_bar_center.setText(getString(R.string.recognize_animal));
                break;
            case RecognizerHelper.PLANT:
                tv_title_bar_center.setText(getString(R.string.recognize_plant));
                break;
            case RecognizerHelper.FOOD:
                tv_title_bar_center.setText(getString(R.string.recognize_food));
                break;
        }
        iv_user_info_back = (ImageView) findViewById(R.id.iv_title_bar_back);
        iv_user_info_back.setVisibility(View.VISIBLE);
        iv_user_info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_car_picture = (ImageView) findViewById(R.id.iv_car_picture);
        btn_civ_take_photo = (CircleImageView) findViewById(R.id.btn_civ_take_photo);
        btn_civ_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //记得注册调用摄像头权限，打log可以看出原因
                //运行时权限
                startActivityForResult(intent1, REQUEST_IMAGE_CAMERA);
            }
        });
        tv_recognize_result = (TextView) findViewById(R.id.tv_recognize_result);

        loadingDialog = new LoadingDialog(RecognizerActivity.this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setTextContent(getString(R.string.recognizing));

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadingDialog.show();

        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();

        if (requestCode == REQUEST_IMAGE_CAMERA) {
            //从相册获取uri
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            //将获取的图片暂时保存起来
            Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "recognize.jpg"));
            if (mSaveUri != null) {
                OutputStream outputStream = null;
                try {
                    outputStream = getContentResolver().openOutputStream(mSaveUri);
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    }
                    iv_car_picture.setImageBitmap(bitmap);

                    //开始识别
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            switch (actitityType) {
                                case RecognizerHelper.CAR:
                                    RecognizerHelper.carRecognize(getCacheDir() + "/recognize.jpg", new RecognizerHelper.CarRecognizeListener() {
                                        @Override
                                        public void onSuccess(final Car car) {
                                            loadingDialog.dismiss();
                                            carResult = car;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    tv_recognize_result.setText("你所搜索的车\n" + (int) (carResult.result.get(0).score * 100) + "%的可能是:" + car.result.get(0).name + "\n生产于:" + car.result.get(0).year + "\n颜色：" + car.color_result);
                                                }
                                            });
                                        }
                                    });
                                    break;
                                case RecognizerHelper.ANIMAL:
                                    RecognizerHelper.animalRecognize(getCacheDir() + "/recognize.jpg", new RecognizerHelper.AnimalOrPlantRecognizeListener() {
                                        @Override
                                        public void onSuccess(final AnimalOrPlant animalOrPlant) {
                                            loadingDialog.dismiss();
                                            animalOrPlantResult = animalOrPlant;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    tv_recognize_result.setText("你所搜索的动物\n" + (int) (Float.parseFloat(animalOrPlantResult.result.get(0).score) * 100) + "%的可能是:" + animalOrPlant.result.get(0).name);
                                                }
                                            });
                                        }
                                    });
                                    break;
                                case RecognizerHelper.PLANT:
                                    RecognizerHelper.plantRecognize(getCacheDir() + "/recognize.jpg", new RecognizerHelper.AnimalOrPlantRecognizeListener() {
                                        @Override
                                        public void onSuccess(final AnimalOrPlant animalOrPlant) {
                                            loadingDialog.dismiss();
                                            animalOrPlantResult = animalOrPlant;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    tv_recognize_result.setText("你所搜索的植物\n" + (int) (Float.parseFloat(animalOrPlantResult.result.get(0).score) * 100) + "%的可能是:" + animalOrPlant.result.get(0).name);
                                                }
                                            });
                                        }
                                    });
                                    break;
                                case RecognizerHelper.FOOD:
                                    RecognizerHelper.dishRecognize(getCacheDir() + "/recognize.jpg", new RecognizerHelper.DishRecognizeListener() {
                                        @Override
                                        public void onSuccess(Dish dish) {
                                            loadingDialog.dismiss();
                                            dishResult = dish;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    tv_recognize_result.setText("你所搜索的菜品\n" + (int) (Float.parseFloat(dishResult.result.get(0).probability) * 100) + "%的可能是:" + dishResult.result.get(0).name + "\n拥有卡路里：" + dishResult.result.get(0).calorie);
                                                }
                                            });
                                        }
                                    });
                                    break;
                            }

                        }
                    }).start();


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

            }
        }
    }


}
