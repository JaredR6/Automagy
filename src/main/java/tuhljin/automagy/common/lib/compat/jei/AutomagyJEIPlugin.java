package tuhljin.automagy.common.lib.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import tuhljin.automagy.client.gui.GuiRecipe;
import tuhljin.automagy.common.blocks.ModBlocks;

@JEIPlugin
public class AutomagyJEIPlugin extends IModPlugin {
    // TODO: Remake entire plugin

    public AutomagyJEIPlugin() {
    }

    public void register(IModRegistry registry) {
        IJeiHelpers helper = registry.getJeiHelpers();
        helper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.torchInversion_off));
        helper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.specialProcess));
        registry.addRecipeClickArea(GuiRecipe.class, 84, 37, 22, 15, "minecraft.crafting");
        IRecipeTransferRegistry rtr = registry.getRecipeTransferRegistry();
        rtr.addRecipeTransferHandler(new GhostRecipeTransferHandler());
    }
}