package com.zhuinden.servicetreenavigatorexample;

import android.view.View;

import com.zhuinden.navigator.StateKey;
import com.zhuinden.navigator.ViewController;
import com.zhuinden.servicetreenavigatorexample.injection.SecondComponent;
import com.zhuinden.simplestack.Backstack;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Owner on 2017. 03. 17..
 */
public class SecondController
        extends ViewController {
    public SecondController(StateKey stateKey) {
        super(stateKey);
        SecondComponent secondComponent = Nodes.getNode(stateKey).getService(Services.DAGGER_COMPONENT);
        secondComponent.inject(this);
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
