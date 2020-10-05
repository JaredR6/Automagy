package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.common.container.slot.SlotGhost;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.items.ItemFilter;
import tuhljin.automagy.common.lib.inventory.InventoryWithFilterOptions;

import javax.annotation.Nonnull;

public class ContainerFilter extends Container {
    public static final int CONTAINER_FILTER_WHITELIST_ID = 2;
    public static final int CONTAINER_FILTER_BLACKLIST_ID = 2;
    public final boolean blacklist;
    public final int blockedSlotID;
    @Nonnull
    private ItemStack modifyingStack = ItemStack.EMPTY;
    public InventoryWithFilterOptions filterInventory;

    public ContainerFilter(boolean isBlacklist, @Nonnull EntityPlayer player) {
        this.blacklist = isBlacklist;
        InventoryPlayer inventoryPlayer = player.inventory;
        this.blockedSlotID = inventoryPlayer.currentItem;

        int i;
        for(i = 0; i < 9; ++i) {
            Slot slot = new Slot(inventoryPlayer, i, 8 + i * 18, 156);
            this.addSlotToContainer(slot);
            if (i == this.blockedSlotID) {
                ItemStack stack = slot.getStack();
                if (!this.blacklist && stack.getItem() instanceof ItemFilter) {
                    this.modifyingStack = stack;
                }
            }
        }

        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryPlayer, 9 + j + i * 9, 8 + j * 18, 98 + i * 18));
            }
        }

        this.filterInventory = ItemFilter.getFilterInventory(this.modifyingStack);
        if (this.filterInventory != InventoryWithFilterOptions.NULL_FILTER) {
            for(i = 0; i < 9; ++i) {
                this.addSlotToContainer(new SlotGhost(this.filterInventory, i, 18 + i % 3 * 19, 18 + i / 3 * 19, 999));
            }
        }

    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return !this.modifyingStack.isEmpty() && ItemStack.areItemStacksEqual(this.modifyingStack, player.inventory.getStackInSlot(this.blockedSlotID));
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slot, int dragType, ClickType clickType, @Nonnull EntityPlayer player) {
        return slot == this.blockedSlotID ? ItemStack.EMPTY : super.slotClick(slot, dragType, clickType, player);
    }

    @Override
    public void onContainerClosed(@Nonnull EntityPlayer player) {
        super.onContainerClosed(player);
        if (!player.world.isRemote && !this.modifyingStack.isEmpty() && this.filterInventory != null) {
            boolean properActiveStack = ItemStack.areItemStacksEqual(this.modifyingStack, player.inventory.getStackInSlot(this.blockedSlotID));
            this.filterInventory.removeDuplicates();
            this.filterInventory.saveToItemStack(this.modifyingStack);
            if (properActiveStack) {
                player.inventory.setInventorySlotContents(this.blockedSlotID, this.modifyingStack);
            }
        }

    }

    public void receiveMessageFromClient(int type, @Nonnull String data) {
        switch(type) {
            case 0:
                this.filterInventory.useItemCount = !data.equals("1");
                break;
            case 1:
                this.filterInventory.ignoreMetadata = data.equals("1");
                break;
            case 2:
                this.filterInventory.ignoreNBT = data.equals("1");
                break;
            case 3:
                this.filterInventory.setNameFilter(data);
                break;
            default:
                Automagy.logWarning("ContainerFilter received invalid packet data. Ignoring. (type=" + type + ")");
        }

    }
}
