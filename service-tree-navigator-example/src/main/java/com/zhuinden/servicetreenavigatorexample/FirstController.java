package com.zhuinden.servicetreenavigatorexample;

import android.content.Context;
import android.view.View;

import com.zhuinden.navigator.StateKey;
import com.zhuinden.navigator.ViewController;
import com.zhuinden.servicetreenavigatorexample.injection.FirstComponent;
import com.zhuinden.simplestack.Backstack;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Owner on 2017. 03. 17..
 */
public class FirstController
        extends ViewController {
    public FirstController(StateKey stateKey) {
        super(stateKey);
        FirstComponent firstComponent = Nodes.getNode(stateKey).getService(Services.DAGGER_COMPONENT);
        firstComponent.inject(this);
    }

    @OnClick(R.id.first_button)
    public void clickFirst(View view) {
        MainActivity.get(view.getContext()).goToSecond();
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
