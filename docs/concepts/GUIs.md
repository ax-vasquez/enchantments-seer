# GUIs

## Mekanism's approach
> The documentation in this section only pertains to blocks meant to store items (and persist them). We don't really need
> to persist the storage, but since implementing this architecture pretty much just comes with the ability, we may as well
> use it.

### `GuiMekanismTile<TILE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<TILE>>`
This is a special class created by Mekanism that combines the `Container` and `TileEntity` classes for a given GUI into
the same class so that the Tile's inventory is available to the container (e.g., for operations such as `getSlots()`).

Mekanism extends this class for any GUI that needs persistent storage. Within this class, they obtain a reference to 
the underlying `TileEntity` class using a modified `Container` class that has a method to obtain the linked `TileEntity`.

## To do
* [ ] Implement a custom `Container` class similar to Mekanism's that has a reference to the underlying `TileEntity`
* [ ] Implement custom `GuiEnchantmentsSeerTile` class that 