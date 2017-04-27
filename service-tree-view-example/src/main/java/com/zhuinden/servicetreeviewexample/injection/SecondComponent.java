package com.zhuinden.servicetreeviewexample.injection;

import com.zhuinden.servicetreeviewexample.SecondView;
import dagger.Component;

/**
 * Created by Owner on 2017. 03. 07..
 */
@ViewScope
@Component(dependencies = {MainComponent.class})
public interface SecondComponent {
    void inject(SecondView secondView);
}
