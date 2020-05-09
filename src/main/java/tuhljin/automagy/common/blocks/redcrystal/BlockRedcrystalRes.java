package tuhljin.automagy.common.blocks.redcrystal;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.RedstoneCalc.PowerResult;
import tuhljin.automagy.common.lib.struct.BlockWithPos;
import tuhljin.automagy.common.tiles.TileRedcrystal;

public class BlockRedcrystalRes extends BlockRedcrystalLarge {
    public static final int MAX_MINIMUM = 15;
    public static final int MIN_MINIMUM = 2;
    private static final int MAX_DISTANCE = 15;

    public BlockRedcrystalRes() {
    }

    @Override
    protected PowerResult calculateRedstonePowerAt(World world, BlockPos pos, EnumFacing orientation) {
        int receivingStrength = 0;

        TileRedcrystal te;
        try {
            te = (TileRedcrystal)world.getTileEntity(pos);
        } catch (Exception ex) {
            return null;
        }

        if (te != null) {
            receivingStrength = Math.max(te.extraData - 1, 0);
            if (receivingStrength >= 99) {
                return new PowerResult(receivingStrength, null);
            }
        }

        PowerResult result = super.calculateRedstonePowerAt(world, pos, orientation);
        if (result != null && result.strength > 0) {
            return receivingStrength > result.strength ? new PowerResult(receivingStrength, null) : result;
        } else {
            return receivingStrength > 0 ? new PowerResult(receivingStrength, null) : null;
        }
    }

    public BlockWithPos findSignalConnection(World world, BlockPos pos, boolean reverse) {
        EnumFacing orientation = this.getOrientation(world, pos);
        if (orientation != null) {
            EnumFacing opp = orientation.getOpposite();
            EnumFacing dir = reverse ? opp : orientation;

            for(int distance = 1; distance <= MAX_DISTANCE; ++distance) {
                BlockPos pos2 = pos.offset(dir, distance);
                Block target = TjUtil.getBlock(world, pos2);
                if (target instanceof BlockRedcrystalRes) {
                    EnumFacing o = ((BlockRedcrystalRes)target).getOrientation(world, pos2);
                    if (reverse) {
                        if (orientation == o) {
                            return new BlockWithPos(target, world, pos2);
                        }

                        if (opp == o) {
                            return null;
                        }
                    } else {
                        if (orientation == o) {
                            return null;
                        }

                        if (opp == o) {
                            return new BlockWithPos(target, world, pos2);
                        }
                    }
                }
            }
        }

        return null;
    }

    public void receiveWirelessSignal(World world, BlockPos pos, int strength) {
        TileRedcrystal te;
        try {
            te = (TileRedcrystal)world.getTileEntity(pos);
        } catch (Exception ex) {
            return;
        }

        if (strength == -1) {
            strength = 0;
            BlockWithPos target = this.findSignalConnection(world, pos, false);
            if (target != null) {
                BlockRedcrystalRes blockR = (BlockRedcrystalRes)target.block;
                blockR.receiveWirelessSignal(world, target, blockR.getRedstoneSignalStrength(world, pos, true));
                strength = blockR.getRedstoneSignalStrength(world, target, true);
            }
        }

        if (te.extraData != strength) {
            te.extraData = strength;
            te.markDirty();
            this.updateAndPropagateChanges(world, pos, true, false, false, false);
        }

    }

    @Override
    public void scheduleAreaUpdateNotification(World world, BlockPos pos, boolean immediate) {
        super.scheduleAreaUpdateNotification(world, pos, immediate);
        if (!world.isRemote) {
            BlockWithPos target = this.findSignalConnection(world, pos, false);
            if (target != null) {
                BlockRedcrystalRes blockR = (BlockRedcrystalRes)target.block;
                blockR.receiveWirelessSignal(world, target, blockR.getRedstoneSignalStrength(world, pos, true));
            }
        }

    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        BlockWithPos target = null;
        if (!world.isRemote) {
            target = this.findSignalConnection(world, pos, false);
        }

        super.breakBlock(world, pos, state);
        if (target != null) {
            ((BlockRedcrystalRes)target.block).receiveWirelessSignal(world, target, -1);
        }

    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            TileRedcrystal te;
            try {
                te = (TileRedcrystal)world.getTileEntity(pos);
            } catch (Exception ex) {
                return;
            }

            BlockWithPos target = this.findSignalConnection(world, pos, false);
            BlockRedcrystalRes blockR;
            if (target != null) {
                blockR = (BlockRedcrystalRes)target.block;
                te.extraData = blockR.getRedstoneSignalStrength(world, target, true);
                te.markForUpdate();
            }

            target = this.findSignalConnection(world, pos, true);
            if (target != null) {
                blockR = (BlockRedcrystalRes)target.block;
                blockR.receiveWirelessSignal(world, target, 0);
            }
        }

        super.onBlockAdded(world, pos, state);
    }
}
