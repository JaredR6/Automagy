package tuhljin.automagy.client.gui;

import net.minecraft.tileentity.TileEntity;
import tuhljin.automagy.common.gui.IGuiAttachable;
import tuhljin.automagy.common.gui.ModContainerAttached;

public abstract class ModGuiContainerAttachable<T extends TileEntity> extends ModGuiContainer implements IGuiAttachable {
    public T tile;

    public ModGuiContainerAttachable(ModContainerAttached<T> container) {
        super(container);
        container.attachGui(this);
        this.tile = container.tile;
    }
}
