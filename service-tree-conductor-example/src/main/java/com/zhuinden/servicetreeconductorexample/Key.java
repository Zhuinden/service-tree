package com.zhuinden.servicetreeconductorexample;

import android.support.annotation.LayoutRes;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeconductorexample.injection.DaggerSecondComponent;
import com.zhuinden.servicetreeconductorexample.injection.MainComponent;

/**
 * Created by Owner on 2017. 03. 17..
 */

public interface Key {
    @LayoutRes int layout();

    void bindServices(ServiceTree.Node.Binder binder);
}
