package com.zhuinden.servicetreeconductorexample.injection;

import com.zhuinden.servicetreeconductorexample.FirstController;

import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */
@ControllerScope(FirstController.class)
@Component(dependencies = {MainComponent.class})
public interface FirstComponent {
    void inject(FirstController firstController);
}
