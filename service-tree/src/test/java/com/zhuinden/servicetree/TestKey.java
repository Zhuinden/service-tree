package com.zhuinden.servicetree;

/**
 * Created by Zhuinden on 2017.03.01..
 */

public class TestKey {
    private String name;

    public TestKey(String name) {
        if(name == null) {
            throw new NullPointerException();
        }
        this.name = name;
    }

    @Override
    public int hashCode() {
        return TestKey.class.hashCode() + 37*name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof TestKey && ((TestKey)obj).name.equals(name);
    }

    @Override
    public String toString() {
        return "TestKey[" + name + "]";
    }
}
