package com.zhuinden.servicetreefragmentexample.injection;

import android.app.Application;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreefragmentexample.Services;

/**
 * Created by Zhuinden on 2017.03.07..
 */

public class CustomApplication
        extends Application {
    public static final String SCOPE_KEY = "CustomApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationComponent applicationComponent = DaggerApplicationComponent.create();
        Injector.INSTANCE.applicationComponent = applicationComponent;
        ServiceTree serviceTree = applicationComponent.serviceTree();
        ServiceTree.Node node = serviceTree.createRootNode(SCOPE_KEY);
        node.bindService(Services.DAGGER_COMPONENT, applicationComponent);
    }
}
