package tuhljin.automagy.common.lib;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tuhljin.automagy.common.blocks.IRedcrystalPowerConductor;

public interface IOrientableRedstoneConductor extends IRedcrystalPowerConductor, IAmplifiedRedstoneOutput {

    int getRedstoneSignalStrength(IBlockAccess var1, BlockPos var2, boolean var3);

    EnumFacing getOrientation(IBlockAccess var1, BlockPos var2);

    EnumFacing getRedstoneSignalSourceSide(IBlockAccess var1, BlockPos var2);

    boolean canSendRedstoneSignalInDirection(IBlockAccess var1, BlockPos var2, EnumFacing var3);

    boolean canReceiveRedstoneSignalFromDirection(IBlockAccess var1, BlockPos var2, EnumFacing var3);

    void onNeighborRedstoneConductorUpdate(World var1, BlockPos var2, EnumFacing var3, int var4);

    boolean isRedstoneSignalInputPoint(IBlockAccess var1, BlockPos var2);
}
