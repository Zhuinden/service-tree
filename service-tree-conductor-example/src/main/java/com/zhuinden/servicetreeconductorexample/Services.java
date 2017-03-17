package com.zhuinden.servicetreeconductorexample;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeconductorexample.injection.Injector;

import javax.inject.Inject;

/**
 * Created by Zhuinden on 2017.03.07..
 */

public class Services {
    public static final String DAGGER_COMPONENT = "DAGGER_COMPONENT";

    public static ServiceTree getTree() {
        return Injector.get().serviceTree();
    }

    private Services() {
    }
}
