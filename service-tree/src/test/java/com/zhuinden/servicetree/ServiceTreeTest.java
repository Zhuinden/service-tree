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
        ServiceTree.Node node = serviceTree.createRootNode(testKey);

        assertThat(serviceTree.hasNodeWithKey(testKey)).isTrue();
        assertThat(serviceTree.getNode(testKey)).isSameAs(node);
        assertThat(serviceTree.getNode(testKey).getKey()).isSameAs(testKey);

        assertThat(node.getTree()).isSameAs(serviceTree);

        serviceTree.removeNodeAndChildren(node);
        assertThat(serviceTree.hasNodeWithKey(testKey)).isFalse();
    }

    @Test
    public void addingChildToRootWorks() {
        TestKey testKey = new TestKey("root");
        TestKey childKey = new TestKey("childKey");
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node parent = serviceTree.createRootNode(testKey);
        ServiceTree.Node child = serviceTree.createChildNode(serviceTree.getNode(testKey), childKey);
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
        ServiceTree.Node node = serviceTree.createRootNode(rootKey).bindService("SERVICE", service);
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
        assertThat(node.hasBoundService("SERVICE")).isTrue();
    }

    @Test
    public void bindingServiceToRootIsInheritedByChild() {
        TestKey rootKey = new TestKey("root");
        TestKey childKey = new TestKey("child");
        Object service = new Object();
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node node = serviceTree.createRootNode(rootKey).bindService("SERVICE", service);
        ServiceTree.Node child = serviceTree.createChildNode(node, childKey);

        assertThat(node.hasBoundService("SERVICE")).isTrue();
        assertThat(child.hasBoundService("SERVICE")).isFalse();
        assertThat(child.getService("SERVICE")).isSameAs(service);
        assertThat(child.getService("SERVICE")).isSameAs(service);

    }

    @Test
    public void rootIsNotInNodeMap() {
        ServiceTree serviceTree = new ServiceTree();
        assertThat(serviceTree.hasNodeWithKey(serviceTree.root.getKey())).isFalse();
    }

    @Test
    public void rootNodeIsRemoved() {
        TestKey rootKey = new TestKey("root");
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node node = serviceTree.createRootNode(rootKey);
        serviceTree.removeAllNodes();

        assertThat(serviceTree.hasNodeWithKey(node)).isFalse();
    }

    @Test
    public void rootServiceRegisteredIsAccessibleByChildren() {
        TestKey rootKey = new TestKey("root");
        TestKey childKey = new TestKey("child");
        Object service = new Object();
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node node = serviceTree.createRootNode(rootKey);
        ServiceTree.Node child = serviceTree.createChildNode(node, childKey);

        node.bindService("SERVICE", service);

        assertThat(child.getService("SERVICE")).isSameAs(service);
        assertThat(node.getService("SERVICE")).isSameAs(service);

        assertThat(child.hasService("SERVICE")).isTrue();
        assertThat(node.hasService("SERVICE")).isTrue();

        try {
            child.getService("SERVICE");
        } catch(IllegalArgumentException e) {
            // OK!
            assertThat(child.hasService("SERVICE")).isFalse();
        }
        try {
            node.getService("SERVICE");
        } catch(IllegalArgumentException e) {
            // OK!
            assertThat(node.hasService("SERVICE")).isFalse();
        }
    }

    @Test
    public void childrenAddedToNodeAreAccessible() {
        TestKey rootKey = new TestKey("root");
        TestKey test1Key = new TestKey("test1");
        TestKey test2Key = new TestKey("test2");
        TestKey test3Key = new TestKey("test3");
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node root = serviceTree.createRootNode(rootKey);
        ServiceTree.Node test1 = serviceTree.createChildNode(root, test1Key);
        assertThat(serviceTree.getNode(rootKey).getChildren()).containsExactly(test1);
        ServiceTree.Node test2 = serviceTree.createChildNode(root, test2Key);
        assertThat(serviceTree.getNode(rootKey).getChildren()).containsExactly(test1, test2);
        ServiceTree.Node test3 = serviceTree.createChildNode(root, test3Key);
        assertThat(serviceTree.getNode(rootKey).getChildren()).containsExactly(test1, test2, test3);

        serviceTree.removeNodeAndChildren(test2);

        List<ServiceTree.Node> children = serviceTree.getNode(rootKey).getChildren();
        assertThat(children).containsExactly(test1, test3);

        serviceTree.removeNodeAndChildren(root);
        assertThat(children).containsExactly(test1, test3); // children are immutable
    }

    @Test
    public void walkCannotBeNull() {
        ServiceTree serviceTree = new ServiceTree();
        try {
            serviceTree.traverseTree(ServiceTree.Walk.POST_ORDER, null);
        } catch(NullPointerException e) {
            // OK!
        }
    }

    @Test
    public void serviceCannotBeNull() {
        TestKey rootKey = new TestKey("root");
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node root = serviceTree.createRootNode(rootKey);
        try {
            root.bindService("SERVICE", null);
        } catch(NullPointerException e) {
            // OK!
        }
    }

    @Test
    public void nodeCannotBeNonexistent() {
        ServiceTree serviceTree = new ServiceTree();
        assertThat(serviceTree.hasNodeWithKey("notintree")).isFalse();
        try {
            serviceTree.getNode("notintree");
        } catch(IllegalStateException e) {
            // OK!
        }
    }

    @Test
    public void serviceNameCannotBeNull() {
        TestKey rootKey = new TestKey("root");
        ServiceTree serviceTree = new ServiceTree();
        ServiceTree.Node root = serviceTree.createRootNode(rootKey);
        try {
            root.hasService(null);
        } catch(NullPointerException e) {
            // OK!
        }
    }

    @Test
    public void keyCannotBeNull() {
        ServiceTree serviceTree = new ServiceTree();
        try {
            serviceTree.hasNodeWithKey(null);
        } catch(NullPointerException e) {
            // OK!
        }
    }

    @Test
    public void nodeCannotBeNull() {
        ServiceTree serviceTree = new ServiceTree();
        try {
            serviceTree.createChildNode(null, "key");
        } catch(NullPointerException e) {
            // OK!
        }
    }
}
