package tuhljin.automagy.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.codechicken.lib.vec.Vector3;

import java.util.List;

public class TileRedcrystal extends ModTileEntity {

    private static Cuboid6 CUBE_CENTER_DOWN = new Cuboid6(new AxisAlignedBB( 0.4D, 0.8D, 0.4D, 0.6D, 1.0D, 0.6D));
    private static Cuboid6 CUBE_NORTH_DOWN = new Cuboid6(new AxisAlignedBB( 0.45D, 0.9D, 0.25D, 0.55D, 1.0D, 0.35D));
    private static Cuboid6 CUBE_SOUTH_DOWN = new Cuboid6(new AxisAlignedBB(0.45D, 0.9D, 0.65D, 0.55D, 1.0D, 0.75D));
    private static Cuboid6 CUBE_WEST_DOWN = new Cuboid6(new AxisAlignedBB(0.65D, 0.9D, 0.45D, 0.75D, 1.0D, 0.55D));
    private static Cuboid6 CUBE_EAST_DOWN = new Cuboid6(new AxisAlignedBB(0.25D, 0.9D, 0.45D, 0.35D, 1.0D, 0.55D));
    private static Cuboid6 CUBE_BASE_DOWN = new Cuboid6(new AxisAlignedBB(0.25D, 0.995D, 0.25D, 0.75D, 1.0D, 0.75D));

    private static Cuboid6 CUBE_CENTER_UP = new Cuboid6(new AxisAlignedBB(0.4D, 0.0D, 0.4D, 0.6D, 0.2D, 0.6D));
    private static Cuboid6 CUBE_NORTH_UP = new Cuboid6(new AxisAlignedBB(0.45D, 0.0D, 0.25D, 0.55D, 0.1D, 0.35D));
    private static Cuboid6 CUBE_SOUTH_UP = new Cuboid6(new AxisAlignedBB(0.45D, 0.0D, 0.65D, 0.55D, 0.1D, 0.75D));
    private static Cuboid6 CUBE_WEST_UP = new Cuboid6(new AxisAlignedBB(0.65D, 0.0D, 0.45D, 0.75D, 0.1D, 0.55D));
    private static Cuboid6 CUBE_EAST_UP = new Cuboid6(new AxisAlignedBB(0.25D, 0.0D, 0.45D, 0.35D, 0.1D, 0.55D));
    private static Cuboid6 CUBE_BASE_UP = new Cuboid6(new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.005D, 0.75D));

    private static Cuboid6 CUBE_CENTER_SOUTH = new Cuboid6(new AxisAlignedBB(0.4D, 0.4D, 0.8D, 0.6D, 0.6D, 1.0D));
    private static Cuboid6 CUBE_UP_SOUTH = new Cuboid6(new AxisAlignedBB(0.45D, 0.65D, 0.9D, 0.55D, 0.75D, 1.0D));
    private static Cuboid6 CUBE_DOWN_SOUTH = new Cuboid6(new AxisAlignedBB(0.45D, 0.25D, 0.9D, 0.55D, 0.35D, 1.0D));
    private static Cuboid6 CUBE_WEST_SOUTH = new Cuboid6(new AxisAlignedBB(0.25D, 0.45D, 0.9D, 0.35D, 0.55D, 1.0D));
    private static Cuboid6 CUBE_EAST_SOUTH = new Cuboid6(new AxisAlignedBB(0.65D, 0.45D, 0.9D, 0.75D, 0.55D, 1.0D));
    private static Cuboid6 CUBE_BASE_SOUTH = new Cuboid6(new AxisAlignedBB(0.25D, 0.25D, 0.995D, 0.75D, 0.75D, 1.0D));

    private static Cuboid6 CUBE_CENTER_NORTH = new Cuboid6(new AxisAlignedBB(0.4D, 0.4D, 0.0D, 0.6D, 0.6D, 0.2D));
    private static Cuboid6 CUBE_UP_NORTH = new Cuboid6(new AxisAlignedBB(0.45D, 0.65D, 0.0D, 0.55D, 0.75D, 0.1D));
    private static Cuboid6 CUBE_DOWN_NORTH = new Cuboid6(new AxisAlignedBB(0.45D, 0.25D, 0.0D, 0.55D, 0.35D, 0.1D));
    private static Cuboid6 CUBE_WEST_NORTH = new Cuboid6(new AxisAlignedBB(0.25D, 0.45D, 0.0D, 0.35D, 0.55D, 0.1D));
    private static Cuboid6 CUBE_EAST_NORTH = new Cuboid6(new AxisAlignedBB(0.65D, 0.45D, 0.0D, 0.75D, 0.55D, 0.1D));
    private static Cuboid6 CUBE_BASE_NORTH = new Cuboid6(new AxisAlignedBB(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.005D));

    private static Cuboid6 CUBE_CENTER_EAST = new Cuboid6(new AxisAlignedBB(0.8D, 0.4D, 0.4D, 1.0D, 0.6D, 0.6D));
    private static Cuboid6 CUBE_UP_EAST = new Cuboid6(new AxisAlignedBB(0.9D, 0.65D, 0.45D, 1.0D, 0.75D, 0.55D));
    private static Cuboid6 CUBE_DOWN_EAST = new Cuboid6(new AxisAlignedBB(0.9D, 0.25D, 0.45D, 1.0D, 0.35D, 0.55D));
    private static Cuboid6 CUBE_NORTH_EAST = new Cuboid6(new AxisAlignedBB(0.9D, 0.45D, 0.25D, 1.0D, 0.55D, 0.35D));
    private static Cuboid6 CUBE_SOUTH_EAST = new Cuboid6(new AxisAlignedBB( 0.9D, 0.45D, 0.65D, 1.0D, 0.55D, 0.75D));
    private static Cuboid6 CUBE_BASE_EAST = new Cuboid6(new AxisAlignedBB(0.995D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D));

    private static Cuboid6 CUBE_CENTER_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.4D, 0.4D, 0.2D, 0.6D, 0.6D));
    private static Cuboid6 CUBE_UP_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.65D, 0.45D, 0.1D, 0.75D, 0.55D));
    private static Cuboid6 CUBE_DOWN_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.25D, 0.45D, 0.1D, 0.35D, 0.55D));
    private static Cuboid6 CUBE_NORTH_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.45D, 0.25D, 0.1D, 0.55D, 0.35D));
    private static Cuboid6 CUBE_SOUTH_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.45D, 0.65D, 0.1D, 0.55D, 0.75D));
    private static Cuboid6 CUBE_BASE_WEST = new Cuboid6(new AxisAlignedBB(0.0D, 0.25D, 0.25D, 0.005D, 0.75D, 0.75D));

    private static Cuboid6 CUBE_CENTER_DOWN_LARGE = new Cuboid6(new AxisAlignedBB( 0.36D, 0.8D, 0.36D, 0.64D, 1.0D, 0.64D));
    private static Cuboid6 CUBE_NORTH_DOWN_LARGE = new Cuboid6(new AxisAlignedBB( 0.44D,0.9D,0.1D,0.56D, 1.0D,0.25D));
    private static Cuboid6 CUBE_SOUTH_DOWN_LARGE = new Cuboid6(new AxisAlignedBB(0.44D,0.9D,0.75D,0.56D, 1.0D,0.9D));
    private static Cuboid6 CUBE_WEST_DOWN_LARGE = new Cuboid6(new AxisAlignedBB(0.75D,0.9D,0.44D,0.9D, 1.0D,0.56D));
    private static Cuboid6 CUBE_EAST_DOWN_LARGE = new Cuboid6(new AxisAlignedBB(0.1D,0.9D,0.44D,0.25D, 1.0D,0.56D));
    private static Cuboid6 CUBE_BASE_DOWN_LARGE = new Cuboid6(new AxisAlignedBB(0.1D, 0.995D,0.1D,0.9D, 1.0D,0.9D));

    private static Cuboid6 CUBE_CENTER_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.36D, 0.0D, 0.36D, 0.64D, 0.2D, 0.64D));
    private static Cuboid6 CUBE_NORTH_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.44D, 0.0D,0.1D,0.56D,0.15D,0.25D));
    private static Cuboid6 CUBE_SOUTH_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.44D, 0.0D,0.75D,0.56D,0.15D,0.9D));
    private static Cuboid6 CUBE_WEST_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.75D, 0.0D,0.44D,0.9D,0.15D,0.56D));
    private static Cuboid6 CUBE_EAST_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.1D, 0.0D,0.44D,0.25D,0.15D,0.56D));
    private static Cuboid6 CUBE_BASE_UP_LARGE = new Cuboid6(new AxisAlignedBB(0.1D, 0.0D,0.1D,0.9D, 0.005D,0.9D));

    private static Cuboid6 CUBE_CENTER_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.36D, 0.36D, 0.8D, 0.64D, 0.64D, 1.0D));
    private static Cuboid6 CUBE_UP_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.44D,0.75D,0.9D,0.56D,0.9D, 1.0D));
    private static Cuboid6 CUBE_DOWN_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.44D,0.1D,0.9D,0.56D,0.25D, 1.0D));
    private static Cuboid6 CUBE_WEST_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.1D,0.44D,0.9D,0.25D,0.56D, 1.0D));
    private static Cuboid6 CUBE_EAST_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.75D,0.44D,0.9D,0.9D,0.56D, 1.0D));
    private static Cuboid6 CUBE_BASE_SOUTH_LARGE = new Cuboid6(new AxisAlignedBB(0.1D,0.1D, 0.995D,0.9D,0.9D, 1.0D));

    private static Cuboid6 CUBE_CENTER_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.36D, 0.36D, 0.0D, 0.64D, 0.64D, 0.2D));
    private static Cuboid6 CUBE_UP_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.44D,0.75D, 0.0D,0.56D,0.9D,0.15D));
    private static Cuboid6 CUBE_DOWN_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.44D,0.1D, 0.0D,0.56D,0.25D,0.15D));
    private static Cuboid6 CUBE_WEST_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.1D,0.44D, 0.0D,0.25D,0.56D,0.15D));
    private static Cuboid6 CUBE_EAST_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.75D,0.44D, 0.0D,0.9D,0.56D,0.15D));
    private static Cuboid6 CUBE_BASE_NORTH_LARGE = new Cuboid6(new AxisAlignedBB(0.1D,0.1D, 0.0D,0.9D,0.9D, 0.005D));

    private static Cuboid6 CUBE_CENTER_EAST_LARGE = new Cuboid6(new AxisAlignedBB(0.8D, 0.36D, 0.36D, 1.0D, 0.64D, 0.64D));
    private static Cuboid6 CUBE_UP_EAST_LARGE = new Cuboid6(new AxisAlignedBB(0.9D,0.75D,0.44D, 1.0D,0.9D,0.56D));
    private static Cuboid6 CUBE_DOWN_EAST_LARGE = new Cuboid6(new AxisAlignedBB(0.9D,0.1D,0.44D, 1.0D,0.25D,0.56D));
    private static Cuboid6 CUBE_NORTH_EAST_LARGE = new Cuboid6(new AxisAlignedBB(0.9D,0.44D,0.1D, 1.0D,0.56D,0.25D));
    private static Cuboid6 CUBE_SOUTH_EAST_LARGE = new Cuboid6(new AxisAlignedBB( 0.9D,0.44D,0.75D, 1.0D,0.56D,0.9D));
    private static Cuboid6 CUBE_BASE_EAST_LARGE = new Cuboid6(new AxisAlignedBB(0.995D,0.1D,0.1D, 1.0D,0.9D,0.9D));

    private static Cuboid6 CUBE_CENTER_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D, 0.36D, 0.36D, 0.2D, 0.64D, 0.64D));
    private static Cuboid6 CUBE_UP_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D,0.75D,0.44D,0.15D,0.9D,0.56D));
    private static Cuboid6 CUBE_DOWN_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D,0.1D,0.44D,0.15D,0.25D,0.56D));
    private static Cuboid6 CUBE_NORTH_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D,0.44D,0.1D,0.15D,0.56D,0.25D));
    private static Cuboid6 CUBE_SOUTH_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D,0.44D,0.75D,0.15D,0.56D,0.9D));
    private static Cuboid6 CUBE_BASE_WEST_LARGE = new Cuboid6(new AxisAlignedBB(0.0D,0.1D,0.1D, 0.005D,0.9D,0.9D));

    public static final int HITDOWN = 0;
    public static final int HITUP = 1;
    public static final int HITNORTH = 2;
    public static final int HITSOUTH = 3;
    public static final int HITWEST = 4;
    public static final int HITEAST = 5;
    public static final int HITCENTER = 6;
    public static final int HITOTHER = 7;

    public EnumFacing orientation;
    public short powerStability;
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
    }

    public void addTraceableCuboids(List<IndexedCuboid6> cuboids, BlockPos pos, Boolean large) {
        Vector3 offset = new Vector3(pos);
        if (large) {
            switch(this.orientation) {
                case DOWN:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_DOWN_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITNORTH, CUBE_NORTH_DOWN_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITSOUTH, CUBE_SOUTH_DOWN_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITWEST, CUBE_WEST_DOWN_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITEAST, CUBE_EAST_DOWN_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_DOWN_LARGE);
                    break;
                case UP:
                default:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_UP_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITNORTH, CUBE_NORTH_UP_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITSOUTH, CUBE_SOUTH_UP_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITWEST, CUBE_WEST_UP_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITEAST, CUBE_EAST_UP_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_UP_LARGE);
                    break;
                case SOUTH:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_SOUTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITUP, CUBE_UP_SOUTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITDOWN, CUBE_DOWN_SOUTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITWEST, CUBE_WEST_SOUTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITEAST, CUBE_EAST_SOUTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_SOUTH_LARGE);
                    break;
                case NORTH:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_NORTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITUP, CUBE_UP_NORTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITDOWN, CUBE_DOWN_NORTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITWEST, CUBE_WEST_NORTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITEAST, CUBE_EAST_NORTH_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_NORTH_LARGE);
                    break;
                case WEST:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_WEST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITUP, CUBE_UP_WEST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITDOWN, CUBE_DOWN_WEST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITNORTH, CUBE_NORTH_WEST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITSOUTH, CUBE_SOUTH_WEST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_WEST_LARGE);
                    break;
                case EAST:
                    this.addIndexedCuboid(cuboids, offset, HITEAST, CUBE_CENTER_EAST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITUP, CUBE_UP_EAST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITDOWN, CUBE_DOWN_EAST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITNORTH, CUBE_NORTH_EAST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITSOUTH, CUBE_SOUTH_EAST_LARGE);
                    this.addIndexedCuboid(cuboids, offset, HITEAST, CUBE_BASE_EAST_LARGE);
            }
        } else {
            switch(this.orientation) {
                case DOWN:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_DOWN);
                    this.addIndexedCuboid(cuboids, offset, HITNORTH, CUBE_NORTH_DOWN);
                    this.addIndexedCuboid(cuboids, offset, HITSOUTH, CUBE_SOUTH_DOWN);
                    this.addIndexedCuboid(cuboids, offset, HITWEST, CUBE_WEST_DOWN);
                    this.addIndexedCuboid(cuboids, offset, HITEAST, CUBE_EAST_DOWN);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_DOWN);
                    break;
                case UP:
                default:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_UP);
                    this.addIndexedCuboid(cuboids, offset, HITNORTH, CUBE_NORTH_UP);
                    this.addIndexedCuboid(cuboids, offset, HITSOUTH, CUBE_SOUTH_UP);
                    this.addIndexedCuboid(cuboids, offset, HITWEST, CUBE_WEST_UP);
                    this.addIndexedCuboid(cuboids, offset, HITEAST, CUBE_EAST_UP);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_UP);
                    break;
                case SOUTH:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_SOUTH);
                    this.addIndexedCuboid(cuboids, offset, HITUP, CUBE_UP_SOUTH);
                    this.addIndexedCuboid(cuboids, offset, HITDOWN, CUBE_DOWN_SOUTH);
                    this.addIndexedCuboid(cuboids, offset, HITWEST, CUBE_WEST_SOUTH);
                    this.addIndexedCuboid(cuboids, offset, HITEAST, CUBE_EAST_SOUTH);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_SOUTH);
                    break;
                case NORTH:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_NORTH);
                    this.addIndexedCuboid(cuboids, offset, HITUP, CUBE_UP_NORTH);
                    this.addIndexedCuboid(cuboids, offset, HITDOWN, CUBE_DOWN_NORTH);
                    this.addIndexedCuboid(cuboids, offset, HITWEST, CUBE_WEST_NORTH);
                    this.addIndexedCuboid(cuboids, offset, HITEAST, CUBE_EAST_NORTH);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_NORTH);
                    break;
                case WEST:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_WEST);
                    this.addIndexedCuboid(cuboids, offset, HITUP, CUBE_UP_WEST);
                    this.addIndexedCuboid(cuboids, offset, HITDOWN, CUBE_DOWN_WEST);
                    this.addIndexedCuboid(cuboids, offset, HITNORTH, CUBE_NORTH_WEST);
                    this.addIndexedCuboid(cuboids, offset, HITSOUTH, CUBE_SOUTH_WEST);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_WEST);
                    break;
                case EAST:
                    this.addIndexedCuboid(cuboids, offset, HITCENTER, CUBE_CENTER_EAST);
                    this.addIndexedCuboid(cuboids, offset, HITUP, CUBE_UP_EAST);
                    this.addIndexedCuboid(cuboids, offset, HITDOWN, CUBE_DOWN_EAST);
                    this.addIndexedCuboid(cuboids, offset, HITNORTH, CUBE_NORTH_EAST);
                    this.addIndexedCuboid(cuboids, offset, HITSOUTH, CUBE_SOUTH_EAST);
                    this.addIndexedCuboid(cuboids, offset, HITOTHER, CUBE_BASE_EAST);
            }
        }
    }

    protected void addIndexedCuboid(List<IndexedCuboid6> cuboids, Vector3 offset, int i, Cuboid6 cuboid) {
        cuboids.add(new IndexedCuboid6(i, cuboid.add(offset)));
    }

    public void readCommonNBT(NBTTagCompound nbttagcompound) {
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

    }

    public void writeCommonNBT(NBTTagCompound nbttagcompound) {
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
}
