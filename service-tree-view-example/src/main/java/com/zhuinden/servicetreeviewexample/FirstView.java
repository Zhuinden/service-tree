package com.zhuinden.servicetreeviewexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zhuinden.servicetreeviewexample.injection.FirstComponent;
import com.zhuinden.simplestack.Backstack;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Owner on 2017. 03. 07..
 */

public class FirstView
        extends LinearLayout {
    public static final String TAG = "FirstView";

    @Inject
    BackstackHolder backstackHolder;

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
            FirstComponent firstComponent = Services.getNode(Backstack.getKey(getContext())).getService(Services.DAGGER_COMPONENT);
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
        backstackHolder.getBackstack().goTo(SecondKey.create());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
}
