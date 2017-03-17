package com.zhuinden.servicetreeconductorexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeconductorexample.injection.ApplicationComponent;
import com.zhuinden.servicetreeconductorexample.injection.DaggerFirstComponent;
import com.zhuinden.servicetreeconductorexample.injection.DaggerMainComponent;
import com.zhuinden.servicetreeconductorexample.injection.DaggerSecondComponent;
import com.zhuinden.servicetreeconductorexample.injection.FirstComponent;
import com.zhuinden.servicetreeconductorexample.injection.Injector;
import com.zhuinden.servicetreeconductorexample.injection.MainComponent;
import com.zhuinden.servicetreeconductorexample.injection.SecondComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private ServiceTree serviceTree;

    public void goToSecond() {
        router.pushController(RouterTransaction.with(new SecondController()));
    }

    @BindView(R.id.root)
    RelativeLayout root;

    Router router;

    MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serviceTree = Injector.get().serviceTree();
        if(!serviceTree.hasNodeWithKey(TAG)) {
            ServiceTree.Node.Binder binder = serviceTree.createRootNode(TAG);
            ApplicationComponent applicationComponent = binder.getService(Services.DAGGER_COMPONENT);
            mainComponent = DaggerMainComponent.builder().applicationComponent(applicationComponent).build();
            binder.bindService(Services.DAGGER_COMPONENT, mainComponent);
            mainComponent.inject(this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        router = Conductor.attachRouter(this, root, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new FirstController()));
        }
    }

    @Override
    public void onBackPressed() {
        if(!router.handleBack()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            serviceTree.removeNodeAndChildren(serviceTree.getNode(TAG));
        }
    }

    // share activity
    @Override
    public Object getSystemService(String name) {
        if(name.equals(TAG)) {
            return this;
        }
        return super.getSystemService(name);
    }

    public static MainActivity get(Context context) {
        // noinspection ResourceType
        return (MainActivity) context.getSystemService(TAG);
    }
}

