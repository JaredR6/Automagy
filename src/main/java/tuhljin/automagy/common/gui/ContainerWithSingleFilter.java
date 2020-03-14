package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import tuhljin.automagy.common.lib.inventory.IContainsFilter;

public class ContainerWithSingleFilter<T extends TileEntity> extends ContainerWithFilter<T> {
    protected SlotRestrictedWithReporting filterSlot;

    public ContainerWithSingleFilter(T tile, InventoryPlayer invPlayer) {
        super(tile, invPlayer);
        this.addPlayerInventorySlots(0);
        Object inv;
        if (tile instanceof IContainsFilter) {
            inv = ((IContainsFilter)tile).getFilterInventory();
        } else {
            inv = tile;
        }

        this.filterSlot = new SlotRestrictedWithReporting((IInventory)inv, 0, 80, 28);
        this.addSlotToContainer(this.filterSlot);
    }

    @Override
    public void attachGui(IGuiAttachable gui) {
        super.attachGui(gui);
        this.filterSlot.setReportee((IReporteeForSlot)gui);
    }
}
