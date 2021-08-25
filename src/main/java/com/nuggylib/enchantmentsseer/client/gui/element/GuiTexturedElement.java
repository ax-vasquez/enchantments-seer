package com.nuggylib.enchantmentsseer.client.gui.element;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/element/GuiTexturedElement.java"
 */
public abstract class GuiTexturedElement extends GuiRelativeElement {

    protected final ResourceLocation resource;

    public GuiTexturedElement(ResourceLocation resource, IGuiWrapper gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
        this.resource = resource;
    }

    protected ResourceLocation getResource() {
        return resource;
    }

    public interface IInfoHandler {

        List<ITextComponent> getInfo();
    }
}
