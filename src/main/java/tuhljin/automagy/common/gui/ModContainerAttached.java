package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import javax.annotation.Nullable;

public class ModContainerAttached<T extends TileEntity> extends ModContainer<T> {
    @Nullable
    private IGuiAttachable attachedGui = null;

    public ModContainerAttached(T tile, InventoryPlayer invPlayer) {
        super(tile, invPlayer);
    }

    public void attachGui(IGuiAttachable gui) {
        this.attachedGui = gui;
    }

    public void sendDataToGui(int id, int data) {
        if (this.attachedGui != null) {
            this.attachedGui.receiveContainerUpdate(id, data);
        }

    }
}
