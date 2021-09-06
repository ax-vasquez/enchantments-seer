package com.nuggylib.enchantmentsseer.common.base;

import com.mojang.authlib.GameProfile;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.function.Function;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * Global, shared FakePlayer for Mekanism-specific uses
 *
 * This was introduced to fix https://github.com/dizzyd/Mekanism/issues/2. In that issue, another mod was trying to apply a potion to the fake player and causing the
 * whole system to crash due to essential potion related structures not being initialized for a fake player.
 *
 * The broader problem is that the FakePlayer in Forge 14.23.5.2768 isn't really complete and short of patching Forge and requiring everyone in the world to upgrade,
 * there's no easy fix -- so we introduce our own FakePlayer that will let us override other methods as necessary.
 *
 * Use of the fake player is via a consumer type lambda, where usage is only valid inside the lambda. Afterwards it may be garbage collected at any point.
 *
 * Supports emulating a specific UUID, for use with TileComponentSecurity
 */
public class EnchantmentSeerFakePlayer extends FakePlayer {

    private static WeakReference<EnchantmentSeerFakePlayer> INSTANCE;

    /**
     * UUID of a player we are pretending to be, null to use the default Mek one
     */
    private UUID emulatingUUID = null;

    public EnchantmentSeerFakePlayer(ServerWorld world) {
        super(world, new FakeGameProfile());
        ((FakeGameProfile) this.getGameProfile()).myFakePlayer = this;
    }

    @Override
    public boolean canBeAffected(@Nonnull EffectInstance effect) {
        return false;
    }

    public void setEmulatingUUID(UUID uuid) {
        this.emulatingUUID = uuid;
    }

    @Nonnull
    @Override
    public UUID getUUID() {
        return this.emulatingUUID != null ? this.emulatingUUID : super.getUUID();
    }

    /**
     * Acquire a Fake Player and call a function which makes use of the player. Afterwards, the Fake Player's world is nulled out to prevent GC issues. Emulated UUID is
     * also reset.
     *
     * Do NOT store a reference to the Fake Player, so that it may be Garbage Collected. A fake player _should_ only need to be short-lived
     *
     * @param world              World to set on the fake player
     * @param fakePlayerConsumer consumer of the fake player
     * @param <R>                Result of a computation, etc
     *
     * @return the return value of fakePlayerConsumer
     */
    @SuppressWarnings("WeakerAccess")
    public static <R> R withFakePlayer(ServerWorld world, Function<EnchantmentSeerFakePlayer, R> fakePlayerConsumer) {
        EnchantmentSeerFakePlayer actual = INSTANCE != null ? INSTANCE.get() : null;
        if (actual == null) {
            actual = new EnchantmentSeerFakePlayer(world);
            actual.connection = new EnchantmentSeerFakeNetHandler(world.getServer(), actual);
            INSTANCE = new WeakReference<>(actual);
        }
        EnchantmentSeerFakePlayer player = actual;
        player.level = world;
        R result = fakePlayerConsumer.apply(player);
        player.emulatingUUID = null;
        player.level = null;//don't keep reference to the World
        return result;
    }

    /**
     * Same as {@link EnchantmentSeerFakePlayer#withFakePlayer(net.minecraft.world.server.ServerWorld, java.util.function.Function)} but sets the Fake Player's position. Use when you
     * think the entity position is relevant.
     *
     * @param world              World to set on the fake player
     * @param fakePlayerConsumer consumer of the fake player
     * @param x                  X pos to set
     * @param y                  Y pos to set
     * @param z                  Z pos to set
     * @param <R>                Result of a computation, etc
     *
     * @return the return value of fakePlayerConsumer
     */
    public static <R> R withFakePlayer(ServerWorld world, double x, double y, double z, Function<EnchantmentSeerFakePlayer, R> fakePlayerConsumer) {
        return withFakePlayer(world, fakePlayer -> {
            fakePlayer.setPosRaw(x, y, z);
            return fakePlayerConsumer.apply(fakePlayer);
        });
    }

    public static void releaseInstance(IWorld world) {
        // If the fake player has a reference to the world getting unloaded,
        // null out the fake player so that the world can unload
        EnchantmentSeerFakePlayer actual = INSTANCE != null ? INSTANCE.get() : null;
        if (actual != null && actual.level == world) {
            actual.level = null;
        }
    }

    /**
     * Game profile supporting our UUID emulation
     */
    private static class FakeGameProfile extends GameProfile {

        private EnchantmentSeerFakePlayer myFakePlayer = null;

        public FakeGameProfile() {
            super(EnchantmentsSeer.gameProfile.getId(), EnchantmentsSeer.gameProfile.getName());
        }

        private UUID getEmulatingUUID() {
            return myFakePlayer != null ? myFakePlayer.emulatingUUID : null;
        }

        @Override
        public UUID getId() {
            UUID emulatingUUID = getEmulatingUUID();
            return emulatingUUID != null ? emulatingUUID : super.getId();
        }

        @Override
        public String getName() {
            UUID emulatingUUID = getEmulatingUUID();
            return emulatingUUID != null ? EnchantmentsSeerUtils.getLastKnownUsername(emulatingUUID) : super.getName();
        }

        //NB: super check they're the same class, we only check that name & id match
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GameProfile)) {
                return false;
            }

            final GameProfile that = (GameProfile) o;

            if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
                return false;
            }
            return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
        }

        @Override
        public int hashCode() {
            int result = getId() != null ? getId().hashCode() : 0;
            result = 31 * result + (getName() != null ? getName().hashCode() : 0);
            return result;
        }
    }

}
