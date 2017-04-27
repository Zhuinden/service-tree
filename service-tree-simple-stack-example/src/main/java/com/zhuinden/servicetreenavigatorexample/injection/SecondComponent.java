package com.zhuinden.servicetreenavigatorexample.injection;

import com.zhuinden.servicetreenavigatorexample.SecondKey;
import com.zhuinden.servicetreenavigatorexample.SecondView;

import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */
@KeyScope(SecondKey.class)
@Component(dependencies = {MainComponent.class}, modules = {SecondModule.class})
public interface SecondComponent {
    SecondKey secondKey();

    void inject(SecondView secondView);
}
