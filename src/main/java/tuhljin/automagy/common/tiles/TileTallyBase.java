package tuhljin.automagy.common.tiles;

import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import javax.annotation.Nullable;
import tuhljin.automagy.common.blocks.BlockTallyBase;
import tuhljin.automagy.common.lib.NeighborNotifier;
import tuhljin.automagy.common.lib.inventory.FilteringItemList;
import tuhljin.automagy.common.lib.inventory.SizelessItem;
import tuhljin.automagy.common.lib.struct.WorldSpecificCoordinates;

import javax.annotation.Nonnull;

public abstract class TileTallyBase extends ModTileEntityWithFilterMainInventory implements ITickable {
    protected final int COOLDOWN_TIME = 10;
    public int targetX;
    public int targetY = -1;
    public int targetZ;
    @Nonnull
    public boolean[] outputDirs = new boolean[6];
    protected boolean power = false;
    public boolean requireAllMatches = true;
    protected int cooldown = -1;
    protected boolean antiloopIsChecking = false;
    protected boolean antiloopIsDoingCalc = false;

    protected TileTallyBase(String name) {
        super(name);
    }

    public void setOutputDir(@Nonnull EnumFacing dir, boolean enabled) {
        boolean prev = this.outputDirs[dir.getIndex()];
        if (prev != enabled) {
            this.outputDirs[dir.getIndex()] = enabled;
            this.markForUpdate();
            if (this.power) {
                NeighborNotifier.notifyBlocksOfExtendedNeighborChange(this.world, this.pos);
            }
        }

    }

    public boolean isOutputDir(@Nonnull EnumFacing dir) {
        return this.outputDirs[dir.getIndex()];
    }

    public void findNewTarget(boolean neighborMayBeRemoteComparator) {
        if (!this.antiloopIsDoingCalc) {
            int lastX = this.targetX;
            int lastY = this.targetY;
            int lastZ = this.targetZ;
            if (this.cooldown == -1) {
                this.cooldown = 5 + this.world.rand.nextInt(5);
            }

            EnumFacing orientation = ((BlockTallyBase)this.getBlockType()).getFacing(this.world, this.pos);
            if (orientation == EnumFacing.UP) {
                TileEntity te = this.world.getTileEntity(this.pos.offset(EnumFacing.UP));
                if (te instanceof TileRemoteComparator) {
                    if (!neighborMayBeRemoteComparator) {
                        return;
                    }

                    this.targetY = -1;
                    WorldSpecificCoordinates coord = ((TileRemoteComparator)te).getLinkLocation();
                    if (((TileRemoteComparator)te).coordinatesAreInRange(coord) && coord != null) {
                        this.targetX = coord.x;
                        this.targetY = coord.y;
                        this.targetZ = coord.z;
                    }

                    this.calculateRedstoneSignalStrength();
                    if (lastX != this.targetX || lastY != this.targetY || lastZ != this.targetZ) {
                        this.markForUpdate();
                    }

                    return;
                }
            }

            this.targetX = this.pos.getX() + orientation.getXOffset();
            this.targetY = this.pos.getY() + orientation.getYOffset();
            this.targetZ = this.pos.getZ() + orientation.getZOffset();
            if (lastX != this.targetX || lastY != this.targetY || lastZ != this.targetZ) {
                this.foundNewDirectTarget();
            }

        }
    }

    protected void foundNewDirectTarget() {
        this.calculateRedstoneSignalStrength();
        this.markForUpdate();
    }

    @Override
    public void update() {
        if (!this.world.isRemote) {
            if (this.cooldown > 0) {
                --this.cooldown;
            } else {
                this.cooldown = 10;
                this.calculateRedstoneSignalStrength();
            }
        }
    }

    protected void calculateRedstoneSignalStrength() {
        if (!this.antiloopIsDoingCalc) {
            this.antiloopIsDoingCalc = true;
            if (this.filter == null) {
                this.setPower(false);
                this.antiloopIsDoingCalc = false;
            } else {
                FilteringItemList invMap = this.getDetectedItems();
                if (invMap == null) {
                    this.setPower(false);
                    this.antiloopIsDoingCalc = false;
                } else {
                    boolean partialMatching = this.filterIsBlacklist != this.requireAllMatches;
                    boolean anyQualify = false;
                    int size = this.filter.getSizeInventory();

                    SizelessItem key;
                    for(int i = 0; i < size; ++i) {
                        ItemStack stack = this.filter.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            key = new SizelessItem(stack);
                            int amt = invMap.get(key, this.filter.ignoreMetadata, this.filter.ignoreNBT);
                            if (this.compare(amt, stack.getCount())) {
                                if (!partialMatching) {
                                    this.setPower(true);
                                    this.antiloopIsDoingCalc = false;
                                    return;
                                }

                                anyQualify = true;
                            } else if (partialMatching) {
                                this.setPower(false);
                                this.antiloopIsDoingCalc = false;
                                return;
                            }
                        }
                    }

                    if (this.filter.getPattern() == null) {
                        this.setPower(anyQualify);
                    } else {
                        for (Entry<SizelessItem, Integer> sitem : invMap) {
                            key = sitem.getKey();
                            if (this.nameFilterMatches(key)) {
                                this.setPower(!this.filterIsBlacklist);
                                this.antiloopIsDoingCalc = false;
                                return;
                            }
                        }

                        this.setPower(this.filterIsBlacklist);
                    }

                    this.antiloopIsDoingCalc = false;
                }
            }
        }
    }

    @Nullable
    public abstract FilteringItemList getDetectedItems();

    protected boolean compare(int a, int b) {
        if (this.filter.useItemCount) {
            if (this.filterIsBlacklist) {
                return a < b;
            } else {
                return a >= b;
            }
        } else {
            return this.filterIsBlacklist ? a == 0 : a > 0;
        }
    }

    public void setPower(boolean enabled) {
        if (this.power) {
            if (!enabled) {
                this.power = false;
                this.markForUpdate();
                NeighborNotifier.notifyBlocksOfExtendedNeighborChange(this.world, this.pos);
            }
        } else if (enabled) {
            this.power = true;
            this.markForUpdate();
            NeighborNotifier.notifyBlocksOfExtendedNeighborChange(this.world, this.pos);
        }

    }

    @Nullable
    public BlockPos getUltimateTarget() {
        if (this.antiloopIsChecking) {
            return null;
        } else {
            TileEntity tileAtTarget = this.world.getTileEntity(new BlockPos(this.targetX, this.targetY, this.targetZ));
            BlockPos target;
            if (tileAtTarget instanceof TileTallyBase) {
                this.antiloopIsChecking = true;
                target = ((TileTallyBase)tileAtTarget).getUltimateTarget();
                this.antiloopIsChecking = false;
            } else {
                target = new BlockPos(this.targetX, this.targetY, this.targetZ);
            }

            return target;
        }
    }

    public abstract boolean hasValidTarget();

    public void setRequireAllMatches(boolean enabled) {
        if (this.requireAllMatches != enabled) {
            this.requireAllMatches = enabled;
            this.markDirty();
        }

    }

    @Override
    public void readServerNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.readServerNBT(nbttagcompound);
        this.requireAllMatches = nbttagcompound.getBoolean("requireAllMatches");
    }

    @Override
    public void writeServerNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.writeServerNBT(nbttagcompound);
        nbttagcompound.setBoolean("requireAllMatches", this.requireAllMatches);
    }

    @Override
    public void readCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.readCommonNBT(nbttagcompound);
        this.targetX = nbttagcompound.getInteger("targetX");
        this.targetY = nbttagcompound.getInteger("targetY");
        this.targetZ = nbttagcompound.getInteger("targetZ");
        this.power = nbttagcompound.getBoolean("power");
        this.outputDirs = getBooleanArrayFromNbtOrDefault(nbttagcompound, "output", false, 6);
        if (this.world != null && this.world.isRemote) {
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        }

    }

    @Override
    public void writeCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.writeCommonNBT(nbttagcompound);
        nbttagcompound.setInteger("targetX", this.targetX);
        nbttagcompound.setInteger("targetY", this.targetY);
        nbttagcompound.setInteger("targetZ", this.targetZ);
        nbttagcompound.setBoolean("power", this.power);
        setBooleanArrayInNbt(nbttagcompound, "output", this.outputDirs);
    }

    public int getRedstoneOutput(@Nonnull EnumFacing dir) {
        return this.power && this.isOutputDir(dir) ? 15 : 0;
    }

    public int getRedstoneOutput() {
        return this.power ? 15 : 0;
    }
}
