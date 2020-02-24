package tuhljin.automagy.common.lib.struct;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockWithPos extends BlockPos {
    public final Block block;
    public final IBlockAccess blockaccess;

    public BlockWithPos(Block block, IBlockAccess blockaccess, int x, int y, int z) {
        super(x, y, z);
        this.block = block;
        this.blockaccess = blockaccess;
    }

    public BlockWithPos(Block block, IBlockAccess blockaccess, BlockPos pos) {
        super(pos);
        this.block = block;
        this.blockaccess = blockaccess;
    }
}
