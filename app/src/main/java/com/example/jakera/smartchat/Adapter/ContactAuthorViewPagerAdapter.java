package com.example.jakera.smartchat.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jakera.smartchat.R;

/**
 * Created by jakera on 18-3-23.
 */

public class ContactAuthorViewPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] imageResIds;

    public ContactAuthorViewPagerAdapter(Context context, int[] imageResIds) {
        this.context = context;
        this.imageResIds = imageResIds;
    }

    @Override
    public int getCount() {
        return imageResIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.vp_item_author, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_vp_item);
        imageView.setImageResource(imageResIds[position]);
        container.addView(view);
        return view;
    }
}
