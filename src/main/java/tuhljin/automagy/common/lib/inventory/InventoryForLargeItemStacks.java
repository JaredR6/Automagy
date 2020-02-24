package tuhljin.automagy.common.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tuhljin.automagy.common.lib.TjUtil;

public class InventoryForLargeItemStacks extends InventoryForItem {
    private final int limit;

    public InventoryForLargeItemStacks(ItemStack containerStack, String inventoryName, int numSlots, int limit) {
        super(containerStack, inventoryName, numSlots);
        this.limit = limit;
    }

    @Override
    protected ItemStack loadItemStack(NBTTagCompound nbt) {
        return TjUtil.readLargeItemStackFromNBT(nbt);
    }

    @Override
    protected void writeItemStack(ItemStack stack, NBTTagCompound nbt) {
        TjUtil.writeLargeItemStackToNBT(stack, nbt);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.limit;
    }
}
