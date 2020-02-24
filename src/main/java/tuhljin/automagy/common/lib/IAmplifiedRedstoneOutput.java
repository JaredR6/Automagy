package tuhljin.automagy.common.lib;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IAmplifiedRedstoneOutput {
    int getRedstoneSignalStrength(IBlockAccess var1, BlockPos var2, EnumFacing var3, boolean var4);

    boolean isRedstoneWire();
}
