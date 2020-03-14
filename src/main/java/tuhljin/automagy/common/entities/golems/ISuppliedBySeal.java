package tuhljin.automagy.common.entities.golems;

import net.minecraft.item.ItemStack;
import tuhljin.automagy.common.lib.inventory.IncomingItemsTracker;

public interface ISuppliedBySeal {
    IncomingItemsTracker getSupplyTracker();

    ItemStack receiveSupplyFromTracker(ItemStack var1);

    boolean hasInventorySpaceFor(ItemStack var1);
}