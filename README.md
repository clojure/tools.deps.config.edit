# tools.deps.config.edit

Writing per-tool configuration files

# Rationale

[tools.deps.config](https://github.com/clojure/tools.deps.config) defines functions to read a tool's configuration files. This library provides functions to write those files. The two are separate to isolate dependencies needed for writing but not reading.

# Release Information

Latest release: 0.1.2

* [All released versions](https://central.sonatype.com/artifact/org.clojure/tools.deps.config.edit/versions)

[deps.edn](https://clojure.org/reference/deps_edn) dependency information:

```
org.clojure/tools.deps.config.edit {:mvn/version "0.1.2"}
```

[Leiningen](https://github.com/technomancy/leiningen/) dependency information:

```
[org.clojure/tools.deps.config.edit "0.1.2"]
```

[Maven](https://maven.apache.org) dependency information:

```xml
<dependency>
  <groupId>org.clojure</groupId>
  <artifactId>tools.deps.config.edit</artifactId>
  <version>0.1.2</version>
</dependency>
```

# API

Tools are identified by a qualified lib symbol (e.g., `my.org/my-tool`). Tool
config files live under a `.cljconf` directory at one of two locations:

* `:user` - the [user config dir](https://clojure.github.io/tools.deps.edn/#clojure.tools.deps.edn/user-config-dir) shared by all of a user's projects
* `:project` - the [project dir](https://clojure.github.io/tools.deps.edn/#clojure.tools.deps.edn/project-dir) specific to a single project

The write functions create files and directories as needed.

```clojure
(require '[clojure.tools.deps.config.edit :as dce])
```

### [write-config](https://clojure.github.io/tools.deps.config.edit/#clojure.tools.deps.config.edit/write-config)

`(write-config location lib config)` - write the config as EDN, overwriting any existing file

```clojure
(dce/write-config :project 'my.org/my-tool {:width 120})
```

### [assoc-config](https://clojure.github.io/tools.deps.config.edit/#clojure.tools.deps.config.edit/assoc-config)

`(assoc-config location lib k v)` - set a single key in the config file, preserving existing formatting and comments

```clojure
;; Persist one setting, creating the file if it does not exist
(dce/assoc-config :user 'my.org/my-tool :color :dark)
```

# Developer Information

* [GitHub project](https://github.com/clojure/tools.deps.config.edit)
* [How to contribute](https://clojure.org/community/contributing)
* [Bug Tracker](https://clojure.atlassian.net/browse/TDEPS)
* [Continuous Integration](https://github.com/clojure/tools.deps.config.edit/actions/workflows/test.yml)

# Copyright and License

Copyright © Rich Hickey and contributors

All rights reserved. The use and
distribution terms for this software are covered by the
[Eclipse Public License 1.0] which can be found in the file
LICENSE at the root of this distribution. By using this software
in any fashion, you are agreeing to be bound by the terms of this
license. You must not remove this notice, or any other, from this
software.

[Eclipse Public License 1.0]: https://opensource.org/license/epl-1-0/
