package com.zhuinden.servicetreenavigatorexample;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.simplestack.navigator.StateKey;

/**
 * Created by Owner on 2017. 03. 17..
 */

public interface Key
        extends StateKey {
    void bindServices(ServiceTree.Node node);
}
