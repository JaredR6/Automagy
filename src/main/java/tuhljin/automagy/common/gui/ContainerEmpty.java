package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import javax.annotation.Nonnull;

public class ContainerEmpty extends Container {
    public ContainerEmpty() {
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return false;
    }
}