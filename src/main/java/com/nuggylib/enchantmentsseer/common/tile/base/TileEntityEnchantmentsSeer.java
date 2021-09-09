package com.nuggylib.enchantmentsseer.common.tile.base;

import com.nuggylib.enchantmentsseer.api.IContentsListener;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.api.NBTConstants;
import com.nuggylib.enchantmentsseer.api.DataHandlerUtils;
import com.nuggylib.enchantmentsseer.api.inventory.IEnchantmentsSeerInventory;
import com.nuggylib.enchantmentsseer.api.providers.IBlockProvider;
import com.nuggylib.enchantmentsseer.client.render.text.TextComponentUtil;
import com.nuggylib.enchantmentsseer.common.block.IHasTileEntity;
import com.nuggylib.enchantmentsseer.common.block.attribute.Attribute;
import com.nuggylib.enchantmentsseer.common.block.attribute.AttributeGui;
import com.nuggylib.enchantmentsseer.common.capabilities.holder.slot.IInventorySlotHolder;
import com.nuggylib.enchantmentsseer.common.capabilities.resolver.manager.ICapabilityHandlerManager;
import com.nuggylib.enchantmentsseer.common.capabilities.resolver.manager.ItemHandlerManager;
import com.nuggylib.enchantmentsseer.common.tile.component.TileComponentConfig;
import com.nuggylib.enchantmentsseer.common.tile.component.config.ITileComponent;
import com.nuggylib.enchantmentsseer.common.tile.interfaces.ISustainedInventory;
import com.nuggylib.enchantmentsseer.common.tile.interfaces.ITileDirectional;
import com.nuggylib.enchantmentsseer.api.inventory.IInventorySlot;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Inspired by the Mekanism codebase.
 *
 * Our version of this file is heavily reduced in size since much of what Mekanism has is specific to content that
 * simply doesn't exist in this mod.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/base/TileEntityMekanism.java"
 */
public abstract class TileEntityEnchantmentsSeer extends CapabilityTileEntity implements ITickableTileEntity, IEnchantmentsSeerInventory, ISustainedInventory,
        ITileDirectional, IContentsListener {

    protected final ItemHandlerManager itemHandlerManager;
    private final List<ICapabilityHandlerManager<?>> capabilityHandlerManagers = new ArrayList<>();
    // Taken from https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/prefab/TileEntityConfigurableMachine.java#L14
    public TileComponentConfig configComponent;//does not tick!

    /**
     * The players currently using this block.
     */
    public final Set<PlayerEntity> playersUsing = new ObjectOpenHashSet<>();

    /**
     * A timer used to send packets to clients.
     */
    public int ticker;
    private final List<ITileComponent> components = new ArrayList<>();

    protected final IBlockProvider blockProvider;

    private boolean hasGui;

    //Methods for implementing ITileDirectional
    @Nullable
    private Direction cachedDirection;

    public TileEntityEnchantmentsSeer(IBlockProvider blockProvider) {
        super(((IHasTileEntity<? extends TileEntity>) blockProvider.getBlock()).getTileType());
        this.blockProvider = blockProvider;
        Block block = this.blockProvider.getBlock();
        capabilityHandlerManagers.add(itemHandlerManager = new ItemHandlerManager(getInitialInventory(), this));
        setSupportedTypes(block);
        presetVariables();
    }

    private void setSupportedTypes(Block block) {
        //Used to get any data we may need
        hasGui = Attribute.has(block, AttributeGui.class);
    }

    /**
     * Sets variables up, called immediately after {@link #setSupportedTypes(Block)} but before any things start being created.
     *
     * @implNote This method should be used for setting any variables that would normally be set directly, except that gets run to late to set things up properly in our
     * constructor.
     */
    protected void presetVariables() {
    }

    public Block getBlockType() {
        return blockProvider.getBlock();
    }

    public final boolean hasGui() {
        return hasGui;
    }

    public void addComponent(ITileComponent component) {
        components.add(component);
    }

    public List<ITileComponent> getComponents() {
        return components;
    }

    @Nonnull
    public ITextComponent getName() {
        return TextComponentUtil.translate(Util.makeDescriptionId("container", getBlockType().getRegistryName()));
    }

    protected void notifyComparatorChange() {
        level.updateNeighbourForOutputSignal(worldPosition, getBlockType());
    }

    public ActionResultType openGui(PlayerEntity player) {
        try {
            if (hasGui() && !isRemote() && !player.isShiftKeyDown()) {
                // NOTE: Without the !isRemote() check, casting player to ServerPlayerEntity will fail - you must always ensure that this check is performed
                NetworkHooks.openGui((ServerPlayerEntity) player, Attribute.get(blockProvider.getBlock(), AttributeGui.class).getProvider(this), worldPosition);
                return ActionResultType.SUCCESS;
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            EnchantmentsSeer.logger.error(String.format("Failed to open GUI. There is a problem with the container and/or gui slot configuration for: %s", blockProvider.getBlock().getRegistryName()));
        } catch (Exception e) {
            EnchantmentsSeer.logger.error(String.format("Failed to open menu. Error: %s", e));
        }

        return ActionResultType.PASS;
    }

    public void open(PlayerEntity player) {
        playersUsing.add(player);
    }

    public void close(PlayerEntity player) {
        playersUsing.remove(player);
    }

    @Override
    public final boolean hasInventory() {
        return itemHandlerManager.canHandle();
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbtTags) {
        super.load(state, nbtTags);
        for (ITileComponent component : components) {
            component.read(nbtTags);
        }
        if (hasInventory() && persistInventory()) {
            DataHandlerUtils.readContainers(getInventorySlots(null), nbtTags.getList(NBTConstants.ITEMS, NBT.TAG_COMPOUND));
        }
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbtTags) {
        super.save(nbtTags);
        for (ITileComponent component : components) {
            component.write(nbtTags);
        }
        return nbtTags;
    }

    //Methods for implementing ITileContainer
    @Nullable
    protected IInventorySlotHolder getInitialInventory() {
        return null;
    }

    /**
     * Should the inventory be persisted in this tile save
     */
    public boolean persistInventory() {
        return hasInventory();
    }
    //End methods ITileContainer

    @Override
    public void tick() {
        ticker++;
    }

    @Nonnull
    @Override
    public final List<IInventorySlot> getInventorySlots(@Nullable Direction side) {
        return itemHandlerManager.getContainers(side);
    }

    @Override
    public void setInventory(ListNBT nbtTags, Object... data) {
        if (nbtTags != null && !nbtTags.isEmpty() && persistInventory()) {
            DataHandlerUtils.readContainers(getInventorySlots(null), nbtTags);
        }
    }

    @Override
    public ListNBT getInventory(Object... data) {
        return persistInventory() ? DataHandlerUtils.writeContainers(getInventorySlots(null)) : new ListNBT();
    }

    //Methods for implementing ITileDirectional
    @Nonnull
    @Override
    public final Direction getDirection() {
        if (isDirectional()) {
            if (cachedDirection != null) {
                return cachedDirection;
            }
            BlockState state = getBlockState();
            cachedDirection = Attribute.getFacing(state);
            if (cachedDirection != null) {
                return cachedDirection;
            } else if (!getType().isValid(state.getBlock())) {
                //This is probably always true if we couldn't get the direction it is facing
                // but double check just in case before logging
                EnchantmentsSeer.logger.warn("Error invalid block for tile {} at {} in {}. Unable to get direction, falling back to north, "
                        + "things will probably not work correctly. This is almost certainly due to another mod incorrectly "
                        + "trying to move this tile and not properly updating the position.", getType().getRegistryName(), worldPosition, level);
            }
        }
        //TODO: Remove, give it some better default, or allow it to be null
        return Direction.NORTH;
    }

    @Override
    public void setFacing(@Nonnull Direction direction) {
        if (isDirectional() && direction != cachedDirection && level != null) {
            cachedDirection = direction;
            BlockState state = Attribute.setFacing(getBlockState(), direction);
            if (state != null) {
                level.setBlockAndUpdate(worldPosition, state);
            }
        }
    }
    //End methods ITileDirectional

    /**
     * Update call for machines. Use instead of updateEntity -- it's called every tick on the client side.
     */
    protected void onUpdateClient() {
    }

    /**
     * Update call for machines. Use instead of updateEntity -- it's called every tick on the server side.
     */
    protected void onUpdateServer() {
    }

    @Override
    public void onContentsChanged() {
        markDirty(false);
    }
}
