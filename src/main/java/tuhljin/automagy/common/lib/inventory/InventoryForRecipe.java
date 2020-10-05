package tuhljin.automagy.common.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import javax.annotation.Nullable;

import javax.annotation.Nonnull;

public class InventoryForRecipe extends InventoryForItem {
    @Nullable
    private FilteringItemList list = null;
    @Nullable
    private InventoryCraftingCachedResult craftInv = null;
    private World worldObj;
    private boolean craftInvUpdated = false;

    public InventoryForRecipe(@Nonnull ItemStack containerStack, String inventoryName, int numSlots, World worldObj) {
        super(containerStack, inventoryName, numSlots + 1);
        this.notifyOnInventoryChanged = true;
        this.worldObj = worldObj;
    }

    public void onInventoryChanged(int slot, ItemStack prevStack) {
        this.list = null;
        this.craftInvUpdated = false;
    }

    @Nullable
    public IItemMap getIngredientItemMap() {
        if (this.list == null) {
            this.list = (new FilteringItemList()).populateFromInventory(this, false, 0, this.numSlots - 2);
        }

        return this.list;
    }

    public ItemStack getCraftingResult() {
        if (!this.craftInvUpdated) {
            if (this.craftInv == null) {
                this.craftInv = new InventoryCraftingCachedResult(this.nbtKey + "CraftingInv");
                this.craftInv.setWorld(this.worldObj);
            }

            for(int i = 0; i < this.numSlots - 1; ++i) {
                this.craftInv.setInventorySlotContents(i, this.inventorySlots.get(i));
            }

            this.craftInvUpdated = true;
        }

        ItemStack result = this.craftInv.getCraftingResult();
        this.setInventorySlotContentsSoftly(this.numSlots - 1, result);
        return result;
    }

    @Nullable
    public InventoryCraftingCachedResult getCraftingInventory() {
        return this.craftInv;
    }
}
