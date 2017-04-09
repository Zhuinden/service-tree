package com.zhuinden.servicetreeconductorexample;

import com.bluelinelabs.conductor.Controller;
import com.zhuinden.servicetree.ServiceTree;

/**
 * Created by Zhuinden on 2017.04.09..
 */

public abstract class BaseController
        extends Controller {
    public abstract void bindServices(ServiceTree.Node node);
}
