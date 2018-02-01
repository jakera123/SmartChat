package com.example.jakera.smartchat.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.jakera.smartchat.Entry.ChatMessageEntry;
import com.example.jakera.smartchat.Entry.MessageEntry;
import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.Views.BubbleTextView;

import java.util.List;

/**
 * Created by jakera on 18-2-1.
 */

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter {
    private List<ChatMessageEntry> datas;


    public void setDatas(List<ChatMessageEntry> datas){
        this.datas=datas;
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
            case ChatMessageEntry.RECEIVEMESSAGE:
                ChatMessageLeftViewHolder viewHolder=new ChatMessageLeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left_item,null));
                return viewHolder;
            case ChatMessageEntry.SENDMESSAGE:
                ChatMessageRightViewHolder viewHolder1=new ChatMessageRightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right_item,null));
                return viewHolder1;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessageEntry tempMessage;
        switch (getItemViewType(position)){
            case ChatMessageEntry.SENDMESSAGE:
                ChatRecyclerViewAdapter.ChatMessageRightViewHolder tempHolder=(ChatRecyclerViewAdapter.ChatMessageRightViewHolder)holder;
                tempMessage=datas.get(position);
                tempHolder.iv_portrait.setImageBitmap(tempMessage.getPortrait());
                //为Item设置监听所用
                tempHolder.iv_portrait.setTag(position);
                tempHolder.bubble_content.setText(tempMessage.getContent());
                break;

            case ChatMessageEntry.RECEIVEMESSAGE:
                ChatRecyclerViewAdapter.ChatMessageLeftViewHolder tempHolder1=(ChatRecyclerViewAdapter.ChatMessageLeftViewHolder)holder;
                tempMessage=datas.get(position);
                tempHolder1.iv_portrait.setImageBitmap(tempMessage.getPortrait());
                //为Item设置监听所用
                tempHolder1.iv_portrait.setTag(position);
                tempHolder1.bubble_content.setText(tempMessage.getContent());
                break;
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


    private class ChatMessageLeftViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_portrait;
        private BubbleTextView bubble_content;

        public ChatMessageLeftViewHolder(View itemView) {
            super(itemView);
            iv_portrait=(ImageView)itemView.findViewById(R.id.iv_chat_left_portrait);
            bubble_content=(BubbleTextView)itemView.findViewById(R.id.bubble_chat_left);
        }
    }

    private class ChatMessageRightViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_portrait;
        private BubbleTextView bubble_content;

        public ChatMessageRightViewHolder(View itemView) {
            super(itemView);
            iv_portrait=(ImageView)itemView.findViewById(R.id.iv_chat_right_portrait);
            bubble_content=(BubbleTextView)itemView.findViewById(R.id.bubble_chat_right);
        }
    }


}
