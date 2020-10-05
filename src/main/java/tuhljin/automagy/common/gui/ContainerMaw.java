package tuhljin.automagy.common.gui;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import tuhljin.automagy.common.lib.inventory.IContainsFilter;
import tuhljin.automagy.common.tiles.TileMawBase;

import javax.annotation.Nonnull;

public class ContainerMaw<T extends TileMawBase & IContainsFilter & IInventory> extends ContainerWithSingleFilter<T> {
    private boolean oldRedSense = true;

    public ContainerMaw(T tile, InventoryPlayer invPlayer) {
        super(tile, invPlayer);
    }

    @Override
    public void addListener(@Nonnull IContainerListener player) {
        super.addListener(player);
        player.sendWindowProperty(this, 0, this.tile.isRedstoneSensitive() ? 1 : 0);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0) {
            this.tile.setRedstoneSensitive(data == 1);
        }
        this.sendDataToGui(id, data);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener player : this.listeners) {
            if (this.tile.isRedstoneSensitive() != this.oldRedSense) {
                player.sendWindowProperty(this, 0, this.tile.isRedstoneSensitive() ? 1 : 0);
            }
        }

        this.oldRedSense = this.tile.isRedstoneSensitive();
    }

    @Override
    public boolean enchantItem(EntityPlayer playerIn, int id) {
        this.tile.setRedstoneSensitive(id == 1);
        return true;
    }
}
