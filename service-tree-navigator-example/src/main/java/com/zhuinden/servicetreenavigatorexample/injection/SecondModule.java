package com.zhuinden.servicetreenavigatorexample.injection;

import com.zhuinden.servicetreenavigatorexample.SecondKey;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Owner on 2017. 03. 17..
 */

@Module
public class SecondModule {
    private SecondKey secondKey;

    public SecondModule(SecondKey secondKey) {
        this.secondKey = secondKey;
    }

    @Provides
    public SecondKey secondKey() {
        return secondKey;
    }
}
