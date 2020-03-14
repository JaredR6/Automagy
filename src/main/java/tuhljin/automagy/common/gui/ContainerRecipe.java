package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.common.container.slot.SlotGhost;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.items.ItemRecipe;
import tuhljin.automagy.common.lib.inventory.InventoryForRecipe;

import javax.annotation.Nonnull;

public class ContainerRecipe extends Container {
    public final int blockedSlotID;
    @Nonnull ItemStack modifyingStack = ItemStack.EMPTY;
    private int productSlotID;
    public InventoryForRecipe recipeInventory;

    public ContainerRecipe(EntityPlayer player) {
        InventoryPlayer inventoryPlayer = player.inventory;
        this.blockedSlotID = inventoryPlayer.currentItem;

        int i;
        for(i = 0; i < 9; ++i) {
            Slot slot = new Slot(inventoryPlayer, i, 8 + i * 18, 156);
            this.addSlotToContainer(slot);
            if (i == this.blockedSlotID) {
                ItemStack stack = slot.getStack();
                if (ItemRecipe.stackIsRecipe(stack)) {
                    this.modifyingStack = stack;
                }
            }
        }

        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryPlayer, 9 + j + i * 9, 8 + j * 18, 98 + i * 18));
            }
        }

        this.recipeInventory = ItemRecipe.getRecipeInventory(this.modifyingStack, player.getEntityWorld());
        if (this.recipeInventory != null) {
            for(i = 0; i < 9; ++i) {
                this.addSlotToContainer(new SlotGhost(this.recipeInventory, i, 18 + i % 3 * 19, 18 + i / 3 * 19, 1));
            }

            Slot slot = this.addSlotToContainer(new SlotGhost(this.recipeInventory, 9, 122, 37));
            this.productSlotID = slot.slotNumber;
        }

    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return !this.modifyingStack.isEmpty() && ItemStack.areItemStacksEqual(this.modifyingStack, player.inventory.getStackInSlot(this.blockedSlotID));
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slot, int dragType, ClickType clickType, EntityPlayer player) {
        if (slot != this.blockedSlotID && slot != this.productSlotID) {
            ItemStack stack = super.slotClick(slot, dragType, clickType, player);
            if (slot >= this.productSlotID - 9 && slot < this.productSlotID) {
                this.recipeInventory.getCraftingResult();
            }
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!player.world.isRemote && !this.modifyingStack.isEmpty() && this.recipeInventory != null) {
            boolean properActiveStack = ItemStack.areItemStacksEqual(this.modifyingStack, player.inventory.getStackInSlot(this.blockedSlotID));
            this.recipeInventory.saveToItemStack(this.modifyingStack);
            if (properActiveStack) {
                player.inventory.setInventorySlotContents(this.blockedSlotID, this.modifyingStack);
            }
        }

    }

    public void receiveMessageFromClient(ItemStack[] stacks) {
        if (stacks.length != 9) {
            Automagy.logError("ContainerFilter received invalid packet data. Ignoring.");
        } else {
            int i = 0;

            for(int slot = this.productSlotID - 9; slot < this.productSlotID; ++slot) {
                this.getSlot(slot).putStack(stacks[i]);
                ++i;
            }

            this.recipeInventory.getCraftingResult();
        }
    }
}
