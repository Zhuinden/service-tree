package com.zhuinden.servicetreefragmentexample.injection;

import com.zhuinden.servicetreefragmentexample.SecondFragment;
import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */
@FragmentScope
@Component(dependencies = {MainComponent.class})
public interface SecondComponent {
    void inject(SecondFragment secondFragment);
}
