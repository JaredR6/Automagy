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
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.lib.struct.BlockWithPos;

public class RedstoneCalc {
    public static final int MAX_POWER = 15;
    public static final int MAX_POWER_AMPLIFIED = 99;
    public static final boolean COUNT_PISTONHEAD_AS_AIR = true;

    public RedstoneCalc() {
    }

    public static RedstoneCalc.PowerResult getRedstonePowerAt(World world, BlockPos pos) {
        return getRedstonePowerAt(world, pos, EnumFacing.UP, false, (EnumFacing)null, false, 15, EnumFacing.VALUES);
    }

    public static RedstoneCalc.PowerResult getRedstonePowerAt(World world, BlockPos pos, EnumFacing... directions) {
        return getRedstonePowerAt(world, pos, EnumFacing.UP, false, (EnumFacing)null, false, 15, directions);
    }

    public static RedstoneCalc.PowerResult getRedstonePowerAt(World world, BlockPos pos, EnumFacing attachedToSide, boolean isWire, EnumFacing excludeRedstoneWireDir, boolean allowAmplifiedPower) {
        return getRedstonePowerAt(world, pos, attachedToSide, isWire, excludeRedstoneWireDir, allowAmplifiedPower, 15, EnumFacing.VALUES);
    }

    public static RedstoneCalc.PowerResult getRedstonePowerAt(World world, BlockPos pos, EnumFacing attachedToSide, boolean isWire, EnumFacing excludeRedstoneWireDir, boolean allowAmplifiedPower, EnumFacing... directions) {
        return getRedstonePowerAt(world, pos, attachedToSide, isWire, excludeRedstoneWireDir, allowAmplifiedPower, 15, directions);
    }

    public static RedstoneCalc.PowerResult getRedstonePowerAt(World world, BlockPos pos, EnumFacing attachedToSide, boolean isWire, EnumFacing excludeRedstoneWireDir, boolean allowAmplifiedPower, int maxPower, EnumFacing... directions) {
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

            if (strength >= (allowAmplifiedPower ? 99 : maxPower)) {
                break;
            }
        }

        return strength > 0 ? new RedstoneCalc.PowerResult(strength, directionToPower) : null;
    }

    public static boolean isAirBlock(Block block, IBlockAccess blockaccess, BlockPos pos) {
        return blockaccess.isAirBlock(pos) || block == Blocks.PISTON_HEAD;
    }

    public static BlockWithPos getORCAroundCorner(IBlockAccess blockaccess, BlockPos pos2, EnumFacing attachedToSide, EnumFacing dir) {
        if (attachedToSide == null) {
            Automagy.logError("[Automagy] RedstoneCalc was passed invalid attachedToSide. Will calculate as if given DOWN.");
            attachedToSide = EnumFacing.DOWN;
        }

        EnumFacing subdir = attachedToSide.getOpposite();
        if (dir != attachedToSide && subdir != attachedToSide) {
            BlockPos pos3 = pos2.offset(subdir);
            Block block2 = blockaccess.getBlockState(pos3).getBlock();
            if (block2 instanceof IOrientableRedstoneConductor) {
                EnumFacing otherside = ((IOrientableRedstoneConductor)block2).getOrientation(blockaccess, pos3);
                if (TjUtil.getNextSideOnBlockFromDir(attachedToSide, dir) == otherside) {
                    return new BlockWithPos(block2, blockaccess, pos3);
                }
            }
        }

        return null;
    }

    public static LinkedHashMap<BlockPos, EnumFacing> getConnectedWireAwayFromSource(IBlockAccess blockaccess, BlockPos bc) {
        LinkedHashMap<BlockPos, EnumFacing> wiring = new LinkedHashMap();
        BlockPos bcCopy = new BlockPos(bc);
        buildWiringSet(wiring, blockaccess, bc, (EnumFacing)null, 2);
        wiring.remove(bcCopy);
        return wiring;
    }

    public static LinkedHashMap<BlockPos, EnumFacing> getConnectedWireAwayFromSourceWithNoInputPoint(IBlockAccess blockaccess, BlockPos bc) {
        LinkedHashMap<BlockPos, EnumFacing> wiring = new LinkedHashMap();
        BlockPos bcCopy = new BlockPos(bc);
        boolean ret = buildWiringSet(wiring, blockaccess, bc, (EnumFacing)null, -1);
        if (ret) {
            return null;
        } else {
            wiring.remove(bcCopy);
            return wiring;
        }
    }

    private static boolean buildWiringSet(LinkedHashMap<BlockPos, EnumFacing> wiring, IBlockAccess blockaccess, BlockPos bc, EnumFacing backDir, int distance) {
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
                return 15;
            } else {
                int strength = MathHelper.floor(1.0F + (float)amt / (float)capacity * 14.0F);
                return strength;
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
