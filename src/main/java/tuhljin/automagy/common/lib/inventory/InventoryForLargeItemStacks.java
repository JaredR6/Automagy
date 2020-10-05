package tuhljin.automagy.common.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tuhljin.automagy.common.lib.TjUtil;

import javax.annotation.Nonnull;

public class InventoryForLargeItemStacks extends InventoryForItem {
    private final int limit;

    public InventoryForLargeItemStacks(@Nonnull ItemStack containerStack, String inventoryName, int numSlots, int limit) {
        super(containerStack, inventoryName, numSlots);
        this.limit = limit;
    }

    @Nonnull
    @Override
    protected ItemStack loadItemStack(@Nonnull NBTTagCompound nbt) {
        return TjUtil.readLargeItemStackFromNBT(nbt);
    }

    @Override
    protected void writeItemStack(@Nonnull ItemStack stack, @Nonnull NBTTagCompound nbt) {
        TjUtil.writeLargeItemStackToNBT(stack, nbt);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.limit;
    }
}
