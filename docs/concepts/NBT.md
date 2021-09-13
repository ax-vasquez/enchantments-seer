# [NBT](https://minecraft.fandom.com/wiki/NBT_format) format
Named Binary Tag (NBT) format is **used by Minecraft for the various files in which it saves data.**
* Format is designed to store data in a tree structure
  * The tree is composed of _Tags_
  * All tags have:
    * ID
    * Name

> Another more user-friendly format of NBT is in plain string, as used in [commands](https://minecraft.fandom.com/wiki/Commands)
> This format is referred to as **SNBT**, short for **stringified NBT**

## NBT in MinecraftForge
Within the MinecraftForge codebase, the `net.minecraftforge.common.util.Constants.NBT` contains some constants related
to NBT operations and appear to be used in deserializing Tag data.

**Aside from these constants, MinecraftForge does not appear to offer any other classes, interfaces or enums intended
to assist in working with NBTs.**

## NBT in Vanilla Minecraft
There are a large variety of classes and enumerations intended to assist in working with NBTs.

### `INBT`
**Crucial interface - any NBT should implement this interface.** `INBT` describes the core features/functionality of an
`NBT` object.

`NBTDynamicOps` is a helper class to create a variety of NBTs - each of the methods responsible for creating an NBT return
an `INBT` instance, meaning they can return anything that implements the `INBT` interface.

### `NBTDynamicOps`
This class appears to be one of the "core" NBT classes. It's responsible for high-level NBT-related operations such as:
1. Creating various NBT types
2. Getting string values of NBTs
3. Filling NBT collections more NBTs
4. Merging NBTs into lists
5. Merging NBTs into maps
6. Getting values for an NBT map
7. Getting entries for an NBT map

There are other methods in this class related to getting data from NBTs, as well as streaming NBT/data. We need to clarify
the docs here a bit more later.

### `NBTUtil`
The `NBTUtil` class holds many crucial NBT-related operations:
1. Reading the game profile
2. Writing to the game profile
3. Comparing NBTs
4. A helper to create an NBT from UUIDs
5. A helper to deserialize a UUID from a given NBT
6. Read block position
7. Write block position
8. Reading the block state
9. Writing the block state
10. Helpers to set values
11. Helpers to update existing NBT values

### `NBTQueryManager`
This class is a **client-only** class intended to:
1. Handle NBT responses
2. Start NBT transactions
3. Query Entity tag
4. Query Block Entity tag

### `NBTtoSNBTConverter`
A helper class to convert regular NBTs to _stringified_ NBTs. Refer to the note in the first section of this document
for more information on stringified NBTs. We also need to clarify the docs on when and why we would need to do this.





