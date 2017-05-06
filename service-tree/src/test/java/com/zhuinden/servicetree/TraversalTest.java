package com.zhuinden.servicetree;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Zhuinden on 2017.03.01..
 */

public class TraversalTest {
    TestKey root1;
    TestKey root2;
    TestKey child1A;
    TestKey child1B;
    TestKey child2A;
    TestKey child2B;
    TestKey child1A1;
    TestKey child1A2;
    TestKey child1B1;
    TestKey child1B2;

    ServiceTree serviceTree;

    ServiceTree.Node root1Node;
    ServiceTree.Node root2Node;
    ServiceTree.Node child1ANode;
    ServiceTree.Node child1BNode;
    ServiceTree.Node child2ANode;
    ServiceTree.Node child2BNode;

    ServiceTree.Node child1A1Node;
    ServiceTree.Node child1A2Node;
    ServiceTree.Node child1B1Node;
    ServiceTree.Node child1B2Node;

    @Before
    public void setup() {
        root1 = new TestKey("root1");
        root2 = new TestKey("root2");
        child1A = new TestKey("child1A");
        child1B = new TestKey("child1B");
        child2A = new TestKey("child2A");
        child2B = new TestKey("child2B");
        child1A1 = new TestKey("child1A1");
        child1A2 = new TestKey("child1A2");
        child1B1 = new TestKey("child1B1");
        child1B2 = new TestKey("child1B2");

        serviceTree = new ServiceTree();

        root1Node = serviceTree.createRootNode(root1);
        child1ANode = serviceTree.createChildNode(root1Node, child1A);
        child1BNode = serviceTree.createChildNode(root1Node, child1B);

        root2Node = serviceTree.createRootNode(root2);
        child2ANode = serviceTree.createChildNode(root2Node, child2A);
        child2BNode = serviceTree.createChildNode(root2Node, child2B);

        child1A1Node = serviceTree.createChildNode(child1ANode, child1A1);
        child1A2Node = serviceTree.createChildNode(child1ANode, child1A2);
        child1B1Node = serviceTree.createChildNode(child1BNode, child1B1);
        child1B2Node = serviceTree.createChildNode(child1BNode, child1B2);
    }

    @Test
    public void traversePreOrderWorksAsIntended() {
        final List<ServiceTree.Node> nodes = new LinkedList<>();
        serviceTree.traverseTree(ServiceTree.Walk.PRE_ORDER, new ServiceTree.Walk() {
            @Override
            public void execute(@NonNull ServiceTree.Node node, @NonNull CancellationToken cancellationToken) {
                nodes.add(node);
            }
        });
        assertThat(nodes).containsExactly( //
                root1Node,
                child1ANode,
                child1A1Node,
                child1A2Node,
                child1BNode,
                child1B1Node,
                child1B2Node,
                root2Node,
                child2ANode,
                child2BNode);
    }

    @Test
    public void traversePreorderCancelWorksAsIntended() {
        final List<ServiceTree.Node> nodes = new LinkedList<>();
        serviceTree.traverseTree(ServiceTree.Walk.PRE_ORDER, new ServiceTree.Walk() {
            @Override
            public void execute(@NonNull ServiceTree.Node node, @NonNull CancellationToken cancellationToken) {
                nodes.add(node);
                if(node == child1B1Node) {
                    cancellationToken.cancel();
                }
            }
        });
        assertThat(nodes).containsExactly(root1Node, child1ANode, child1A1Node, child1A2Node, child1BNode, child1B1Node);
    }

    @Test
    public void traversePostOrderWorksAsIntended() {
        final List<ServiceTree.Node> nodes = new LinkedList<>();
        serviceTree.traverseTree(ServiceTree.Walk.POST_ORDER, new ServiceTree.Walk() {
            @Override
            public void execute(@NonNull ServiceTree.Node node, @NonNull CancellationToken cancellationToken) {
                nodes.add(node);
                if(child1A1Node == node) {
                    cancellationToken.cancel();
                }
            }
        });
        assertThat(nodes).containsExactly( //
                child2BNode, child2ANode, root2Node, child1B2Node, child1B1Node, child1BNode, child1A2Node, child1A1Node);
    }

    @Test
    public void traverseChainWorksAsIntended() {
        final List<ServiceTree.Node> nodes = new LinkedList<>();
        serviceTree.traverseChain(child1B2Node, new ServiceTree.Walk() {
            @Override
            public void execute(@NonNull ServiceTree.Node node, @NonNull CancellationToken cancellationToken) {
                nodes.add(node);
            }
        });
        assertThat(nodes).containsExactly(child1B2Node, child1BNode, root1Node);
    }

    @Test
    public void traverseChainCancelWorksAsIntended() {
        final List<ServiceTree.Node> nodes = new LinkedList<>();
        serviceTree.traverseChain(child1B2Node, new ServiceTree.Walk() {
            @Override
            public void execute(@NonNull ServiceTree.Node node, @NonNull CancellationToken cancellationToken) {
                nodes.add(node);
                if(node == child1BNode) {
                    cancellationToken.cancel();
                }
            }
        });
        assertThat(nodes).containsExactly(child1B2Node, child1BNode);
    }

    @Test
    public void traverseChainPreOrderWorksAsIntended() {
        final List<ServiceTree.Node> nodes = new LinkedList<>();
        serviceTree.traverseChain(child1ANode, ServiceTree.Walk.PRE_ORDER, new ServiceTree.Walk() {
            @Override
            public void execute(@NonNull ServiceTree.Node node, @NonNull CancellationToken cancellationToken) {
                nodes.add(node);
            }
        });
        assertThat(nodes).containsExactly(root1Node, child1ANode);
    }

    @Test
    public void traverseChainPreOrderWorksAsIntended2() {
        final List<ServiceTree.Node> nodes = new LinkedList<>();
        serviceTree.traverseChain(child1A1Node, ServiceTree.Walk.PRE_ORDER, new ServiceTree.Walk() {
            @Override
            public void execute(@NonNull ServiceTree.Node node, @NonNull CancellationToken cancellationToken) {
                nodes.add(node);
            }
        });
        assertThat(nodes).containsExactly(root1Node, child1ANode, child1A1Node);
    }

    @Test
    public void traverseChainPreOrderCancelWorksAsIntended() {
        final List<ServiceTree.Node> nodes = new LinkedList<>();
        serviceTree.traverseChain(child1A1Node, ServiceTree.Walk.PRE_ORDER, new ServiceTree.Walk() {
            @Override
            public void execute(@NonNull ServiceTree.Node node, @NonNull CancellationToken cancellationToken) {
                nodes.add(node);
                if(child1ANode == node) {
                    cancellationToken.cancel();
                }
            }
        });
        assertThat(nodes).containsExactly(root1Node, child1ANode);
    }

    @Test
    public void findRootFindsRoot() {
        assertThat(serviceTree.findRoot(root1Node)).isSameAs(root1Node);
    }

    @Test
    public void findRootForChildFindsRoot() {
        assertThat(serviceTree.findRoot(child1A2Node)).isSameAs(root1Node);
    }
}
