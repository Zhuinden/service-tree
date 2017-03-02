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
 * Created by Zhuinden on 2017.03.01..
 */

public class ServiceTree {
    public static class Node {
        private Node parent;

        private Object localKey;

        private Set<Node> children = new LinkedHashSet<>();

        private Map<String, Object> services = new LinkedHashMap<>();

        Node(Node parent, Object key) {
            this.parent = parent;
            this.localKey = key;
        }

        public <T> T getKey() {
            // noinspection unchecked
            return (T) localKey;
        }

        public <T> T getService(String name) {
            if(services.containsKey(name)) {
                // noinspection unchecked
                return (T) services.get(name);
            } else {
                // noinspection unchecked
                return parent == null ? null : (T)parent.getService(name);
            }
        }

        public List<Object> getBoundServices() {
            return new ArrayList<>(services.values());
        }

        void addService(String name, Object service) {
            services.put(name, service);
        }

        void execute(Walk walk) {
            walk.execute(this);
        }

        public Node getParent() {
            return parent;
        }

        private void removeService(String name) {
            services.remove(name);
        }

        public static class Binder {
            private Node node;

            Binder(Object localKey, ServiceTree serviceTree, Node parent) {
                node = new Node(parent, localKey);
                parent.children.add(node);
                serviceTree.addNode(node);
            }

            public Object getService(String name) {
                return node.getService(name);
            }

            public Binder bindService(String name, Object service) {
                node.addService(name, service);
                return this;
            }

            public Node get() {
                return node;
            }
        }

        @Override
        public int hashCode() {
            return Node.class.hashCode() + 37*getKey().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof Node && ((Node)obj).getKey().equals(localKey);
        }

        @Override
        public String toString() {
            return "Node[" + localKey + "]";
        }
    }

    static final Object ROOT_KEY = new RootKey();

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

    private Node root = new Node(null, ROOT_KEY);

    private Map<Object, Node> nodeMap = new LinkedHashMap<>();

    {
        nodeMap.put(ROOT_KEY, root);
    }

    private void addNode(Node node) {
        nodeMap.put(node.getKey(), node);
    }

    public boolean hasNodeWithKey(Object key) {
        return nodeMap.containsKey(key);
    }

    public Node.Binder createRootNode(Object nodeKey) {
        return new Node.Binder(nodeKey, this, root);
    }

    public Node.Binder createChildNode(Node parentNode, Object nodeKey) {
        return new Node.Binder(nodeKey, this, parentNode);
    }

    public Node getNode(Object key) {
        return nodeMap.get(key);
    }

    public void traverseTree(@Walk.Mode int mode, Walk walk) {
        traverseSubtree(root, mode, walk);
    }

    public void removeAllNodes() {
        removeNodeAndChildren(nodeMap.get(ROOT_KEY));
    }

    public void removeNodeAndChildren(Node node) {
        traverseSubtree(node, Walk.POST_ORDER, new Walk() {
            @Override
            public void execute(Node node) {
                if(node.getKey().equals(ROOT_KEY)) {
                    return;
                }
                node.parent.children.remove(node);
                nodeMap.remove(node.getKey());
            }
        });
    }

    public void traverseSubtree(Node node, @Walk.Mode int mode, Walk walk) {
        if(mode == Walk.PRE_ORDER) {
            node.execute(walk);
            for(Node child : node.children) {
                traverseSubtree(child, mode, walk);
            }
        } else if(mode == Walk.POST_ORDER) {
            List<Node> reversedChildren = new LinkedList<>(node.children);
            Collections.reverse(reversedChildren);
            for(Node child : reversedChildren) {
                traverseSubtree(child, mode, walk);
            }
            node.execute(walk);
        }
    }

    public void registerRootService(String name, Object service) {
        root.addService(name, service);
    }

    public void unregisterRootService(String name) {
        root.removeService(name);
    }

    public interface Walk {
        void execute(Node node);

        @Retention(SOURCE)
        @IntDef({PRE_ORDER, POST_ORDER})
        @interface Mode {
        }

        int PRE_ORDER = 1;
        int POST_ORDER = 2;
    }
}
