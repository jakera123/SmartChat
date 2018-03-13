package com.example.jakera.smartchat.Fragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.jakera.smartchat.SmartChatConstant;
import com.example.jakera.smartchat.SmartChatService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by jakera on 18-1-25.
 */

public class MessageListFragment extends Fragment implements ItemClickListener, SmartChatService.getMessageListener {

    private String TAG = "MessageListFragment";

    private RecyclerView recyclerView;
    private List<MessageEntry> datas;
    private MessageRecyclerViewAdapter adapter;

    private ServiceConnection serviceConnection;
    private SmartChatService smartChatService;

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
        messageEntry0.setUsername(getString(R.string.app_name));
        messageEntry0.setNickname("我叫小智");
        messageEntry0.setContent("快来自言智语吧");
        messageEntry0.setTime("2018.3.9");
        datas.add(messageEntry0);
        adapter=new MessageRecyclerViewAdapter();
        adapter.setDatas(datas);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "绑定服务成功");
                SmartChatService.SmartChatBinder smartChatBinder = (SmartChatService.SmartChatBinder) service;
                smartChatService = smartChatBinder.getService();
                smartChatService.setGetMessageListenr(MessageListFragment.this);
                //只能在应用的周期里调用一次，否则会重复读入数据。
                smartChatService.initSQlite();
                smartChatService.getMessageFromDB();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                smartChatService = null;
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (smartChatService != null && smartChatService.getMessageList().size() > 0) {
            datas.clear();
            MessageEntry messageEntry = new MessageEntry();
            messageEntry.setUsername(getString(R.string.app_name));
            messageEntry.setNickname("我叫小智");
            messageEntry.setContent("快来自言智语吧");
            messageEntry.setTime("2018.3.9");
            datas.add(messageEntry);
            datas.addAll(smartChatService.getMessageList());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnItemClick(View v, int position) {
        Intent intent=new Intent();
        intent.setClass(getContext(), ChatActivity.class);
        Bundle data = new Bundle();
        if (position == 0) {
            data.putString("username", SmartChatConstant.APPNAME);
        } else {
            data.putString("username", datas.get(position).getUsername());
        }
        intent.putExtra("username", data);
        startActivity(intent);
    }

    public void initView() {
        // ((MainActivity)getActivity()).setTitlebar(this);
        Intent intent = new Intent(getActivity(), SmartChatService.class);
        getActivity().bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }


    @Override
    public void onPause() {
        super.onPause();
        smartChatService.saveMessageListToDB();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public void getMessageList(final List<MessageEntry> messageList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //假如只是保留一项进行遍历，不知为何会导致结果不一致，
                datas.clear();
                MessageEntry messageEntry = new MessageEntry();
                messageEntry.setUsername(getString(R.string.app_name));
                messageEntry.setNickname("我叫小智");
                messageEntry.setContent("快来自言智语吧");
                messageEntry.setTime("2018.3.9");
                datas.add(messageEntry);
                datas.addAll(messageList);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
