package tuhljin.automagy.common.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.container.slot.SlotGhost;
import tuhljin.automagy.common.items.ItemRecipe;
import tuhljin.automagy.common.lib.inventory.InventoryForRecipe;

import javax.annotation.Nonnull;

public class SlotGhostRecipe extends SlotGhost {
    private World world;

    public SlotGhostRecipe(IInventory inventoryIn, int index, int xPosition, int yPosition, World world) {
        super(inventoryIn, index, xPosition, yPosition);
        this.world = world;
    }

    public SlotGhostRecipe(@Nonnull Slot slot, World world) {
        this(slot.inventory, slot.getSlotIndex(), slot.xPos, slot.yPos, world);
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            InventoryForRecipe rec = ItemRecipe.getRecipeInventory(stack, this.world);
            if (rec != null) {
                ItemStack product = rec.getCraftingResult();
                if (!product.isEmpty()) {
                    stack = product;
                }
            }
        }

        super.putStack(stack);
    }
}
