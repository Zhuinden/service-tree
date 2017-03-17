package com.zhuinden.servicetreeconductorexample;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
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
        extends Controller {
    public static final String TAG = "FirstController";

    FirstComponent firstComponent;

    @Inject
    ServiceTree serviceTree;

    public FirstController() {
        ServiceTree serviceTree = Services.getTree();
        ServiceTree.Node parentNode = Services.getNode(MainActivity.TAG);
        MainComponent mainComponent = parentNode.getService(Services.DAGGER_COMPONENT);
        firstComponent = DaggerFirstComponent.builder().mainComponent(mainComponent).build();
        serviceTree.createChildNode(parentNode, TAG).bindService(Services.DAGGER_COMPONENT, firstComponent);
        firstComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        serviceTree.removeNodeAndChildren(serviceTree.getNode(TAG));
        super.onDestroy();
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
}
