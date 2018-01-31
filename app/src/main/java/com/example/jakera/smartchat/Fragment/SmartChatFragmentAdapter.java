package com.example.jakera.smartchat.Fragment;

import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.jakera.smartchat.SmartChatConstant;

/**
 * Created by jakera on 18-1-25.
 */


//http://www.runoob.com/w3cnote/android-tutorial-fragment-demo4.html

public class SmartChatFragmentAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT=4;
    private MessageListFragment mMessageListFragment=null;
    private FriendsListFragment mFriendsListFragment=null;
    private WatchMoreFragment mWatchMoreFragment=null;
    private UserInfoFragment mUserInfoFragment=null;

    public SmartChatFragmentAdapter(FragmentManager fm) {
        super(fm);
        mMessageListFragment=new MessageListFragment();
        mFriendsListFragment=new FriendsListFragment();
        mWatchMoreFragment=new WatchMoreFragment();
        mUserInfoFragment=new UserInfoFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case SmartChatConstant.PAGE_ONE:
                fragment=mMessageListFragment;
                break;
            case SmartChatConstant.PAGE_TWO:
                fragment=mFriendsListFragment;
                break;
            case SmartChatConstant.PAGE_THREE:
                fragment=mWatchMoreFragment;
                break;
            case SmartChatConstant.PAGE_FOUR:
                fragment=mUserInfoFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
