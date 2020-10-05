package tuhljin.automagy.client.gui;

import net.minecraft.tileentity.TileEntity;
import tuhljin.automagy.common.gui.IGuiAttachable;
import tuhljin.automagy.common.gui.ModContainerAttached;

import javax.annotation.Nonnull;

public abstract class ModGuiContainerAttachable<T extends TileEntity> extends ModGuiContainer implements IGuiAttachable {
    public T tile;

    public ModGuiContainerAttachable(@Nonnull ModContainerAttached<T> container) {
        super(container);
        container.attachGui(this);
        this.tile = container.tile;
    }
}
