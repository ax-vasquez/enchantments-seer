# The `mods.toml` file

The `mods.toml` file contains high-level information about the mod. Much of the information is displayed on the 
mod's information page within the game client (on the main menu, select "Mods" > locate your mod).

The `mods.toml` file should go in `/src/main/resources/META-INF/`

## Our configuration
1. `modLoader`: Should always be set to `"javafml"` for our mod (indicates that this is a Forge mod)
2. `loaderVersion`: We simply use the default that Forge set - I'll update this later with more information
3. `license`: We use GNU so that anyone can refer to, and use, our code/mod - we just don't want people taking this mod and repacking it as their own without making significant changes
4. `modId`: The unique identifier for the mod
5. `version`: The version of the mod - we are using semantic versioning
6. `displayName`: The display name of the mod (shocking, right?)
7. `logoFile`: The image to use as the mod logo - this gets displayed in the mod details overview
    * The image should be located in `/src/main/resources/`
8. `description`: A long description for the mod

### Dependencies
None, aside from Forge and Minecraft. This section will be updated later.
