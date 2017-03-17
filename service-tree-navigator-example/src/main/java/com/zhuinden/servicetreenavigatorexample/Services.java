package com.zhuinden.servicetreenavigatorexample;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreenavigatorexample.injection.Injector;

/**
 * Created by Zhuinden on 2017.03.07..
 */

public class Services {
    public static final String DAGGER_COMPONENT = "DAGGER_COMPONENT";

    public static ServiceTree getTree() {
        return Injector.get().serviceTree();
    }

    public static ServiceTree.Node getNode(Object key) {
        return getTree().getNode(key);
    }

    private Services() {
    }
}
