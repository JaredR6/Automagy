package tuhljin.automagy.client.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import tuhljin.automagy.common.gui.ContainerWithFilter;
import tuhljin.automagy.common.gui.IReporteeForSlot;
import tuhljin.automagy.common.gui.SlotRestrictedWithReporting;
import tuhljin.automagy.common.items.ItemFilter;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.inventory.InventoryWithFilterOptions;

public class GuiWithSingleFilter<T extends TileEntity> extends ModGuiContainerAttachable<T> implements IReporteeForSlot {
    public final ResourceLocation texture;
    protected boolean listInstalled;
    protected boolean filterUseItemCount;
    protected boolean filterIsBlacklist;

    public GuiWithSingleFilter(ContainerWithFilter<T> container) {
        super(container);
        this.texture = new ResourceLocation(References.GUI_FILTERHOLDER);
        this.listInstalled = false;
        this.filterUseItemCount = false;
        this.filterIsBlacklist = false;
    }

    public void onSlotChanged(SlotRestrictedWithReporting slot) {
        ItemStack stack = slot.getStack();
        if (!stack.isEmpty()) {
            this.listInstalled = true;
            this.filterIsBlacklist = ItemFilter.stackIsBlacklist(stack);
            InventoryWithFilterOptions inv = ItemFilter.getFilterInventory(stack);
            if (inv != InventoryWithFilterOptions.NULL_FILTER) {
                this.filterUseItemCount = inv.useItemCount;
                return;
            }
        } else {
            this.listInstalled = false;
        }

        this.filterUseItemCount = false;
    }

    public void receiveContainerUpdate(int id, int data) {
    }
}
