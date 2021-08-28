# `client` README

The `client` package contains "client-only" code. This generally code related to anything to rendering, including
GUIs, model baking, texture stitching, etc.

In other words, this package shouldn't contain any "core" business logic for the mod. Things like registration and core
item/block class definitions belongs in the `common` package.