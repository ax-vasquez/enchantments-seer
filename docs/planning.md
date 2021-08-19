# enchantments-seer
This mod will be something of a spiritual successor to the [Enchanting Plus](https://www.curseforge.com/minecraft/mc-mods/enchanting-plus) mod. **We will not use any code or concepts from their mod - it's protected under the GNU license, which expressly-forbids modifications.**

## Getting started
1. Clone `https://github.com/MinecraftForge/MinecraftForge` locally
2. Checkout the branch for the game version that you intend to develop a mod for (in our case, we're using branch `1.16.x`, but intend to support later versions as well)
3. Make sure you have the correct JDK (and that your Java setup is correct)
4. Open the project (I use IntelliJ) > Run the Gradle `setup` task
5. You're now ready to browse the MinecraftForge codebase (including the deobfuscated Minecraft code)

## Project philosophies
1. **Never make the code proprietary**
	1. *MIT license*
	2. *Not only allow changes, but actively encourage and teach others how to make changes/implement their own version*
2. **Write code as though someone else will pick up from where you left off**
	1. *Ample documentation*
	2. *Code samples*
3. **Light-touch**
	1. *Re-use as many assets as possible*
	2. *Don't add new enchantments*

## Features
1. **Upgraded enchantment table**
	1. Same base model as normal one
	2. Accepts either a seeing stone, or enchanted book as an ingredient
		1. Seer's Manuscript - allows the user to see ALL enchantments and can consume the Manuscript to apply ONE enchantment to the item, with no limit for the effect value (the levels used to create the Manuscript are therefore consumed during enchantment)
		2. Enchanted book - allows the user to consume the book to apply the max-value for the enchantment the book gives at no enchantment cost
			* No other enchantments are visible when using an enchant-specific book

## Items
1. **Upgraded enchantment table**
	1. Differs from normal enchanting table in that it has no book unless one has been placed in it's inventory
		1. Inventory should be global (so all players can access it)
		2. The book on top will look either like a normal book, or the Seer's Manuscript
2. **Seer's Manuscript**
	1. A high-level item crafted by combining a Seer's stone and an enchanted book (of any kind) AND some amount of levels or experience
		1. Effectively, this means that enchantment costs for this mod only increase linearly
		2. This mod shifts the "difficulty" from obtaining levels to crafting the reagents
3. **Seer's Stone**
	1. A high-level item used as an ingredient for the Seer's Text
	2. Crafted by:

| Blaze Powder | Blaze Powder | Blaze Powder |
| Ender Pearl	| Diamond		  | Ender Pearl	|
| Blaze Powder | Blaze Powder | Blaze Powder |

## Notes

### EnchantingTable (Vanilla)
The vanilla enchanting table (in `1.16.x`) is defined in `net.minecraft.block` in the `EnchantingTableBlock.java` file
* Extends the `ContainerBlock` class
* Has the following methods:
	* `public EnchantingBlock` (constructor)
	* `public boolean useShapeForLightOcclusion` (deprecated)
	* `public VoxelShape getShape` (deprecated)
	* `public void animateTick`
		* Annotated with `@OnlyIn(Dist.CLIENT)`
	* `public BlockRenderType getRenderShape`
	* `public TileEntity newBlockEntity`
	* `public ActionResultType use` (deprecated)
	* `public INamedContainerProvider getMenuProvider`
		* Annotated with `@Nullable`
	* `public void setPlacedBy`
	* `public boolean isPathFindable` (deprecated)

It appears that the enchating table class *is not* responsible for logic of actually triggering the enchantment. It seems to only define the actual **block**'s behavior; not anything to do with how the enchanting process works.

### Enchantment (Vanilla)
The vanilla Enchantment class (in `1.16.x`) is defined in `net.minecraft.enchantment` in the `Enchantment.java` file
* Is an `abstract` class (is never directly-instantiated)
* The constructor takes three arguments; `Enchantment.Rarity`, `EnchantmentType`, `EquipmentSlotType[]`
* Defines the following methods (excluding constructor):
	* `public Map<EquipmentSlotType, ItemStack> getSlotItems`
	* `public Enchantment.Rarity getRarity`
	* `public int getMinLevel`
	* `public int getMaxLevel`
	* `public int getMinCost`
	* `public int getMaxCost`
	* `public int getDamageProtection`
	* `public float getDamageBonus`
	* `public final boolean isCompatibleWith`
	* `protected String getOrCreateDescriptionId`
	* `public String getDescriptionId`
	* `public ITextComponent getFullName`
	* `public boolean canEnchant``
	* `public void doPostAttack`
	* `public void doPostHurt`
	* `public boolean isCurse`
	* `public boolean isTradeable`
	* `public boolean isDiscoverable`

This class also has an internally-defined `public static enum` called `Rarity`, which defines 4 rarity values:
1. `COMMON` = 10
2. `UNCOMMON` = 5
3. `RARE` = 2
4. `VERY_RARE` = 1

In short, this class appears to define some data-related information about enchantments, but *does not* appear to define the actual enchantment logic itself. For instance, the `Enchantment` class is responsible for (among other things) determining which "type" an enchantment is, which seems to control which type of items the enchantment can be applied to. However, it doesn't determine what an enchantment does.

Confusingly, this means that things like the logic for Silk Touch _are not_ stored in the `SilkTouchEnchantment` class, but somewhere else. I'm still not sure where that "somewhere else" is.
