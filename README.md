# Service Tree

It's a tree that holds a `Map<String, Object>` in each of its nodes. Therefore, it allows you to store your services in it, each node identified by a particular key.

The `service-tree` is therefore a hierarchical service locator, independent from the Android platform.

This is a key difference to `square/Mortar`, which utilizes `context.getSystemService()` to create the hierarchy and inheritance.

You can also traverse the tree, either from top to bottom, or in reverse order from bottom to up.

Each Node is constructed with `serviceTree.createNode()`, and services can be bound to it using `node.bindService(String, Object)`.

The first level is created with `createRootNode()`, the following levels are created by `createChildNode()`.

Root nodes inherit root services, and child nodes inherit services from their parent node.

## Creating a hierarchy

### Singleton / Application

You can instantiate the `ServiceTree` as a singleton in a custom application class, and register root services with `serviceTree.registerRootService(String, Object).`

### Activity

Activities are direct children of the root, so you can use `serviceTree.createRootNode(String nodeTag);`.

### Fragment

Fragments are tricky, but they are children of the Activity. Their node can be created with `serviceTree.createChildNode(Node parentNode, String childNodeTag);`

## Accessing services

The services you bind to a node can be accessed with `node.getService(String serviceName)`.

While you're binding to a new node, you can also access the inherited services with `node.getService(String serviceName)`.

## Using Service Tree

In order to use Service Tree, you need to add jitpack to your project root gradle:

    buildscript {
        repositories {
            // ...
            maven { url "https://jitpack.io" }
        }
        // ...
    }
    allprojects {
        repositories {
            // ...
            maven { url "https://jitpack.io" }
        }
        // ...
    }


and add the compile dependency to your module level gradle.

    compile 'com.github.Zhuinden:service-tree:1.2.1'


## License

    Copyright 2017 Gabor Varadi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
