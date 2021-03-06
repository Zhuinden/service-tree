package com.zhuinden.servicetreenavigatorexample.injection;

import com.zhuinden.servicetreenavigatorexample.MainActivity;

import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */

@ActivityScope
@Component(dependencies = {ApplicationComponent.class})
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
