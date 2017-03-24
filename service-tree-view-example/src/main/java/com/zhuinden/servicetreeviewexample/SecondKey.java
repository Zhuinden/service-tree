package com.zhuinden.servicetreeviewexample;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeviewexample.injection.DaggerSecondComponent;
import com.zhuinden.servicetreeviewexample.injection.MainComponent;

/**
 * Created by Owner on 2017. 03. 17..
 */

@AutoValue
public abstract class SecondKey
        implements Key, Parcelable {
    @Override
    public int layout() {
        return R.layout.path_second;
    }

    @Override
    public void bindServices(ServiceTree.Node node) {
        MainComponent mainComponent = node.getService(Services.DAGGER_COMPONENT);
        node.bindService(Services.DAGGER_COMPONENT, DaggerSecondComponent.builder().mainComponent(mainComponent).build());
    }

    public static SecondKey create() {
        return new AutoValue_SecondKey();
    }
}
