package tuhljin.automagy.common.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.items.IAutomagyLocationLink;
import tuhljin.automagy.common.lib.NeighborNotifier;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.TileRemoteComparator;

import javax.annotation.Nonnull;

public class BlockRemoteComparator extends ModTileRenderedBlockWithFacing implements IBlockFacesHorizontal {
    public BlockRemoteComparator() {
        super(ModBlocks.materialWaterproofCircuit);
        //this.func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setSoundType(SoundType.STONE);
        this.setHardness(2.5F);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileRemoteComparator();
    }

    @Override
    public boolean canPlaceBlockAt(@Nonnull World world, @Nonnull BlockPos pos) {
        return this.canBlockStay(world, pos) && super.canPlaceBlockAt(world, pos);
    }

    public boolean canBlockStay(@Nonnull World world, @Nonnull BlockPos pos) {
        return TjUtil.isAcceptableSurfaceBelowPos(world, pos, true, true, true);
    }

    @Override
    public void onNeighborChange(IBlockAccess blockAccess, @Nonnull BlockPos pos, BlockPos neighbor) {
        World world = (World)blockAccess;
        if (!this.canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockToAir(pos);
        }

    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        NeighborNotifier.notifyBlocksOfExtendedNeighborChange(worldIn, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileRemoteComparator) {
            TileRemoteComparator teRC = (TileRemoteComparator)te;
            if (teRC.getFloatingDisplayItem() != null) {
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();
                int color = -1;
                if (teRC.coordinatesAreInRange()) {
                    if (teRC.isOverridden()) {
                        Block block = TjUtil.getBlock(world, pos.offset(EnumFacing.DOWN));
                        if (block instanceof IRemoteComparatorOverride) {
                            color = ((IRemoteComparatorOverride)block).getRemoteComparatorParticleColor(world, pos.offset(EnumFacing.DOWN), teRC);
                        }
                    } else if (teRC.getBlockAtLinkedLocationIfValid() != null) {
                        color = 2;
                    }

                    if (color == -1) {
                        return;
                    }
                } else {
                    color = 5;
                }

                ThaumcraftExtension.sparkle((float)x + 0.2F + rand.nextFloat() * 0.6F, (float)y + 0.2F, (float)z + 0.2F + rand.nextFloat() * 0.6F, 1.0F, color, -0.1F);
            }
        }

    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileRemoteComparator te = (TileRemoteComparator)world.getTileEntity(pos);
        ItemStack stack = te.getStackInSlot(0);
        ItemStack playerStack = player.getHeldItem(hand);
        if (stack.isEmpty()) {
            if (!playerStack.isEmpty()) {
                if (te.canInsertItem(0, playerStack, EnumFacing.UP)) {
                    te.setInventorySlotContents(0, playerStack.splitStack(1));
                    if (playerStack.getCount() <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                    }

                    te.markForUpdate();
                    world.notifyNeighborsOfStateChange(pos, this, true);
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 1.5F, false);
                    return true;
                }

                    if (playerStack.getItem() instanceof IAutomagyLocationLink) {
                    if (world.isRemote) {
                        TjUtil.sendChatToPlayer(player, "automagy.chat.remoteComparator.itemUnlinked");
                    }

                    return true;
                }
            }
        } else if (playerStack.isEmpty() || playerStack.getItem() instanceof IAutomagyLocationLink) {
            if (!player.inventory.addItemStackToInventory(stack)) {
                player.dropItem(stack, false);
            }

            te.setInventorySlotContents(0, ItemStack.EMPTY);
            te.markForUpdate();
            world.notifyNeighborsOfStateChange(pos, this, true);
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 1.5F, false);
            return true;
        }

        return false;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getStrongPower(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return side != EnumFacing.UP ? this.getWeakPower(state, world, pos, side) : 0;
    }

    @Override
    public int getWeakPower(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileRemoteComparator ? ((TileRemoteComparator)te).getRedstoneSignalStrength() : 0;
    }
}
