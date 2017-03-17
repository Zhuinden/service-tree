package com.zhuinden.servicetreenavigatorexample.injection;

import com.zhuinden.servicetreenavigatorexample.SecondController;
import com.zhuinden.servicetreenavigatorexample.SecondKey;

import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */
@ControllerScope(SecondKey.class)
@Component(dependencies = {MainComponent.class}, modules = {SecondModule.class})
public interface SecondComponent {
    SecondKey secondKey();

    SecondController secondController();
}
