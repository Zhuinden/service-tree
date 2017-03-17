package com.zhuinden.servicetreenavigatorexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhuinden.navigator.Navigator;
import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreenavigatorexample.injection.ApplicationComponent;
import com.zhuinden.servicetreenavigatorexample.injection.DaggerMainComponent;
import com.zhuinden.servicetreenavigatorexample.injection.Injector;
import com.zhuinden.servicetreenavigatorexample.injection.MainComponent;
import com.zhuinden.simplestack.BackstackDelegate;
import com.zhuinden.simplestack.HistoryBuilder;
import com.zhuinden.simplestack.StateChange;
import com.zhuinden.simplestack.StateChanger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity
        implements StateChanger {
    public static final String TAG = "MainActivity";

    private ServiceTree serviceTree;

    public void goToSecond() {
        Navigator.getBackstack(this).goTo(SecondKey.create());
    }

    @BindView(R.id.root)
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serviceTree = Injector.get().serviceTree();
        if(!serviceTree.hasNodeWithKey(TAG)) {
            ServiceTree.Node.Binder binder = serviceTree.createRootNode(TAG);
            ApplicationComponent applicationComponent = binder.getService(Services.DAGGER_COMPONENT);
            MainComponent mainComponent = DaggerMainComponent.builder().applicationComponent(applicationComponent).build();
            binder.bindService(Services.DAGGER_COMPONENT, mainComponent);
            mainComponent.inject(this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Navigator.configure().setStateChanger(this).install(this, root, HistoryBuilder.single(FirstKey.create()));
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
            Key newKey = (Key)_newKey;
            if(!serviceTree.hasNodeWithKey(newKey)) {
                newKey.bindServices(serviceTree.createChildNode(serviceTree.getNode(TAG), newKey));
            }
        }
        completionCallback.stateChangeComplete();
    }
}

