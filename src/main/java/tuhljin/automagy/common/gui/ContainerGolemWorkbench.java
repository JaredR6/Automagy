package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tuhljin.automagy.common.items.ItemRecipe;
import tuhljin.automagy.common.tiles.TileGolemWorkbench;

import javax.annotation.Nonnull;

public class ContainerGolemWorkbench extends ModContainerAttached<TileGolemWorkbench> implements IReporteeForSlot {
    private World world;

    public ContainerGolemWorkbench(@Nonnull TileGolemWorkbench tile, @Nonnull InventoryPlayer invPlayer) {
        super(tile, invPlayer);
        this.world = invPlayer.player.world;
        this.addPlayerInventorySlots(39);
        int numRows = 1;
        int i = (numRows - 4) * 18;

        for(int j = 0; j < numRows; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new SlotRestrictedWithReporting(tile, k + j * 9, 8 + k * 18, 86 + j * 18, this));
            }
        }

        this.addSlotToContainer(new SlotRestricted(tile, 9, 62, 9));
        this.addSlotToContainer(new SlotRestricted(tile, 10, 98, 9));
        this.addSlotToContainer(new SlotRestricted(tile, 11, 62, 45));
        this.addSlotToContainer(new SlotRestricted(tile, 12, 98, 45));
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotNum);
        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();
            if (slotNum > 35) {
                if (!this.mergeItemStack(stackInSlot, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (ItemRecipe.isItemPopulatedRecipe(stackInSlot, this.world)) {
                if (!this.mergeItemStack(stackInSlot, 45, this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stackInSlot, 36, this.inventorySlots.size() - 4, false)) {
                return ItemStack.EMPTY;
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

    @Override
    public void onSlotChanged(@Nonnull SlotRestrictedWithReporting slot) {
        if (!this.world.isRemote) {
            this.tile.onInventoryChanged(slot.getSlotIndex());
        }

    }
}
