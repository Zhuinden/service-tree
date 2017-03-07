package com.zhuinden.servicetreefragmentexample;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreefragmentexample.injection.Injector;

/**
 * Created by Owner on 2017. 03. 07..
 */

public class Nodes {
    public static ServiceTree.Node getNode(String nodeTag) {
        return Injector.get().serviceTree().getNode(nodeTag);
    }
}
