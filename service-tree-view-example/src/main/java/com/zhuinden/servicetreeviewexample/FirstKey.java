package com.zhuinden.servicetreeviewexample;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeviewexample.injection.DaggerFirstComponent;
import com.zhuinden.servicetreeviewexample.injection.MainComponent;

/**
 * Created by Owner on 2017. 03. 17..
 */

@AutoValue
public abstract class FirstKey implements Key, Parcelable {
    @Override
    public int layout() {
        return R.layout.path_first;
    }

    @Override
    public void bindServices(ServiceTree.Node node) {
        MainComponent mainComponent = node.getService(Services.DAGGER_COMPONENT);
        node.bindService(Services.DAGGER_COMPONENT, DaggerFirstComponent.builder().mainComponent(mainComponent).build());
    }

    public static FirstKey create() {
        return new AutoValue_FirstKey();
    }
}
