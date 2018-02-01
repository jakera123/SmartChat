package com.example.jakera.smartchat.Activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jakera.smartchat.Adapter.ChatRecyclerViewAdapter;
import com.example.jakera.smartchat.Adapter.MessageRecyclerViewAdapter;
import com.example.jakera.smartchat.Entry.ChatMessageEntry;
import com.example.jakera.smartchat.Entry.MessageEntry;
import com.example.jakera.smartchat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakera on 18-2-1.
 */

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ChatMessageEntry> datas;
    private ChatRecyclerViewAdapter adapter;

    private TextView tv_send;
    private EditText et_input_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ChatRecyclerViewAdapter();

        datas=new ArrayList<>();

        ChatMessageEntry messageEntry0=new ChatMessageEntry();
        messageEntry0.setPortrait(BitmapFactory.decodeResource(getResources(),R.drawable.robot_portrait));
        messageEntry0.setContent("嗨，我是小智，来和我聊天吧！！！");
        messageEntry0.setViewType(ChatMessageEntry.RECEIVEMESSAGE);

        et_input_text=(EditText)findViewById(R.id.et_input_text);
        tv_send=(TextView)findViewById(R.id.tv_send);
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMessageEntry messageEntry=new ChatMessageEntry();
                messageEntry.setPortrait(BitmapFactory.decodeResource(getResources(),R.drawable.robot_portrait));
                messageEntry.setContent(et_input_text.getText().toString());
                messageEntry.setViewType(ChatMessageEntry.SENDMESSAGE);
                et_input_text.setText("");
                datas.add(messageEntry);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(datas.size()-1);
            }
        });

        datas.add(messageEntry0);

        adapter.setDatas(datas);
        recyclerView.setAdapter(adapter);
    }
}
