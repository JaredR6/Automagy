package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

public class ModContainer<T extends TileEntity> extends Container {
    public T tile;
    public InventoryPlayer invPlayer;

    public ModContainer(T tile, InventoryPlayer invPlayer) {
        this.tile = tile;
        this.invPlayer = invPlayer;
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        if (this.tile.isInvalid()) {
            return false;
        } else if (this.tile instanceof IInventory) {
            return ((IInventory)this.tile).isUsableByPlayer(player);
        } else {
            return player.getDistanceSqToCenter(this.tile.getPos()) <= 64.0D;
        }
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return ItemStack.EMPTY;
    }

    protected void addPlayerInventorySlots(int offsetY) {
        int i;
        for(i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(this.invPlayer, i, 8 + i * 18, 142 + offsetY));
        }

        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(this.invPlayer, 9 + j + i * 9, 8 + j * 18, 84 + i * 18 + offsetY));
            }
        }

    }
}
