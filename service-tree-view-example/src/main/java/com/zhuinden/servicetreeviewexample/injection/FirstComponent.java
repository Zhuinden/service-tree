package com.zhuinden.servicetreeviewexample.injection;

import com.zhuinden.servicetreeviewexample.FirstView;
import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */
@ViewScope
@Component(dependencies = {MainComponent.class})
public interface FirstComponent {
    void inject(FirstView firstView);
}
