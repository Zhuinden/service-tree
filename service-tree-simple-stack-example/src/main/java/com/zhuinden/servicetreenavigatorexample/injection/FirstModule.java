package com.zhuinden.servicetreenavigatorexample.injection;

import com.zhuinden.servicetreenavigatorexample.FirstKey;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Owner on 2017. 03. 17..
 */

@Module
public class FirstModule {
    private FirstKey firstKey;

    public FirstModule(FirstKey firstKey) {
        this.firstKey = firstKey;
    }

    @Provides
    public FirstKey firstKey() {
        return firstKey;
    }
}
