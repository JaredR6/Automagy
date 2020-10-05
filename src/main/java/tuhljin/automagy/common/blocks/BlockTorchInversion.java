package tuhljin.automagy.common.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.lib.AutomagyConfig;
import tuhljin.automagy.common.lib.NeighborNotifier;
import tuhljin.automagy.common.tiles.TileTorchInversion;

import javax.annotation.Nonnull;

public class BlockTorchInversion extends ModBlockTorchWithTE {
    private boolean isLit;
    @Nonnull
    private static HashMap<World, ArrayList<Toggle>> toggles = new HashMap<>();

    public BlockTorchInversion(boolean lit) {
        this.isLit = lit;
        if (lit) {
            this.setLightLevel(0.5F);
        }

        this.setTickRandomly(false);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return this.isLit ? new TileTorchInversion() : null;
    }

    private boolean isBurnedOut(@Nonnull World world, BlockPos pos, boolean turnOff) {
        if (!toggles.containsKey(world)) {
            toggles.put(world, new ArrayList<>());
        }

        ArrayList<Toggle> list = toggles.get(world);
        Toggle newToggle = null;
        if (turnOff) {
            newToggle = new BlockTorchInversion.Toggle(pos, world.getTotalWorldTime());
            list.add(newToggle);
        }

        int l = 0;

        for (Toggle value : list) {
            if (value.pos.equals(pos)) {
                ++l;
                if (l >= AutomagyConfig.redstoneBurnoutPoint || value.forceWait) {
                    if (newToggle != null && !value.forceWait) {
                        newToggle.forceWait = true;
                    }

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, @Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos, EnumFacing side) {
        if (this.isLit) {
            TileEntity te = blockaccess.getTileEntity(pos);
            if (te instanceof TileTorchInversion) {
                return ((TileTorchInversion)te).getRedstoneSignalStrength();
            }
        }

        return 0;
    }

    @Override
    public int getStrongPower(IBlockState state, @Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? this.getWeakPower(state, blockaccess, pos, side) : 0;
    }

    @Nonnull
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.torchInversion_on);
    }

    @Override
    public void onNeighborChange(IBlockAccess source, @Nonnull BlockPos pos, BlockPos neighbor) {
        World world = (World)source;
        if (!this.onNeighborChangeInternal(world, pos, world.getBlockState(pos))) {
            world.scheduleUpdate(pos, this, this.tickRate(world));
        }

    }

    @Override
    public void onBlockAdded(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleUpdate(pos, this, this.tickRate(world));
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (this.isLit) {
            NeighborNotifier.notifyBlocksOfExtendedNeighborChange(world, pos);
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public int tickRate(World world) {
        return 2;
    }

    @Override
    public void randomTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random random) {
    }

    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        ArrayList<Toggle> list = toggles.get(world);

        while(list != null && !list.isEmpty() && world.getTotalWorldTime() - list.get(0).time > 60L) {
            list.remove(0);
        }

        if (this.isLit) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileTorchInversion) {
                TileTorchInversion teTI = (TileTorchInversion)te;
                int prev = teTI.getRedstoneSignalStrength();
                int strength = this.getPowerIntoAttachedBlock(world, pos, state);
                strength = 15 - strength;
                if (strength != prev && this.isBurnedOut(world, pos, true)) {
                    strength = 0;
                    int x = pos.getX();
                    int y = pos.getY();
                    int z = pos.getZ();
                    world.playSound(null, x + 0.5D, y + 0.5D, z + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                    for(int l = 0; l < 5; ++l) {
                        double d0 = x + rand.nextDouble() * 0.6D + 0.2D;
                        double d1 = y + rand.nextDouble() * 0.6D + 0.2D;
                        double d2 = z + rand.nextDouble() * 0.6D + 0.2D;
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                    }

                    world.scheduleUpdate(pos, world.getBlockState(pos).getBlock(), 160);
                }

                if (strength == 0) {
                    world.setBlockState(pos, ModBlocks.torchInversion_off.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
                } else if (strength != prev) {
                    teTI.setRedstoneSignalStrength(strength);
                    NeighborNotifier.notifyBlocksOfExtendedNeighborChange(world, pos);
                }
            }
        } else if (this.getPowerIntoAttachedBlock(world, pos, state) < 15 && !this.isBurnedOut(world, pos, false)) {
            world.setBlockState(pos, ModBlocks.torchInversion_on.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (this.isLit) {
            double x = pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            double y = pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
            double z = pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            EnumFacing enumfacing = state.getValue(FACING);
            if (enumfacing.getAxis().isHorizontal()) {
                EnumFacing opp = enumfacing.getOpposite();
                double ratio = 0.27D;
                x += ratio * opp.getFrontOffsetX();
                y += ratio;
                z += ratio * opp.getFrontOffsetZ();
            }

            world.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
        }

    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(ModBlocks.torchInversion_on);
    }

    @Override
    public boolean isAssociatedBlock(Block block) {
        return block == ModBlocks.torchInversion_off || block == ModBlocks.torchInversion_on;
    }

    static class Toggle {
        BlockPos pos;
        long time;
        boolean forceWait = false;

        public Toggle(BlockPos pos, long time) {
            this.pos = pos;
            this.time = time;
        }
    }
}
