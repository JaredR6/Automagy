package tuhljin.automagy.common.lib;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class NeighborNotifier {
    public NeighborNotifier() {
    }

    public static void notifyBlocksOfExtendedNeighborChange(@Nonnull World world, @Nonnull BlockPos pos) {
        notifyBlocksOfExtendedNeighborChange(world, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void notifyBlocksOfExtendedNeighborChange(@Nonnull World world, int x, int y, int z) {
        if (!world.isRemote) {
            notifyBlockUpdate(world, x - 1, y, z);
            notifyBlockUpdate(world, x + 1, y, z);
            notifyBlockUpdate(world, x, y - 1, z);
            notifyBlockUpdate(world, x, y + 1, z);
            notifyBlockUpdate(world, x, y, z - 1);
            notifyBlockUpdate(world, x, y, z + 1);
            notifyBlockUpdate(world, x - 1, y - 1, z);
            notifyBlockUpdate(world, x - 1, y, z - 1);
            notifyBlockUpdate(world, x - 1, y, z + 1);
            notifyBlockUpdate(world, x - 1, y + 1, z);
            notifyBlockUpdate(world, x, y - 1, z - 1);
            notifyBlockUpdate(world, x, y - 1, z + 1);
            notifyBlockUpdate(world, x, y + 1, z - 1);
            notifyBlockUpdate(world, x, y + 1, z + 1);
            notifyBlockUpdate(world, x + 1, y - 1, z);
            notifyBlockUpdate(world, x + 1, y, z - 1);
            notifyBlockUpdate(world, x + 1, y, z + 1);
            notifyBlockUpdate(world, x + 1, y + 1, z);
            notifyBlockUpdate(world, x - 2, y, z);
            notifyBlockUpdate(world, x + 2, y, z);
            notifyBlockUpdate(world, x, y - 2, z);
            notifyBlockUpdate(world, x, y + 2, z);
            notifyBlockUpdate(world, x, y, z - 2);
            notifyBlockUpdate(world, x, y, z + 2);
        }

    }

    private static void notifyBlockUpdate(@Nonnull World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }
}
