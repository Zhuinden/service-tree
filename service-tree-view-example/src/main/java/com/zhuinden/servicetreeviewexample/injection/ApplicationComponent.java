package com.zhuinden.servicetreeviewexample.injection;

import com.zhuinden.servicetree.ServiceTree;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Zhuinden on 2017.03.07..
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    ServiceTree serviceTree();
}
