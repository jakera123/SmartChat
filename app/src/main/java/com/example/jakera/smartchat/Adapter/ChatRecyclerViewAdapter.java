package com.example.jakera.smartchat.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jakera.smartchat.Entry.BaseMessageEntry;
import com.example.jakera.smartchat.Entry.TextMessageEntry;
import com.example.jakera.smartchat.Entry.VoiceMessageEntry;
import com.example.jakera.smartchat.Interface.ItemClickListener;
import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.Views.BubbleLinearLayout;
import com.example.jakera.smartchat.Views.BubbleTextView;

import java.util.List;

/**
 * Created by jakera on 18-2-1.
 */

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter {
    private List<BaseMessageEntry> datas;
    private ItemClickListener mItemClickListener;


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
                ChatRecyclerViewAdapter.ChatMessageRightViewHolder tempHolder = (ChatRecyclerViewAdapter.ChatMessageRightViewHolder) holder;
                if (datas.get(position) instanceof TextMessageEntry){
                    TextMessageEntry tempMessage = (TextMessageEntry) datas.get(position);
                    tempHolder.bubble_textview.setVisibility(View.VISIBLE);
                    tempHolder.chat_right_bubble.setVisibility(View.GONE);
                    tempHolder.iv_portrait.setImageBitmap(tempMessage.getPortrait());
                    //为Item设置监听所用
                    tempHolder.iv_portrait.setTag(position);
                    tempHolder.bubble_textview.setText(tempMessage.getContent());
                }else if(datas.get(position) instanceof VoiceMessageEntry){
                    VoiceMessageEntry tempMessage=(VoiceMessageEntry)datas.get(position);
                    tempHolder.bubble_textview.setVisibility(View.GONE);
                    tempHolder.chat_right_bubble.setVisibility(View.VISIBLE);
                    tempHolder.iv_portrait.setImageBitmap(tempMessage.getPortrait());
                    tempHolder.iv_portrait.setTag(position);
                }

        }else if (getItemViewType(position)==TextMessageEntry.RECEIVEMESSAGE){
                ChatRecyclerViewAdapter.ChatMessageLeftViewHolder tempHolder1 = (ChatRecyclerViewAdapter.ChatMessageLeftViewHolder) holder;
                if (datas.get(position) instanceof TextMessageEntry){
                    TextMessageEntry tempMessage = (TextMessageEntry) datas.get(position);
                    tempHolder1.iv_portrait.setImageBitmap(tempMessage.getPortrait());
                    tempHolder1.bubble_textview.setVisibility(View.VISIBLE);
                    tempHolder1.chat_left_bubble.setVisibility(View.GONE);
                    //为Item设置监听所用
                    tempHolder1.iv_portrait.setTag(position);
                    tempHolder1.bubble_textview.setText(tempMessage.getContent());
                }else if (datas.get(position) instanceof VoiceMessageEntry){
                    VoiceMessageEntry tempMessage=(VoiceMessageEntry)datas.get(position);
                    tempHolder1.bubble_textview.setVisibility(View.GONE);
                    tempHolder1.chat_left_bubble.setVisibility(View.VISIBLE);
                    tempHolder1.iv_portrait.setImageBitmap(tempMessage.getPortrait());
                    tempHolder1.iv_portrait.setTag(position);
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

        public ChatMessageLeftViewHolder(View itemView) {
            super(itemView);
            iv_portrait=(ImageView)itemView.findViewById(R.id.iv_chat_left_portrait);
            bubble_textview=(BubbleTextView)itemView.findViewById(R.id.bubble_chat_tv_left);
            chat_left_bubble=(BubbleLinearLayout)itemView.findViewById(R.id.chat_left_bubble);
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

        public ChatMessageRightViewHolder(View itemView) {
            super(itemView);
            iv_portrait=(ImageView)itemView.findViewById(R.id.iv_chat_right_portrait);
            bubble_textview=(BubbleTextView)itemView.findViewById(R.id.bubble_tv_chat_right);
            chat_right_bubble=(BubbleLinearLayout)itemView.findViewById(R.id.chat_right_bubble);
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
