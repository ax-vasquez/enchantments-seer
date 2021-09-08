# Enchanting Table overview

This is a high-level discussion of the Enchanting table logic. This documentation was based on Minecraft 1.16.5.

## GUI
* **Class:** `EnchantmentScreen`

The GUI class for the Vanilla enchanting table handles the following logic:
1. Links GUI to related textures
2. Defines, sets up and runs the in-GUI book animation
3. `mouseClicked` logic
4. `renderBg` logic
5. `render` logic

> Note that Vanilla textures for a specific GUI are self-contained. There is only one texture image used for vanilla 
> GUI textures; this texture file contains all the elements needed to draw the GUI as it appears in-game. This differs
> quite a bit from Mekanism, which uses a wide variety of generic textures and builds them as-needed for any given 
> GUI.

## Container
* **Class:** `EnchantmentContainer`

This is the vanilla Enchantment Table subclass of the `Container` class. It handles the following logic:
1. Defines the enchant slots (as an `IInventory` instance)
2. Provides an overloaded constructor that takes the base `Container` arguments, plus an `IWorldPosCallable` argument
3. Creates the inventory and hot bar slots
4. Populates three data slots with three data fields, `cost`, `enchantClue`, and `levelClue`
5. Defines the `getPower` method, which is used when enchanting to check how many bookshelves are near the enchanting table
6. `slotsChanged` logic
7. `clickMenuButton` logic
8. `getEnchantmentsList` logic
9. `getGoldCount` logic
10. `getEnchantmentSeed` logic
11. `removed` logic
12. `stillValid` logic
13. `quickMoveStack` logic

## Block
* **Class:** `EnchantingTableBlock`

1. Defines the base shape of the block (which is shorter than the default block dimensions)
2. `useShapeForLightOcclusion` logic
3. `getShape` logic
4. `animateTick` logic, which only handles rendering the particles and nothing more (e.g., nothing to do with book animations)
5. `getRenderShape` logic
6. `newBlockEntity` logic
7. `use` logic, which handles the logic for opening the menu (or simply passing when called from the client side)
8. `getMenuProvider` logic
9. `setPlacedBy` logic
10. `isPathFindable` logic, but always returns `false`

> We don't implement our own `Block` class directly; we generate them using logic provided by Mekanism that creates
> `TileBlock` instances. With that said, we still need to refer to this class so we can pull in logic as-needed.

## TileEntity
* **Class:** `EnchantingTableTileEntity`

The Tile Entity class defines the "behavior" of the block. Note that not all blocks need a tile entity, even if it's
a container. The only blocks that need a tile entity are ones that are meant to do things other than simply exist as a
block. For example, any block with an animation also requires a Tile Entity class, such as the Enchanting Table.

The logic for the Enchanting Table's Tile Entity class includes:
1. Defines all fields necessary to perform the book model animation (world-only animation - the GUI book animation is handled in the screen class)
2. `save` logic
3. `load` logic
4. `tick` logic, which defines the book animation for the Enchanting Table block
5. `getName` logic
6. `setCustomName` logic
7. `getCustomName` logic
