package com.zhuinden.servicetreenavigatorexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zhuinden.servicetreenavigatorexample.injection.SecondComponent;
import com.zhuinden.simplestack.Backstack;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Owner on 2017. 03. 17..
 */
public class SecondView
        extends LinearLayout {

    Unbinder unbinder;

    public SecondView(Context context) {
        super(context);
        init(context);
    }

    public SecondView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SecondView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public SecondView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        if(!isInEditMode()) {
            SecondComponent secondComponent = Services.getNode(Backstack.getKey(context)).getService(Services.DAGGER_COMPONENT);
            secondComponent.inject(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
}
