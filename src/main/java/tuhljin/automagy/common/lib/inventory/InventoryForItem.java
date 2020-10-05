package tuhljin.automagy.common.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class InventoryForItem extends InventorySimple {
    public InventoryForItem(@Nonnull ItemStack containerStack, String inventoryName, int numSlots) {
        super(inventoryName, numSlots);
        if (!containerStack.isEmpty() && containerStack.hasTagCompound()) {
            this.readCustomNBT(containerStack.getTagCompound());
        }

    }

    public void saveToItemStack(@Nonnull ItemStack stack) {
        boolean hasTagCompound = stack.hasTagCompound();
        NBTTagCompound nbttagcompound = hasTagCompound ? stack.getTagCompound() : new NBTTagCompound();
        this.writeCustomNBT(nbttagcompound);
        if (!hasTagCompound) {
            stack.setTagCompound(nbttagcompound);
        }

    }
}
