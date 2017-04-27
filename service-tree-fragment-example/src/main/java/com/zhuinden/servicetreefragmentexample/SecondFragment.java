package com.zhuinden.servicetreefragmentexample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreefragmentexample.injection.DaggerSecondComponent;
import com.zhuinden.servicetreefragmentexample.injection.MainComponent;
import com.zhuinden.servicetreefragmentexample.injection.SecondComponent;
import butterknife.ButterKnife;

/**
 * Created by Owner on 2017. 03. 07..
 */
public class SecondFragment
        extends Fragment
        implements HasServices {
    public static final String TAG = "SecondFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        ButterKnife.bind(this, view);
        SecondComponent secondComponent = Services.getNode(TAG).getService(Services.DAGGER_COMPONENT);
        secondComponent.inject(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = MainActivity.get(context);
        mainActivity.registerFragmentServices(this);
    }

    @Override
    public String getNodeTag() {
        return TAG;
    }

    @Override
    public void bindServices(ServiceTree.Node node) {
        MainComponent mainComponent = node.getService(Services.DAGGER_COMPONENT);
        node.bindService(Services.DAGGER_COMPONENT, DaggerSecondComponent.builder().mainComponent(mainComponent).build());
    }
}
