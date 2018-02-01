package com.example.jakera.smartchat.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jakera.smartchat.Activity.ChatActivity;
import com.example.jakera.smartchat.Activity.MainActivity;
import com.example.jakera.smartchat.Adapter.MessageRecyclerViewAdapter;
import com.example.jakera.smartchat.Entry.MessageEntry;
import com.example.jakera.smartchat.Interface.ItemClickListener;
import com.example.jakera.smartchat.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakera on 18-1-25.
 */

public class MessageListFragment extends Fragment implements ItemClickListener{

    private RecyclerView recyclerView;
    private List<MessageEntry> datas;
    private MessageRecyclerViewAdapter adapter;

    public MessageListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.message_list_fragment,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MessageEntry messageEntry0=new MessageEntry();
        datas=new ArrayList<>();
        messageEntry0.setPortrait(BitmapFactory.decodeResource(getResources(),R.drawable.robot_portrait));
        messageEntry0.setTitle("我叫小智");
        messageEntry0.setContent("快来自言智语吧");
        messageEntry0.setTime("2018.3.9");
        datas.add(messageEntry0);
        adapter=new MessageRecyclerViewAdapter();
        adapter.setDatas(datas);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void OnItemClick(View v, int position) {
        Intent intent=new Intent();
        intent.setClass(getContext(), ChatActivity.class);
        startActivity(intent);
    }
}
