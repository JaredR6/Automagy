package tuhljin.automagy.common.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryForItem extends InventorySimple {
    public InventoryForItem(ItemStack containerStack, String inventoryName, int numSlots) {
        super(inventoryName, numSlots);
        if (!containerStack.isEmpty() && containerStack.hasTagCompound()) {
            this.readCustomNBT(containerStack.getTagCompound());
        }

    }

    public void saveToItemStack(ItemStack stack) {
        boolean hasTagCompound = stack.hasTagCompound();
        NBTTagCompound nbttagcompound = hasTagCompound ? stack.getTagCompound() : new NBTTagCompound();
        this.writeCustomNBT(nbttagcompound);
        if (!hasTagCompound) {
            stack.setTagCompound(nbttagcompound);
        }

    }
}
