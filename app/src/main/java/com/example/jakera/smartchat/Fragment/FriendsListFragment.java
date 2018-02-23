package com.example.jakera.smartchat.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    //子线程模式
    public void onEvent(ContactNotifyEvent event) {
        String reason = event.getReason();
        final String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();

        switch (event.getType()) {
            case invite_received://收到好友邀请
                AlertDialog dialogReceived = new AlertDialog.Builder(getActivity())
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getString(R.string.friend_invite_received))//设置对话框的标题
                        .setMessage(fromUsername + getString(R.string.friend_invite_tip))//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton(getString(R.string.reject), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContactManager.declineInvitation(fromUsername, null, "", null);
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContactManager.acceptInvitation(fromUsername, null, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
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
                                    }
                                });

                                dialog.dismiss();
                            }
                        }).create();
                dialogReceived.show();
                break;
            case invite_accepted://对方接收了你的好友邀请
                AlertDialog dialogAccept = new AlertDialog.Builder(getActivity())
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getString(R.string.add_friends))//设置对话框的标题
                        .setMessage(fromUsername + getString(R.string.friend_invite_accept))//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialogAccept.show();
                break;
            case invite_declined://对方拒绝了你的好友邀请
                AlertDialog dialogReject = new AlertDialog.Builder(getActivity())
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getString(R.string.add_friends))//设置对话框的标题
                        .setMessage(fromUsername + getString(R.string.friend_invite_reject))//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton(getString(R.string.reject), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialogReject.show();
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                Log.i(TAG, "对方将你从好友中删除");
                break;
            default:
                break;
        }
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
