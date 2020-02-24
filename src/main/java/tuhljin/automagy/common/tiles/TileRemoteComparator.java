package tuhljin.automagy.common.tiles;

import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import tuhljin.automagy.common.blocks.IRemoteComparatorOverride;
import tuhljin.automagy.common.items.IAutomagyLocationLink;
import tuhljin.automagy.common.lib.NeighborNotifier;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.struct.WorldSpecificCoordinates;

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

    public static boolean isValidContainerForReading(Block block, World world, BlockPos posIn) {
        if (block.func_149740_M()) {
            return true;
        } else {
            TileEntity te = world.func_175625_s(posIn);
            return te instanceof IInventory;
        }
    }

    public static int getComparatorReading(Block block, World world, BlockPos posIn) {
        if (block.func_149740_M()) {
            return block.func_180641_l(world, posIn);
        } else {
            TileEntity te = world.func_175625_s(posIn);
            return te instanceof IInventory ? Container.func_94526_b((IInventory)te) : 0;
        }
    }

    public WorldSpecificCoordinates getLinkLocation() {
        ItemStack stack = this.func_70301_a(0);
        return stack != null && stack.func_77973_b() instanceof IAutomagyLocationLink ? ((IAutomagyLocationLink)stack.func_77973_b()).getLinkLocation(stack) : null;
    }

    public boolean coordinatesAreInRange(WorldSpecificCoordinates coord) {
        if (coord != null && coord.dim == this.field_145850_b.field_73011_w.func_177502_q()) {
            float distance = TjUtil.getDistanceBetweenPoints(this.field_174879_c.func_177958_n(), this.field_174879_c.func_177956_o(), this.field_174879_c.func_177952_p(), coord.x, coord.y, coord.z);
            return distance <= 20.0F;
        } else {
            return false;
        }
    }

    public boolean coordinatesAreInRange() {
        return this.coordinatesAreInRange(this.getLinkLocation());
    }

    public Block getBlockAtLinkedLocationIfValid() {
        WorldSpecificCoordinates coord = this.getLinkLocation();
        if (coord != null && coord.dim == this.field_145850_b.field_73011_w.func_177502_q()) {
            BlockPos linkPos = coord.toBlockPos();
            Block block = TjUtil.getBlock(this.field_145850_b, linkPos);
            if (isValidContainerForReading(block, this.field_145850_b, linkPos)) {
                return block;
            }
        }

        return null;
    }

    public ItemStack getFloatingDisplayItem() {
        return this.func_70301_a(0);
    }

    public boolean isRedstoneSignalBeingSent() {
        if (this.linkMode == -2) {
            BlockPos downPos = this.field_174879_c.func_177972_a(EnumFacing.DOWN);
            Block block = TjUtil.getBlock(this.field_145850_b, downPos);
            if (block == null) {
                return false;
            } else if (block instanceof IRemoteComparatorOverride) {
                return ((IRemoteComparatorOverride)block).hasActiveRedstoneSignal(this.field_145850_b, downPos, this);
            } else {
                return block.func_180656_a(this.field_145850_b, downPos, this.field_145850_b.func_180495_p(downPos), EnumFacing.NORTH) > 0;
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
            NeighborNotifier.notifyBlocksOfExtendedNeighborChange(this.field_145850_b, this.field_174879_c, this.func_145838_q());
        }

    }

    public void func_73660_a() {
        if (!this.field_145850_b.field_72995_K && this.linkMode >= 0) {
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

                this.cooldown = 4;
            }

            Block block = this.getBlockAtLinkedLocationIfValid();
            if (block == null) {
                this.setRedstoneSignalStrength(0);
            } else {
                int strength = 0;

                try {
                    strength = getComparatorReading(block, this.field_145850_b, new BlockPos(this.linkX, this.linkY, this.linkZ));
                } catch (Exception var4) {
                }

                this.setRedstoneSignalStrength(strength);
            }

        }
    }

    public ItemStack func_70298_a(int slot, int num) {
        ItemStack stack = super.func_70298_a(slot, num);
        if (this.linkMode == -2) {
            this.field_145850_b.func_180496_d(this.field_174879_c.func_177972_a(EnumFacing.DOWN), this.func_145838_q());
        } else {
            this.linkMode = 0;
            this.markForUpdate();
        }

        return stack;
    }

    public void func_70299_a(int slot, ItemStack stack) {
        super.func_70299_a(slot, stack);
        if (this.linkMode == -2) {
            this.field_145850_b.func_180496_d(this.field_174879_c.func_177972_a(EnumFacing.DOWN), this.func_145838_q());
        } else {
            this.linkMode = 0;
            this.markForUpdate();
        }

    }

    public int func_70297_j_() {
        return 1;
    }

    public boolean func_94041_b(int slot, ItemStack stack) {
        return stack != null && stack.func_77973_b() instanceof IAutomagyLocationLink && ((IAutomagyLocationLink)stack.func_77973_b()).getLinkLocation(stack) != null;
    }

    public void readCommonNBT(NBTTagCompound nbttagcompound) {
        super.readCommonNBT(nbttagcompound);
        this.redstoneSignalStrength = nbttagcompound.func_74765_d("strength");
        this.linkMode = nbttagcompound.func_74765_d("linkMode");
        this.linkX = nbttagcompound.func_74762_e("linkX");
        this.linkY = nbttagcompound.func_74762_e("linkY");
        this.linkZ = nbttagcompound.func_74762_e("linkZ");
    }

    public void writeCommonNBT(NBTTagCompound nbttagcompound) {
        super.writeCommonNBT(nbttagcompound);
        nbttagcompound.func_74777_a("strength", (short)this.redstoneSignalStrength);
        nbttagcompound.func_74777_a("linkMode", (short)this.linkMode);
        nbttagcompound.func_74768_a("linkX", this.linkX);
        nbttagcompound.func_74768_a("linkY", this.linkY);
        nbttagcompound.func_74768_a("linkZ", this.linkZ);
    }
}
