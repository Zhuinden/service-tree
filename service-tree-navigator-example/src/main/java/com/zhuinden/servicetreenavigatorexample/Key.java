package com.zhuinden.servicetreenavigatorexample;

import android.support.annotation.LayoutRes;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreenavigatorexample.injection.DaggerSecondComponent;
import com.zhuinden.servicetreenavigatorexample.injection.MainComponent;

/**
 * Created by Owner on 2017. 03. 17..
 */

public interface Key {
    @LayoutRes int layout();

    void bindServices(ServiceTree.Node.Binder binder);
}
