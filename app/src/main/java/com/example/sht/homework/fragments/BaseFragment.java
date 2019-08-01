package com.example.sht.homework.fragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment {

    public void toast(String content){
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }

}
