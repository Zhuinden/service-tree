package com.zhuinden.servicetreeconductorexample;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.RouterTransaction;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeconductorexample.injection.ControllerScope;
import com.zhuinden.servicetreeconductorexample.injection.DaggerFirstComponent;
import com.zhuinden.servicetreeconductorexample.injection.FirstComponent;
import com.zhuinden.servicetreeconductorexample.injection.MainComponent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Owner on 2017. 03. 17..
 */
@ControllerScope(FirstController.class)
public class FirstController
        extends BaseController {
    public static final String TAG = "FirstController";

    FirstComponent firstComponent;

    @Inject
    ServiceTree serviceTree;

    public FirstController() {
    }

    @OnClick(R.id.first_button)
    public void clickFirst(View view) {
        getRouter().pushController(RouterTransaction.with(new SecondController()));
    }

    Unbinder unbinder;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.path_first, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }

    @Override
    public void bindServices(ServiceTree.Node node) {
        MainComponent mainComponent = node.getService(Services.DAGGER_COMPONENT);
        node.bindService(Services.DAGGER_COMPONENT,
                DaggerFirstComponent.builder().mainComponent(mainComponent).build());
        firstComponent = node.getService(Services.DAGGER_COMPONENT);
        firstComponent.inject(this);
    }
}
