package com.zhuinden.servicetreefragmentexample;

import android.content.Context;

import com.zhuinden.servicetree.ServiceTree;

/**
 * Created by Owner on 2017. 03. 07..
 */

public class Services {
    public static final String TAG = "ServiceTree";

    public static ServiceTree get(Context context) {
        // noinspection ResourceType
        return (ServiceTree)context.getSystemService(TAG);
    }
}
