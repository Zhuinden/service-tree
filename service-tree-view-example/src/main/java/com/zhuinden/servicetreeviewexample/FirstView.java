package com.zhuinden.servicetreeviewexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhuinden.servicetree.ServiceTree;
import com.zhuinden.servicetreeviewexample.injection.DaggerFirstComponent;
import com.zhuinden.servicetreeviewexample.injection.FirstComponent;
import com.zhuinden.servicetreeviewexample.injection.MainComponent;
import com.zhuinden.simplestack.Backstack;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Owner on 2017. 03. 07..
 */

public class FirstView
        extends LinearLayout {
    public static final String TAG = "FirstView";

    public FirstView(Context context) {
        super(context);
        init(context);
    }

    public FirstView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FirstView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if(!isInEditMode()) {
            FirstComponent firstComponent = Nodes.getNode(Backstack.getKey(getContext())).getService(Services.DAGGER_COMPONENT);
            firstComponent.inject(this);
        }
    }

    @TargetApi(21)
    public FirstView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @OnClick(R.id.first_button)
    public void clickFirst() {
        MainActivity.get(getContext()).goToSecond();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
}
