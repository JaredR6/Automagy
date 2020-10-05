package tuhljin.automagy.common.tiles;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalMerc;
import tuhljin.automagy.common.data.RedstoneMirrorNetwork;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.struct.WorldSpecificCoordinates;

import javax.annotation.Nonnull;

public class TileRedcrystalMerc extends TileRedcrystal implements ITickable {
    public static final int MAX_DISTANCE = 8;
    public int mirrorX;
    public int mirrorY = -1;
    public int mirrorZ;
    public int mirrorDim;
    public int mirrorLinkedX;
    public int mirrorLinkedY = -1;
    public int mirrorLinkedZ;
    public int mirrorLinkedDim;
    public int strengthWithoutMirror;
    protected boolean mirrorValid = false;
    protected boolean mirrorStartedValid = false;
    private int ticksSinceMirrorCheck = 0;
    public static int rotationSpeedFactor = 3240;
    public static double clientRenderFloatingTop_unpowered = -0.2D;
    public static double clientRenderFloatingTop_powered = 0.1D;
    public static double clientRenderFloatingBtm_unpowered = -0.3D;
    public static double clientRenderFloatingBtm_powered = -0.1D;
    public int clientRenderRotationHelper = 0;
    public double clientRenderFloatingDistance;
    public boolean clientRenderGoingUp;

    public TileRedcrystalMerc() {
        this.clientRenderFloatingDistance = clientRenderFloatingTop_unpowered;
        this.clientRenderGoingUp = false;
    }

    public TileRedcrystalMerc(EnumFacing orientation, boolean noConnection) {
        super(orientation, noConnection);
        this.clientRenderFloatingDistance = clientRenderFloatingTop_unpowered;
        this.clientRenderGoingUp = false;
    }

    public boolean hasValidMirrorConnection() {
        return this.mirrorValid;
    }

    public void setMirrorLink(int dimensionID, int x, int y, int z) {
        this.mirrorX = x;
        this.mirrorY = y;
        this.mirrorZ = z;
        this.mirrorDim = dimensionID;
        this.extraData = 0;
        if (this.mirrorY == -1) {
            this.mirrorValid = false;
        } else if (this.mirrorDim == this.world.provider.getDimension()) {
            double distance = this.pos.getDistance(x, y, z);
            if (distance <= MAX_DISTANCE) {
                TileEntity te = this.world.getTileEntity(new BlockPos(this.mirrorX, this.mirrorY, this.mirrorZ));
                if (ThaumcraftExtension.tileIsMirror(te)) {
                    this.mirrorValid = true;
                    this.mirrorStartedValid = true;
                    WorldSpecificCoordinates coord = ThaumcraftExtension.getLinkedMirrorCoordinates(te);
                    if (coord == null) {
                        this.mirrorLinkedY = -1;
                    } else {
                        this.mirrorLinkedDim = coord.dim;
                        this.mirrorLinkedX = coord.x;
                        this.mirrorLinkedY = coord.y;
                        this.mirrorLinkedZ = coord.z;
                    }
                } else if (TjUtil.isChunkLoaded(this.world, x, z)) {
                    this.mirrorY = -1;
                    this.mirrorValid = false;
                }
            } else {
                this.mirrorValid = false;
            }
        } else {
            this.mirrorValid = false;
        }

        if (!this.world.isRemote) {
            this.updateMirrorNetwork();
        }

        this.markForUpdate();
    }

    @Override
    public void update() {
        if (this.world.isRemote) {
            this.clientRenderRotationHelper += 3;
            boolean powered = this.getBlockMetadata() > 0;
            if (powered) {
                this.clientRenderRotationHelper += 7;
            }

            if (this.clientRenderRotationHelper >= rotationSpeedFactor) {
                this.clientRenderRotationHelper -= rotationSpeedFactor;
            }

            double adj = 0.0D;
            if (this.clientRenderFloatingDistance < (powered ? clientRenderFloatingBtm_powered : clientRenderFloatingBtm_unpowered)) {
                this.clientRenderGoingUp = true;
            } else if (this.clientRenderFloatingDistance > (powered ? clientRenderFloatingTop_powered : clientRenderFloatingTop_unpowered)) {
                this.clientRenderGoingUp = false;
            }

            adj += this.clientRenderGoingUp ? 5.0E-4D : -5.0E-4D;
            if (powered) {
                adj *= this.clientRenderFloatingDistance < clientRenderFloatingBtm_powered ? 12 : 3;
            } else if (this.clientRenderFloatingDistance > clientRenderFloatingTop_unpowered) {
                adj *= 7.0D;
            }

            this.clientRenderFloatingDistance += adj;
        } else if (this.mirrorStartedValid) {
            ++this.ticksSinceMirrorCheck;
            if (this.ticksSinceMirrorCheck >= 100) {
                this.ticksSinceMirrorCheck = 0;
                TileEntity te = this.world.getTileEntity(new BlockPos(this.mirrorX, this.mirrorY, this.mirrorZ));
                if (ThaumcraftExtension.tileIsMirror(te)) {
                    WorldSpecificCoordinates coord = ThaumcraftExtension.getLinkedMirrorCoordinates(te);
                    if (coord == null) {
                        this.mirrorLinkedY = -1;
                    } else {
                        this.mirrorLinkedDim = coord.dim;
                        this.mirrorLinkedX = coord.x;
                        this.mirrorLinkedY = coord.y;
                        this.mirrorLinkedZ = coord.z;
                    }

                    if (!this.mirrorValid) {
                        this.mirrorValid = true;
                        this.markForUpdate();
                        return;
                    }

                    this.markDirty();
                } else if (this.mirrorValid && TjUtil.isChunkLoaded(this.world, this.mirrorX, this.mirrorZ)) {
                    this.mirrorValid = false;
                    this.mirrorLinkedY = -1;
                    this.extraData = 0;
                    this.markForUpdate();
                    RedstoneMirrorNetwork closeMirrorData = RedstoneMirrorNetwork.getData(this.mirrorDim);
                    closeMirrorData.removeSignal(this.mirrorX, this.mirrorY, this.mirrorZ);
                    this.updateBlock();
                    return;
                }
            }

            if (this.mirrorValid && this.mirrorLinkedY != -1) {
                RedstoneMirrorNetwork farMirrorData = RedstoneMirrorNetwork.getData(this.mirrorLinkedDim);
                int mirrorStrength = farMirrorData.getSignalGoingIn(this.mirrorLinkedX, this.mirrorLinkedY, this.mirrorLinkedZ);
                if (this.extraData != mirrorStrength) {
                    this.extraData = mirrorStrength;
                    int strength = this.getBlockMetadata() + this.powerStability;
                    this.updateBlock();
                }
            }
        }

    }

    public void removeFromMirrorNetwork() {
        if (this.mirrorStartedValid) {
            RedstoneMirrorNetwork closeMirrorData = RedstoneMirrorNetwork.getData(this.mirrorDim);
            closeMirrorData.removeContributor(this.mirrorX, this.mirrorY, this.mirrorZ, this);
        }

    }

    public void updateMirrorNetwork() {
        if (this.mirrorStartedValid) {
            RedstoneMirrorNetwork closeMirrorData = RedstoneMirrorNetwork.getData(this.mirrorDim);
            closeMirrorData.contributeToSignalInto(this.mirrorX, this.mirrorY, this.mirrorZ, this, this.strengthWithoutMirror);
        }

    }

    protected void updateBlock() {
        Block block = TjUtil.getBlock(world, this.getPos());
        if (block instanceof BlockRedcrystalMerc) {
            ((BlockRedcrystalMerc)block).updateAndPropagateChanges(this.world, this.getPos(), true, false, false, false);
        }

    }

    @Override
    public void writeCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.writeCommonNBT(nbttagcompound);
        if (this.mirrorY != -1) {
            nbttagcompound.setInteger("mirrorX", this.mirrorX);
            nbttagcompound.setInteger("mirrorY", this.mirrorY);
            nbttagcompound.setInteger("mirrorZ", this.mirrorZ);
            nbttagcompound.setInteger("mirrorDim", this.mirrorDim);
            nbttagcompound.setBoolean("mirrorValid", this.mirrorValid);
            nbttagcompound.setBoolean("mirrorStartedValid", this.mirrorStartedValid);
            nbttagcompound.setInteger("strengthWithoutMirror", this.strengthWithoutMirror);
            if (this.mirrorLinkedY != -1) {
                nbttagcompound.setInteger("mirrorLinkedX", this.mirrorLinkedX);
                nbttagcompound.setInteger("mirrorLinkedY", this.mirrorLinkedY);
                nbttagcompound.setInteger("mirrorLinkedZ", this.mirrorLinkedZ);
                nbttagcompound.setInteger("mirrorLinkedDim", this.mirrorLinkedDim);
            }
        }

    }

    @Override
    public void readCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.readCommonNBT(nbttagcompound);
        this.mirrorX = nbttagcompound.getInteger("mirrorX");
        this.mirrorY = nbttagcompound.hasKey("mirrorY") ? nbttagcompound.getInteger("mirrorY") : -1;
        this.mirrorZ = nbttagcompound.getInteger("mirrorZ");
        this.mirrorDim = nbttagcompound.getInteger("mirrorDim");
        this.mirrorValid = nbttagcompound.getBoolean("mirrorValid");
        this.mirrorStartedValid = nbttagcompound.getBoolean("mirrorStartedValid");
        this.strengthWithoutMirror = nbttagcompound.getInteger("strengthWithoutMirror");
        this.mirrorLinkedX = nbttagcompound.getInteger("mirrorLinkedX");
        this.mirrorLinkedY = nbttagcompound.hasKey("mirrorLinkedY") ? nbttagcompound.getInteger("mirrorLinkedY") : -1;
        this.mirrorLinkedZ = nbttagcompound.getInteger("mirrorLinkedZ");
        this.mirrorLinkedDim = nbttagcompound.getInteger("mirrorLinkedDim");
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        return new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
    }
}
