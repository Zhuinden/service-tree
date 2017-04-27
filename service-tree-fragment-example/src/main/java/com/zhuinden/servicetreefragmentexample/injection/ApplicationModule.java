package com.zhuinden.servicetreefragmentexample.injection;

import com.zhuinden.servicetree.ServiceTree;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Zhuinden on 2017.03.07..
 */

@Module
public class ApplicationModule {
    @Provides
    @Singleton
    public ServiceTree serviceTree() {
        return new ServiceTree();
    }
}
