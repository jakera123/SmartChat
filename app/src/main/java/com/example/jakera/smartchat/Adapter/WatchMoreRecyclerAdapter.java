package com.example.jakera.smartchat.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jakera.smartchat.Entry.BeautifulPictureEntry;
import com.example.jakera.smartchat.Interface.ItemClickListener;
import com.example.jakera.smartchat.Others.PicassoTransformation;
import com.example.jakera.smartchat.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakera on 18-2-23.
 */

public class WatchMoreRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private List<BeautifulPictureEntry.Data> datas = new ArrayList<>();

    public void setDatas(List<BeautifulPictureEntry.Data> datas, boolean isRefresh) {
        if (isRefresh) {
            this.datas = datas;
        } else {
            this.datas.addAll(datas);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WatchMoreViewHolder watchMoreViewHolder = new WatchMoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.watch_more_list_item, null));
        return watchMoreViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WatchMoreViewHolder tempViewHolder = (WatchMoreViewHolder) holder;
        BeautifulPictureEntry.Data picData = datas.get(position);
        //按比例缩放
        Transformation transformation = new PicassoTransformation(tempViewHolder.iv_watch_more_item, 2);
        Picasso.with(context)
                .load(picData.url)
                .transform(transformation)
                .placeholder(R.drawable.loading)
                .into(tempViewHolder.iv_watch_more_item);
        tempViewHolder.iv_watch_more_item.setTag(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    private class WatchMoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_watch_more_item;

        public WatchMoreViewHolder(View itemView) {
            super(itemView);
            iv_watch_more_item = itemView.findViewById(R.id.iv_watch_more_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.OnItemClick(v, (Integer) iv_watch_more_item.getTag());
            }
        }
    }

}
