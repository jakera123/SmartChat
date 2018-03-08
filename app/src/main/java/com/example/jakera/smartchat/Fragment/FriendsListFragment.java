package com.example.jakera.smartchat.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jakera.smartchat.Activity.ChatActivity;
import com.example.jakera.smartchat.Activity.MainActivity;
import com.example.jakera.smartchat.Adapter.FriendsListRecyclerAdapter;
import com.example.jakera.smartchat.Interface.ItemClickListener;
import com.example.jakera.smartchat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by jakera on 18-1-25.
 */

public class FriendsListFragment extends Fragment implements ItemClickListener {

    private String TAG = "FriensListFragment";
    private RecyclerView recyclerView;
    private FriendsListRecyclerAdapter friendsListRecyclerAdapter;
    private List<UserInfo> datas;


    public FriendsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.friends_list_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_friends_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsListRecyclerAdapter = new FriendsListRecyclerAdapter();
        friendsListRecyclerAdapter.setOnItemClickListener(this);
        datas = new ArrayList<>();
        recyclerView.setAdapter(friendsListRecyclerAdapter);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //注册事件，以接收各种用户请求
        JMessageClient.registerEventReceiver(this);

        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
                    friendsListRecyclerAdapter.setDatas(userInfoList);
                    datas = userInfoList;
                    friendsListRecyclerAdapter.notifyDataSetChanged();
                } else {
                    //获取好友列表失败
                    Log.i(TAG, "获取好友列表失败");
                }
            }
        });
    }

    public void initView() {

    }

    @Override
    public void onPause() {
        super.onPause();
        JMessageClient.unRegisterEventReceiver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    /**
     * 类似MessageEvent事件的接收，上层在需要的地方增加OfflineMessageEvent事件的接收
     * 即可实现离线消息的接收。
     **/
    public void onEvent(OfflineMessageEvent event) {
        //获取事件发生的会话对象
        Log.i(TAG, "收到消息");
        Conversation conversation = event.getConversation();
        List<Message> newMessageList = event.getOfflineMessageList();//获取此次离线期间会话收到的新消息列表
        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到%d条来自%s的离线消息。\n", newMessageList.size(), conversation.getTargetId()));
        for (int i = 0; i < newMessageList.size(); i++) {
            //  {"text":"你好","extras":{}}
            Log.i(TAG, "i=" + i + "," + newMessageList.get(i).getContent().toJson());
//            Log.i(TAG,"i="+i+","+newMessageList.get(i).getContent().);

        }


    }


    @Override
    public void OnItemClick(View v, int position) {
        String username = ((UserInfo) datas.get(position)).getUserName();
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        Bundle data = new Bundle();
        data.putString("username", username);
        intent.putExtra("username", data);
        startActivity(intent);
    }
}
