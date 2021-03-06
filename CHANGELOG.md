# Change log

-Service Tree 1.6.1 (2017-06-03)
--------------------------------
- CHANGE: traverseSubtree(@NonNull final Node node, @Walk.Mode final int mode, final boolean includeTreeRoot, @NonNull final Walk walk) is package-private.

-Service Tree 1.6.0 (2017-05-13)
--------------------------------
- ADDED: `ServiceTree.Scoped` interface which provides `onEnterScope(Node)` and `onExitScope()` methods.

`onEnterScope` is called when the service is bound to the node. 

`onExitScope()` is called if the service is removed from the node, or the node is removed from the tree.

- ADDED: `traverseChain`, `traverseTree`, `traverseSubtree` variants with `boolean` that specifies whether the tree root should be included in the traversal.

-Service Tree 1.5.2 (2017-05-13)
--------------------------------
- ADDED: `hasRootService()` method, which *should* have existed to pair with `@NonNull` `getRootService()`. Whoops.
- ADDED: `node.createChild(Object)` to allow creating a child for a node without directly chatting with the tree.
- ADDED: `node.removeNodeAndChildren()` which removes it from its associated tree.
- CHANGE: `node.removeService(String)` is now public, because I shouldn't tell you how to live your life.
- READDED: `serviceTree.getTreeRoot()` so that you *can* do `treeRoot.getBoundServices()` if you so desire.

-Service Tree 1.5.1 (2017-05-06)
--------------------------------
- ADDED: `findRoot(Node)` method that finds the root for a particular node.
- ADDED: `traverseChain(Node, int, Walk)` method where `PRE_ORDER` executes the chain traversal from root to child.
- FIX: `getRootService()` is now marked with `@NonNull` as it should have been.

-Service Tree 1.5.0 (2017-04-27)
--------------------------------
- RE-ADDED: `registerRootService()`, `unregisterRootService()`, `getRootService()`. Sorry for the inconvenience.

Now the root CAN contain information, BUT it is no longer exposed to anyone outside the library. It should have been like this from the start.

- REMOVED: `getTreeRoot()`.

- CHANGE: `getParent()` will return `null` for root keys instead of `getTreeRoot()`.

- ADDED: `node.getChild()` and `node.hasChild()` methods that returns the direct child.

-Service Tree 1.4.0 (2017-04-06)
--------------------------------
- BREAKING CHANGE: removed `registerRootService()`, `unregisterRootService()`, `getRootService()` methods.

After thinking a lot about it, the fact that ROOT_KEY and its corresponding "root" exists is an implementation detail, and functions as a guard.

The guard should not be used to store information. If a root is needed for a tree, it should be created with `createRootNode()`.

Every other node is a child of that node. It is preferred that the hierarchy is constructed directly by the user.

For safety sake (as the tree root is the parent of all root keys), `getTreeRoot()` is still kept. However, `traverseChain()` is preferred operation.

- BREAKING CHANGE: `traverseChain()`, `traverseTree()`, and `traverseSubtree()` can no longer include the "tree root" bound to the internal `ROOT_KEY`.

-Service Tree 1.3.0 (2017-03-30)
--------------------------------
- BREAKING CHANGE: `getService()` is now `@NonNull` instead of `@Nullable`, and if service is not found in the tree.
- ADDED: `hasService()` method to check if the service exists in the given chain.
- BREAKING CHANGE: `ChainWalk` is merged into `Walk`, `ChainWalk.CancellationToken` merged into `Walk.CancellationToken`
- BREAKING CHANGE: `ServiceTree.traverseChain(Node, ChainWalk)` is now `ServiceTree.traverseChain(Node, Walk)`
- BREAKING CHANGE: `Walk.execute(Node)` is now `Walk.execute(Node, CancellationToken)`
- ENHANCEMENT: Any `Walk` on any subtree or chain is now cancelable using `cancellationToken.cancel()`.

-Service Tree 1.2.3 (2017-03-28)
--------------------------------
- Add `ServiceTree.traverseChain(Node, ChainWalk)` method. It allows traversing the chain from a particular node through all its parents.

-Service Tree 1.2.2 (2017-03-27)
--------------------------------
- Add `Node.getTree()` method.

-Service Tree 1.2.1 (2017-03-24)
--------------------------------
- Add `ServiceTree.getKeys()` method.

-Service Tree 1.2.0 (2017-03-24)
--------------------------------
- BREAKING CHANGE: Remove `Node.Binder`. It was actually just an unnecessary wrapper around the `Node`.

Therefore, any code using `Node.Binder binder` can be replaced with `Node node`.

-Service Tree 1.1.1 (2017-03-23)
--------------------------------
- Decrease minSDK to 1.

-Service Tree 1.1.0 (2017-03-16)
--------------------------------
- Added `node.getChildren()`.

-Service Tree 1.0.5 (2017-03-02)
--------------------------------
- Added Javadocs and null checks.

-Service Tree 1.0.3 (2017-03-02)
--------------------------------
- Added `getRootService(String name)`.

-Service Tree 1.0.2 (2017-03-02)
--------------------------------
- `unregisterRootService(String name)` should return the removed service.

-Service Tree 1.0.1 (2017-03-02)
--------------------------------
- `getBoundServices()` should return a `List<Entry`, because `List<Object>` containing services without their keys is essentially useless.

-Service Tree 1.0.0 (2017-03-02)
--------------------------------
- Initial release.