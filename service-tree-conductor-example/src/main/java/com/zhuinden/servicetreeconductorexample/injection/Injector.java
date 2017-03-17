package com.zhuinden.servicetreeconductorexample.injection;

/**
 * Created by Zhuinden on 2017.03.07..
 */

public enum Injector {
    INSTANCE;

    ApplicationComponent applicationComponent;

    public static ApplicationComponent get() {
        return INSTANCE.applicationComponent;
    }
}
