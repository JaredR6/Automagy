package tuhljin.automagy.common.lib.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import tuhljin.automagy.client.gui.GuiRecipe;
import tuhljin.automagy.common.blocks.ModBlocks;
import tuhljin.automagy.common.lib.References;

import javax.annotation.Nonnull;

@JEIPlugin
public class AutomagyJEIPlugin implements IModPlugin {

    public void register(@Nonnull IModRegistry registry) {
        IJeiHelpers helper = registry.getJeiHelpers();
        helper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.torchInversion_off));
        helper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.specialProcess));
        registry.addRecipeClickArea(GuiRecipe.class, 84, 37, 22, 15, "minecraft.crafting");
        IRecipeTransferRegistry rtr = registry.getRecipeTransferRegistry();
        rtr.addRecipeTransferHandler(new GhostRecipeTransferHandler(), References.MOD_ID);
    }
}