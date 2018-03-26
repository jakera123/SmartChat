package com.example.jakera.smartchat.Views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jakera.smartchat.R;

/**
 * Created by jakera on 18-2-9.
 */

public class LoadingDialog extends Dialog {

    private TextView tv;
    private String content;

    /**
     * style很关键
     */
    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        tv = (TextView) findViewById(R.id.tv_dialog_loading);
        // tv.setText(getContext().getString(R.string.login_now));
        tv.setText(content);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.linearLayout_loading_dialog);
        linearLayout.getBackground().setAlpha(210);
    }

    public void setTextContent(String content) {
        this.content = content;
    }


}
