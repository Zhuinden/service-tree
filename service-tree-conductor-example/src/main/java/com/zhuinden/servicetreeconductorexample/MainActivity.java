package com.zhuinden.servicetreeconductorexample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeconductorexample.injection.ApplicationComponent;
import com.zhuinden.servicetreeconductorexample.injection.CustomApplication;
import com.zhuinden.servicetreeconductorexample.injection.DaggerMainComponent;
import com.zhuinden.servicetreeconductorexample.injection.MainComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private ServiceTree serviceTree;

    @BindView(R.id.root)
    RelativeLayout root;

    Router router;

    List<RouterTransaction> previousState = Collections.emptyList();
    ControllerChangeHandler.ControllerChangeListener controllerChangeListener = new ControllerChangeHandler.ControllerChangeListener() {
        @Override
        public void onChangeStarted(@Nullable Controller to, @Nullable Controller from, boolean isPush, @NonNull ViewGroup container, @NonNull ControllerChangeHandler handler) {

        }

        @Override
        public void onChangeCompleted(@Nullable Controller to, @Nullable Controller from, boolean isPush, @NonNull ViewGroup container, @NonNull ControllerChangeHandler handler) {
            List<RouterTransaction> newState = router.getBackstack();
            Log.d("MainActivity",
                    Arrays.toString(previousState.toArray()) + " :: " + Arrays.toString(newState.toArray()));
            for(RouterTransaction previousKey : previousState) {
                if(!newState.contains(previousKey)) {
                    serviceTree.removeNodeAndChildren(serviceTree.getNode(previousKey));
                    Log.d("MainActivity", "Destroying [" + previousKey + "]");
                }
            }
            for(RouterTransaction newKey : newState) {
                if(!serviceTree.hasNodeWithKey(newKey)) {
                    ((BaseController) newKey.controller()).bindServices(serviceTree.createChildNode(serviceTree.getNode(
                            TAG), newKey));
                    Log.d("MainActivity", "Bind service to [" + newKey + "]");
                }
            }
            previousState = router.getBackstack();
        }
    };

    MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serviceTree = Services.getTree();
        if(!serviceTree.hasNodeWithKey(TAG)) {
            ServiceTree.Node node = serviceTree.createRootNode(TAG);
            ApplicationComponent applicationComponent = node.getService(Services.DAGGER_COMPONENT);
            mainComponent = DaggerMainComponent.builder().applicationComponent(applicationComponent).build();
            node.bindService(Services.DAGGER_COMPONENT, mainComponent);
        } else {
            mainComponent = Services.getNode(TAG).getService(Services.DAGGER_COMPONENT);
        }
        mainComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        router = Conductor.attachRouter(this, root, savedInstanceState);
        router.addChangeListener(controllerChangeListener);
        if(!router.hasRootController()) {
            Log.d("MainActivity", "Set root [FirstController]");
            router.setRoot(RouterTransaction.with(new FirstController()));
        }
        previousState = router.getBackstack();
    }

    @Override
    public void onBackPressed() {
        if(!router.handleBack()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        router.removeChangeListener(controllerChangeListener);
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

