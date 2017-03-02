package com.zhuinden.servicetree;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Zhuinden on 2017.03.01..
 */

public class ServiceTreeTest {
    @Test
    public void addingToRootWorks() {
        TestKey testKey = new TestKey("root");
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node node = serviceTree.createRootNode(testKey).get();

        assertThat(serviceTree.hasNodeWithKey(testKey)).isTrue();
        assertThat(serviceTree.getNode(testKey)).isSameAs(node);
        assertThat(serviceTree.getNode(testKey).getKey()).isSameAs(testKey);

        serviceTree.removeNodeAndChildren(node);
        assertThat(serviceTree.hasNodeWithKey(testKey)).isFalse();
    }

    @Test
    public void addingChildToRootWorks() {
        TestKey testKey = new TestKey("root");
        TestKey childKey = new TestKey("childKey");
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node parent = serviceTree.createRootNode(testKey).get();
        ServiceTree.Node child = serviceTree.createChildNode(serviceTree.getNode(testKey), childKey).get();
        assertThat(child.getParent()).isSameAs(parent);
        assertThat(child.getKey()).isSameAs(childKey);

        serviceTree.removeNodeAndChildren(parent);
        assertThat(serviceTree.hasNodeWithKey(childKey)).isFalse();
        assertThat(serviceTree.hasNodeWithKey(testKey)).isFalse();
    }

    @Test
    public void bindingServiceWorks() {
        TestKey rootKey = new TestKey("root");
        Object service = new Object();
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node node = serviceTree.createRootNode(rootKey).bindService("SERVICE", service).get();
        List<ServiceTree.Node.Entry> entries = node.getBoundServices();
        boolean didFind = false;
        for(ServiceTree.Node.Entry entry : entries) {
            if(entry.getService() == service) {
                didFind = true;
                break;
            }
        }
        assertThat(didFind).isTrue();
        assertThat(node.getService("SERVICE")).isSameAs(service);
    }

    @Test
    public void bindingServiceToRootIsInheritedByChild() {
        TestKey rootKey = new TestKey("root");
        TestKey childKey = new TestKey("child");
        Object service = new Object();
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node node = serviceTree.createRootNode(rootKey).bindService("SERVICE", service).get();
        ServiceTree.Node.Binder childBinder = serviceTree.createChildNode(node, childKey);
        ServiceTree.Node child = childBinder.get();

        assertThat(childBinder.getService("SERVICE")).isSameAs(service);
        assertThat(child.getService("SERVICE")).isSameAs(service);
    }

    @Test
    public void rootCannotBeRemoved() {
        TestKey rootKey = new TestKey("root");
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node node = serviceTree.createRootNode(rootKey).get();
        serviceTree.removeAllNodes();

        assertThat(serviceTree.hasNodeWithKey(ServiceTree.ROOT_KEY)).isTrue();
        assertThat(serviceTree.hasNodeWithKey(node)).isFalse();
    }

    @Test
    public void rootServiceRegisteredIsAccessibleByChildren() {
        TestKey rootKey = new TestKey("root");
        TestKey childKey = new TestKey("child");
        Object service = new Object();
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node node = serviceTree.createRootNode(rootKey).get();
        ServiceTree.Node.Binder childBinder = serviceTree.createChildNode(node, childKey);
        ServiceTree.Node child = childBinder.get();

        serviceTree.registerRootService("SERVICE", service);

        assertThat(child.getService("SERVICE")).isSameAs(service);
        assertThat(node.getService("SERVICE")).isSameAs(service);

        assertThat(serviceTree.unregisterRootService("SERVICE")).isSameAs(service);

        assertThat(child.getService("SERVICE")).isNull();
        assertThat(node.getService("SERVICE")).isNull();
    }
}
