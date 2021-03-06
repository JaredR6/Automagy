package tuhljin.automagy.common.lib;

import java.util.LinkedHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.lib.struct.BlockWithPos;

import javax.annotation.Nonnull;

public class RedstoneCalc {
    public static final int MAX_POWER = 15;
    public static final int MAX_POWER_AMPLIFIED = 99;
    public static final boolean COUNT_PISTONHEAD_AS_AIR = true;

    public RedstoneCalc() {
    }

    @Nullable
    public static RedstoneCalc.PowerResult getRedstonePowerAt(@Nonnull World world, @Nonnull BlockPos pos) {
        return getRedstonePowerAt(world, pos, EnumFacing.UP, false, (EnumFacing)null, false, MAX_POWER, EnumFacing.VALUES);
    }

    @Nullable
    public static RedstoneCalc.PowerResult getRedstonePowerAt(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing... directions) {
        return getRedstonePowerAt(world, pos, EnumFacing.UP, false, (EnumFacing)null, false, MAX_POWER, directions);
    }

    @Nullable
    public static RedstoneCalc.PowerResult getRedstonePowerAt(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing attachedToSide, boolean isWire, EnumFacing excludeRedstoneWireDir, boolean allowAmplifiedPower) {
        return getRedstonePowerAt(world, pos, attachedToSide, isWire, excludeRedstoneWireDir, allowAmplifiedPower, MAX_POWER, EnumFacing.VALUES);
    }

    @Nullable
    public static RedstoneCalc.PowerResult getRedstonePowerAt(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing attachedToSide, boolean isWire, EnumFacing excludeRedstoneWireDir, boolean allowAmplifiedPower, EnumFacing... directions) {
        return getRedstonePowerAt(world, pos, attachedToSide, isWire, excludeRedstoneWireDir, allowAmplifiedPower, MAX_POWER, directions);
    }

    @Nullable
    public static RedstoneCalc.PowerResult getRedstonePowerAt(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing attachedToSide, boolean isWire, EnumFacing excludeRedstoneWireDir, boolean allowAmplifiedPower, int maxPower, @Nonnull EnumFacing... directions) {
        int strength = 0;
        EnumFacing directionToPower = null;

        for(EnumFacing dir : directions) {
            BlockPos pos2 = pos.offset(dir);
            IBlockState state = world.getBlockState(pos2);
            Block block = state.getBlock();
            int s;
            if (block instanceof BlockRedstoneWire) {
                if (excludeRedstoneWireDir != dir) {
                    s = state.getValue(BlockRedstoneWire.POWER);
                    if (isWire) {
                        s--;
                    }

                    if (s > strength) {
                        strength = s;
                        directionToPower = dir;
                    }
                }
            } else {
                int data;
                if (block instanceof IAmplifiedRedstoneOutput) {
                    IAmplifiedRedstoneOutput amp = (IAmplifiedRedstoneOutput)block;
                    data = amp.getRedstoneSignalStrength(world, pos2, dir.getOpposite(), allowAmplifiedPower);
                    if (isWire && amp.isRedstoneWire()) {
                        --data;
                    }

                    if (data > strength) {
                        strength = data;
                        directionToPower = dir;
                    }
                } else if (isAirBlock(block, world, pos2)) {
                    if (isWire) {
                        BlockWithPos bwp = getORCAroundCorner(world, pos2, attachedToSide, dir);
                        if (bwp != null) {
                            data = ((IOrientableRedstoneConductor)bwp.block).getRedstoneSignalStrength(world, bwp, attachedToSide, allowAmplifiedPower);
                            --data;
                            if (data > strength) {
                                strength = data;
                                directionToPower = dir;
                            }
                        }
                    }
                } else {
                    s = world.getRedstonePower(pos2, dir);
                    if (s > strength) {
                        if (isWire && !block.canProvidePower(state)) {
                            for (EnumFacing subdir : EnumFacing.VALUES) {
                                if (subdir.getOpposite() != dir) {
                                    BlockPos pos3 = pos2.offset(subdir);
                                    IBlockState state2 = world.getBlockState(pos3);
                                    Block block2 = state2.getBlock();
                                    if (!(block2 instanceof BlockRedstoneWire) && !(block2 instanceof IOrientableRedstoneConductor) && block2.canProvidePower(state2)) {
                                        int s2 = block2.getStrongPower(state2, world, pos3, subdir);
                                        if (s2 == s) {
                                            strength = s;
                                            directionToPower = dir;
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            strength = s;
                            directionToPower = dir;
                        }
                    }
                }
            }

            if (strength >= (allowAmplifiedPower ? MAX_POWER_AMPLIFIED : maxPower)) {
                break;
            }
        }

        return strength > 0 ? new RedstoneCalc.PowerResult(strength, directionToPower) : null;
    }

    public static boolean isAirBlock(Block block, @Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos) {
        return blockaccess.isAirBlock(pos) || block == Blocks.PISTON_HEAD;
    }

    @Nullable
    public static BlockWithPos getORCAroundCorner(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos2, @Nullable EnumFacing attachedToSide, @Nonnull EnumFacing dir) {
        if (attachedToSide == null) {
            Automagy.logError("[Automagy] RedstoneCalc was passed invalid attachedToSide. Will calculate as if given DOWN.");
            attachedToSide = EnumFacing.DOWN;
        }

        EnumFacing subdir = attachedToSide.getOpposite();
        if (dir != attachedToSide && subdir != attachedToSide) {
            BlockPos pos3 = pos2.offset(subdir);
            Block block2 = TjUtil.getBlock(blockaccess, pos3);
            if (block2 instanceof IOrientableRedstoneConductor) {
                EnumFacing otherside = ((IOrientableRedstoneConductor)block2).getOrientation(blockaccess, pos3);
                if (TjUtil.getNextSideOnBlockFromDir(attachedToSide, dir) == otherside) {
                    return new BlockWithPos(block2, blockaccess, pos3);
                }
            }
        }

        return null;
    }

    @Nonnull
    public static LinkedHashMap<BlockPos, EnumFacing> getConnectedWireAwayFromSource(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos bc) {
        LinkedHashMap<BlockPos, EnumFacing> wiring = new LinkedHashMap<>();
        BlockPos bcCopy = new BlockPos(bc);
        buildWiringSet(wiring, blockaccess, bc, null, 2);
        wiring.remove(bcCopy);
        return wiring;
    }

    @Nullable
    public static LinkedHashMap<BlockPos, EnumFacing> getConnectedWireAwayFromSourceWithNoInputPoint(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos bc) {
        LinkedHashMap<BlockPos, EnumFacing> wiring = new LinkedHashMap<>();
        BlockPos bcCopy = new BlockPos(bc);
        boolean ret = buildWiringSet(wiring, blockaccess, bc, null, -1);
        if (ret) {
            return null;
        } else {
            wiring.remove(bcCopy);
            return wiring;
        }
    }

    private static boolean buildWiringSet(@Nonnull LinkedHashMap<BlockPos, EnumFacing> wiring, @Nonnull IBlockAccess blockaccess, @Nonnull BlockPos bc, @Nullable EnumFacing backDir, int distance) {
        if (!wiring.containsKey(bc)) {
            IBlockState state = blockaccess.getBlockState(bc);
            Block block = state.getBlock();
            if (backDir == null || block instanceof IOrientableRedstoneConductor) {
                IOrientableRedstoneConductor orc = block instanceof IOrientableRedstoneConductor ? (IOrientableRedstoneConductor)block : null;
                if (backDir != null && !orc.canReceiveRedstoneSignalFromDirection(blockaccess, bc, backDir) && !orc.canSendRedstoneSignalInDirection(blockaccess, bc, backDir)) {
                    return false;
                }

                wiring.put(bc, backDir);
                if (distance == -1) {
                    if (orc != null && orc.isRedstoneSignalInputPoint(blockaccess, bc)) {
                        return true;
                    }
                } else {
                    --distance;
                    if (distance == 0) {
                        return false;
                    }
                }

                for (EnumFacing dir : EnumFacing.VALUES) {
                    if (dir != backDir && (orc == null || orc.canSendRedstoneSignalInDirection(blockaccess, bc, dir) || orc.canReceiveRedstoneSignalFromDirection(blockaccess, bc, dir))) {
                        BlockPos bc2 = bc.offset(dir);
                        if (isAirBlock(block, blockaccess, bc2)) {
                            if (orc == null) {

                                for (EnumFacing attachedToSide : EnumFacing.VALUES) {
                                    BlockWithPos bwp = getORCAroundCorner(blockaccess, bc2, attachedToSide, dir);
                                    if (bwp != null) {
                                        boolean ret = buildWiringSet(wiring, blockaccess, bwp, attachedToSide, distance);
                                        if (ret) {
                                            return true;
                                        }
                                    }
                                }
                            } else {
                                EnumFacing attachedToSide = orc.getOrientation(blockaccess, bc);
                                BlockWithPos bwp = getORCAroundCorner(blockaccess, bc2, attachedToSide, dir);
                                if (bwp != null) {
                                    boolean ret = buildWiringSet(wiring, blockaccess, bwp, attachedToSide, distance);
                                    if (ret) {
                                        return true;
                                    }
                                }
                            }
                        } else {
                            boolean ret = buildWiringSet(wiring, blockaccess, bc2, dir.getOpposite(), distance);
                            if (ret) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public static int getRedstoneSignalStrengthFromValues(int amt, int capacity) {
        if (amt >= 1 && capacity >= 1) {
            if (amt >= capacity) {
                return MAX_POWER;
            } else {
                return MathHelper.floor(1.0F + (float)amt / (float)capacity * 14.0F);
            }
        } else {
            return 0;
        }
    }

    public static class PowerResult {
        public final int strength;
        public final EnumFacing sourceDirection;

        public PowerResult(int strength, EnumFacing sourceDirection) {
            this.strength = strength;
            this.sourceDirection = sourceDirection;
        }
    }
}
