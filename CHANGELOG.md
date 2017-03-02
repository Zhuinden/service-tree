# Change log

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