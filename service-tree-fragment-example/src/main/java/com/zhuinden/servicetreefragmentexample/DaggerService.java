package com.zhuinden.servicetreefragmentexample;

import com.zhuinden.servicetree.ServiceTree;

/**
 * Created by Owner on 2017. 03. 07..
 */

public class DaggerService {
    public static final String TAG = "DaggerComponent";

    public static <T> T getService(ServiceTree.Node node) {
        return node.getService(TAG);
    }

    public static <T> T bind(ServiceTree.Node.Binder binder, T component) {
        binder.bindService(TAG, component);
        return component;
    }
}
