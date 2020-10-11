package tuhljin.automagy.common.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.inventory.FilterHelper;
import tuhljin.automagy.common.lib.inventory.FilteringItemList;
import tuhljin.automagy.common.lib.inventory.IContainsFilter;
import tuhljin.automagy.common.lib.inventory.InventoryObjectFilter;
import tuhljin.automagy.common.lib.inventory.InventoryWithFilterOptions;
import tuhljin.automagy.common.lib.inventory.ItemHandlerUtil;
import tuhljin.automagy.common.lib.inventory.SizelessItem;

import javax.annotation.Nonnull;

public class TileMawSpitting extends TileMawBase implements IContainsFilter, ITickable {
    public static int CHECK_INTERVAL = 10;
    public boolean redSense;
    protected InventoryObjectFilter invFilter;
    private int ticksUntilCheck;

    public TileMawSpitting() {
        this.ticksUntilCheck = CHECK_INTERVAL;
        this.invFilter = new InventoryObjectFilter("Maw Filter", 1);
    }

    @Override
    public void setWorld(@Nonnull World world) {
        if (world.rand != null) {
            this.ticksUntilCheck = 20 + world.rand.nextInt(20);
        }

        super.setWorld(world);
    }

    public InventoryObjectFilter getFilterInventory() {
        return this.invFilter;
    }

    @Override
    public void update() {
        if (!this.world.isRemote) {
            --this.ticksUntilCheck;
            if (this.ticksUntilCheck < 1) {
                this.ticksUntilCheck = CHECK_INTERVAL;
                if (!this.blockedBySignal()) {
                    EnumFacing facing = this.getFacing();
                    TileEntity te = this.world.getTileEntity(this.pos.offset(facing));
                    IItemHandler itemHandler = ItemHandlerUtil.getHandler(te, facing.getOpposite());
                    if (itemHandler != null) {
                        this.expelExtraItems(itemHandler);
                    } else {
                        this.getBlockType().neighborChanged(this.world.getBlockState(this.pos), this.world, this.pos, this.getBlockType(), this.pos);
                    }
                }
            }

        }
    }

    public void expelExtraItems(@Nonnull IItemHandler handler) {
        InventoryWithFilterOptions iwfo = this.invFilter.getFilter(0);
        FilteringItemList wanted = new FilteringItemList();

        for(int slot = 0; slot < handler.getSlots(); ++slot) {
            ItemStack stack = handler.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                stack = stack.copy();
                int amtWanted;
                if (iwfo == null) {
                    amtWanted = 0;
                } else {
                    amtWanted = Math.min(stack.getCount(), FilterHelper.getMaxAllowedAfterFilter(stack, this.invFilter.getFilter(0), this.invFilter.isBlacklist(0), wanted));
                }

                if (amtWanted == stack.getCount()) {
                    wanted.add(stack);
                } else {
                    if (amtWanted < 0) {
                        amtWanted = 0;
                    }

                    ItemStack extracted = handler.extractItem(slot, stack.getCount() - amtWanted, false);
                    if (!extracted.isEmpty()) {
                        this.spawnItem(extracted);
                        if (stack.getCount() - extracted.getCount() > 0) {
                            wanted.add(new SizelessItem(stack), stack.getCount() - extracted.getCount());
                        }
                    } else {
                        wanted.add(stack);
                    }
                }
            }
        }

    }

    public void spawnItem(@Nonnull ItemStack stack) {
        EnumFacing facing = this.getFacing().getOpposite();
        double x = this.getPos().getX() + 0.5D - facing.getXOffset() * 0.3D;
        double y = this.getPos().getY() + 0.25D - facing.getYOffset() * 0.3D;
        double z = this.getPos().getZ() + 0.5D - facing.getZOffset() * 0.3D;
        double motionX = facing.getXOffset() * 0.15F;
        double motionY = facing.getYOffset() * 0.15F;
        double motionZ = facing.getZOffset() * 0.15F;
        TjUtil.dropItemIntoWorldWithMotion(stack, this.world, x, y, z, motionX, motionY, motionZ);
    }

    public boolean isRedstoneSensitive() {
        return this.redSense;
    }

    public void setRedstoneSensitive(boolean enabled) {
        this.redSense = enabled;
        this.markDirty();
    }

    public void writeServerNBT(@Nonnull NBTTagCompound nbttagcompound) {
        this.invFilter.writeCustomNBT(nbttagcompound);
        nbttagcompound.setBoolean("redSense", this.redSense);
    }

    public void readServerNBT(@Nonnull NBTTagCompound nbttagcompound) {
        this.invFilter.readCustomNBT(nbttagcompound);
        this.redSense = nbttagcompound.getBoolean("redSense");
    }
}

