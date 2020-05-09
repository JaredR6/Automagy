package tuhljin.automagy.common.lib.compat.jei;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.gui.IGuiIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import tuhljin.automagy.common.gui.ContainerRecipe;
import tuhljin.automagy.common.network.MessageGUIRecipe;

import javax.annotation.Nonnull;

public class GhostRecipeTransferHandler implements IRecipeTransferHandler {
    public GhostRecipeTransferHandler() {
    }

    @Nonnull
    @Override
    public Class<? extends Container> getContainerClass() {
        return ContainerRecipe.class;
    }

    public String getRecipeCategoryUid() {
        return "minecraft.crafting";
    }

    public IRecipeTransferError transferRecipe(@Nonnull Container container, @Nonnull IRecipeLayout recipeLayout, @Nonnull EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
        if (doTransfer) {
            boolean found = false;
            ItemStack[] recipe = new ItemStack[9];
            Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients = recipeLayout.getItemStacks().getGuiIngredients();

            for (Entry<Integer, ? extends IGuiIngredient<ItemStack>> integerEntry : ingredients.entrySet()) {
                int slot = integerEntry.getKey() - 1;
                if (slot >= 0) {
                    IGuiIngredient<ItemStack> items = integerEntry.getValue();
                    if (items != null) {
                        List<ItemStack> list = items.getAllIngredients();
                        if (!list.isEmpty()) {
                            recipe[slot] = list.get(0);
                            found = true;
                        }
                    }
                }
            }

            if (found) {
                MessageGUIRecipe.sendToServer(recipe);
            }
        }

        return null;
    }
}