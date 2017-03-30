package com.zhuinden.servicetreenavigatorexample;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.zhuinden.navigator.ViewChangeHandler;
import com.zhuinden.navigator.changehandlers.SegueViewChangeHandler;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreenavigatorexample.injection.DaggerFirstComponent;
import com.zhuinden.servicetreenavigatorexample.injection.FirstModule;
import com.zhuinden.servicetreenavigatorexample.injection.MainComponent;

/**
 * Created by Owner on 2017. 03. 17..
 */

@AutoValue
public abstract class FirstKey
        implements Key, Parcelable {
    @Override
    public int layout() {
        return R.layout.path_first;
    }

    @NonNull
    @Override
    public ViewChangeHandler viewChangeHandler() {
        return new SegueViewChangeHandler();
    }

    @Override
    public void bindServices(ServiceTree.Node node) {
        MainComponent mainComponent = node.getService(Services.DAGGER_COMPONENT);
        node.bindService(Services.DAGGER_COMPONENT,
                DaggerFirstComponent.builder().mainComponent(mainComponent).firstModule(new FirstModule(this)).build());
    }

    public static FirstKey create() {
        return new AutoValue_FirstKey();
    }
}
