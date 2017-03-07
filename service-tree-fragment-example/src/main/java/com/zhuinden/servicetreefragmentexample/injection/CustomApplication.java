package com.zhuinden.servicetreefragmentexample.injection;

import android.app.Application;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreefragmentexample.Services;

/**
 * Created by Zhuinden on 2017.03.07..
 */

public class CustomApplication
        extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationComponent applicationComponent = DaggerApplicationComponent.create();
        Injector.INSTANCE.applicationComponent = applicationComponent;
        ServiceTree serviceTree = applicationComponent.serviceTree();
        serviceTree.registerRootService(Services.DAGGER_COMPONENT, applicationComponent);
    }
}
