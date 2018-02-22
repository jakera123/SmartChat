package com.example.jakera.smartchat.Adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jakera.smartchat.Entry.MessageEntry;
import com.example.jakera.smartchat.Interface.ItemClickListener;
import com.example.jakera.smartchat.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by jakera on 18-2-22.
 */

public class FriendsListRecyclerAdapter extends RecyclerView.Adapter {
    private List<UserInfo> datas;
    private ItemClickListener mItemClickListener;

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setDatas(List<UserInfo> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendsViewHolder viewHolder = new FriendsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_item, null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FriendsViewHolder friendsViewHolder = (FriendsViewHolder) holder;
        UserInfo userInfo = datas.get(position);
//        TODO:更改头像
//        friendsViewHolder.iv_portrait.setBackground();
        friendsViewHolder.tv_username.setText(userInfo.getUserName());
    }

    @Override
    public int getItemCount() {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        return datas.size();
    }


    private class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_portrait;
        private TextView tv_username;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            iv_portrait = (ImageView) itemView.findViewById(R.id.iv_friends_list_portrait);
            tv_username = (TextView) itemView.findViewById(R.id.tv_friends_list_username);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.OnItemClick(v, (Integer) iv_portrait.getTag());
            }
        }
    }

}
