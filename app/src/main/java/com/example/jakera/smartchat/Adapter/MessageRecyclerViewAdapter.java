package com.example.jakera.smartchat.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jakera.smartchat.Entry.MessageEntry;
import com.example.jakera.smartchat.R;

import java.util.List;

/**
 * Created by jakera on 18-2-1.
 */

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter {
    private List<MessageEntry> datas;

    public void setDatas(List<MessageEntry> datas){
        this.datas=datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessageViewHolder viewHolder=new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item,null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageViewHolder tempHolder=(MessageViewHolder)holder;
        MessageEntry tempMessage=datas.get(position);
        tempHolder.iv_portrait.setImageBitmap(tempMessage.getPortrait());
        tempHolder.tv_title.setText(tempMessage.getTitle());
        tempHolder.tv_content.setText(tempMessage.getContent());
        tempHolder.tv_time.setText(tempMessage.getTime());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    private class MessageViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_portrait;
        private TextView tv_title,tv_content,tv_time;

        public MessageViewHolder(View itemView) {
            super(itemView);
            iv_portrait=(ImageView)itemView.findViewById(R.id.iv_message_list_portrait);
            tv_title=(TextView)itemView.findViewById(R.id.tv_message_list_title);
            tv_content=(TextView)itemView.findViewById(R.id.tv_message_list_content);
            tv_time=(TextView)itemView.findViewById(R.id.tv_message_list_time);

        }
    }
}
