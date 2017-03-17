package com.zhuinden.servicetreenavigatorexample;

import android.view.View;

import com.zhuinden.navigator.ViewController;
import com.zhuinden.servicetreenavigatorexample.injection.ControllerScope;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Owner on 2017. 03. 17..
 */
@ControllerScope(SecondKey.class)
public class SecondController
        extends ViewController {
    @Inject
    public SecondController(SecondKey stateKey) {
        super(stateKey);
    }

    Unbinder unbinder;

    @Override
    protected void onViewCreated(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void onViewRestored(View view) {

    }

    @Override
    protected void onViewDestroyed(View view) {
        unbinder.unbind();
        unbinder = null;
    }
}
