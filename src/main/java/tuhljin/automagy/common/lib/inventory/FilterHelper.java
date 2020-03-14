package tuhljin.automagy.common.lib.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class FilterHelper {
    public FilterHelper() {
    }

    public static int getMaxAllowedAfterFilter(ItemStack queryStack, InventoryWithFilterOptions filter, boolean isBlacklist, IItemHandler destHandler) {
        return getMaxAllowedAfterFilter(queryStack, filter, isBlacklist, destHandler, null);
    }

    public static int getMaxAllowedAfterFilter(ItemStack queryStack, InventoryWithFilterOptions filter, boolean isBlacklist, FilteringItemList destList) {
        return getMaxAllowedAfterFilter(queryStack, filter, isBlacklist, null, destList);
    }

    protected static int getMaxAllowedAfterFilter(ItemStack queryStack, InventoryWithFilterOptions filter, boolean isBlacklist, IItemHandler destHandler, FilteringItemList destList) {
        ItemStack stack = null;
        boolean found = false;
        Item qItem = queryStack.getItem();

        int qtyNeighborTotal;
        for(qtyNeighborTotal = 0; qtyNeighborTotal < filter.getSizeInventory(); ++qtyNeighborTotal) {
            stack = filter.getStackInSlot(qtyNeighborTotal);
            if (!stack.isEmpty() && stack.getItem() == qItem && (filter.ignoreMetadata || stack.getItemDamage() == queryStack.getItemDamage())) {
                if (filter.ignoreNBT) {
                    found = true;
                    break;
                }

                if (ItemStack.areItemStackTagsEqual(stack, queryStack)) {
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            if (!filter.getNameFilter().isEmpty() && filter.nameFilterMatches(queryStack)) {
                return isBlacklist ? 0 : Integer.MAX_VALUE;
            } else {
                return isBlacklist ? Integer.MAX_VALUE : 0;
            }
        } else if (!filter.useItemCount) {
            return isBlacklist ? 0 : Integer.MAX_VALUE;
        } else if (isBlacklist) {
            qtyNeighborTotal = queryStack.getCount();
            return qtyNeighborTotal - stack.getCount();
        } else {
            if (destList == null) {
                destList = (new FilteringItemList()).populateFromInventory(destHandler);
            }

            SizelessItem item = new SizelessItem(queryStack);
            int qtyHere = destList.get(item, filter.ignoreMetadata, filter.ignoreNBT);
            return stack.getCount() - qtyHere;
        }
    }
}