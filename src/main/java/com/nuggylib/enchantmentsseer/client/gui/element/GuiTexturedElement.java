package com.nuggylib.enchantmentsseer.client.gui.element;

import com.nuggylib.enchantmentsseer.client.gui.IGuiWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * Wrapper class for a Gui Element with a texture
 */
public class GuiTexturedElement extends GuiRelativeElement {

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
