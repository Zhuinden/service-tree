package com.zhuinden.servicetreeviewexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeviewexample.injection.ApplicationComponent;
import com.zhuinden.servicetreeviewexample.injection.DaggerMainComponent;
import com.zhuinden.servicetreeviewexample.injection.Injector;
import com.zhuinden.servicetreeviewexample.injection.MainComponent;
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
        backstackDelegate.getBackstack().goTo(SecondKey.create());
    }

    @BindView(R.id.root)
    RelativeLayout root;

    BackstackDelegate backstackDelegate;

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

        backstackDelegate = new BackstackDelegate(null);
        backstackDelegate.onCreate(savedInstanceState, //
                getLastCustomNonConfigurationInstance(), //
                HistoryBuilder.single(FirstKey.create()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        backstackDelegate.setStateChanger(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return backstackDelegate.onRetainCustomNonConfigurationInstance();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        backstackDelegate.onPostResume();
    }

    @Override
    protected void onPause() {
        backstackDelegate.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        backstackDelegate.persistViewToState(root.getChildAt(0)); // <-- persisting view state
        backstackDelegate.onSaveInstanceState(outState); // <-- persisting backstack + view states
    }

    @Override
    public void onBackPressed() {
        if(!backstackDelegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        backstackDelegate.onDestroy();
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

    // StateChanger implementation
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
        if(stateChange.topNewState().equals(stateChange.topPreviousState())) {
            completionCallback.stateChangeComplete();
            return;
        }
        backstackDelegate.persistViewToState(root.getChildAt(0));
        root.removeAllViews();
        Key newKey = stateChange.topNewState();
        Context newContext = stateChange.createContext(this, newKey);
        View view = LayoutInflater.from(newContext).inflate(newKey.layout(), root, false);
        backstackDelegate.restoreViewFromState(view);
        root.addView(view);
        completionCallback.stateChangeComplete();
    }
}

