package tuhljin.automagy.common.blocks;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.lib.References;

import javax.annotation.Nonnull;

public abstract class ModBlockTorchWithTE extends BlockTorch implements ITileEntityProvider {

    public ModBlockTorchWithTE(String name) {
        super();
        setTranslationKey(name);
        setRegistryName(new ResourceLocation(References.MOD_ID, name));
    }

    @Override
    public boolean eventReceived(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, int eventID, int eventParam) {
        super.eventReceived(state, world, pos, eventID, eventParam);
        TileEntity tileentity = world.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }

    protected boolean shouldBeOff(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING).getOpposite();
        return world.isSidePowered(pos.offset(enumfacing), enumfacing);
    }

    public int getPowerIntoAttachedBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING).getOpposite();
        return world.getRedstonePower(pos.offset(enumfacing), enumfacing);
    }
}