package com.zhuinden.servicetreeviewexample;

import com.zhuinden.servicetreeviewexample.injection.ActivityScope;
import com.zhuinden.simplestack.Backstack;

import javax.inject.Inject;

/**
 * Created by Owner on 2017. 03. 17..
 */
@ActivityScope
public class BackstackHolder {
    @Inject
    public BackstackHolder() {
    }

    private Backstack backstack;

    public Backstack getBackstack() {
        return backstack;
    }

    public void setBackstack(Backstack backstack) {
        this.backstack = backstack;
    }
}
