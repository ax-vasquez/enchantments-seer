package com.nuggylib.enchantmentsseer.common.util;

import com.mojang.authlib.GameProfile;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnchantmentsSeerUtils {

    private static final List<UUID> warnedFails = new ArrayList<>();

    /**
     * Copied from the Mekanism codebase; all credit goes to them
     *
     * This enum was pulled from within the `MekanismUtils` class
     *
     * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/util/MekanismUtils.java"
     */
    public enum ResourceType {
        // TODO: Figure out how Mekanism included the "texture" portion of the path without manually adding it here
        GUI("textures/gui"),
        GUI_BUTTON("gui/button"),
        GUI_BAR("gui/bar"),
        GUI_HUD("gui/hud"),
        GUI_PROGRESS("gui/progress"),
        GUI_SLOT("textures/gui/slot"),
        GUI_TAB("gui/tabs"),
        SOUND("sound"),
        RENDER("render"),
        TEXTURE_BLOCKS("textures/block"),
        TEXTURE_ITEMS("textures/item"),
        MODEL("models")
        ;

        private final String prefix;

        ResourceType(String s) {
            prefix = s;
        }

        public String getPrefix() {
            return prefix + "/";
        }
    }

    @Nonnull
    public static String getLastKnownUsername(@Nullable UUID uuid) {
        if (uuid == null) {
            return "<???>";
        }
        String ret = UsernameCache.getLastKnownUsername(uuid);
        if (ret == null && !warnedFails.contains(uuid) && EffectiveSide.get().isServer()) { // see if MC/Yggdrasil knows about it?!
            GameProfile gp = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(uuid);
            if (gp != null) {
                ret = gp.getName();
            }
        }
        if (ret == null && !warnedFails.contains(uuid)) {
            EnchantmentsSeer.logger.warn("Failed to retrieve username for UUID {}, you might want to add it to the JSON cache", uuid);
            warnedFails.add(uuid);
        }
        return ret != null ? ret : "<" + uuid + ">";
    }

}
