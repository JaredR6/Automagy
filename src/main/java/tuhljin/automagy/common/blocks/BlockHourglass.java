package tuhljin.automagy.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import thaumcraft.common.items.casters.ItemCaster;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.lib.NeighborNotifier;
import tuhljin.automagy.common.lib.RedstoneCalc;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.RedstoneCalc.PowerResult;
import tuhljin.automagy.common.tiles.TileHourglass;

import javax.annotation.Nonnull;

public class BlockHourglass extends ModTileRenderedBlockWithFacing implements IBlockFacesHorizontal {
    public static final int TIMER_MINIMUM = 5;
    public static final int TIMER_MAXIMUM = 60;
    public static final int TIMER_INCREMENTS = 5;
    public static final AxisAlignedBB DEFAULT_BOUNDS = new AxisAlignedBB(0.35F, 0.0F, 0.35F, 0.65F, 0.4F, 0.65F);


    public BlockHourglass(Material material, MapColor mapColor, String name) {
        super(material, mapColor, name);
        this.setHardness(1.0F);
    }

    public BlockHourglass() {
        this(Material.WOOD, Material.WOOD.getMaterialMapColor(), References.BLOCK_HOURGLASS);
        this.setSoundType(SoundType.WOOD);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileHourglass();
    }

    @Override
    public boolean canPlaceBlockAt(@Nonnull World world, @Nonnull BlockPos pos) {
        return this.canBlockStay(world, pos) && super.canPlaceBlockAt(world, pos);
    }

    public boolean canBlockStay(@Nonnull World world, @Nonnull BlockPos pos) {
        return TjUtil.isAcceptableSurfaceBelowPos(world, pos, true, true, true);
    }

    @Override
    public void onBlockAdded(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileHourglass te = (TileHourglass)world.getTileEntity(pos);
        PowerResult result = RedstoneCalc.getRedstonePowerAt(world, pos, this.getRedstoneInputDirections(world, pos, state));
        if (result != null) {
            te.receivingSignal = true;
            te.markForUpdate();
        }

    }

    @Override
    public void neighborChanged(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        } else if (!world.isRemote) {
            TileHourglass te = null;

            try {
                te = (TileHourglass)world.getTileEntity(pos);
            } catch (Exception ignored) {
            }

            if (te != null) {
                PowerResult result = RedstoneCalc.getRedstonePowerAt(world, pos, this.getRedstoneInputDirections(world, pos, state));
                te.updateRedstoneInput(result != null);
            }
        }

    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        NeighborNotifier.notifyBlocksOfExtendedNeighborChange(world, pos);
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileHourglass te = null;

            try {
                te = (TileHourglass)world.getTileEntity(pos);
            } catch (Exception ignored) {
            }

            if (te == null) {
                return true;
            }

            ItemStack stack = player.getActiveItemStack();
            if (!stack.isEmpty()) {
                Item equippedItem = stack.getItem();
                if (equippedItem == Items.STICK || equippedItem instanceof ItemCaster) {
                    int target = te.getTargetTimeSeconds();
                    target += TIMER_INCREMENTS;
                    if (target > TIMER_MAXIMUM) {
                        target = TIMER_MINIMUM;
                    }

                    te.setTargetTimeSeconds(target);
                    TjUtil.sendFormattedChatToPlayer(player, "automagy.chat.hourglass.setTimer", target);
                    world.playSound(player, pos, SoundsTC.key, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return true;
                }
            }

            if (te.startFlipOnServer(true)) {
                world.playSound(null, pos, SoundsTC.clack, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }

        return true;
    }

    @Override
    public int getWeakPower(@Nonnull IBlockState state, @Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos, EnumFacing side) {
        EnumFacing orientation = state.getValue(this.FACING);
        if (side != orientation && side != orientation.getOpposite()) {
            TileHourglass te = null;

            try {
                te = (TileHourglass)blockaccess.getTileEntity(pos);
            } catch (Exception ignored) {
            }

            return te == null ? 0 : te.getRedstoneSignalStrength();
        } else {
            return 0;
        }
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos) {
        TileHourglass te = null;

        try {
            te = (TileHourglass)world.getTileEntity(pos);
        } catch (Exception ignored) {
        }

        return te == null ? 0 : te.getComparatorSignalStrength();
    }

    @Nonnull
    public EnumFacing[] getRedstoneInputDirections(IBlockAccess blockaccess, BlockPos pos, @Nonnull IBlockState state) {
        EnumFacing orientation = (EnumFacing)state.getValue(this.FACING);
        return orientation != EnumFacing.NORTH && orientation != EnumFacing.SOUTH ? new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.UP} : new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.DOWN, EnumFacing.UP};
    }
}
