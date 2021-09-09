# Creating and maintaining GUIs

Adding a GUI is a pretty involved process; it's easy to forget what you need to do. Hopefully this document makes 
working with GUIs much easier.

> Note that these docs are specific to our mod setup; the process to creating and maintaining GUIs is pretty flexible
> in general (yet, confusing), but we need to work within the bounds of our mod architecture.

## The base GUI class
**The base GUI class doesn't have slots added directly to it.** While it seems possible to add them, doing so will bypass
the container-related logic, which is presumably required in order for the contents to persist.

## The Container class


## The TileEntity class
The TileEntity class _must_ have the appropriate slots (and they must be initialized). If you add the slots without initializing
them in the TileEntity's `getInitialInventory` method, the game will crash if you attempt to open the linked GUI.

**The TileEntity should be seen as the "source of truth" for the slot definitions.** When a block needs to store things,
it needs to have a corresponding TileEntity class that possesses an inventory. _This inventory controls how the GUI
will look in the end_.

## Helper info
1. `*ContainerSlot`
2. `*InventorySlot` - use the `at` method in the corresponding `*InventorySlot` class in the _TileEntity_
3. 
