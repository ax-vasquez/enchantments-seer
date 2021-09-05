package com.nuggylib.enchantmentsseer.common.network.to_client;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.network.IEnchantmentsSeerPacket;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityUpdateable;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_client/PacketUpdateTile.java"
 */
public class PacketUpdateTile implements IEnchantmentsSeerPacket {

    private final CompoundNBT updateTag;
    private final BlockPos pos;

    public PacketUpdateTile(TileEntityUpdateable tile) {
        this(tile.getBlockPos(), tile.getReducedUpdateTag());
    }

    private PacketUpdateTile(BlockPos pos, CompoundNBT updateTag) {
        this.pos = pos;
        this.updateTag = updateTag;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ClientWorld world = Minecraft.getInstance().level;
        if (world != null) {
            TileEntityUpdateable tile = WorldUtils.getTileEntity(TileEntityUpdateable.class, world, pos, true);
            if (tile == null) {
                EnchantmentsSeer.logger.info("Update tile packet received for position: {} in world: {}, but no valid tile was found.", pos,
                        world.dimension().location());
            } else {
                tile.handleUpdatePacket(updateTag);
            }
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeNbt(updateTag);
    }

    public static PacketUpdateTile decode(PacketBuffer buffer) {
        return new PacketUpdateTile(buffer.readBlockPos(), buffer.readNbt());
    }
}
