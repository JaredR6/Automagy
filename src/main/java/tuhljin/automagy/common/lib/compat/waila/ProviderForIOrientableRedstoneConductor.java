package tuhljin.automagy.common.lib.compat.waila;

import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalDense;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalDim;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalRes;
import tuhljin.automagy.common.lib.IOrientableRedstoneConductor;
import tuhljin.automagy.common.tiles.TileRedcrystal;

import javax.annotation.Nonnull;

public class ProviderForIOrientableRedstoneConductor implements IWailaDataProvider {
    public boolean SHOW_DEBUG = false;

    @Nonnull
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        IOrientableRedstoneConductor block;
        try {
            block = (IOrientableRedstoneConductor)accessor.getBlock();
        } catch (Exception var11) {
            return currenttip;
        }

        BlockPos pos = accessor.getPosition();
        int power = block.getRedstoneSignalStrength(accessor.getWorld(), pos, true);
        String s;
        int min;
        if (power > 15) {
            min = power - 15;
            s = I18n.format("wailaTip.redcrystalPowerWithAmp", 15, min);
        } else if (block instanceof BlockRedcrystalDim) {
            min = ((BlockRedcrystalDim)block).getRedstoneSignalCap(accessor.getWorld(), pos);
            s = I18n.format("wailaTip.redcrystalPowerWithCap", power, min);
        } else {
            s = I18n.format("wailaTip.redcrystalPower", power);
        }

        if (block instanceof BlockRedcrystalDense) {
            min = ((BlockRedcrystalDense)block).getRedstoneSignalMin(accessor.getWorld(), pos);
            s = s + "  " + I18n.format("wailaTip.redcrystalPowerMin", min);
        }

        if (!s.isEmpty()) {
            currenttip.add(s);
        }

        if (this.SHOW_DEBUG) {
            if (power > 0) {
                EnumFacing sourceSide = block.getRedstoneSignalSourceSide(accessor.getWorld(), pos);
                if (block.isRedstoneSignalInputPoint(accessor.getWorld(), pos)) {
                    currenttip.add("source=" + sourceSide + " [primary]");
                } else {
                    currenttip.add("source=" + sourceSide);
                }
            }

            if (block instanceof BlockRedcrystalRes) {
                TileEntity te = accessor.getTileEntity();
                if (te instanceof TileRedcrystal) {
                    int p = ((TileRedcrystal)te).extraData;
                    if (p <= 15) {
                        currenttip.add("Remote : " + p);
                    } else {
                        currenttip.add("Remote : 15 (+" + (p - 15) + ")");
                    }
                }

                currenttip.add(pos.getX() + "," + pos.getY() + "," + pos.getZ());
            }
        }

        return currenttip;
    }

    @Nonnull
    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        return tag;
    }
}
