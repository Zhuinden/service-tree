package com.zhuinden.servicetreenavigatorexample;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreenavigatorexample.injection.Injector;

/**
 * Created by Owner on 2017. 03. 07..
 */

public class Nodes {
    public static ServiceTree.Node getNode(Object nodeKey) {
        return Injector.get().serviceTree().getNode(nodeKey);
    }
}
