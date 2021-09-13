# [`TileEntity`](https://mcforge.readthedocs.io/en/1.16.x/tileentities/tileentity/) class

The `TileEntity` class for a given block could be referred to as the "brains" of a block. Blocks that don't do
anything typically don't have a corresponding `TileEntity` class because they have no need for one. I like to call
such blocks "brainless blocks"; ones such as a block of dirt or planks.

## When do you need a `TileEntity` class?
The following use cases are good examples for when you would need to implement a `TileEntity` class:
1. When you want to create an animated block
2. When a block has a GUI
3. When a block is intended to store things (e.g., items, fluids, etc.)

## Setting up a `TileEntity` for item storage


