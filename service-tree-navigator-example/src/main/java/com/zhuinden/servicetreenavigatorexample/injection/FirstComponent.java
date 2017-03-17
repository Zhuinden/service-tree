package com.zhuinden.servicetreenavigatorexample.injection;

import com.zhuinden.servicetreenavigatorexample.FirstController;

import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */
@ViewScope
@Component(dependencies = {MainComponent.class})
public interface FirstComponent {
    void inject(FirstController firstController);
}
