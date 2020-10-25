package tuhljin.automagy.common.blocks.redcrystal;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.casters.ICaster;
import thaumcraft.codechicken.lib.raytracer.ExtendedMOP;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.codechicken.lib.vec.BlockCoord;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.blocks.IRedcrystalPowerConductor;
import tuhljin.automagy.common.blocks.ItemBlockRedcrystal;
import tuhljin.automagy.common.blocks.ModTileRenderedBlock;
import tuhljin.automagy.common.lib.*;
import tuhljin.automagy.common.lib.RedstoneCalc.PowerResult;
import tuhljin.automagy.common.lib.struct.BlockWithPos;
import tuhljin.automagy.common.tiles.TileRedcrystal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockRedcrystal extends ModTileRenderedBlock implements IOrientableRedstoneConductor, IRedcrystalPowerConductor {

    @Nonnull
    private static AxisAlignedBB AABB_BASIC_DOWN = new AxisAlignedBB(0.25F, 0.8F, 0.25F, 0.75F, 1.0F, 0.75F);
    @Nonnull
    private static AxisAlignedBB AABB_BASIC_NORTH = new AxisAlignedBB(0.25F, 0.25F, 0.8F, 0.75F, 0.75F, 1.0F);
    @Nonnull
    private static AxisAlignedBB AABB_BASIC_SOUTH = new AxisAlignedBB(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.2F);
    @Nonnull
    private static AxisAlignedBB AABB_BASIC_WEST = new AxisAlignedBB(0.8F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
    @Nonnull
    private static AxisAlignedBB AABB_BASIC_EAST = new AxisAlignedBB(0.0F, 0.25F, 0.25F, 0.2F, 0.75F, 0.75F);
    @Nonnull
    private static AxisAlignedBB AABB_BASIC_UP = new AxisAlignedBB(0.25F, 0.0F, 0.25F, 0.75F, 0.2F, 0.75F);

    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    protected final boolean CONNECT_REDSTONE_REL_TOP = false;
    protected static final int MAX_POWER = 15;
    private boolean simplePowerCheck = true;
    protected static boolean resettingPowerLevels = false;
    @Nonnull
    private HashSet<BlockPos> newPrimaryPowerInputLocations = new HashSet<>();
    @Nonnull
    private HashMap<Integer, LinkedHashSet<BlockPos>> queuedBlockUpdateLocations = new HashMap<>();
    private Field redstonedustAllowingPowerCheck;
    @Nullable
    public EnumFacing nextTEOrientation = null;
    public boolean nextTENoConnections = false;
    @Nonnull
    private RayTracer rayTracer = new RayTracer();

    public BlockRedcrystal() {
        this(References.BLOCK_REDCRYSTAL);
    }

    public BlockRedcrystal(String name) {
        super(Material.CIRCUITS, name);
        this.setHardness(0.1F);
        this.setSoundType(SoundType.STONE);
        this.disableStats();
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(POWER, 0));
        this.setCreativeTab(Automagy.creativeTab);

        try {
            this.redstonedustAllowingPowerCheck = ReflectionHelper.findField(Blocks.REDSTONE_WIRE.getClass(), "canProvidePower");
        } catch (Exception ex) {
            Automagy.logSevereError("Failed to access redstone dust's signal-checking field. Improper signal strengths may result when redstone wire is placed next to redcrystal.");
        }

    }

    @Nonnull
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockRedcrystal.class;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        TileRedcrystal te = new TileRedcrystal(this.nextTEOrientation, this.nextTENoConnections);
        this.nextTEOrientation = null;
        this.nextTENoConnections = false;
        return te;
    }

    @Override
    public void onBlockAdded(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        this.updateAndPropagateChanges(world, pos, true, false, false, false);
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        this.updateAndPropagateChanges(world, pos, false, false, true, true);
    }

    @Override
    public int getLightValue(@Nonnull IBlockState state) {
        if (!AutomagyConfig.redcrystalEmitsLight) {
            return super.getLightValue(state);
        } else {
            double strength = state.getValue(POWER);
            strength = Math.floor(strength * 2/3);
            return (int)strength;
        }
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWER, meta & 15);
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return state.getValue(POWER);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWER);
    }

    @Override
    public boolean canPlaceBlockAt(@Nonnull World world, @Nonnull BlockPos pos) {
        EnumFacing side = this.getOrientation(world, pos);
        return this.canPlaceBlockOnSide(world, pos, side);
    }

    @Override
    public boolean canPlaceBlockOnSide(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        BlockPos pos2 = pos.offset(side.getOpposite());
        return world.isSideSolid(pos2, side) || TjUtil.getBlock(world, pos) == Blocks.GLOWSTONE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        int strength = state.getValue(POWER);
        if (strength > 0) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            double d0 =x + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = y + 0.1625D;
            double d2 = z + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float)strength / MAX_POWER;
            float f1 = f * 0.6F + 0.4F;

            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }

            if (f3 < 0.0F) {
                f3 = 0.0F;
            }

            EnumFacing orientation = this.getOrientation(world, pos);
            if (orientation == null) {
                orientation = EnumFacing.UP;
            }

            switch(orientation) {
                case DOWN:
                    d1 += 0.6D;
                case UP:
                default:
                    break;
                case NORTH:
                    d1 += 0.28D;
                    d2 += 0.3D;
                    break;
                case SOUTH:
                    d1 += 0.28D;
                    d2 -= 0.3D;
                    break;
                case WEST:
                    d0 += 0.3D;
                    d1 += 0.28D;
                    break;
                case EAST:
                    d0 -= 0.3D;
                    d1 += 0.28D;
            }

            world.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, f1, f2, f3);
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
            switch(side) {
                case DOWN:
                    side = EnumFacing.NORTH;
                    break;
                case UP:
                    side = EnumFacing.EAST;
                    break;
                case NORTH:
                    side = EnumFacing.SOUTH;
                    break;
                case SOUTH:
                    side = EnumFacing.WEST;
                    break;
                default:
                    return false;
            }

            return side != this.getOrientation(world, pos) && this.canSendRedstoneSignalInDirection(world, pos, side);
        }
    }

    @Override
    public int getStrongPower(@Nonnull IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        if (!this.simplePowerCheck) {
            return 0;
        } else {
            try {
                if (!this.redstonedustAllowingPowerCheck.getBoolean(Blocks.REDSTONE_WIRE)) {
                    return 0;
                }
            } catch (Exception ignored) {
            }

            return this.getWeakPower(blockState, blockAccess, pos, side);
        }
    }

    @Override
    public int getWeakPower(@Nonnull IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        if (!this.simplePowerCheck) {
            return 0;
        } else {
            EnumFacing orientation = this.getOrientation(blockAccess, pos);
            if (orientation != null && side != this.getOrientation(blockAccess, pos).getOpposite()) {
                int strength = blockState.getValue(POWER);
                if (strength > 0) {
                    if (!this.canSendRedstoneSignalInDirection(blockAccess, pos, side.getOpposite())) {
                        return 0;
                    }

                    try {
                        if (!this.redstonedustAllowingPowerCheck.getBoolean(Blocks.REDSTONE_WIRE)) {
                            --strength;
                        }
                    } catch (Exception ignored) {
                    }
                }

                return strength;
            } else {
                return 0;
            }
        }
    }

    @Override
    public boolean getWeakChanges(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, @Nonnull BlockPos pos, @Nonnull BlockPos neighbor) {
        if (world instanceof World) {
            if (!((World)world).isRemote) {
                EnumFacing side = this.getOrientation(world, pos);
                if (side == null) {
                    side = EnumFacing.UP;
                }
                if (this.canPlaceBlockOnSide((World)world, pos, side)) {
                    if (!(TjUtil.getBlock(world, neighbor) instanceof IRedcrystalPowerConductor)) {
                        this.updateAndPropagateChanges((World)world, pos, true, false, false, false);
                    }
                } else {
                    this.dropBlockAsItem((World)world, pos, world.getBlockState(pos), 0);
                    ((World) world).setBlockToAir(pos);
                }

                super.onNeighborChange(world, pos, neighbor);
            }
        }
    }

    public void updateAndPropagateChanges(@Nonnull World world, @Nonnull BlockPos pos, boolean checkStrength, boolean calledByNeighborWire, boolean forcePropagate, boolean immediateNeighborUpdates) {
        if (!world.isRemote) {
            boolean notifyChain = false;
            boolean needClientUpdate = false;
            boolean needNotifyNeighbors = false;
            int strength = -1;
            TileRedcrystal te = null;
            if (checkStrength) {
                te = (TileRedcrystal)world.getTileEntity(pos);
                if (te == null) {
                    return;
                }

                EnumFacing orientation = te.orientation;
                if (orientation != null) {
                    this.simplePowerCheck = false;
                    PowerResult result = this.calculateRedstonePowerAt(world, pos, orientation);
                    this.simplePowerCheck = true;
                    strength = result == null ? 0 : result.strength;
                    EnumFacing sourceSide = result == null ? null : result.sourceDirection;
                    if (sourceSide != te.powerSourceSide) {
                        te.powerSourceSide = sourceSide;
                        needClientUpdate = true;
                        notifyChain = true;
                    }

                    if (sourceSide != null && strength >= 1) {
                        BlockPos bc = pos.offset(sourceSide);
                        IBlockState sourceBlockState = world.getBlockState(bc);
                        Block sourceBlock = sourceBlockState.getBlock();
                        if (!(sourceBlock instanceof IRedcrystalPowerConductor) && !RedstoneCalc.isAirBlock(sourceBlock, world, bc)) {
                            if (!te.powerSourceOutsideNetwork) {
                                te.powerSourceOutsideNetwork = true;
                                needClientUpdate = true;
                                notifyChain = true;
                            }

                            if (resettingPowerLevels) {
                                this.newPrimaryPowerInputLocations.add(new BlockPos(pos));
                            }
                        } else if (te.powerSourceOutsideNetwork) {
                            te.powerSourceOutsideNetwork = false;
                            needClientUpdate = true;
                            notifyChain = true;
                        }
                    } else if (te.powerSourceOutsideNetwork) {
                        te.powerSourceOutsideNetwork = false;
                        needClientUpdate = true;
                        notifyChain = true;
                    }

                    IBlockState state = world.getBlockState(pos);
                    int power = state.getValue(POWER);
                    int amp = 0;
                    if (power == MAX_POWER) {
                        amp = te.powerStability;
                    }

                    if (strength != power + amp) {
                        if (strength > MAX_POWER) {
                            power = MAX_POWER;
                            te.powerStability = (short)(strength - power);
                        } else {
                            power = strength;
                            if (amp > 0) {
                                te.powerStability = 0;
                            }
                        }

                        IBlockState newState = state.withProperty(POWER, power);
                        world.setBlockState(pos, newState, 2);
                        needNotifyNeighbors = true;
                        needClientUpdate = true;
                        notifyChain = true;
                    }
                }
            } else {
                strength = 0;
                notifyChain = true;
            }

            if (notifyChain || forcePropagate) {
                if (strength == -1) {
                    strength = this.getRedstoneSignalStrength(world, pos, true);
                }

                Map<BlockPos, EnumFacing> wireTrace;
                IOrientableRedstoneConductor orc;
                if (!calledByNeighborWire && (needClientUpdate || !checkStrength)) {
                    wireTrace = RedstoneCalc.getConnectedWireAwayFromSourceWithNoInputPoint(world, pos);
                    if (wireTrace != null) {
                        for (Entry<BlockPos, EnumFacing> entry : wireTrace.entrySet()) {
                            BlockPos bc = entry.getKey();
                            orc = (IOrientableRedstoneConductor)TjUtil.getBlock(world, bc);
                            if (orc.getRedstoneSignalStrength(world, bc, true) > 0) {
                                resettingPowerLevels = true;
                                orc.onNeighborRedstoneConductorUpdate(world, bc, entry.getValue(), 100);
                                resettingPowerLevels = false;
                            }
                        }

                        for (BlockPos bc : this.newPrimaryPowerInputLocations) {
                            Block block = TjUtil.getBlock(world, bc);
                            if (block instanceof BlockRedcrystal) {
                                ((BlockRedcrystal)block).updateAndPropagateChanges(world, bc, true, false, true, false);
                            }
                        }

                        this.newPrimaryPowerInputLocations.clear();
                    }
                }

                wireTrace = RedstoneCalc.getConnectedWireAwayFromSource(world, pos);

                for (Entry<BlockPos, EnumFacing> entry : wireTrace.entrySet()) {
                    BlockPos bc = entry.getKey();
                    orc = (IOrientableRedstoneConductor)TjUtil.getBlock(world, bc);
                    orc.onNeighborRedstoneConductorUpdate(world, bc, entry.getValue(), strength);

                }
            }

            if (needClientUpdate) {
                te.markForUpdate();
            }

            if (needNotifyNeighbors || forcePropagate) {
                this.scheduleAreaUpdateNotification(world, pos, immediateNeighborUpdates);
            }

        }
    }

    @Override
    public int getRedstoneSignalStrength(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos, boolean allowAmplifiedPower) {
        if (resettingPowerLevels) {
            return 0;
        } else {
            int power = this.internalGetMetadata(blockaccess, pos);
            if (power == MAX_POWER && allowAmplifiedPower) {
                TileRedcrystal te = (TileRedcrystal)blockaccess.getTileEntity(pos);
                return power + te.powerStability;
            } else {
                return power;
            }
        }
    }

    @Override
    public int getRedstoneSignalStrength(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos, @Nonnull EnumFacing intoDirection, boolean allowAmplifiedPower) {
        int strength = this.getRedstoneSignalStrength(blockaccess, pos, allowAmplifiedPower);
        return strength > 0 && !this.canSendRedstoneSignalInDirection(blockaccess, pos, intoDirection) ? 0 : strength;
    }

    @Nullable
    @Override
    public EnumFacing getOrientation(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos) {
        EnumFacing orientation = null;
        TileRedcrystal te = (TileRedcrystal)blockaccess.getTileEntity(pos);
        if (te != null) {
            orientation = te.orientation;
        }

        return orientation;
    }

    public boolean isRedstoneWire() {
        return true;
    }

    @Nullable
    public EnumFacing getRedstoneSignalSourceSide(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos) {
        EnumFacing side = null;
        TileRedcrystal te = (TileRedcrystal)blockaccess.getTileEntity(pos);
        if (te != null) {
            side = te.powerSourceSide;
        }

        return side;
    }

    public boolean canReceiveRedstoneSignalFromDirection(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos, EnumFacing dir) {
        EnumFacing[] dirs = this.getRedstoneInputDirections(blockaccess, pos);

        for (EnumFacing d : dirs) {
            if (d == dir) {
                return true;
            }
        }

        return false;
    }

    public void onNeighborRedstoneConductorUpdate(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing neighborDir, int neighborStrength) {
        int strength = this.getRedstoneSignalStrength(world, pos, true);
        if (neighborStrength > strength || this.getRedstoneSignalSourceSide(world, pos) == neighborDir) {
            this.updateAndPropagateChanges(world, pos, true, true, false, false);
        }

    }

    public void onNeighborRedstoneConductorUpdate(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state) {
        this.updateAndPropagateChanges(world, pos, true, true, false, false);
    }

    public boolean isRedstoneSignalInputPoint(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos) {
        TileRedcrystal te = (TileRedcrystal)blockaccess.getTileEntity(pos);
        return te != null && te.powerSourceOutsideNetwork;
    }

    @Nullable
    protected PowerResult calculateRedstonePowerAt(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing orientation) {
        return RedstoneCalc.getRedstonePowerAt(world, pos, orientation, true, orientation, true, this.getRedstoneInputDirections(world, pos));
    }

    public void setConnection(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos, boolean connects, EnumFacing... sides) {
        this.setConnection(blockaccess, pos, blockaccess.getBlockState(pos), connects, sides);
    }

    public void setConnection(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos, IBlockState state, boolean connects, @Nonnull EnumFacing... sides) {
        TileRedcrystal te;
        try {
            te = (TileRedcrystal)blockaccess.getTileEntity(pos);
        } catch (Exception ex) {
            return;
        }
        boolean madeChanges = false;
        for (EnumFacing side : sides) {
            if (side == EnumFacing.DOWN) {
                side = te.orientation;
            } else if (side == EnumFacing.UP) {
                side = te.orientation.getOpposite();
            }

            switch(side) {
                case NORTH:
                    te.connectN = connects;
                    madeChanges = true;
                    break;
                case SOUTH:
                    te.connectS = connects;
                    madeChanges = true;
                    break;
                case WEST:
                    te.connectW = connects;
                    madeChanges = true;
                    break;
                case EAST:
                    te.connectE = connects;
                    madeChanges = true;
            }
        }

        if (madeChanges) {
            te.markForUpdate();
        }

        this.updateAndPropagateChanges(te.getWorld(), pos, true, false, true, false);
    }

    public boolean canSendRedstoneSignalInDirection(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos, @Nonnull EnumFacing dir) {
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
            case DOWN:
            case UP:
                switch(dir) {
                    case DOWN:
                    case UP:
                        return true;
                    case NORTH:
                        return te.connectN;
                    case SOUTH:
                        return te.connectS;
                    case WEST:
                        return te.connectW;
                    case EAST:
                        return te.connectE;
                    default:
                        return false;
                }
            case NORTH:
                switch(dir) {
                    case DOWN:
                        return te.connectN;
                    case UP:
                        return te.connectS;
                    case NORTH:
                    case SOUTH:
                        return true;
                    case WEST:
                        return te.connectW;
                    case EAST:
                        return te.connectE;
                    default:
                        return false;
                }
            case SOUTH:
                switch(dir) {
                    case DOWN:
                        return te.connectS;
                    case UP:
                        return te.connectN;
                    case NORTH:
                    case SOUTH:
                        return true;
                    case WEST:
                        return te.connectW;
                    case EAST:
                        return te.connectE;
                    default:
                        return false;
                }
            case WEST:
                switch(dir) {
                    case DOWN:
                        return te.connectW;
                    case UP:
                        return te.connectE;
                    case NORTH:
                        return te.connectN;
                    case SOUTH:
                        return te.connectS;
                    case WEST:
                    case EAST:
                        return true;
                    default:
                        return false;
                }
            case EAST:
                switch(dir) {
                    case DOWN:
                        return te.connectE;
                    case UP:
                        return te.connectW;
                    case NORTH:
                        return te.connectN;
                    case SOUTH:
                        return te.connectS;
                    case WEST:
                    case EAST:
                        return true;
                }
        }

        return false;
    }

    @Nullable
    public EnumFacing[] getRedstoneInputDirections(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos) {
        TileRedcrystal te;
        try {
            te = (TileRedcrystal)blockaccess.getTileEntity(pos);
        } catch (Exception ex) {
            return null;
        }

        List<EnumFacing> list = new ArrayList<>();
        EnumFacing orientation = te.orientation;
        if (orientation == null) {
            orientation = EnumFacing.UP;
        }

        switch(orientation) {
            case NORTH:
                if (te.connectN) {
                    list.add(EnumFacing.DOWN);
                }

                if (te.connectS) {
                    list.add(EnumFacing.UP);
                }

                if (te.connectW) {
                    list.add(EnumFacing.WEST);
                }

                if (te.connectE) {
                    list.add(EnumFacing.EAST);
                }

                list.add(EnumFacing.NORTH);
                list.add(EnumFacing.SOUTH);
                break;
            case SOUTH:
                if (te.connectN) {
                    list.add(EnumFacing.UP);
                }

                if (te.connectS) {
                    list.add(EnumFacing.DOWN);
                }

                if (te.connectW) {
                    list.add(EnumFacing.WEST);
                }

                if (te.connectE) {
                    list.add(EnumFacing.EAST);
                }

                list.add(EnumFacing.NORTH);
                list.add(EnumFacing.SOUTH);
                break;
            case WEST:
                if (te.connectN) {
                    list.add(EnumFacing.NORTH);
                }

                if (te.connectS) {
                    list.add(EnumFacing.SOUTH);
                }

                if (te.connectW) {
                    list.add(EnumFacing.DOWN);
                }

                if (te.connectE) {
                    list.add(EnumFacing.UP);
                }

                list.add(EnumFacing.EAST);
                list.add(EnumFacing.WEST);
                break;
            case EAST:
                if (te.connectN) {
                    list.add(EnumFacing.NORTH);
                }

                if (te.connectS) {
                    list.add(EnumFacing.SOUTH);
                }

                if (te.connectW) {
                    list.add(EnumFacing.UP);
                }

                if (te.connectE) {
                    list.add(EnumFacing.DOWN);
                }

                list.add(EnumFacing.EAST);
                list.add(EnumFacing.WEST);
                break;
            case UP:
            default:
                if (te.connectN) {
                    list.add(EnumFacing.NORTH);
                }

                if (te.connectS) {
                    list.add(EnumFacing.SOUTH);
                }

                if (te.connectW) {
                    list.add(EnumFacing.WEST);
                }

                if (te.connectE) {
                    list.add(EnumFacing.EAST);
                }

                list.add(EnumFacing.UP);
                list.add(EnumFacing.DOWN);
        }

        return list.toArray(new EnumFacing[0]);
    }

    public void scheduleAreaUpdateNotification(@Nonnull World world, @Nonnull BlockPos pos, boolean immediate) {
        if (!world.isRemote) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            int dimension = world.provider.getDimension();
            LinkedHashSet<BlockPos> bcs;
            if (this.queuedBlockUpdateLocations.containsKey(dimension)) {
                bcs = this.queuedBlockUpdateLocations.get(dimension);
            } else {
                bcs = new LinkedHashSet<>();
                this.queuedBlockUpdateLocations.put(dimension, bcs);
            }

            bcs.add(new BlockPos(x - 1, y, z));
            bcs.add(new BlockPos(x + 1, y, z));
            bcs.add(new BlockPos(x, y - 1, z));
            bcs.add(new BlockPos(x, y + 1, z));
            bcs.add(new BlockPos(x, y, z - 1));
            bcs.add(new BlockPos(x, y, z + 1));
            bcs.add(new BlockPos(x - 1, y - 1, z));
            bcs.add(new BlockPos(x - 1, y, z - 1));
            bcs.add(new BlockPos(x - 1, y, z + 1));
            bcs.add(new BlockPos(x - 1, y + 1, z));
            bcs.add(new BlockPos(x, y - 1, z - 1));
            bcs.add(new BlockPos(x, y - 1, z + 1));
            bcs.add(new BlockPos(x, y + 1, z - 1));
            bcs.add(new BlockPos(x, y + 1, z + 1));
            bcs.add(new BlockPos(x + 1, y - 1, z));
            bcs.add(new BlockPos(x + 1, y, z - 1));
            bcs.add(new BlockPos(x + 1, y, z + 1));
            bcs.add(new BlockPos(x + 1, y + 1, z));
            bcs.add(new BlockPos(x - 2, y, z));
            bcs.add(new BlockPos(x + 2, y, z));
            bcs.add(new BlockPos(x, y - 2, z));
            bcs.add(new BlockPos(x, y + 2, z));
            bcs.add(new BlockPos(x, y, z - 2));
            bcs.add(new BlockPos(x, y, z + 2));
            if (immediate) {
                this.handleQueuedBlockUpdates(world);
            } else {
                world.scheduleBlockUpdate(pos, this, 1, 0);
            }
        }

    }

    private void handleQueuedBlockUpdates(@Nonnull World world) {
        if (!world.isRemote) {
            int dimension = world.provider.getDimension();
            LinkedHashSet<BlockPos> bcs = this.queuedBlockUpdateLocations.get(dimension);
            if (bcs == null) {
                return;
            }
            BlockPos[] queue = bcs.toArray(new BlockPos[0]);
            bcs.clear();
            for (BlockPos bc : queue) {
                world.notifyBlockUpdate(bc, world.getBlockState(bc), world.getBlockState(bc), 3);
            }
        }

    }

    @Override
    public void updateTick(@Nonnull World world, BlockPos pos, IBlockState state, Random rand) {
        this.handleQueuedBlockUpdates(world);
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
            if (!world.isRemote && player.getHeldItem(hand).getItem() instanceof ICaster) {
            RayTraceResult hit = RayTracer.retraceBlock(world, player, pos);
            if (hit != null) {
                TileRedcrystal.CrystalHit calcSide = TileRedcrystal.CrystalHit.fromVal(hit.subHit);
                if (calcSide == TileRedcrystal.CrystalHit.HITCENTER) {
                    return this.onBlockActivatedCenter(world, pos, state, player);
                }

                if (!TileRedcrystal.CrystalHit.isDir(calcSide)) {
                    return false;
                }

                return this.onBlockActivatedAdjustedSide(world, pos, state, player, calcSide.toFacing());
            }
        }

        return player.getHeldItem(hand).getItem() instanceof ICaster;
    }

    public boolean onBlockActivatedCenter(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    public boolean onBlockActivatedAdjustedSide(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, EntityPlayer player, @Nonnull EnumFacing side) {
        if (this.toggleConnectionToDirection(world, pos, state, side, true)) {
            world.playSound(player, pos, SoundsTC.crystal, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }

    public boolean toggleConnectionToDirection(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EnumFacing side, boolean alterOtherConnection) {
        TileRedcrystal te = null;

        try {
            te = (TileRedcrystal)world.getTileEntity(pos);
        } catch (Exception ignored) {
        }

        if (te == null) {
            return false;
        } else {
            boolean connects;
            switch(side) {
                case NORTH:
                    connects = te.connectN;
                    break;
                case SOUTH:
                    connects = te.connectS;
                    break;
                case WEST:
                    connects = te.connectW;
                    break;
                case EAST:
                    connects = te.connectE;
                    break;
                default:
                    return false;
            }

            connects = !connects;
            BlockPos connectedPos = null;
            boolean connectionAroundCorner = false;
            boolean overheadConnection = false;
            EnumFacing dir = side;
            EnumFacing orientation = this.getOrientation(world, pos);
            if (orientation == side) {
                dir = EnumFacing.DOWN;
            } else if (orientation == side.getOpposite()) {
                dir = EnumFacing.UP;
            }

            BlockPos pos2 = pos.offset(dir);
            Block block = state.getBlock();
            if (block instanceof BlockRedcrystal) {
                connectedPos = pos2;
                if (this.getOrientation(world, pos2) == dir.getOpposite()) {
                    overheadConnection = true;
                }
            } else if (block.isAir(state, world, pos2)) {
                BlockWithPos bwp = RedstoneCalc.getORCAroundCorner(world, pos2, orientation, dir);
                if (bwp != null && bwp.block instanceof BlockRedcrystal) {
                    block = bwp.block;
                    connectedPos = bwp;
                    connectionAroundCorner = true;
                }
            } else if (dir != side) {
                BlockPos pos3 = pos.offset(side);
                block = TjUtil.getBlock(world, pos3);
                if (block instanceof BlockRedcrystal) {
                    connectedPos = pos3;
                }
            }

            this.setConnection(world, pos, connects, side);
            if (connectedPos != null) {
                if (alterOtherConnection && !overheadConnection && ((BlockRedcrystal)block).allowNeighborToChangeConnection(world, connectedPos)) {
                    EnumFacing otherSide = side.getOpposite();
                    if (connectionAroundCorner) {
                        otherSide = orientation;
                    }

                    ((BlockRedcrystal)block).setConnection(world, connectedPos, connects, otherSide);
                    this.updateAndPropagateChanges(world, pos, true, false, true, false);
                } else if (!connects) {
                    ((BlockRedcrystal)block).updateAndPropagateChanges(world, connectedPos, true, false, true, false);
                }
            }

            te.markDirty();
            return true;
        }
    }

    public boolean allowNeighborToChangeConnection(World world, BlockPos pos) {
        return true;
    }


    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(@Nonnull IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {
        Block block = blockState.getBlock();
        if (block != this) {
            return super.collisionRayTrace(blockState, world, pos, start, end);
        }
        List<IndexedCuboid6> cuboids = new LinkedList<>();
        addTraceableCuboids(cuboids, world, pos);
        BlockCoord bc = new BlockCoord(pos);
        ArrayList<ExtendedMOP> list = new ArrayList<>();
        this.rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, bc, this, list);
        if (list.size() > 0) {

        }
        return list.size() > 0 ? list.get(0) : this.rayTrace(pos, start, end, this.getNoCasterBlockBounds(world, pos));
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null && ThaumcraftExtension.playerHasCasterEquipped(player)) {
            RayTraceResult hit = RayTracer.retraceBlock((World)source, player, pos);
            List<IndexedCuboid6> cuboids = new LinkedList<>();
            addTraceableCuboids(cuboids, source, pos);
            if (hit != null && hit.subHit >= 0 && hit.subHit <= 6) {

                for (IndexedCuboid6 cc : cuboids) {
                    if ((Integer) cc.data == hit.subHit) {
                        Automagy.logInfo("" + hit.subHit);
                        return cc.sub(new Vector3(pos)).aabb();
                    }
                }
            }
        }
        return this.getNoCasterBlockBounds(source, pos);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos)
    {
        return this.getNoCasterBlockBounds(world, pos);
    }

    protected void addTraceableCuboids(@Nonnull List<IndexedCuboid6> cuboids, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        ((TileRedcrystal)source.getTileEntity(pos)).addTraceableCuboids(cuboids, new Vector3(pos), false);
    }

    protected AxisAlignedBB getNoCasterBlockBounds(@Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos) {
        EnumFacing orientation = this.getOrientation(blockAccess, pos);
        if (orientation == null) {
            orientation = EnumFacing.UP;
        }

        switch(orientation) {
            case DOWN:
                return AABB_BASIC_DOWN;
            case NORTH:
                return AABB_BASIC_NORTH;
            case SOUTH:
                return AABB_BASIC_SOUTH;
            case WEST:
                return AABB_BASIC_WEST;
            case EAST:
                return AABB_BASIC_EAST;
            case UP:
            default:
                return AABB_BASIC_UP;
        }

    }

    private int internalGetMetadata(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos) {
        return blockaccess.getTileEntity(pos).getBlockMetadata();
    }
}
