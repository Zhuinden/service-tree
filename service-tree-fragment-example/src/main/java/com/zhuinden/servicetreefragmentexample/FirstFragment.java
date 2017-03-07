package com.zhuinden.servicetreefragmentexample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuinden.servicetree.ServiceTree;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Owner on 2017. 03. 07..
 */

public class FirstFragment extends Fragment implements HasServices {
    public static final String TAG = "FirstFragment";

    @OnClick(R.id.first_button)
    public void clickFirst() {
        MainActivity.get(getContext()).goToSecond();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = MainActivity.get(context);
        mainActivity.registerFragmentServices(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        ButterKnife.bind(this, view);
        FirstComponent firstComponent = Services.get(getContext()).getNode(TAG).getService(DaggerService.TAG);
        firstComponent.inject(this);
        return view;
    }

    @Override
    public String getServiceTag() {
        return TAG;
    }

    @Override
    public void bindServices(ServiceTree.Node.Binder binder) {
        MainComponent mainComponent = binder.getService(DaggerService.TAG);
        binder.bindService(DaggerService.TAG, DaggerFirstComponent.builder().mainComponent(mainComponent).build());
    }
}
