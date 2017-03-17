package com.zhuinden.servicetreeconductorexample;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeconductorexample.injection.ControllerScope;
import com.zhuinden.servicetreeconductorexample.injection.DaggerSecondComponent;
import com.zhuinden.servicetreeconductorexample.injection.MainComponent;
import com.zhuinden.servicetreeconductorexample.injection.SecondComponent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Owner on 2017. 03. 17..
 */
@ControllerScope(SecondController.class)
public class SecondController
        extends Controller {
    public static final String TAG = "SecondController";

    SecondComponent secondComponent;

    public SecondController() {
        ServiceTree serviceTree = Services.getTree();
        ServiceTree.Node parentNode = Services.getNode(MainActivity.TAG);
        MainComponent mainComponent = parentNode.getService(Services.DAGGER_COMPONENT);
        secondComponent = DaggerSecondComponent.builder().mainComponent(mainComponent).build();
        serviceTree.createChildNode(parentNode, TAG).bindService(Services.DAGGER_COMPONENT, secondComponent);
        secondComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        ServiceTree serviceTree = Services.getTree();
        serviceTree.removeNodeAndChildren(serviceTree.getNode(TAG));
        super.onDestroy();
    }

    Unbinder unbinder;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.path_second, container, false);
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
