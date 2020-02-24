package tuhljin.automagy.common.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IRedstoneOutput {
    boolean isAnySideOutputtingPower(IBlockAccess var1, BlockPos var2);
}