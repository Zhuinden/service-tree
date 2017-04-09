package com.zhuinden.servicetreenavigatorexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zhuinden.servicetreenavigatorexample.injection.FirstComponent;
import com.zhuinden.simplestack.Backstack;
import com.zhuinden.simplestack.navigator.Navigator;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Owner on 2017. 03. 17..
 */
public class FirstView
        extends LinearLayout {
    public FirstView(Context context) {
        super(context);
        init(context);
    }

    public FirstView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FirstView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public FirstView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        if(!isInEditMode()) {
            FirstComponent firstComponent = Services.getNode(Backstack.getKey(context)).getService(Services.DAGGER_COMPONENT);
            firstComponent.inject(this);
        }
    }

    @OnClick(R.id.first_button)
    public void clickFirst(View view) {
        Navigator.getBackstack(view.getContext()).goTo(SecondKey.create());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
}
