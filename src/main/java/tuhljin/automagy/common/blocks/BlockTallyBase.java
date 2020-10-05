package tuhljin.automagy.common.blocks;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import thaumcraft.api.casters.ICaster;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.gui.AutomagyGUIHandler;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.struct.FacingPropertyBool;
import tuhljin.automagy.common.tiles.TileRemoteComparator;
import tuhljin.automagy.common.tiles.TileTallyBase;

import javax.annotation.Nonnull;

public abstract class BlockTallyBase extends ModTileBlockWithFacing implements IRemoteComparatorOverride, IRedstoneOutput {
    public FacingPropertyBool outputProp;
    public PropertyBool HASPOWER;

    public BlockTallyBase() {
        super(Material.WOOD);
        this.setHardness(2.5F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.WOOD);
        IBlockState state = this.outputProp.addToState(this.getDefaultState()).withProperty(this.HASPOWER, false);
        this.setDefaultState(state);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if (this.FACING == null) {
            this.FACING = PropertyDirection.create("facing");
            this.outputProp = new FacingPropertyBool("output");
            this.HASPOWER = PropertyBool.create("power");
        }

        List<IProperty> list = this.outputProp.getProperties();
        list.add(this.FACING);
        list.add(this.HASPOWER);
        return new BlockStateContainer(this, list.toArray(new IProperty[0]));
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(this.FACING, facing.getOpposite());
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileTallyBase) {
            state = this.outputProp.addToState(state, ((TileTallyBase)te).outputDirs);
            state = state.withProperty(this.HASPOWER, ((TileTallyBase)te).getRedstoneOutput() > 0);
        }

        return state;
    }

    @Override
    public void onBlockAdded(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileTallyBase) {
                TileTallyBase teT = (TileTallyBase)te;
                boolean rcAbove = false;
                TileEntity teRC = world.getTileEntity(pos.offset(EnumFacing.UP));
                if (teRC instanceof TileRemoteComparator) {
                    ((TileRemoteComparator)teRC).setOverride(true);
                    if (state.getValue(this.FACING) != EnumFacing.UP) {
                        this.changeFacing(teT, world, pos, state, EnumFacing.UP, false);
                    }

                    rcAbove = true;
                }

                teT.setOutputDir(((EnumFacing)state.getValue(this.FACING)).getOpposite(), true);
                teT.findNewTarget(rcAbove);
            }
        }

    }

    @Override
    public void onNeighborChange(@Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull BlockPos neighbor) {
        World world = (World)blockAccess;
        if (!world.isRemote) {
            TileTallyBase teT = null;
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileTallyBase) {
                teT = (TileTallyBase)te;
            }

            TileEntity teRC = world.getTileEntity(pos.offset(EnumFacing.UP));
            if (teRC instanceof TileRemoteComparator) {
                if (TjUtil.getBlock(blockAccess, neighbor) instanceof BlockRemoteComparator) {
                    ((TileRemoteComparator)teRC).setOverride(true);
                }

                if (world.getBlockState(pos).getValue(this.FACING) != EnumFacing.UP) {
                    this.changeFacing(teT, world, pos, world.getBlockState(pos), EnumFacing.UP, true);
                }
            }

            if (teT != null) {
                teT.findNewTarget(true);
            }
        }

    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nullable EnumFacing side) {
        if (side == null) {
            return false;
        } else {
            TileEntity te = world.getTileEntity(pos);
            return (te instanceof TileTallyBase) && ((TileTallyBase) te).isOutputDir(side.getOpposite());
        }
    }

    @Override
    public int getStrongPower(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return this.getWeakPower(state, world, pos, side);
    }

    @Override
    public int getWeakPower(IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nullable EnumFacing side) {
        if (side == null) {
            return 0;
        } else {
            TileEntity te = world.getTileEntity(pos);
            return te instanceof TileTallyBase ? ((TileTallyBase)te).getRedstoneOutput(side.getOpposite()) : 0;
        }
    }

    @Override
    public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack held = player.getHeldItem(hand);
        if (!held.isEmpty()) {
            Item item = held.getItem();
            if (item == ModItems.tallyLens) {
                return true;
            }

            if (side == EnumFacing.UP && item == Item.getItemFromBlock(ModBlocks.remoteComparator)) {
                return false;
            }

            if (!world.isRemote && item instanceof ICaster) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileTallyBase) {
                    TileTallyBase teT = (TileTallyBase)te;
                    teT.setOutputDir(side, !teT.isOutputDir(side));
                    world.playSound(player, pos, SoundsTC.key, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return true;
            }
        }

        if (!world.isRemote) {
            AutomagyGUIHandler.openGUI(5, player, world, pos);
        }

        return true;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (!world.isRemote) {
            TileEntity teRC = world.getTileEntity(pos.offset(EnumFacing.UP));
            if (teRC instanceof TileRemoteComparator) {
                ((TileRemoteComparator)teRC).setOverride(false);
            }
        }

        super.breakBlock(world, pos, state);
    }

    public boolean isAnySideOutputtingPower(@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileTallyBase) {
            return ((TileTallyBase)te).getRedstoneOutput() > 0;
        } else {
            return false;
        }
    }

    public int getRemoteComparatorParticleColor(@Nonnull World world, @Nonnull BlockPos pos, TileRemoteComparator teRC) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileTallyBase && ((TileTallyBase)te).hasValidTarget() ? 0 : -1;
    }

    @Override
    public boolean hasActiveRedstoneSignal(@Nonnull World world, @Nonnull BlockPos pos, TileRemoteComparator teRC) {
        return this.isAnySideOutputtingPower(world, pos);
    }

    public void changeFacing(@Nonnull TileTallyBase te, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EnumFacing facing, boolean blockUpdate) {
        if (te.isOutputDir(facing)) {
            te.setOutputDir(facing, false);
        }

        world.setBlockState(pos, state.withProperty(this.FACING, facing), blockUpdate ? 3 : 2);
    }
}
