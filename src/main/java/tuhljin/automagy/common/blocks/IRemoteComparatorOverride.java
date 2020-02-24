package tuhljin.automagy.common.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tuhljin.automagy.common.tiles.TileRemoteComparator;

public interface IRemoteComparatorOverride {
    int getRemoteComparatorParticleColor(World var1, BlockPos var2, TileRemoteComparator var3);

    boolean hasActiveRedstoneSignal(World var1, BlockPos var2, TileRemoteComparator var3);
}
