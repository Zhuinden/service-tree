package com.zhuinden.servicetreenavigatorexample;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.zhuinden.navigator.StateKey;
import com.zhuinden.navigator.ViewController;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreenavigatorexample.injection.DaggerFirstComponent;
import com.zhuinden.servicetreenavigatorexample.injection.FirstComponent;
import com.zhuinden.servicetreenavigatorexample.injection.FirstModule;
import com.zhuinden.servicetreenavigatorexample.injection.MainComponent;

/**
 * Created by Owner on 2017. 03. 17..
 */

@AutoValue
public abstract class FirstKey
        extends StateKey
        implements Key, Parcelable {
    @Override
    public int layout() {
        return R.layout.path_first;
    }

    @Override
    public ViewController createViewController() {
        ServiceTree.Node node = Nodes.getNode(this);
        FirstComponent firstComponent = node.getService(Services.DAGGER_COMPONENT);
        return firstComponent.firstController();
    }

    @Override
    public void bindServices(ServiceTree.Node.Binder binder) {
        MainComponent mainComponent = binder.getService(Services.DAGGER_COMPONENT);
        binder.bindService(Services.DAGGER_COMPONENT,
                DaggerFirstComponent.builder().mainComponent(mainComponent).firstModule(new FirstModule(this)).build());
    }

    public static FirstKey create() {
        return new AutoValue_FirstKey();
    }
}
