package com.zhuinden.servicetreefragmentexample;

import com.zhuinden.servicetree.ServiceTree;

/**
 * Created by Owner on 2017. 03. 07..
 */

public interface HasServices {
    String getNodeTag();

    void bindServices(ServiceTree.Node.Binder binder);
}
