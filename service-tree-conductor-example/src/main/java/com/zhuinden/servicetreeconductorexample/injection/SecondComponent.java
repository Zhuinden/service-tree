package com.zhuinden.servicetreeconductorexample.injection;

import com.zhuinden.servicetreeconductorexample.SecondController;

import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */
@ControllerScope(SecondController.class)
@Component(dependencies = {MainComponent.class})
public interface SecondComponent {
    void inject(SecondController secondController);
}
