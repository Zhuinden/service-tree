package com.zhuinden.servicetreeviewexample.injection;

import com.zhuinden.servicetreeviewexample.BackstackHolder;
import com.zhuinden.servicetreeviewexample.MainActivity;

import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */

@ActivityScope
@Component(dependencies = {ApplicationComponent.class})
public interface MainComponent {
    BackstackHolder backstackHolder();

    void inject(MainActivity mainActivity);
}
