package tuhljin.automagy.common.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import javax.annotation.Nullable;
import tuhljin.automagy.common.lib.inventory.FilterHelper;
import tuhljin.automagy.common.lib.inventory.IContainsFilter;
import tuhljin.automagy.common.lib.inventory.InventoryObjectFilter;

import javax.annotation.Nonnull;

public class TileMawFinical extends TileMawHungry implements IContainsFilter {
    public boolean redSense = false;
    @Nonnull
    protected InventoryObjectFilter invFilter = new InventoryObjectFilter("Maw Filter", 1);

    public TileMawFinical() {
    }

    @Nonnull
    public InventoryObjectFilter getFilterInventory() {
        return this.invFilter;
    }

    @Nullable
    protected ItemStack depositItem(@Nonnull ItemStack stack, IItemHandler itemHandler) {
        if (this.invFilter.getFilter(0) == null) {
            return super.depositItem(stack, itemHandler);
        } else {
            int amt = Math.min(FilterHelper.getMaxAllowedAfterFilter(stack, this.invFilter.getFilter(0), this.invFilter.isBlacklist(0), itemHandler), stack.getCount());
            if (amt < 1) {
                return stack;
            } else {
                ItemStack depositStack = stack.copy();
                depositStack.setCount(amt);
                int remaining = stack.getCount() - amt;
                ItemStack leftovers = super.depositItem(depositStack, itemHandler);
                if (leftovers != null) {
                    remaining += leftovers.getCount();
                }

                if (remaining < 1) {
                    return null;
                } else {
                    ItemStack newStack = stack.copy();
                    newStack.setCount(remaining);
                    return newStack;
                }
            }
        }
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