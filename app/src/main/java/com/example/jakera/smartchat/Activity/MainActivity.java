package com.example.jakera.smartchat.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakera.smartchat.Fragment.FriendsListFragment;
import com.example.jakera.smartchat.Fragment.MessageListFragment;
import com.example.jakera.smartchat.Fragment.SmartChatFragmentAdapter;
import com.example.jakera.smartchat.Fragment.UserInfoFragment;
import com.example.jakera.smartchat.Fragment.WatchMoreFragment;
import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.SmartChatConstant;
import com.example.jakera.smartchat.Utils.AudioRecorderUtil;
import com.example.jakera.smartchat.Utils.RecognizerHelper;
import com.example.jakera.smartchat.Utils.SpeechSynthesizerUtil;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener,RecognizerDialogListener{



    //http://www.runoob.com/w3cnote/android-tutorial-fragment-demo4.html


    private String TAG="MainActivity";

    private RadioGroup rg_foot_bar;
    private RadioButton rb_foot_btn01,rb_foot_btn02,rb_foot_btn03,rb_foot_btn04;
    private ViewPager mainViewPager;
    private ImageView btn_voice;

    private SmartChatFragmentAdapter mSmartChatFragmentAdapter;

    private List<RadioButton> radioButtons;


    private SpeechRecognizer speechRecognizer;
    private RecognizerHelper recognizerHelper;
    private RecognizerDialog mReconizerDialog;

    private RelativeLayout ralat_layout_title_bar;
    private TextView tv_title_bar_center;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.BLACK);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
       //请注意，录音为危险权限，所以在android 6.0以上版本的手机上要进行动态申请.使用前可先做权限判断
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1);
        }

        speechRecognizer=SpeechRecognizer.createRecognizer(this,null);
        mReconizerDialog=new RecognizerDialog(this,null);
        recognizerHelper=new RecognizerHelper(this,speechRecognizer,mReconizerDialog);
        recognizerHelper.setListener(this);


        mSmartChatFragmentAdapter=new SmartChatFragmentAdapter(getSupportFragmentManager());
        initView();
    }

    private void initView() {

        ralat_layout_title_bar = (RelativeLayout) findViewById(R.id.ralat_main_title_bar);
        tv_title_bar_center = (TextView) findViewById(R.id.tv_title_bar_center);

        rg_foot_bar=(RadioGroup)findViewById(R.id.rg_foot_bar);
        rb_foot_btn01=(RadioButton)findViewById(R.id.rb_foot_btn01);
        rb_foot_btn02=(RadioButton)findViewById(R.id.rb_foot_btn02);
        rb_foot_btn03=(RadioButton)findViewById(R.id.rb_foot_btn03);
        rb_foot_btn04=(RadioButton)findViewById(R.id.rb_foot_btn04);

        radioButtons=new ArrayList<>(4);
        radioButtons.add(rb_foot_btn01);
        radioButtons.add(rb_foot_btn02);
        radioButtons.add(rb_foot_btn03);
        radioButtons.add(rb_foot_btn04);

        mainViewPager=(ViewPager)findViewById(R.id.mainViewPager);
        mainViewPager.setAdapter(mSmartChatFragmentAdapter);
        mainViewPager.setCurrentItem(0);
        mainViewPager.addOnPageChangeListener(this);

        rg_foot_bar.setOnCheckedChangeListener(this);


        btn_voice=(ImageView)findViewById(R.id.btn_voice);
        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizerHelper.startSpeechRecognizer();
            }
        });

        setTitlebar(SmartChatConstant.PAGE_ONE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Toast.makeText(this,"congratulations,your application granted sucess...",Toast.LENGTH_LONG).show();

                }
                return;
            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {

            switch (mainViewPager.getCurrentItem()){

                case SmartChatConstant.PAGE_ONE:
                    rb_foot_btn01.setChecked(true);
                    changeRadioButtonState(SmartChatConstant.PAGE_ONE);
                    setTitlebar(SmartChatConstant.PAGE_ONE);
                    break;
                case SmartChatConstant.PAGE_TWO:
                    rb_foot_btn02.setChecked(true);
                    rb_foot_btn02.setButtonTintList(ColorStateList.valueOf(Color.YELLOW));
                    changeRadioButtonState(SmartChatConstant.PAGE_TWO);
                    setTitlebar(SmartChatConstant.PAGE_TWO);
                    break;
                case SmartChatConstant.PAGE_THREE:
                    rb_foot_btn03.setChecked(true);
                    rb_foot_btn03.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                    changeRadioButtonState(SmartChatConstant.PAGE_THREE);
                    setTitlebar(SmartChatConstant.PAGE_THREE);
                    break;
                case SmartChatConstant.PAGE_FOUR:
                    rb_foot_btn04.setChecked(true);
                    rb_foot_btn04.setForegroundTintList(ColorStateList.valueOf(Color.YELLOW));
                    changeRadioButtonState(SmartChatConstant.PAGE_FOUR);
//                    recognizerHelper.recognizeStream(Environment.getExternalStorageDirectory() + "/smart_chat_recorder_audios/yinfu.pcm");
                    setTitlebar(SmartChatConstant.PAGE_FOUR);
                    break;
            }



        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_foot_btn01:
                mainViewPager.setCurrentItem(SmartChatConstant.PAGE_ONE);
                changeRadioButtonState(SmartChatConstant.PAGE_ONE);
                break;
            case R.id.rb_foot_btn02:
                mainViewPager.setCurrentItem(SmartChatConstant.PAGE_TWO);
                changeRadioButtonState(SmartChatConstant.PAGE_TWO);
                break;
            case R.id.rb_foot_btn03:
                mainViewPager.setCurrentItem(SmartChatConstant.PAGE_THREE);
                changeRadioButtonState(SmartChatConstant.PAGE_THREE);
                break;
            case R.id.rb_foot_btn04:
                mainViewPager.setCurrentItem(SmartChatConstant.PAGE_FOUR);
                changeRadioButtonState(SmartChatConstant.PAGE_FOUR);
                break;

        }
    }


    private void changeRadioButtonState(int index){
        for (int i=0;i<4;i++){
            RadioButton temp=radioButtons.get(i);
            if (i==index){
                temp.setCompoundDrawableTintList(ColorStateList.valueOf(Color.BLUE));
                temp.setTextColor(Color.BLUE);
            }else {
                temp.setCompoundDrawableTintList(ColorStateList.valueOf(Color.BLACK));
                temp.setTextColor(Color.BLACK);
            }
        }

    }


    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b) {
        String text=RecognizerHelper.parseIatResult(recognizerResult.getResultString());
        switch (text){
            case "聊聊":
                mainViewPager.setCurrentItem(SmartChatConstant.PAGE_ONE);
                changeRadioButtonState(SmartChatConstant.PAGE_ONE);
                break;
            case "好友列表":
                mainViewPager.setCurrentItem(SmartChatConstant.PAGE_TWO);
                changeRadioButtonState(SmartChatConstant.PAGE_TWO);
                break;
            case "看看":
                mainViewPager.setCurrentItem(SmartChatConstant.PAGE_THREE);
                changeRadioButtonState(SmartChatConstant.PAGE_THREE);
                break;
            case "我的":
                mainViewPager.setCurrentItem(SmartChatConstant.PAGE_FOUR);
                changeRadioButtonState(SmartChatConstant.PAGE_FOUR);
                break;

        }
        Log.i(TAG,text);
    }

    @Override
    public void onError(SpeechError speechError) {

    }

    public void setTitlebar(int page) {
        Log.i(TAG, TAG + page);
        switch (page) {
            case SmartChatConstant.PAGE_ONE:
                ralat_layout_title_bar.setVisibility(View.VISIBLE);
                tv_title_bar_center.setText(getString(R.string.rg_foot_btn01));
                break;
            case SmartChatConstant.PAGE_TWO:
                ralat_layout_title_bar.setVisibility(View.VISIBLE);
                tv_title_bar_center.setText(getString(R.string.rg_foot_btn02));
                break;
            case SmartChatConstant.PAGE_THREE:
                ralat_layout_title_bar.setVisibility(View.VISIBLE);
                tv_title_bar_center.setText(getString(R.string.rg_foot_btn03));
                break;
            case SmartChatConstant.PAGE_FOUR:
                ralat_layout_title_bar.setVisibility(View.GONE);
                break;
        }
    }
}
