package tuhljin.automagy.common.blocks.redcrystal;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tuhljin.automagy.common.lib.RedstoneCalc;
import tuhljin.automagy.common.lib.RedstoneCalc.PowerResult;
import tuhljin.automagy.common.tiles.TileRedcrystal;

public class BlockRedcrystalAmp extends BlockRedcrystalLarge {
    public static int SIGNAL_OUTPUT = 50;
    private final boolean ACCEPT_POWER_FROM_ATTACHED_DIRECTION = true;

    public BlockRedcrystalAmp() {
    }

    protected PowerResult calculateRedstonePowerAt(World world, BlockPos pos, EnumFacing orientation) {
        PowerResult result = RedstoneCalc.getRedstonePowerAt(world, pos, orientation, true, orientation, true, 1, this.getRedstoneInputDirections(world, pos));
        return result != null && result.strength > 0 ? new PowerResult(Math.max(result.strength, SIGNAL_OUTPUT), result.sourceDirection) : null;
    }

    public EnumFacing[] getRedstoneInputDirections(IBlockAccess blockaccess, BlockPos pos) {
        TileRedcrystal te;
        try {
            te = (TileRedcrystal)blockaccess.getTileEntity(pos);
        } catch (Exception var6) {
            return null;
        }

        List<EnumFacing> list = new ArrayList();
        EnumFacing orientation = te.orientation;
        if (orientation == null) {
            orientation = EnumFacing.UP;
        }

        switch(orientation) {
            case NORTH:
                if (te.connectN) {
                    list.add(EnumFacing.UP);
                }

                if (te.connectS) {
                    list.add(EnumFacing.DOWN);
                }

                if (te.connectW) {
                    list.add(EnumFacing.EAST);
                }

                if (te.connectE) {
                    list.add(EnumFacing.WEST);
                }
                break;
            case SOUTH:
                if (te.connectN) {
                    list.add(EnumFacing.DOWN);
                }

                if (te.connectS) {
                    list.add(EnumFacing.UP);
                }

                if (te.connectW) {
                    list.add(EnumFacing.EAST);
                }

                if (te.connectE) {
                    list.add(EnumFacing.WEST);
                }
                break;
            case WEST:
                if (te.connectN) {
                    list.add(EnumFacing.SOUTH);
                }

                if (te.connectS) {
                    list.add(EnumFacing.NORTH);
                }

                if (te.connectW) {
                    list.add(EnumFacing.UP);
                }

                if (te.connectE) {
                    list.add(EnumFacing.DOWN);
                }
                break;
            case EAST:
                if (te.connectN) {
                    list.add(EnumFacing.SOUTH);
                }

                if (te.connectS) {
                    list.add(EnumFacing.NORTH);
                }

                if (te.connectW) {
                    list.add(EnumFacing.DOWN);
                }

                if (te.connectE) {
                    list.add(EnumFacing.UP);
                }
                break;
            default:
                if (te.connectN) {
                    list.add(EnumFacing.SOUTH);
                }

                if (te.connectS) {
                    list.add(EnumFacing.NORTH);
                }

                if (te.connectW) {
                    list.add(EnumFacing.EAST);
                }

                if (te.connectE) {
                    list.add(EnumFacing.WEST);
                }
        }

        list.add(orientation.getOpposite());
        return list.toArray(new EnumFacing[0]);
    }

    public boolean canSendRedstoneSignalInDirection(IBlockAccess blockaccess, BlockPos pos, EnumFacing dir) {
        TileRedcrystal te;
        try {
            te = (TileRedcrystal)blockaccess.getTileEntity(pos);
        } catch (Exception ex) {
            return false;
        }

        EnumFacing orientation = te.orientation;
        if (orientation == null) {
            orientation = EnumFacing.UP;
        }

        switch(orientation) {
            case NORTH:
                switch(dir) {
                    case WEST:
                        return te.connectW;
                    case EAST:
                        return te.connectE;
                    case DOWN:
                        return te.connectN;
                    case UP:
                        return te.connectS;
                    default:
                        return false;
                }
            case SOUTH:
                switch(dir) {
                    case WEST:
                        return te.connectW;
                    case EAST:
                        return te.connectE;
                    case DOWN:
                        return te.connectS;
                    case UP:
                        return te.connectN;
                    default:
                        return false;
                }
            case WEST:
                switch(dir) {
                    case NORTH:
                        return te.connectN;
                    case SOUTH:
                        return te.connectS;
                    case WEST:
                    case EAST:
                    default:
                        return false;
                    case DOWN:
                        return te.connectW;
                    case UP:
                        return te.connectE;
                }
            case EAST:
                switch(dir) {
                    case NORTH:
                        return te.connectN;
                    case SOUTH:
                        return te.connectS;
                    case WEST:
                    case EAST:
                    default:
                        return false;
                    case DOWN:
                        return te.connectE;
                    case UP:
                        return te.connectW;
                }
            case DOWN:
            case UP:
                switch(dir) {
                    case NORTH:
                        return te.connectN;
                    case SOUTH:
                        return te.connectS;
                    case WEST:
                        return te.connectW;
                    case EAST:
                        return te.connectE;
                }
        }

        return false;
    }

    public boolean canConnectRedstone(IBlockAccess world, BlockPos pos, EnumFacing side) {
        switch(side) {
            case NORTH:
                side = EnumFacing.SOUTH;
                break;
            case SOUTH:
                side = EnumFacing.WEST;
                break;
            case WEST:
            case EAST:
            default:
                return false;
            case DOWN:
                side = EnumFacing.NORTH;
                break;
            case UP:
                side = EnumFacing.EAST;
        }

        return this.canSendRedstoneSignalInDirection(world, pos, side) || this.canSendRedstoneSignalInDirection(world, pos, side.getOpposite());
    }

    public boolean allowNeighborToChangeConnection(World world, BlockPos pos) {
        return false;
    }

    public boolean onBlockActivatedAdjustedSide(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side) {
        TileRedcrystal te = null;

        try {
            te = (TileRedcrystal)world.getTileEntity(pos);
        } catch (Exception ignored) {
        }

        if (te == null) {
            return false;
        } else {
            if (side != EnumFacing.NORTH && te.connectN) {
                this.toggleConnectionToDirection(world, pos, state, EnumFacing.NORTH, false);
            }

            if (side != EnumFacing.SOUTH && te.connectS) {
                this.toggleConnectionToDirection(world, pos, state, EnumFacing.SOUTH, false);
            }

            if (side != EnumFacing.WEST && te.connectW) {
                this.toggleConnectionToDirection(world, pos, state, EnumFacing.WEST, false);
            }

            if (side != EnumFacing.EAST && te.connectE) {
                this.toggleConnectionToDirection(world, pos, state, EnumFacing.EAST, false);
            }

            return super.onBlockActivatedAdjustedSide(world, pos, state, player, side);
        }
    }
}
