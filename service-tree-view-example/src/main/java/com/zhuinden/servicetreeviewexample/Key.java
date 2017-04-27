package com.zhuinden.servicetreeviewexample;

import android.support.annotation.LayoutRes;
import com.zhuinden.servicetree.ServiceTree;

/**
 * Created by Owner on 2017. 03. 17..
 */

public interface Key {
    @LayoutRes int layout();

    void bindServices(ServiceTree.Node node);
}
