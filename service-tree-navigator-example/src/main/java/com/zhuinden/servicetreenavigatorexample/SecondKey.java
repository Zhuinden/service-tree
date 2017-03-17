package com.zhuinden.servicetreenavigatorexample;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.zhuinden.navigator.StateKey;
import com.zhuinden.navigator.ViewController;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreenavigatorexample.injection.DaggerSecondComponent;
import com.zhuinden.servicetreenavigatorexample.injection.MainComponent;
import com.zhuinden.servicetreenavigatorexample.injection.SecondComponent;
import com.zhuinden.servicetreenavigatorexample.injection.SecondModule;

/**
 * Created by Owner on 2017. 03. 17..
 */

@AutoValue
public abstract class SecondKey
        extends StateKey
        implements Key, Parcelable {
    @Override
    public int layout() {
        return R.layout.path_second;
    }

    @Override
    public ViewController createViewController() {
        ServiceTree.Node node = Services.getNode(this);
        SecondComponent secondComponent = node.getService(Services.DAGGER_COMPONENT);
        return secondComponent.secondController();
    }

    @Override
    public void bindServices(ServiceTree.Node.Binder binder) {
        MainComponent mainComponent = binder.getService(Services.DAGGER_COMPONENT);
        binder.bindService(Services.DAGGER_COMPONENT,
                DaggerSecondComponent.builder().mainComponent(mainComponent).secondModule(new SecondModule(this)).build());
    }

    public static SecondKey create() {
        return new AutoValue_SecondKey();
    }
}
