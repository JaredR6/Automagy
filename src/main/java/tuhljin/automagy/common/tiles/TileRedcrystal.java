package tuhljin.automagy.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.codechicken.lib.vec.Vector3;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.blocks.ModBlocks;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.util.List;

public class TileRedcrystal extends ModTileEntity {

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_DOWN = new Cuboid6(new AxisAlignedBB( 0.4D, 0.8D, 0.4D, 0.6D, 1.0D, 0.6D));
    @Nonnull
    private static final Cuboid6 CUBE_NORTH_DOWN = new Cuboid6(new AxisAlignedBB( 0.45D, 0.9D, 0.25D, 0.55D, 1.0D, 0.35D));
    @Nonnull
    private static final Cuboid6 CUBE_SOUTH_DOWN = new Cuboid6(new AxisAlignedBB(0.45D, 0.9D, 0.65D, 0.55D, 1.0D, 0.75D));
    @Nonnull
    private static final Cuboid6 CUBE_WEST_DOWN = new Cuboid6(new AxisAlignedBB(0.25D, 0.9D, 0.45D, 0.35D, 1.0D, 0.55D));
    @Nonnull
    private static final Cuboid6 CUBE_EAST_DOWN = new Cuboid6(new AxisAlignedBB(0.65D, 0.9D, 0.45D, 0.75D, 1.0D, 0.55D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_DOWN = new Cuboid6(new AxisAlignedBB(0.25D, 0.995D, 0.25D, 0.75D, 1.0D, 0.75D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_UP = new Cuboid6(new AxisAlignedBB(0.4D, 0.0D, 0.4D, 0.6D, 0.2D, 0.6D));
    @Nonnull
    private static final Cuboid6 CUBE_NORTH_UP = new Cuboid6(new AxisAlignedBB(0.45D, 0.0D, 0.25D, 0.55D, 0.1D, 0.35D));
    @Nonnull
    private static final Cuboid6 CUBE_SOUTH_UP = new Cuboid6(new AxisAlignedBB(0.45D, 0.0D, 0.65D, 0.55D, 0.1D, 0.75D));
    @Nonnull
    private static final Cuboid6 CUBE_WEST_UP = new Cuboid6(new AxisAlignedBB(0.25D, 0.0D, 0.45D, 0.35D, 0.1D, 0.55D));
    @Nonnull
    private static final Cuboid6 CUBE_EAST_UP = new Cuboid6(new AxisAlignedBB(0.65D, 0.0D, 0.45D, 0.75D, 0.1D, 0.55D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_UP = new Cuboid6(new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.005D, 0.75D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_SOUTH = new Cuboid6(new AxisAlignedBB(0.4D, 0.4D, 0.8D, 0.6D, 0.6D, 1.0D));
    @Nonnull
    private static final Cuboid6 CUBE_UP_SOUTH = new Cuboid6(new AxisAlignedBB(0.45D, 0.65D, 0.9D, 0.55D, 0.75D, 1.0D));
    @Nonnull
    private static final Cuboid6 CUBE_DOWN_SOUTH = new Cuboid6(new AxisAlignedBB(0.45D, 0.25D, 0.9D, 0.55D, 0.35D, 1.0D));
    @Nonnull
    private static final Cuboid6 CUBE_WEST_SOUTH = new Cuboid6(new AxisAlignedBB(0.25D, 0.45D, 0.9D, 0.35D, 0.55D, 1.0D));
    @Nonnull
    private static final Cuboid6 CUBE_EAST_SOUTH = new Cuboid6(new AxisAlignedBB(0.65D, 0.45D, 0.9D, 0.75D, 0.55D, 1.0D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_SOUTH = new Cuboid6(new AxisAlignedBB(0.25D, 0.25D, 0.995D, 0.75D, 0.75D, 1.0D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_NORTH = new Cuboid6(new AxisAlignedBB(0.4D, 0.4D, 0.0D, 0.6D, 0.6D, 0.2D));
    @Nonnull
    private static final Cuboid6 CUBE_UP_NORTH = new Cuboid6(new AxisAlignedBB(0.45D, 0.65D, 0.0D, 0.55D, 0.75D, 0.1D));
    @Nonnull
    private static final Cuboid6 CUBE_DOWN_NORTH = new Cuboid6(new AxisAlignedBB(0.45D, 0.25D, 0.0D, 0.55D, 0.35D, 0.1D));
    @Nonnull
    private static final Cuboid6 CUBE_WEST_NORTH = new Cuboid6(new AxisAlignedBB(0.25D, 0.45D, 0.0D, 0.35D, 0.55D, 0.1D));
    @Nonnull
    private static final Cuboid6 CUBE_EAST_NORTH = new Cuboid6(new AxisAlignedBB(0.65D, 0.45D, 0.0D, 0.75D, 0.55D, 0.1D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_NORTH = new Cuboid6(new AxisAlignedBB(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.005D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_EAST = new Cuboid6(new AxisAlignedBB(0.8D, 0.4D, 0.4D, 1.0D, 0.6D, 0.6D));
    @Nonnull
    private static final Cuboid6 CUBE_UP_EAST = new Cuboid6(new AxisAlignedBB(0.9D, 0.65D, 0.45D, 1.0D, 0.75D, 0.55D));
    @Nonnull
    private static final Cuboid6 CUBE_DOWN_EAST = new Cuboid6(new AxisAlignedBB(0.9D, 0.25D, 0.45D, 1.0D, 0.35D, 0.55D));
    @Nonnull
    private static final Cuboid6 CUBE_NORTH_EAST = new Cuboid6(new AxisAlignedBB(0.9D, 0.45D, 0.25D, 1.0D, 0.55D, 0.35D));
    @Nonnull
    private static final Cuboid6 CUBE_SOUTH_EAST = new Cuboid6(new AxisAlignedBB( 0.9D, 0.45D, 0.65D, 1.0D, 0.55D, 0.75D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_EAST = new Cuboid6(new AxisAlignedBB(0.995D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.4D, 0.4D, 0.2D, 0.6D, 0.6D));
    @Nonnull
    private static final Cuboid6 CUBE_UP_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.65D, 0.45D, 0.1D, 0.75D, 0.55D));
    @Nonnull
    private static final Cuboid6 CUBE_DOWN_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.25D, 0.45D, 0.1D, 0.35D, 0.55D));
    @Nonnull
    private static final Cuboid6 CUBE_NORTH_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.45D, 0.25D, 0.1D, 0.55D, 0.35D));
    @Nonnull
    private static final Cuboid6 CUBE_SOUTH_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.45D, 0.65D, 0.1D, 0.55D, 0.75D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.25D, 0.25D, 0.005D, 0.75D, 0.75D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_DOWN_LARGE = new Cuboid6(new AxisAlignedBB( 0.36D, 0.8D, 0.36D, 0.64D, 1.0D, 0.64D));
    @Nonnull
    private static final Cuboid6 CUBE_NORTH_DOWN_LARGE = new Cuboid6(new AxisAlignedBB( 0.44D,0.9D,0.1D,0.56D, 1.0D,0.25D));
    @Nonnull
    private static final Cuboid6 CUBE_SOUTH_DOWN_LARGE = new Cuboid6(new AxisAlignedBB(0.44D,0.9D,0.75D,0.56D, 1.0D,0.9D));
    @Nonnull
    private static final Cuboid6 CUBE_WEST_DOWN_LARGE = new Cuboid6(new AxisAlignedBB(0.75D,0.9D,0.44D,0.9D, 1.0D,0.56D));
    @Nonnull
    private static final Cuboid6 CUBE_EAST_DOWN_LARGE = new Cuboid6(new AxisAlignedBB(0.1D,0.9D,0.44D,0.25D, 1.0D,0.56D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_DOWN_LARGE = new Cuboid6(new AxisAlignedBB(0.1D, 0.995D,0.1D,0.9D, 1.0D,0.9D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.36D, 0.0D, 0.36D, 0.64D, 0.2D, 0.64D));
    @Nonnull
    private static final Cuboid6 CUBE_NORTH_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.44D, 0.0D,0.1D,0.56D,0.15D,0.25D));
    @Nonnull
    private static final Cuboid6 CUBE_SOUTH_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.44D, 0.0D,0.75D,0.56D,0.15D,0.9D));
    @Nonnull
    private static final Cuboid6 CUBE_WEST_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.75D, 0.0D,0.44D,0.9D,0.15D,0.56D));
    @Nonnull
    private static final Cuboid6 CUBE_EAST_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.1D, 0.0D,0.44D,0.25D,0.15D,0.56D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.1D, 0.0D,0.1D,0.9D, 0.005D,0.9D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.36D, 0.36D, 0.8D, 0.64D, 0.64D, 1.0D));
    @Nonnull
    private static final Cuboid6 CUBE_UP_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.44D,0.75D,0.9D,0.56D,0.9D, 1.0D));
    @Nonnull
    private static final Cuboid6 CUBE_DOWN_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.44D,0.1D,0.9D,0.56D,0.25D, 1.0D));
    @Nonnull
    private static final Cuboid6 CUBE_WEST_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.1D,0.44D,0.9D,0.25D,0.56D, 1.0D));
    @Nonnull
    private static final Cuboid6 CUBE_EAST_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.75D,0.44D,0.9D,0.9D,0.56D, 1.0D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.1D,0.1D, 0.995D,0.9D,0.9D, 1.0D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.36D, 0.36D, 0.0D, 0.64D, 0.64D, 0.2D));
    @Nonnull
    private static final Cuboid6 CUBE_UP_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.44D,0.75D, 0.0D,0.56D,0.9D,0.15D));
    @Nonnull
    private static final Cuboid6 CUBE_DOWN_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.44D,0.1D, 0.0D,0.56D,0.25D,0.15D));
    @Nonnull
    private static final Cuboid6 CUBE_WEST_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.1D,0.44D, 0.0D,0.25D,0.56D,0.15D));
    @Nonnull
    private static final Cuboid6 CUBE_EAST_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.75D,0.44D, 0.0D,0.9D,0.56D,0.15D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.1D,0.1D, 0.0D,0.9D,0.9D, 0.005D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_EAST_LARGE = new Cuboid6(new AxisAlignedBB(0.8D, 0.36D, 0.36D, 1.0D, 0.64D, 0.64D));
    @Nonnull
    private static final Cuboid6 CUBE_UP_EAST_LARGE = new Cuboid6(new AxisAlignedBB(0.9D,0.75D,0.44D, 1.0D,0.9D,0.56D));
    @Nonnull
    private static final Cuboid6 CUBE_DOWN_EAST_LARGE = new Cuboid6(new AxisAlignedBB(0.9D,0.1D,0.44D, 1.0D,0.25D,0.56D));
    @Nonnull
    private static final Cuboid6 CUBE_NORTH_EAST_LARGE = new Cuboid6(new AxisAlignedBB(0.9D,0.44D,0.1D, 1.0D,0.56D,0.25D));
    @Nonnull
    private static final Cuboid6 CUBE_SOUTH_EAST_LARGE = new Cuboid6(new AxisAlignedBB( 0.9D,0.44D,0.75D, 1.0D,0.56D,0.9D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_EAST_LARGE = new Cuboid6(new AxisAlignedBB(0.995D,0.1D,0.1D, 1.0D,0.9D,0.9D));

    @Nonnull
    private static final Cuboid6 CUBE_CENTER_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D, 0.36D, 0.36D, 0.2D, 0.64D, 0.64D));
    @Nonnull
    private static final Cuboid6 CUBE_UP_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D,0.75D,0.44D,0.15D,0.9D,0.56D));
    @Nonnull
    private static final Cuboid6 CUBE_DOWN_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D,0.1D,0.44D,0.15D,0.25D,0.56D));
    @Nonnull
    private static final Cuboid6 CUBE_NORTH_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D,0.44D,0.1D,0.15D,0.56D,0.25D));
    @Nonnull
    private static final Cuboid6 CUBE_SOUTH_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D,0.44D,0.75D,0.15D,0.56D,0.9D));
    @Nonnull
    private static final Cuboid6 CUBE_BASE_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D,0.1D,0.1D, 0.005D,0.9D,0.9D));

    @Nullable
    public EnumFacing orientation;
    public short powerStability;
    @Nullable
    public EnumFacing powerSourceSide;
    public boolean powerSourceOutsideNetwork;
    public boolean connectN;
    public boolean connectE;
    public boolean connectS;
    public boolean connectW;
    public int extraData;

    public TileRedcrystal() {
        this.orientation = null;
        this.extraData = -1;
    }

    public TileRedcrystal(EnumFacing orientation, boolean noConnection) {
        this.orientation = orientation;
        this.powerStability = 0;
        this.powerSourceSide = null;
        this.powerSourceOutsideNetwork = false;
        if (noConnection) {
            this.connectN = false;
            this.connectE = false;
            this.connectS = false;
            this.connectW = false;
        } else {
            this.connectN = true;
            this.connectE = true;
            this.connectS = true;
            this.connectW = true;
        }

        this.extraData = -1;
        this.markDirty();
    }

    public void addTraceableCuboids(@Nonnull List<IndexedCuboid6> cuboids, @Nonnull Vector3 offset, Boolean large) {
        if (this.orientation == null) {
            this.orientation = EnumFacing.UP;
            this.markDirty();
        }
        if (large) {
            switch(this.orientation) {
                case DOWN:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_DOWN_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITNORTH, CUBE_NORTH_DOWN_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITSOUTH, CUBE_SOUTH_DOWN_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITWEST, CUBE_WEST_DOWN_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITEAST, CUBE_EAST_DOWN_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_DOWN_LARGE);
                    break;
                case UP:
                default:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_UP_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITNORTH, CUBE_NORTH_UP_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITSOUTH, CUBE_SOUTH_UP_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITWEST, CUBE_WEST_UP_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITEAST, CUBE_EAST_UP_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_UP_LARGE);
                    break;
                case SOUTH:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_SOUTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITUP, CUBE_UP_SOUTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITDOWN, CUBE_DOWN_SOUTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITWEST, CUBE_WEST_SOUTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITEAST, CUBE_EAST_SOUTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_SOUTH_LARGE);
                    break;
                case NORTH:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_NORTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITUP, CUBE_UP_NORTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITDOWN, CUBE_DOWN_NORTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITWEST, CUBE_WEST_NORTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITEAST, CUBE_EAST_NORTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_NORTH_LARGE);
                    break;
                case WEST:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_WEST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITUP, CUBE_UP_WEST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITDOWN, CUBE_DOWN_WEST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITNORTH, CUBE_NORTH_WEST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITSOUTH, CUBE_SOUTH_WEST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_WEST_LARGE);
                    break;
                case EAST:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITEAST, CUBE_CENTER_EAST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITUP, CUBE_UP_EAST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITDOWN, CUBE_DOWN_EAST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITNORTH, CUBE_NORTH_EAST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITSOUTH, CUBE_SOUTH_EAST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITEAST, CUBE_BASE_EAST_LARGE);
            }
        } else {
            switch(this.orientation) {
                case DOWN:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_DOWN);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITNORTH, CUBE_NORTH_DOWN);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITSOUTH, CUBE_SOUTH_DOWN);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITWEST, CUBE_WEST_DOWN);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITEAST, CUBE_EAST_DOWN);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_DOWN);
                    break;
                case UP:
                default:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_UP);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITNORTH, CUBE_NORTH_UP);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITSOUTH, CUBE_SOUTH_UP);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITWEST, CUBE_WEST_UP);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITEAST, CUBE_EAST_UP);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_UP);
                    break;
                case SOUTH:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_SOUTH);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITUP, CUBE_UP_SOUTH);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITDOWN, CUBE_DOWN_SOUTH);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITWEST, CUBE_WEST_SOUTH);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITEAST, CUBE_EAST_SOUTH);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_SOUTH);
                    break;
                case NORTH:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_NORTH);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITUP, CUBE_UP_NORTH);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITDOWN, CUBE_DOWN_NORTH);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITWEST, CUBE_WEST_NORTH);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITEAST, CUBE_EAST_NORTH);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_NORTH);
                    break;
                case WEST:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_WEST);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITUP, CUBE_UP_WEST);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITDOWN, CUBE_DOWN_WEST);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITNORTH, CUBE_NORTH_WEST);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITSOUTH, CUBE_SOUTH_WEST);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_WEST);
                    break;
                case EAST:
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITCENTER, CUBE_CENTER_EAST);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITUP, CUBE_UP_EAST);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITDOWN, CUBE_DOWN_EAST);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITNORTH, CUBE_NORTH_EAST);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITSOUTH, CUBE_SOUTH_EAST);
                    this.addIndexedCuboid(cuboids, offset, CrystalHit.HITOTHER, CUBE_BASE_EAST);
            }
        }
    }

    protected void addIndexedCuboid(@Nonnull List<IndexedCuboid6> cuboids, Vector3 offset, CrystalHit hit, @Nonnull Cuboid6 cuboid) {
        cuboids.add(new IndexedCuboid6(hit.getVal(), cuboid.copy().add(offset)));
    }

    @Override
    public void readCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        this.orientation = getEnumFacingFromNBT(nbttagcompound, "orientation", EnumFacing.UP);
        this.powerStability = nbttagcompound.getShort("powerStability");
        this.powerSourceSide = getEnumFacingFromNBT(nbttagcompound, "powerSourceSide", null, true);
        this.powerSourceOutsideNetwork = nbttagcompound.getBoolean("powerSourceOutsideNetwork");
        this.connectN = nbttagcompound.getBoolean("connectN");
        this.connectE = nbttagcompound.getBoolean("connectE");
        this.connectS = nbttagcompound.getBoolean("connectS");
        this.connectW = nbttagcompound.getBoolean("connectW");
        if (nbttagcompound.hasKey("extraData")) {
            try {
                this.extraData = nbttagcompound.getInteger("extraData");
            } catch (Exception ex) {
                this.extraData = -1;
            }
        } else {
            this.extraData = -1;
        }
        world.scheduleBlockUpdate(pos, ModBlocks.redcrystal, 1, 0);
        Automagy.logInfo("Loaded redcrystal tile entity");
    }

    @Override
    public void writeCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        setEnumFacingInNBT(nbttagcompound, "orientation", this.orientation);
        nbttagcompound.setShort("powerStability", this.powerStability);
        setEnumFacingInNBT(nbttagcompound, "powerSourceSide", this.powerSourceSide);
        nbttagcompound.setBoolean("powerSourceOutsideNetwork", this.powerSourceOutsideNetwork);
        nbttagcompound.setBoolean("connectN", this.connectN);
        nbttagcompound.setBoolean("connectE", this.connectE);
        nbttagcompound.setBoolean("connectS", this.connectS);
        nbttagcompound.setBoolean("connectW", this.connectW);
        if (this.extraData != -1) {
            nbttagcompound.setInteger("extraData", this.extraData);
        }

    }
    
    public enum CrystalHit {

        HITDOWN(0),
        HITUP(1),
        HITNORTH(2),
        HITSOUTH(3),
        HITWEST(4),
        HITEAST(5),
        HITCENTER(6),
        HITOTHER(7);
        
        private int val;
        private static CrystalHit[] values = {HITDOWN, HITUP, HITNORTH, HITSOUTH, HITWEST, HITEAST, HITCENTER, HITOTHER};
        
        CrystalHit(int val) {
            this.val = val;
        }
        
        public int getVal() { return val; }

        public static CrystalHit fromVal(int val) {
            return val < 0 ? HITOTHER : values[val];
        }

        public static boolean isVertical(CrystalHit hit) {
            return hit == HITUP || hit == HITDOWN;
        }

        public static boolean isDir(CrystalHit hit) {
            return hit != HITCENTER && hit != HITOTHER;
        }


        public EnumFacing toFacing() {
            if (!isDir(this)) {
                return EnumFacing.UP;
            } else {
                return EnumFacing.VALUES[this.val];
            }
        }
    }
}
