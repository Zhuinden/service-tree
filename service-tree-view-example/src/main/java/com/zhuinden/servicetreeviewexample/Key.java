package com.zhuinden.servicetreeviewexample;

import android.support.annotation.LayoutRes;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeviewexample.injection.DaggerSecondComponent;
import com.zhuinden.servicetreeviewexample.injection.MainComponent;

/**
 * Created by Owner on 2017. 03. 17..
 */

public interface Key {
    @LayoutRes int layout();

    String getNodeTag();

    void bindServices(ServiceTree.Node.Binder binder);
}
