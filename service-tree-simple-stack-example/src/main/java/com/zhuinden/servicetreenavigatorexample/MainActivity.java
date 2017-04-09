package com.zhuinden.servicetreenavigatorexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreenavigatorexample.injection.ApplicationComponent;
import com.zhuinden.servicetreenavigatorexample.injection.CustomApplication;
import com.zhuinden.servicetreenavigatorexample.injection.DaggerMainComponent;
import com.zhuinden.servicetreenavigatorexample.injection.Injector;
import com.zhuinden.servicetreenavigatorexample.injection.MainComponent;
import com.zhuinden.simplestack.HistoryBuilder;
import com.zhuinden.simplestack.StateChange;
import com.zhuinden.simplestack.StateChanger;
import com.zhuinden.simplestack.navigator.DefaultStateChanger;
import com.zhuinden.simplestack.navigator.Navigator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity
        implements StateChanger {
    public static final String TAG = "MainActivity";

    private ServiceTree serviceTree;

    @BindView(R.id.root)
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serviceTree = Injector.get().serviceTree();
        MainComponent mainComponent;
        if(!serviceTree.hasNodeWithKey(TAG)) {
            ServiceTree.Node node = serviceTree.createChildNode(serviceTree.getNode(CustomApplication.SCOPE_KEY), TAG);
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
        Navigator.configure()
                .setStateChanger(DefaultStateChanger.configure().setExternalStateChanger(this).create(this, root))
                .install(this, root, HistoryBuilder.single(FirstKey.create()));
    }

    @Override
    public void onBackPressed() {
        if(!Navigator.onBackPressed(this)) {
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

    @Override
    public void handleStateChange(StateChange stateChange, StateChanger.Callback completionCallback) {
        for(Object previousKey : stateChange.getPreviousState()) {
            if(!stateChange.getNewState().contains(previousKey)) {
                serviceTree.removeNodeAndChildren(serviceTree.getNode(previousKey));
            }
        }
        for(Object _newKey : stateChange.getNewState()) {
            Key newKey = (Key) _newKey;
            if(!serviceTree.hasNodeWithKey(newKey)) {
                newKey.bindServices(serviceTree.createChildNode(serviceTree.getNode(TAG), newKey));
            }
        }
        completionCallback.stateChangeComplete();
    }
}

