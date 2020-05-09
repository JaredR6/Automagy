package tuhljin.automagy.common.blocks;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class ModBlockTorchWithTE extends BlockTorch implements ITileEntityProvider {

    @Override
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int eventID, int eventParam) {
        super.eventReceived(state, world, pos, eventID, eventParam);
        TileEntity tileentity = world.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }

    protected boolean shouldBeOff(World world, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING).getOpposite();
        return world.isSidePowered(pos.offset(enumfacing), enumfacing);
    }

    public int getPowerIntoAttachedBlock(World world, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING).getOpposite();
        return world.getRedstonePower(pos.offset(enumfacing), enumfacing);
    }
}