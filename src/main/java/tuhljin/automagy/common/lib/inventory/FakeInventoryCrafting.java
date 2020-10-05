package tuhljin.automagy.common.lib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import tuhljin.automagy.common.gui.ContainerEmpty;

import javax.annotation.Nonnull;

public class FakeInventoryCrafting extends InventoryCrafting {
    public FakeInventoryCrafting() {
        super(new ContainerEmpty(), 3, 3);
    }

    public FakeInventoryCrafting(@Nonnull IInventory copyInv) {
        this();

        assert copyInv.getSizeInventory() >= 9;

        for(int i = 0; i < 9; ++i) {
            this.setInventorySlotContents(i, copyInv.getStackInSlot(i));
        }

    }
}
