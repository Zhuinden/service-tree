package com.zhuinden.servicetreefragmentexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.zhuinden.servicetree.ServiceTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private List<String> activeTags = new ArrayList<>();

    private ServiceTree serviceTree;

    class BackstackListener implements FragmentManager.OnBackStackChangedListener {
        @Override
        public void onBackStackChanged() {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if(fragments == null) {
                fragments = Collections.emptyList(); // active fragments is initially NULL instead of empty list
            }
            List<String> newTags = new LinkedList<>();
            for(Fragment fragment : fragments) {
                if(fragment != null && fragment instanceof HasServices) { // active fragments is a list that can have NULL element
                    HasServices serviceFragment = ((HasServices)fragment);
                    String newTag = serviceFragment.getNodeTag();
                    newTags.add(newTag);
                    registerFragmentServices(fragment);
                }
            }
            for(String activeTag : activeTags) {
                if(!newTags.contains(activeTag)) {
                    serviceTree.removeNodeAndChildren(serviceTree.getNode(activeTag));
                }
            }
            activeTags = newTags;
        }
    }

    public void registerFragmentServices(Fragment fragment) {
        if(fragment != null && fragment instanceof HasServices) { // active fragments is a list that can have NULL element
            HasServices serviceFragment = ((HasServices)fragment);
            String newTag = serviceFragment.getNodeTag();
            if(!serviceTree.hasNodeWithKey(newTag)) {
                serviceFragment.bindServices(serviceTree.createRootNode(newTag));
            }
        }
    }

    @BindView(R.id.root)
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serviceTree = (ServiceTree)getLastCustomNonConfigurationInstance();
        if(serviceTree == null) {
            serviceTree = new ServiceTree();
            serviceTree.registerRootService(DaggerService.TAG, DaggerMainComponent.create());
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportFragmentManager().addOnBackStackChangedListener(new BackstackListener());
        if(savedInstanceState == null) {
            Fragment firstFragment = new FirstFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.root, firstFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return serviceTree;
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Object getSystemService(String name) {
        if(name.equals(TAG)) {
            return this;
        }
        if(name.equals(Nodes.TAG)) {
            return serviceTree;
        }
        return super.getSystemService(name);
    }

    public static MainActivity get(Context context) {
        // noinspection ResourceType
        return (MainActivity)context.getSystemService(TAG);
    }

    public void goToSecond() {
        getSupportFragmentManager().beginTransaction().replace(R.id.root, new SecondFragment()).addToBackStack(null).commit();
    }
}

