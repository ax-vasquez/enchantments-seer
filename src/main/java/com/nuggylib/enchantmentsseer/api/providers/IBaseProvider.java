package com.nuggylib.enchantmentsseer.api.providers;

import com.nuggylib.enchantmentsseer.client.render.text.IHasTextComponent;
import com.nuggylib.enchantmentsseer.client.render.text.IHasTranslationKey;
import com.nuggylib.enchantmentsseer.client.render.text.TextComponentUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/providers/IBaseProvider.java"
 */
public interface IBaseProvider extends IHasTextComponent, IHasTranslationKey {

    /**
     * Gets the registry name of the element represented by this provider.
     *
     * @return Registry name.
     */
    ResourceLocation getRegistryName();

    /**
     * Gets the "name" or "path" of the registry name.
     */
    default String getName() {
        return getRegistryName().getPath();
    }

    @Override
    default ITextComponent getTextComponent() {
        return TextComponentUtil.translate(getTranslationKey());
    }
}
