# Readme
A Minecraft mod to improve the enchanting experience (but leaves the existing enchanting process intact).

## For mod developers
> I use IntelliJ, so docs only cover steps for that IDE. Be sure to refer to the "Setup process" section near the bottom
> of this Readme.

### Running the client
1. Press `CTRL` twice > type `gradlew runClient`
2. Wait for the game client to start

This process will start the Forge-modded game client with your mod installed.

## From the original Readme...
> *NOTE: This repository was created by copying the MDK provided by MinecraftForge. The original Readme has been replaced
> with this one, but some of the original's contents are preserved in this section.*

This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "un-renamed" MCP source code (aka
SRG Names) - this means that you will not be able to read them directly against
normal code.

### Setup process
1. Open IntelliJ > Click "Open"
2. Navigate to where your mod repository is > **select the `build.gradle` file**
3. Select "Import as Project" when asked how you'd like to import the `build.gradle` file
4. Run the `gradlew genIntellijRuns` task (press `CTRL` twice then search `"gradlew genIntellijRuns"` - you should see it in the list of suggestions)

If at any point you are missing libraries in your IDE, or you've run into problems you can
run `gradlew --refresh-dependencies` to refresh the local cache. `gradlew clean` to reset everything
{this does not affect your code} and then start the process again.

### Mapping names
By default, the MDK is configured to use the official mapping names from Mojang for methods and fields
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license, if you do not agree with it you can change your mapping names to other crowdsourced names in your
build.gradle. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/MinecraftForge/MCPConfig/blob/master/Mojang.md
