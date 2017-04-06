package com.zhuinden.servicetreeconductorexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeconductorexample.injection.ApplicationComponent;
import com.zhuinden.servicetreeconductorexample.injection.CustomApplication;
import com.zhuinden.servicetreeconductorexample.injection.DaggerMainComponent;
import com.zhuinden.servicetreeconductorexample.injection.MainComponent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private ServiceTree serviceTree;

    @BindView(R.id.root)
    RelativeLayout root;

    Router router;

    MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serviceTree = Services.getTree();
        if(!serviceTree.hasNodeWithKey(TAG)) {
            ServiceTree.Node node = serviceTree.createChildNode(Services.getNode(CustomApplication.SCOPE_KEY), TAG);
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

