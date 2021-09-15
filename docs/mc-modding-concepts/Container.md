# `Container`

The `Container` class (and its subclasses) **do not** correspond to storing items, or _in-game_ content of any kind.
Instead, containers are closely-tied to the GUI and "contain" the assets of the screen from the textures, slots,
and text.

Additionally, Containers appear to be responsible for the slot input logic; at least, part of it.

**The main thing to understand about containers is that _they have nothing to do with internal item storage of a block_.**