package com.zhuinden.servicetreenavigatorexample;

import com.zhuinden.navigator.StateKey;
import com.zhuinden.servicetree.ServiceTree;

/**
 * Created by Owner on 2017. 03. 17..
 */

public interface Key
        extends StateKey {
    void bindServices(ServiceTree.Node node);
}
