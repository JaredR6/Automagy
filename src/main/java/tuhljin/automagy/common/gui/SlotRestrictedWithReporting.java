package tuhljin.automagy.common.gui;

import net.minecraft.inventory.IInventory;

import javax.annotation.Nonnull;

public class SlotRestrictedWithReporting extends SlotRestricted {
    public IReporteeForSlot reportTo;

    public SlotRestrictedWithReporting(@Nonnull IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public SlotRestrictedWithReporting(@Nonnull IInventory inventoryIn, int index, int xPosition, int yPosition, IReporteeForSlot parent) {
        this(inventoryIn, index, xPosition, yPosition);
        this.setReportee(parent);
    }

    public void setReportee(IReporteeForSlot reportee) {
        this.reportTo = reportee;
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        if (this.reportTo != null) {
            this.reportTo.onSlotChanged(this);
        }

    }
}
