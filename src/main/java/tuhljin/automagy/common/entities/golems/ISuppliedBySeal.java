package tuhljin.automagy.common.entities.golems;

import net.minecraft.item.ItemStack;
import tuhljin.automagy.common.lib.inventory.IncomingItemsTracker;

import javax.annotation.Nonnull;

public interface ISuppliedBySeal {
    IncomingItemsTracker getSupplyTracker();

    @Nonnull
    ItemStack receiveSupplyFromTracker(ItemStack var1);

    boolean hasInventorySpaceFor(ItemStack var1);
}