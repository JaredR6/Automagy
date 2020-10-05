package tuhljin.automagy.common.lib.compat.waila;

import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import tuhljin.automagy.common.tiles.TileHourglass;

import javax.annotation.Nonnull;

public class ProviderForHourglass implements IWailaDataProvider {
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
        TileHourglass te;
        try {
            te = (TileHourglass)accessor.getTileEntity();
        } catch (Exception var11) {
            return currenttip;
        }

        int time = te.getTargetTimeSeconds();
        int timeLeft;
        int min;
        if (time == 1) {
            currenttip.add(I18n.format("wailaTip.hourglassTimerSecOne"));
        } else if (time <= 60) {
            currenttip.add(I18n.format("wailaTip.hourglassTimerSec", time));
        } else {
            timeLeft = time / 60;
            min = time % 60;
            currenttip.add(I18n.format("wailaTip.hourglassTimerMinSec", timeLeft, min));
        }

        timeLeft = te.getRemainingSeconds();
        if (timeLeft > 0) {
            min = timeLeft / 60;
            int sec = timeLeft % 60;
            String sSec = sec < 10 ? "0" + sec : String.valueOf(sec);
            currenttip.add(I18n.format("wailaTip.hourglassTimerCountdown", min, sSec));
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
