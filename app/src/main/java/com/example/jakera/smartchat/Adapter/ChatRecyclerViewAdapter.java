package com.example.jakera.smartchat.Adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jakera.smartchat.Entry.BaseMessageEntry;
import com.example.jakera.smartchat.Entry.TextMessageEntry;
import com.example.jakera.smartchat.Entry.VoiceMessageEntry;
import com.example.jakera.smartchat.Interface.ItemClickListener;
import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.Views.BubbleLinearLayout;
import com.example.jakera.smartchat.Views.BubbleTextView;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by jakera on 18-2-1.
 */

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter {
    private List<BaseMessageEntry> datas;
    private ItemClickListener mItemClickListener;
    private ChatRecyclerViewAdapter.ChatMessageRightViewHolder tempRightHolder;
    private ChatRecyclerViewAdapter.ChatMessageLeftViewHolder tempLeftHolder;


    public void setDatas(List<BaseMessageEntry> datas){
        this.datas=datas;
    }


    public void setOnItemClickListener(ItemClickListener itemClickListener){
        mItemClickListener=itemClickListener;
    }

    /**
     *
     * @param parent
     * @param viewType 这个viewType由getItemViewType获得，所以得重写这个函数
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TextMessageEntry.RECEIVEMESSAGE:
                ChatMessageLeftViewHolder viewHolder=new ChatMessageLeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left_item,null));
                return viewHolder;
            case TextMessageEntry.SENDMESSAGE:
                ChatMessageRightViewHolder viewHolder1=new ChatMessageRightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right_item,null));
                return viewHolder1;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==TextMessageEntry.SENDMESSAGE) {
            tempRightHolder = (ChatRecyclerViewAdapter.ChatMessageRightViewHolder) holder;
                if (datas.get(position) instanceof TextMessageEntry){
                    TextMessageEntry tempMessage = (TextMessageEntry) datas.get(position);
                    tempRightHolder.bubble_textview.setVisibility(View.VISIBLE);
                    tempRightHolder.chat_right_bubble.setVisibility(View.GONE);
                    tempRightHolder.id_recorder_time_right.setVisibility(View.GONE);
                    JMessageClient.getUserInfo(tempMessage.getUserName(), new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                @Override
                                public void gotResult(int i, String s, Bitmap bitmap) {
                                    tempRightHolder.iv_portrait.setImageBitmap(bitmap);
                                }
                            });
                        }
                    });
                    // tempHolder.iv_portrait.setImageBitmap(tempMessage.getPortrait());
                    //为Item设置监听所用
                    tempRightHolder.iv_portrait.setTag(position);
                    tempRightHolder.bubble_textview.setText(tempMessage.getContent());
                }else if(datas.get(position) instanceof VoiceMessageEntry){
                    VoiceMessageEntry tempMessage=(VoiceMessageEntry)datas.get(position);
                    tempRightHolder.bubble_textview.setVisibility(View.GONE);
                    tempRightHolder.id_recorder_time_right.setVisibility(View.VISIBLE);
                    tempRightHolder.chat_right_bubble.setVisibility(View.VISIBLE);
                    JMessageClient.getUserInfo(tempMessage.getUserName(), new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                @Override
                                public void gotResult(int i, String s, Bitmap bitmap) {
                                    tempRightHolder.iv_portrait.setImageBitmap(bitmap);
                                }
                            });
                        }
                    });
                    // tempHolder.iv_portrait.setImageBitmap(tempMessage.getPortrait());
                    tempRightHolder.iv_portrait.setTag(position);
                    tempRightHolder.id_recorder_time_right.setText((int) tempMessage.getTime() + "");
                }

        }else if (getItemViewType(position)==TextMessageEntry.RECEIVEMESSAGE){
            tempLeftHolder = (ChatRecyclerViewAdapter.ChatMessageLeftViewHolder) holder;
                if (datas.get(position) instanceof TextMessageEntry){
                    TextMessageEntry tempMessage = (TextMessageEntry) datas.get(position);
                    JMessageClient.getUserInfo(tempMessage.getUserName(), new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                @Override
                                public void gotResult(int i, String s, Bitmap bitmap) {
                                    tempLeftHolder.iv_portrait.setImageBitmap(bitmap);
                                }
                            });
                        }
                    });
                    // tempHolder1.iv_portrait.setImageBitmap(tempMessage.getPortrait());
                    tempLeftHolder.bubble_textview.setVisibility(View.VISIBLE);
                    tempLeftHolder.chat_left_bubble.setVisibility(View.GONE);
                    //为Item设置监听所用
                    tempLeftHolder.iv_portrait.setTag(position);
                    tempLeftHolder.bubble_textview.setText(tempMessage.getContent());
                }else if (datas.get(position) instanceof VoiceMessageEntry){
                    VoiceMessageEntry tempMessage=(VoiceMessageEntry)datas.get(position);
                    tempLeftHolder.bubble_textview.setVisibility(View.GONE);
                    tempLeftHolder.chat_left_bubble.setVisibility(View.VISIBLE);
                    JMessageClient.getUserInfo(tempMessage.getUserName(), new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                @Override
                                public void gotResult(int i, String s, Bitmap bitmap) {
                                    tempLeftHolder.iv_portrait.setImageBitmap(bitmap);
                                }
                            });
                        }
                    });
//                    tempHolder1.iv_portrait.setImageBitmap(tempMessage.getPortrait());
                    tempLeftHolder.tv_recorder_time_left.setText((int) tempMessage.getTime() + "");
                    tempLeftHolder.iv_portrait.setTag(position);
                }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    private class ChatMessageLeftViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iv_portrait;
        private BubbleTextView bubble_textview;
        private BubbleLinearLayout chat_left_bubble;
        private TextView tv_recorder_time_left;

        public ChatMessageLeftViewHolder(View itemView) {
            super(itemView);
            iv_portrait=(ImageView)itemView.findViewById(R.id.iv_chat_left_portrait);
            bubble_textview=(BubbleTextView)itemView.findViewById(R.id.bubble_chat_tv_left);
            chat_left_bubble=(BubbleLinearLayout)itemView.findViewById(R.id.chat_left_bubble);
            tv_recorder_time_left = (TextView) itemView.findViewById(R.id.id_recorder_time_left);
            //一定要记得加入这一句，才能成功实现监听
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener!=null){
                mItemClickListener.OnItemClick(v,(Integer) iv_portrait.getTag());
            }
        }
    }

    private class ChatMessageRightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iv_portrait;
        private BubbleTextView bubble_textview;
        private BubbleLinearLayout chat_right_bubble;
        private TextView id_recorder_time_right;

        public ChatMessageRightViewHolder(View itemView) {
            super(itemView);
            iv_portrait=(ImageView)itemView.findViewById(R.id.iv_chat_right_portrait);
            bubble_textview=(BubbleTextView)itemView.findViewById(R.id.bubble_tv_chat_right);
            chat_right_bubble=(BubbleLinearLayout)itemView.findViewById(R.id.chat_right_bubble);
            id_recorder_time_right=(TextView)itemView.findViewById(R.id.id_recorder_time_right);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener!=null){
                mItemClickListener.OnItemClick(v,(Integer) iv_portrait.getTag());
            }

        }
    }


}
