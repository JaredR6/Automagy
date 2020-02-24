package tuhljin.automagy.common.lib.inventory;

import java.util.regex.Pattern;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import tuhljin.automagy.common.items.ItemFilter;

import javax.annotation.Nonnull;

public class InventoryObjectFilter extends InventorySimple {
    protected NonNullList<InventoryWithFilterOptions> filter;
    protected boolean[] blacklist;

    public InventoryObjectFilter(String name, int numSlots) {
        super(name, numSlots, "FilterItems");
        this.filter = NonNullList.withSize(numSlots, InventoryWithFilterOptions.NULL_FILTER);
        this.blacklist = new boolean[numSlots];
        this.notifyOnInventoryChanged = true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
        return ItemFilter.isItemPopulatedFilter(stack);
    }

    @Override
    public void onInventoryChanged(int slot, ItemStack prevStack) {
        this.filter.set(slot, InventoryWithFilterOptions.NULL_FILTER);
        this.blacklist[slot] = ItemFilter.stackIsBlacklist(this.getStackInSlot(slot));
    }

    public InventoryWithFilterOptions getFilter(int slot) {
        if (this.filter.get(slot) == InventoryWithFilterOptions.NULL_FILTER) {
            ItemStack stack = this.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                this.filter.set(slot, ItemFilter.getFilterInventory(stack));
            }
        }
        return this.filter.get(slot);
    }

    public boolean isBlacklist(int slot) {
        return this.blacklist[slot];
    }

    public Pattern getPattern(int slot) {
        InventoryWithFilterOptions invF = this.getFilter(slot);
        return invF == InventoryWithFilterOptions.NULL_FILTER ? null : invF.getPattern();
    }

    public boolean nameFilterMatches(int slot, ItemStack stack) {
        InventoryWithFilterOptions invF = this.getFilter(slot);
        return invF.isNull() && invF.nameFilterMatches(stack);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);

        for(int slot = 0; slot < this.getSizeInventory(); ++slot) {
            this.filter.set(slot, InventoryWithFilterOptions.NULL_FILTER);
            this.blacklist[slot] = ItemFilter.stackIsBlacklist(this.getStackInSlot(slot));
        }

    }
}
