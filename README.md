# Service Tree

It's a tree that holds a `Map<String, Object>` so that you can store your services in it for a particular key.

You can also traverse the tree, either from top to bottom, or in reverse order from bottom to up.

Each Node is constructed with its services set using the `ServiceTree.Node.Binder` returned by `serviceTree.createNode()`.

The first level is created with `createRootNode()`, the following levels are created by `createChildNode()`.

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

    compile 'com.github.Zhuinden:service-tree:1.0.2'

## How does it work?

Check the tests. They're pretty self-explanatory.


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