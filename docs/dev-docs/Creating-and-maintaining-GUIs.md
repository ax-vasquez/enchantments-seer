# Creating and maintaining GUIs

Adding a GUI is a pretty involved process; it's easy to forget what you need to do. Hopefully this document makes 
working with GUIs much easier.

> Note that these docs are specific to our mod setup; the process to creating and maintaining GUIs is pretty flexible
> in general (yet, confusing), but we need to work within the bounds of our mod architecture.

## The base GUI class
**The base GUI class doesn't have slots added directly to it.** While it seems possible to add them, doing so will bypass
the container-related logic, which is presumably required in order for the contents to persist.

### Ideal modifications
As mentioned before, you don't add slots to your GUI manually - that is handled automatically, assuming you added the 
appropriate slots to the TileEntity class, registered the entity class, and registered a container for the TileEntity
class (either through a subclass of `EnchantmentsSeerTileContainer`, which should be an uncommon use case, or by leveraging generics).

**"Non-slot" GUI elements are the ideal modifications to make in a GUI class, such as buttons.**

## The TileEntity class
The TileEntity class _must_ have the appropriate slots (and they must be initialized). If you add the slots without initializing
them in the TileEntity's `getInitialInventory` method, the game will crash if you attempt to open the linked GUI.

**The TileEntity should be seen as the "source of truth" for slot definitions, OTHER THAN the inventory and hot bar
slots.**

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
* Although the TileEntity class technically isn't part of the GUI, the `x` and `y` values passed to the slots control where
 they are placed in the GUI when it's rendered

### Persistent item storage
> At the time of writing this document, I'm facing a bug where items are deleted from the game entirely if you leave
> it in the enchanting table and walk away. I would like to make it so that both the item to be enchanted and the 
> reagent slots can retain their item stacks. This section will cover how to do that.

## The Container class
**You don't usually need an explicit container class in our mod.** This is only because we have logic in place to 
dynamically-construct the containers for TileEntity classes as-needed, using generics.

With that said, there may still be some use cases in which we will need to subclass the `EnchantmentsSeerTileContainer`
in the future.
