package tuhljin.automagy.common.gui;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import tuhljin.automagy.common.tiles.TileTallyBase;

import javax.annotation.Nonnull;

public class ContainerTallyBox extends ContainerWithSingleFilter<TileTallyBase> {
    private boolean oldRequireAllMatches = true;

    public ContainerTallyBox(TileTallyBase tile, InventoryPlayer invPlayer) {
        super(tile, invPlayer);
    }

    @Override
    public void addListener(@Nonnull IContainerListener player) {
        super.addListener(player);
        player.sendWindowProperty(this, 0, this.tile.requireAllMatches ? 1 : 0);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0) {
            this.tile.requireAllMatches = (data == 1);
        }
        this.sendDataToGui(id, data);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener player : this.listeners) {
            if (this.tile.requireAllMatches != this.oldRequireAllMatches) {
                player.sendWindowProperty(this, 0, this.tile.requireAllMatches ? 1 : 0);
            }
        }

        this.oldRequireAllMatches = this.tile.requireAllMatches;
    }

    @Override
    public boolean enchantItem(EntityPlayer player, int id) {
        this.tile.setRequireAllMatches(id != 1);
        return true;
    }
}
