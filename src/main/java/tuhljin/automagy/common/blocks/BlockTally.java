package tuhljin.automagy.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tuhljin.automagy.common.tiles.TileTally;

import javax.annotation.Nonnull;

public class BlockTally extends BlockTallyBase {
    public BlockTally() {
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileTally();
    }
}
