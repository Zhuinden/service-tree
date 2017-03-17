package com.zhuinden.servicetreenavigatorexample.injection;

import com.zhuinden.servicetreenavigatorexample.SecondController;

import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */
@ViewScope
@Component(dependencies = {MainComponent.class})
public interface SecondComponent {
    void inject(SecondController secondController);
}
