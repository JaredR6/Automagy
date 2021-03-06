package tuhljin.automagy.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class ModTileBlockWithFacing extends ModBlockWithFacing implements ITileEntityProvider {
    public ModTileBlockWithFacing(@Nonnull Material material, @Nonnull MapColor mapColor, String name) {
        super(material, mapColor, name);
        this.hasTileEntity = true;
    }

    public ModTileBlockWithFacing(@Nonnull Material material, String name) {
        this(material, material.getMaterialMapColor(), name);
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean eventReceived(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, int id, int param) {
        super.eventReceived(state, world, pos, id, param);
        TileEntity tileentity = world.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        ModTileEntityBlock.handleBreakBlock(world, pos, state);
        super.breakBlock(world, pos, state);
    }
}
