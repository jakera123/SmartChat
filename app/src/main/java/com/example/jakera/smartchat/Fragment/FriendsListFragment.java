package com.example.jakera.smartchat.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jakera.smartchat.Activity.MainActivity;
import com.example.jakera.smartchat.R;

/**
 * Created by jakera on 18-1-25.
 */

public class FriendsListFragment extends Fragment {
    public FriendsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.friends_list_fragment,container,false);
        initView();
        return view;
    }

    public void initView() {
        //  ((MainActivity)getActivity()).setTitlebar(this);
    }


}
