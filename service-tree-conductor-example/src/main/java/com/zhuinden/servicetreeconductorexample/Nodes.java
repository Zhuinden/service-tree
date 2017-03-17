package com.zhuinden.servicetreeconductorexample;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeconductorexample.injection.Injector;

/**
 * Created by Owner on 2017. 03. 07..
 */

public class Nodes {
    public static ServiceTree.Node getNode(Object nodeKey) {
        return Services.getTree().getNode(nodeKey);
    }
}
