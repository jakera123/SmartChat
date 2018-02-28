package com.example.jakera.smartchat.Interface;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created by jakera on 18-2-28.
 */

/**
 * 得进行其他布局管理器的适配
 */

public abstract class EndLessOnScrollListener extends RecyclerView.OnScrollListener {
    //    声明一个 LinearLayoutManager
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    //     已经加载出来的Item的数量
    private int totalItemCount;
    //     在屏幕上可见的item数量
    private int visibleItemCount;
    //     在屏幕可见的Item中的第一个
    private int[] firstVisibleItem;
    //     是否正在上拉数据
    private boolean loading = false;

    public EndLessOnScrollListener(StaggeredGridLayoutManager staggeredGridLayoutManager) {
        this.staggeredGridLayoutManager = staggeredGridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = staggeredGridLayoutManager.getItemCount();
        firstVisibleItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(null);

        if (!loading && totalItemCount - visibleItemCount == firstVisibleItem[0]) {
            onLoadMore();
            loading = true;
        } else {
            loading = false;
        }
    }

    /**
     * 提供一个抽闲方法，在Activity中监听到这个EndLessOnScrollListener * 并且实现这个方法 *
     */
    public abstract void onLoadMore();
}



