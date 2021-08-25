package com.nuggylib.enchantmentsseer.client.gui;

import com.nuggylib.enchantmentsseer.common.lib.collection.LRUCache;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;

/**
 * Main GUI class, inspired by the Mekanism GUI building logic
 *
 * Since we are only building one GUI, our implementation will be more rudimentary. This should also help demonstrate
 * the GUI building logic that we come up with, as well as demonstrate how to work with Minecraft GUIs in general.
 *
 * ## How to use this class
 * Extend this class for every GUI you need. For example, if you have a generator and need to create a
 * GUI for it, you would create a class such as `FurnaceGui` and have that class `extend EnchantmentSeerGui<FurnaceContainer>`
 * (the `FurnaceContainer` class isn't discussed further here - it's simply here to demonstrate that you also need a
 * corresponding container class when creating a GUI subclass).
 * ```java
 * public class FurnaceGui extends EnchantmentsSeerGui<FurnaceContainer> { ... }
 * ```
 * * Note that neither the `FurnaceGui` nor `FurnaceContainer` classes exist in this codebase - they are merely examples
 *
 * @param <CONTAINER>       The corresponding container class for the GUI to be displayed
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/GuiMekanism.java"
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/VirtualSlotContainerScreen.java"
 */
public abstract class EnchantmentsSeerGui<CONTAINER extends Container> extends ContainerScreen<CONTAINER> {

    /**
     * The resource location for the base background image used for all GUIs that extend this class
     *
     * Note that Mekanism uses two additional images in the base GUI class, "shadow" and "blur".
     */
    public static final ResourceLocation BASE_BACKGROUND = new ResourceLocation("enchantments-seer:textures/gui/gui_base");
    protected final LRU<GuiWindow> windows = new LRUCache<>();
    protected final List<GuiElement> focusListeners = new ArrayList<>();

    // TODO: Find out how Mekanism sets this field
    /**
     * A flag used to determine if the GUI's `removed` method was invoked while switching to JEI or not.
     *
     * When switching to JEI, it's nice to have the user return to where they were in the GUI. When this flag is
     * true, we prevent the `super.remove()` method from being called when `remove()` is invoked.
     */
    public boolean switchingToJEI;

    protected EnchantmentsSeerGui(CONTAINER container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    public void removed() {
        if (!switchingToJEI) {
            super.removed();
        }
    }

    @Override
    public void init() {
        super.init();
        // TODO: Continue here...
//        addGuiElements();
    }
}
