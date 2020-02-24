//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package tuhljin.automagy.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tuhljin.automagy.common.lib.NeighborNotifier;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.inventory.IContainsFilter;

import javax.annotation.Nonnull;

public abstract class ModTileEntityBlock extends ModBlock implements ITileEntityProvider {
    public ModTileEntityBlock(Material material, MapColor mapColor) {
        super(material, mapColor);
        this.hasTileEntity = true;
    }

    public ModTileEntityBlock(Material material) {
        this(material, material.getMaterialMapColor());
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess worldIn, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam) {
        super.eventReceived(state, worldIn, pos, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        handleBreakBlock(world, pos, state);
        super.breakBlock(world, pos, state);
    }

    public static void handleBreakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            if (te instanceof IInventory) {
                TjUtil.dropItemsIntoWorld((IInventory)te, world, pos, (Block)null);
            }

            if (te instanceof IContainsFilter) {
                TjUtil.dropItemsIntoWorld(((IContainsFilter)te).getFilterInventory(), world, pos, (Block)null);
            }

            if (te instanceof IRedstoneOutput && ((IRedstoneOutput)te).isAnySideOutputtingPower(world, pos)) {
                NeighborNotifier.notifyBlocksOfExtendedNeighborChange(world, pos);
            }
        }

    }
}
