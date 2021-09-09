# Creating and maintaining GUIs

Adding a GUI is a pretty involved process; it's easy to forget what you need to do. Hopefully this document makes 
working with GUIs much easier.

> Note that these docs are specific to our mod setup; the process to creating and maintaining GUIs is pretty flexible
> in general (yet, confusing), but we need to work within the bounds of our mod architecture.

## The base GUI class
**The base GUI class doesn't have slots added directly to it.** While it seems possible to add them, doing so will bypass
the container-related logic, which is presumably required in order for the contents to persist.

## The Container class

### Container slots

## The TileEntity class
The TileEntity class _must_ have the appropriate slots (and they must be initialized). If you add the slots without initializing
them in the TileEntity's `getInitialInventory` method, the game will crash if you attempt to open the linked GUI.

**The TileEntity should be seen as the "source of truth" for the slot definitions.** When a block needs to store things,
it needs to have a corresponding TileEntity class that possesses an inventory. _This inventory controls how the GUI
will look in the end_.

### TileEntity Slots
**Slots in the TileEntity class are not responsible for saving the contents; if you only implement a slot in a TileEntity,
then an item left in the given slot will disappear after reloading the game.**

The slots that get added during the TileEntity class' `getInitialInventory` method are the ones that end up having
a container created for them in `EnchantmentsSeerTileContainer#addSlots`. _This does not include the base inventory
and hot bar slots._

If you were to put the following in your TileEntity's `getInitialInventory` method, then in `EnchantmentsSeerTileContainer#addSlots`,
2 container slots would be created:
```java
    @Nonnull
    @Override
    protected IInventorySlotHolder getInitialInventory() {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(inputSlot = EnchantInventorySlot.at(null, 15, 47));
        builder.addSlot(reagentSlot = InputInventorySlot.at(null, 35, 47));
        return builder.build();
    }
```


## Helper info
1. `*ContainerSlot`
2. `*InventorySlot` - use the `at` method in the corresponding `*InventorySlot` class in the _TileEntity_
3. 
