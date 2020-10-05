package tuhljin.automagy.common.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import javax.annotation.Nullable;
import tuhljin.automagy.common.items.ItemFilter;
import tuhljin.automagy.common.items.ItemFilterBlack;
import tuhljin.automagy.common.lib.inventory.InventoryWithFilterOptions;
import tuhljin.automagy.common.lib.inventory.SizelessItem;

import javax.annotation.Nonnull;

public class ModTileEntityWithFilterMainInventory extends ModTileEntityWithInventory {
    @Nullable
    protected InventoryWithFilterOptions filter;
    protected boolean filterIsBlacklist;

    public ModTileEntityWithFilterMainInventory(String inventoryName, boolean createDefaultAccessibleSlots) {
        super(inventoryName, 1, false, createDefaultAccessibleSlots);
        this.filter = InventoryWithFilterOptions.NULL_FILTER;
        this.notifyOnInventoryChanged = true;
    }

    public ModTileEntityWithFilterMainInventory(String inventoryName) {
        this(inventoryName, true);
    }

    public boolean nameFilterMatches(@Nonnull ItemStack stack) {
        return this.filter != InventoryWithFilterOptions.NULL_FILTER && this.filter.nameFilterMatches(stack);
    }

    public boolean nameFilterMatches(@Nonnull SizelessItem item) {
        return this.nameFilterMatches(item.getItemStack(1));
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
    public void onInventoryChanged(int slot) {
        ItemStack stack = this.getStackInSlot(slot);
        if (stack.isEmpty()) {
            this.filter = InventoryWithFilterOptions.NULL_FILTER;
        } else {
            this.filter = ItemFilter.getFilterInventory(stack);
            if (this.filter != InventoryWithFilterOptions.NULL_FILTER) {
                this.filterIsBlacklist = (stack.getItem() instanceof ItemFilterBlack);
            }
        }

    }

    @Override
    public void readServerNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.readServerNBT(nbttagcompound);
        ItemStack filterStack = this.getStackInSlot(0);
        if (filterStack.isEmpty()) {
            this.filter = null;
        } else {
            this.filter = ItemFilter.getFilterInventory(filterStack);
            if (this.filter != InventoryWithFilterOptions.NULL_FILTER) {
                this.filterIsBlacklist = (filterStack.getItem() instanceof ItemFilterBlack);
            }
        }

    }
}
