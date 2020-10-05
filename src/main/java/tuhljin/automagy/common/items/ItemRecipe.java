package tuhljin.automagy.common.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.gui.AutomagyGUIHandler;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.inventory.IItemMap;
import tuhljin.automagy.common.lib.inventory.InventoryForRecipe;
import tuhljin.automagy.common.lib.inventory.SizelessItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemRecipe extends ModItem {
    public static final int NUM_COMPONENT_SLOTS = 9;
    protected static ItemStack examinedStack = ItemStack.EMPTY;
    protected static ArrayList<String> examinedStackResults = new ArrayList<>();

    public ItemRecipe() {
        super(References.ITEM_RECIPE);
        this.setMaxStackSize(1);
    }

    public ItemStack onItemRightClick(ItemStack stack, @Nonnull World world, @Nonnull EntityPlayer player) {
        if (!world.isRemote) {
            AutomagyGUIHandler.openGUI(4, player, world, player.getPosition());
        }

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, ITooltipFlag flag) {
        if (!ItemStack.areItemStacksEqual(stack, examinedStack)) {
            examinedStack = stack;
            examinedStackResults = new ArrayList<>();

            InventoryForRecipe inv = getRecipeInventory(stack, Minecraft.getMinecraft().world);
            if (inv != null) {
                IItemMap map = inv.getIngredientItemMap();
                if (map != null) {

                    String s;
                    for (Entry<SizelessItem, Integer> entry : map) {
                        SizelessItem key = entry.getKey();
                        ItemStack slotStack = key.getItemStack(entry.getValue());
                        s = slotStack.getDisplayName();
                        s = "" + TextFormatting.DARK_AQUA + slotStack.getCount() + " " + TextFormatting.GRAY + s;
                        examinedStackResults.add(s);
                    }

                    ItemStack product = inv.getStackInSlot(NUM_COMPONENT_SLOTS);
                    if (!product.isEmpty()) {
                        String name = product.getDisplayName();
                        String num = TextFormatting.AQUA + "" + product.getCount() + TextFormatting.GRAY;
                        s = I18n.format("automagy.tip.recipe.product", num, name);
                        if (examinedStackResults.isEmpty()) {
                            examinedStackResults.add(s);
                            examinedStackResults.add(TextFormatting.ITALIC + I18n.format("automagy.tip.recipe.incomplete"));
                        } else {
                            examinedStackResults.add(0, s);
                            examinedStackResults.add(1, TextFormatting.ITALIC + I18n.format("automagy.tip.recipe.using"));
                        }
                    } else if (!examinedStackResults.isEmpty()) {
                        examinedStackResults.add(TextFormatting.ITALIC + I18n.format("automagy.tip.recipe.incomplete"));
                    }
                }
            }
        }

        tooltip.addAll(examinedStackResults);

    }

    @Nullable
    public static InventoryForRecipe getRecipeInventory(@Nonnull ItemStack stack, World world) {
        return stackIsRecipe(stack) ? new InventoryForRecipe(stack, "Recipe Inventory", 9, world) : null;
    }

    public static boolean stackIsRecipe(@Nonnull ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemRecipe;
    }

    public static boolean isItemPopulatedRecipe(@Nonnull ItemStack stack, World world) {
        if (!stack.isEmpty()) {
            InventoryForRecipe inv = getRecipeInventory(stack, world);
            if (inv != null) {
                return inv.getCraftingResult() != null;
            }
        }

        return false;
    }
}
