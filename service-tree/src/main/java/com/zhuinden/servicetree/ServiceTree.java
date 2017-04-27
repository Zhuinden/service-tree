/*
 * Copyright 2017 Gabor Varadi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhuinden.servicetree;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * A tree that is able to hold bound services in each of its {@link Node}.
 *
 * Also makes it possible to traverse through the tree, or any of its subtrees in a particular type of order using {@link Walk}.
 */
public class ServiceTree {
    /**
     * A node that holds its key, its children and its bound services.
     */
    public static final class Node {
        /**
         * An entry that contains a service by its name.
         */
        public static final class Entry {
            private final String name;
            private final Object service;

            Entry(final String name, final Object service) {
                this.name = name;
                this.service = service;
            }

            @NonNull
            public String getName() {
                return name;
            }

            @NonNull
            public <T> T getService() {
                // noinspection unchecked
                return (T) service;
            }
        }

        private final ServiceTree serviceTree;

        private final Node parent;

        private final Object localKey;

        private final Set<Node> children = new LinkedHashSet<>();

        private final Map<String, Object> services = new LinkedHashMap<>();

        Node(ServiceTree serviceTree, Node parent, Object key) {
            this.serviceTree = serviceTree;
            this.parent = parent;
            this.localKey = key;
        }

        /**
         * Returns the key this node is identified by.
         *
         * @param <T> the type of the key
         * @return the key
         */
        @NonNull
        public <T> T getKey() {
            // noinspection unchecked
            return (T) localKey;
        }

        /**
         * Returns whether the service exists, specified by this name. If not found, then it's checked in its parent, and so on.
         * If not found, it returns false.
         *
         * @param name the name of the service
         * @return whether the service exists in the key or its parent chain
         */
        public boolean hasService(@NonNull String name) {
            checkName(name);
            if(services.containsKey(name)) {
                return true;
            } else {
                if(parent == null) {
                    return false;
                } else {
                    return parent.hasService(name);
                }
            }
        }

        /**
         * Returns the service specified by this name. If not found, then it's checked in its parent, and so on.
         * If not found, it returns null.
         *
         * @param name the name of the service
         * @param <T>  the type of the service
         * @return the service
         */
        @NonNull
        public <T> T getService(@NonNull String name) {
            checkName(name);
            if(services.containsKey(name)) {
                // noinspection unchecked
                return (T) services.get(name);
            } else {
                if(parent == null) { // note: this must never be getParent()!
                    throw new IllegalArgumentException("The service [" + name + "] does not exist in the chain!");
                }
                // noinspection unchecked
                return (T) parent.getService(name); // note: this must never be getParent()!
            }
        }

        /**
         * Returns whether there is a service locally bound to this node. It does not check if any parents have this.
         *
         * To check the existence of a service hierarchically, use {@link Node#hasService(String)}.
         *
         * @param name name of the service
         * @return if the node has the service bound to it locally
         */
        public boolean hasBoundService(String name) {
            checkName(name);
            return services.containsKey(name);
        }

        /**
         * Returns the services that are locally bound to this given node.
         *
         * @return a list of {@link Entry} that contains the services and their names.
         */
        @NonNull
        public List<Entry> getBoundServices() {
            List<Entry> entries = new ArrayList<>(services.size());
            for(Map.Entry<String, Object> entry : services.entrySet()) {
                entries.add(new Entry(entry.getKey(), entry.getValue()));
            }
            return entries;
        }

        /**
         * Binds a service to the node.
         *
         * @param name    the name of the service
         * @param service the service
         * @return the node
         */
        @NonNull
        public Node bindService(@NonNull String name, @NonNull Object service) {
            checkName(name);
            checkService(service);
            services.put(name, service);
            return this;
        }

        void execute(Walk walk, Walk.CancellationToken cancellationToken) {
            walk.execute(this, cancellationToken);
        }

        /**
         * Returns the {@link ServiceTree} this {@link ServiceTree.Node} belongs to.
         *
         * @return the tree
         */
        public ServiceTree getTree() {
            return serviceTree;
        }

        /**
         * Returns the parent of this node.
         * Null for root nodes.
         *
         * @return the parent
         */
        @Nullable
        public Node getParent() {
            return parent == serviceTree.root ? null : parent;
        }

        /**
         * Returns if the root has a direct child with the given key.
         *
         * @param key the key of the direct child
         * @return the direct child
         */
        public boolean hasChild(Object key) {
            checkKey(key);
            for(Node node : children) {
                if(node.getKey().equals(key)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns the direct child with the given key.
         *
         * @param key the key of the direct child
         * @return the child
         * @throws IllegalArgumentException if direct child is not found
         */
        @NonNull
        public Node getChild(Object key) {
            checkKey(key);
            for(Node node : children) {
                if(node.getKey().equals(key)) {
                    return node;
                }
            }
            throw new IllegalArgumentException("No child found in node [" + getKey() + "] for key [" + key + "]");
        }

        /**
         * Returns an unmodifiable list of the children bound to this node.
         * The children are in order of insertion.
         *
         * @return the children of this node
         */
        public List<Node> getChildren() {
            return Collections.unmodifiableList(new ArrayList<>(children));
        }

        <T> T removeService(String name) {
            // noinspection unchecked
            return (T) services.remove(name);
        }

        @Override
        public int hashCode() {
            return Node.class.hashCode() + 37 * getKey().hashCode();
        }

        /**
         * Two nodes are equal if their key is the same.
         *
         * @param obj the object
         * @return whether they are equal.
         */
        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof Node && ((Node) obj).getKey().equals(localKey);
        }

        @Override
        public String toString() {
            return "Node[" + localKey + "]";
        }
    }

    private static final Object ROOT_KEY = new RootKey();

    private static final class RootKey {
        @Override
        public int hashCode() {
            return getClass().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof RootKey;
        }

        @Override
        public String toString() {
            return "ServiceRoot[]";
        }
    }

    Node root = new Node(this, null, ROOT_KEY);

    private Map<Object, Node> nodeMap = new LinkedHashMap<>();

    private void addNode(Node node) {
        nodeMap.put(node.getKey(), node);
    }

    /**
     * Returns if there is a node for the given key.
     *
     * @param key The key
     * @return true if the node exists
     */
    public boolean hasNodeWithKey(@NonNull Object key) {
        checkKey(key);
        return nodeMap.containsKey(key);
    }

    private static void checkKey(Object key) {
        if(key == null) {
            throw new NullPointerException("Key cannot be null!");
        }
    }

    /**
     * Creates a node that is the direct child of the root in the tree, and otherwise has no parent.
     *
     * @param nodeKey the key that identifies the node
     * @return the {@link Node} to bind services to
     */
    @NonNull
    public Node createRootNode(@NonNull Object nodeKey) {
        checkKey(nodeKey);
        return createChildNode(root, nodeKey);
    }

    /**
     * Creates a node that is a child of the specified parent node.
     * Children are removed along with their parent. Children inherit the services of their parent.
     *
     * @param parentNode the parent node this node is the child of
     * @param nodeKey    the key that identifies the child node
     * @return the {@link Node} to bind services to
     */
    @NonNull
    public Node createChildNode(@NonNull Node parentNode, @NonNull Object nodeKey) {
        checkNode(parentNode);
        checkKey(nodeKey);
        Node node = new Node(this, parentNode, nodeKey);
        parentNode.children.add(node);
        this.addNode(node);
        return node;
    }

    private void checkNode(Node node) {
        if(node == null) {
            throw new NullPointerException("Node cannot be null!");
        }
    }

    /**
     * Returns the node for the given key. If not found, an illegal state exception is thrown.
     *
     * @param key the key that identifies the node
     * @return the node
     */
    @NonNull
    public Node getNode(@NonNull Object key) {
        checkKey(key);
        if(!hasNodeWithKey(key)) {
            throw new IllegalStateException("Service node does not exist for key [" + key + "]!");
        }
        return nodeMap.get(key);
    }

    /**
     * Traverses the whole tree, including its root key.
     *
     * @param mode the order mode (see {@link ServiceTree.Walk}).
     * @param walk the {@link Walk} that should be executed for each node
     */
    public void traverseTree(@Walk.Mode final int mode, @NonNull final Walk walk) {
        checkWalk(walk);
        traverseSubtree(root, mode, walk);
    }

    private void checkWalk(Walk walk) {
        if(walk == null) {
            throw new NullPointerException("Walk cannot be null!");
        }
    }

    /**
     * Removes all nodes in the tree.
     */
    public void removeAllNodes() {
        removeNodeAndChildren(root);
    }

    /**
     * Removes the node and its children.
     *
     * @param node the node to be removed
     */
    public void removeNodeAndChildren(@NonNull Node node) {
        checkNode(node);
        traverseSubtree(node, Walk.POST_ORDER, new Walk() {
            @Override
            public void execute(@NonNull Node node, @NonNull CancellationToken cancellationToken) {
                node.parent.children.remove(node);
                nodeMap.remove(node.getKey());
            }
        });
    }

    private void traverseSubtree(@NonNull Node node, @Walk.Mode int mode, @NonNull Walk walk, @NonNull Walk.CancellationToken cancellationToken) {
        if(cancellationToken.isCancelled()) {
            return;
        }
        if(mode == Walk.PRE_ORDER) {
            node.execute(walk, cancellationToken);
            if(cancellationToken.isCancelled()) {
                return;
            }
            for(Node child : node.children) {
                traverseSubtree(child, mode, walk, cancellationToken);
            }
        } else if(mode == Walk.POST_ORDER) {
            List<Node> reversedChildren = new LinkedList<>(node.children);
            Collections.reverse(reversedChildren);
            for(Node child : reversedChildren) {
                traverseSubtree(child, mode, walk, cancellationToken);
                if(cancellationToken.isCancelled()) {
                    return;
                }
            }
            node.execute(walk, cancellationToken);
        }
    }

    /**
     * Traverses the subtree for this given node.
     *
     * @param node the {@link Node} from which the traversal should start
     * @param mode the order mode (see {@link Walk})
     * @param walk the {@link Walk} that should be executed for each node
     */
    public void traverseSubtree(@NonNull final Node node, @Walk.Mode final int mode, @NonNull final Walk walk) {
        checkNode(node);
        checkWalk(walk);

        final Cancellation cancellation = new Cancellation();
        Walk.CancellationToken cancellationToken = new CancellationTokenImpl(cancellation);
        traverseSubtree(node, mode, new Walk() {
            @Override
            public void execute(@NonNull Node node, @NonNull CancellationToken cancellationToken) {
                if(node.getKey().equals(ROOT_KEY)) {
                    return;
                }
                walk.execute(node, cancellationToken);
            }
        }, cancellationToken);
    }

    /**
     * Traversing the chain from the node through its parents, including the root.
     *
     * @param node The node that begins the chain traversal towards its parent.
     */
    public void traverseChain(@NonNull Node node, @NonNull Walk walk) {
        checkNode(node);
        checkWalk(walk);

        final Cancellation cancellation = new Cancellation();
        Walk.CancellationToken cancellationToken = new CancellationTokenImpl(cancellation);
        Node currentNode = node;
        while(currentNode != null) {
            currentNode.execute(walk, cancellationToken);
            if(cancellation.isCancelled()) {
                break;
            }
            currentNode = currentNode.getParent();
        }
    }

    private static void checkService(Object service) {
        if(service == null) {
            throw new NullPointerException("Service cannot be null!");
        }
    }

    private static void checkName(String name) {
        if(name == null) {
            throw new NullPointerException("Name cannot be null!");
        }
    }

    /**
     * Return the keys of the registered nodes.
     *
     * @return an unmodifiable set of a copy of the currently registered nodes' keys
     */
    @NonNull
    public Set<Object> getKeys() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(nodeMap.keySet()));
    }

    /**
     * Adds a service to the node of the tree root.
     *
     * @param name    the name of the service
     * @param service the service
     */
    public void registerRootService(@NonNull String name, @NonNull Object service) {
        checkName(name);
        checkService(name);
        root.bindService(name, service);
    }

    /**
     * Gets the service specified by the given name.
     *
     * @param name the name of the service
     * @param <T>  the type of the service
     * @return the service
     */
    @Nullable
    public <T> T getRootService(String name) {
        // noinspection unchecked
        return (T) root.getService(name);
    }

    /**
     * Removes a service from the node of the tree root.
     *
     * @param name the name of the service
     * @param <T>  the type of the service
     * @return the removed service
     */
    public <T> T unregisterRootService(String name) {
        checkName(name);
        // noinspection unchecked
        return (T) root.removeService(name);
    }


    /**
     * The operation that is executed for each node during {@link ServiceTree#traverseTree(int, Walk)} and {@link ServiceTree#traverseSubtree(Node, int, Walk)}.
     */
    public interface Walk {
        /**
         * This method is called for each node that the traversal matches against.
         *
         * @param node              the current node
         * @param cancellationToken the token which allows cancelling the walk
         */
        void execute(@NonNull Node node, @NonNull CancellationToken cancellationToken);

        /**
         * Can be used to cancel the {@link Walk}.
         */
        interface CancellationToken {
            /**
             * Returns if cancel has been called previously.
             *
             * @return true if cancelled
             */
            boolean isCancelled();

            /**
             * Cancels the walk.
             */
            void cancel();
        }

        @Retention(SOURCE)
        @IntDef({PRE_ORDER, POST_ORDER})
        @interface Mode {
        }

        /**
         * Begins from the specified node, and progresses to its children from left to right.
         */
        int PRE_ORDER = 1;

        /**
         * Begins from the bottom right child, and walks upwards the tree, and ends with the specified node.
         */
        int POST_ORDER = 2;
    }

    private static class CancellationTokenImpl
            implements Walk.CancellationToken {
        private Cancellation cancellation;

        CancellationTokenImpl(Cancellation cancellation) {
            this.cancellation = cancellation;
        }

        @Override
        public boolean isCancelled() {
            return cancellation.isCancelled();
        }

        @Override
        public void cancel() {
            cancellation.cancel();
        }
    }

    private static class Cancellation {
        private boolean cancelled;

        private boolean isCancelled() {
            return cancelled;
        }

        private void cancel() {
            this.cancelled = true;
        }
    }
}
