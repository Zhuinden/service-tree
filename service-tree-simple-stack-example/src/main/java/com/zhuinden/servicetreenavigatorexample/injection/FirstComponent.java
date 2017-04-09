package com.zhuinden.servicetreenavigatorexample.injection;

import com.zhuinden.servicetreenavigatorexample.FirstKey;
import com.zhuinden.servicetreenavigatorexample.FirstView;

import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */
@ControllerScope(FirstKey.class)
@Component(dependencies = {MainComponent.class}, modules = {FirstModule.class})
public interface FirstComponent {
    FirstKey firstKey();

    void inject(FirstView firstView);
}
