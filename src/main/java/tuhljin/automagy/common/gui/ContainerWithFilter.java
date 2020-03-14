package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import tuhljin.automagy.common.items.ItemFilter;

import javax.annotation.Nonnull;

public abstract class ContainerWithFilter<T extends TileEntity> extends ModContainerAttached<T> {
    public ContainerWithFilter(T tile, InventoryPlayer invPlayer) {
        super(tile, invPlayer);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotNum);
        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();
            if (slot instanceof SlotRestrictedWithReporting) {
                if (!this.mergeItemStack(stackInSlot, 0, 35, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!ItemFilter.isItemPopulatedFilter(stackInSlot)) {
                    return ItemStack.EMPTY;
                }

                if (!this.mergeItemStack(stackInSlot, 36, this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stackInSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return stack;
    }
}
