package tuhljin.automagy.common.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import tuhljin.automagy.common.blocks.IRemoteComparatorOverride;
import tuhljin.automagy.common.items.IAutomagyLocationLink;
import tuhljin.automagy.common.lib.NeighborNotifier;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.struct.WorldSpecificCoordinates;

import javax.annotation.Nonnull;

public class TileRemoteComparator extends ModTileEntityWithInventory implements ITickable {
    protected static final int MAX_DISTANCE = 20;
    protected static final int COOLDOWN_TIME = 4;
    protected int redstoneSignalStrength = 0;
    protected int linkMode = 0;
    protected int linkX;
    protected int linkY;
    protected int linkZ;
    protected int cooldown = 0;

    public TileRemoteComparator() {
        super("remoteComparator", 1, true, true);
    }

    public static boolean isValidContainerForReading(@Nonnull Block block, @Nonnull World world, @Nonnull BlockPos pos) {
        if (block.hasComparatorInputOverride(world.getBlockState(pos))) {
            return true;
        } else {
            TileEntity te = world.getTileEntity(pos);
            return te instanceof IInventory;
        }
    }

    public static int getComparatorReading(@Nonnull Block block, @Nonnull World world, @Nonnull BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (block.hasComparatorInputOverride(state)) {
            return block.getComparatorInputOverride(state, world, pos);
        } else {
            TileEntity te = world.getTileEntity(pos);
            return te instanceof IInventory ? Container.calcRedstoneFromInventory((IInventory)te) : 0;
        }
    }

    @Nullable
    public WorldSpecificCoordinates getLinkLocation() {
        ItemStack stack = this.getStackInSlot(0);
        return stack.getItem() instanceof IAutomagyLocationLink ? ((IAutomagyLocationLink)stack.getItem()).getLinkLocation(stack) : null;
    }

    public boolean coordinatesAreInRange(@Nullable WorldSpecificCoordinates coord) {
        if (coord != null && coord.dim == this.world.provider.getDimension()) {
            return this.pos.distanceSq(coord.x, coord.y, coord.z) <= MAX_DISTANCE;
        } else {
            return false;
        }
    }

    public boolean coordinatesAreInRange() {
        return this.coordinatesAreInRange(this.getLinkLocation());
    }

    @Nullable
    public Block getBlockAtLinkedLocationIfValid() {
        WorldSpecificCoordinates coord = this.getLinkLocation();
        if (coord != null && coord.dim == this.world.provider.getDimension()) {
            BlockPos linkPos = coord.toBlockPos();
            Block block = TjUtil.getBlock(this.world, linkPos);
            if (isValidContainerForReading(block, this.world, linkPos)) {
                return block;
            }
        }

        return null;
    }

    @Nonnull
    public ItemStack getFloatingDisplayItem() {
        return this.getStackInSlot(0);
    }

    public boolean isRedstoneSignalBeingSent() {
        if (this.linkMode == -2) {
            BlockPos downPos = this.pos.offset(EnumFacing.DOWN);
            Block block = TjUtil.getBlock(this.world, downPos);
            if (this.world.isAirBlock(downPos)) {
                return false;
            } else if (block instanceof IRemoteComparatorOverride) {
                return ((IRemoteComparatorOverride)block).hasActiveRedstoneSignal(this.world, downPos, this);
            } else {
                return block.getWeakPower(this.world.getBlockState(downPos), this.world, downPos, EnumFacing.NORTH) > 0;
            }
        } else {
            return this.getRedstoneSignalStrength() > 0;
        }
    }

    public int getRedstoneSignalStrength() {
        return this.linkMode >= 0 ? this.redstoneSignalStrength : 0;
    }

    public void setOverride(boolean override) {
        if (override) {
            this.linkMode = -2;
            this.setRedstoneSignalStrength(0);
        } else {
            this.linkMode = 0;
        }

        this.markForUpdate();
    }

    public boolean isOverridden() {
        return this.linkMode == -2;
    }

    protected void setRedstoneSignalStrength(int strength) {
        if (this.redstoneSignalStrength != strength) {
            this.redstoneSignalStrength = strength;
            this.markForUpdate();
            NeighborNotifier.notifyBlocksOfExtendedNeighborChange(this.world, this.pos);
        }

    }

    @Override
    public void update() {
        if (!this.world.isRemote && this.linkMode >= 0) {
            if (this.linkMode == 0) {
                WorldSpecificCoordinates coord = this.getLinkLocation();
                if (!this.coordinatesAreInRange(coord)) {
                    this.linkMode = -1;
                    this.setRedstoneSignalStrength(0);
                    this.markForUpdate();
                    return;
                }

                this.linkX = coord.x;
                this.linkY = coord.y;
                this.linkZ = coord.z;
                this.linkMode = 1;
                this.markForUpdate();
                this.cooldown = 0;
            } else {
                if (this.cooldown > 0) {
                    --this.cooldown;
                    return;
                }

                this.cooldown = COOLDOWN_TIME;
            }

            Block block = this.getBlockAtLinkedLocationIfValid();
            if (block == null) {
                this.setRedstoneSignalStrength(0);
            } else {
                int strength = getComparatorReading(block, this.world, new BlockPos(this.linkX, this.linkY, this.linkZ));
                this.setRedstoneSignalStrength(strength);
            }

        }
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int slot, int num) {
        ItemStack stack = super.decrStackSize(slot, num);
        if (this.linkMode == -2) {
            this.world.notifyBlockUpdate(this.pos.offset(EnumFacing.DOWN), this.world.getBlockState(pos), this.world.getBlockState(pos), 3);
        } else {
            this.linkMode = 0;
            this.markForUpdate();
        }

        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack stack) {
        super.setInventorySlotContents(slot, stack);
        if (this.linkMode == -2) {
            this.world.notifyBlockUpdate(this.pos.offset(EnumFacing.DOWN), this.world.getBlockState(pos), this.world.getBlockState(pos), 3);
        } else {
            this.linkMode = 0;
            this.markForUpdate();
        }

    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof IAutomagyLocationLink && ((IAutomagyLocationLink)stack.getItem()).getLinkLocation(stack) != null;
    }

    @Override
    public void readCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.readCommonNBT(nbttagcompound);
        this.redstoneSignalStrength = nbttagcompound.getShort("strength");
        this.linkMode = nbttagcompound.getShort("linkMode");
        this.linkX = nbttagcompound.getInteger("linkX");
        this.linkY = nbttagcompound.getInteger("linkY");
        this.linkZ = nbttagcompound.getInteger("linkZ");
    }

    @Override
    public void writeCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.writeCommonNBT(nbttagcompound);
        nbttagcompound.setShort("strength", (short)this.redstoneSignalStrength);
        nbttagcompound.setShort("linkMode", (short)this.linkMode);
        nbttagcompound.setInteger("linkX", this.linkX);
        nbttagcompound.setInteger("linkY", this.linkY);
        nbttagcompound.setInteger("linkZ", this.linkZ);
    }
}
