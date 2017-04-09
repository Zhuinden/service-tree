package com.zhuinden.servicetreenavigatorexample;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreenavigatorexample.injection.DaggerSecondComponent;
import com.zhuinden.servicetreenavigatorexample.injection.MainComponent;
import com.zhuinden.servicetreenavigatorexample.injection.SecondModule;
import com.zhuinden.simplestack.navigator.ViewChangeHandler;
import com.zhuinden.simplestack.navigator.changehandlers.SegueViewChangeHandler;

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

    @NonNull
    @Override
    public ViewChangeHandler viewChangeHandler() {
        return new SegueViewChangeHandler();
    }

    @Override
    public void bindServices(ServiceTree.Node node) {
        MainComponent mainComponent = node.getService(Services.DAGGER_COMPONENT);
        node.bindService(Services.DAGGER_COMPONENT,
                DaggerSecondComponent.builder().mainComponent(mainComponent).secondModule(new SecondModule(this)).build());
    }

    public static SecondKey create() {
        return new AutoValue_SecondKey();
    }
}
