package tuhljin.automagy.common.blocks.redcrystal;

import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import tuhljin.automagy.common.tiles.TileRedcrystal;

public class BlockRedcrystalLarge extends BlockRedcrystal {

    private AxisAlignedBB AABB_BASIC_DOWN = new AxisAlignedBB(0.12F, 0.8F, 0.12F, 0.88F, 1.0F, 0.88F);
    private AxisAlignedBB AABB_BASIC_NORTH = new AxisAlignedBB(0.12F, 0.12F, 0.8F, 0.88F, 0.88F, 1.0F);
    private AxisAlignedBB AABB_BASIC_SOUTH = new AxisAlignedBB(0.12F, 0.12F, 0.0F, 0.88F, 0.88F, 0.2F);
    private AxisAlignedBB AABB_BASIC_WEST = new AxisAlignedBB(0.8F, 0.12F, 0.12F, 1.0F, 0.88F, 0.88F);
    private AxisAlignedBB AABB_BASIC_EAST = new AxisAlignedBB(0.0F, 0.12F, 0.12F, 0.2F, 0.88F, 0.88F);
    private AxisAlignedBB AABB_BASIC_UP = new AxisAlignedBB(0.12F, 0.0F, 0.12F, 0.88F, 0.2F, 0.88F);

    @Override
    protected void addTraceableCuboids(List<IndexedCuboid6> cuboids, IBlockAccess source, BlockPos pos) {
        ((TileRedcrystal)source.getTileEntity(pos)).addTraceableCuboids(cuboids, pos, true);
    }

    @Override
    protected AxisAlignedBB getNoCasterBlockBounds(IBlockAccess blockAccess, BlockPos pos) {
        EnumFacing orientation = this.getOrientation(blockAccess, pos);
        if (orientation == null) {
            orientation = EnumFacing.UP;
        }

        switch(orientation) {
            case DOWN:
                return AABB_BASIC_DOWN;
            case NORTH:
                return AABB_BASIC_NORTH;
            case SOUTH:
                return AABB_BASIC_SOUTH;
            case WEST:
                return AABB_BASIC_WEST;
            case EAST:
                return AABB_BASIC_EAST;
            case UP:
            default:
                return AABB_BASIC_UP;
        }

    }
}
