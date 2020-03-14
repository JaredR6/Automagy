package tuhljin.automagy.common.items;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.gui.AutomagyGUIHandler;
import tuhljin.automagy.common.lib.inventory.InventoryWithFilterOptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFilter extends ModVariantItem {
    public static final int NUM_SLOTS = 9;
    public static final int STACK_LIMIT = 999;
    protected static ItemStack examinedStack = ItemStack.EMPTY;
    protected static ArrayList<String> examinedStackResults = null;

    public ItemFilter() {
        super(ImmutableMap.of(0, "white", 1, "black"));
        this.setMaxStackSize(1);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (!world.isRemote) {
            if (stackIsWhitelist(player.getHeldItem(hand))) {
                AutomagyGUIHandler.openGUI(2, player, world, player.getPosition());
            } else if (stackIsBlacklist(player.getHeldItem(hand))) {
                AutomagyGUIHandler.openGUI(3, player, world, player.getPosition());
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        if (!ItemStack.areItemStacksEqual(stack, examinedStack)) {
            examinedStack = stack;
            examinedStackResults = new ArrayList<>();
            InventoryWithFilterOptions inv = getFilterInventory(stack);
            if (!inv.isNull()) {
                int count = 0;

                for(int i = 0; i < NUM_SLOTS; i++) {
                    ItemStack slotStack = inv.getStackInSlot(i);
                    if (!slotStack.isEmpty()) {
                        String s = slotStack.getDisplayName();
                        if (inv.useItemCount) {
                            s = "" + TextFormatting.DARK_AQUA + slotStack.getCount() + " " + TextFormatting.GRAY + s;
                        }

                        examinedStackResults.add(s);
                        count++;
                    }
                }

                if (count > 0) {
                    if (inv.ignoreMetadata) {
                        examinedStackResults.add("" + TextFormatting.DARK_GRAY + TextFormatting.ITALIC + I18n.format("Automagy.gui.filter.ignoreMetadata"));
                    }

                    if (inv.ignoreNBT) {
                        examinedStackResults.add("" + TextFormatting.DARK_GRAY + TextFormatting.ITALIC + I18n.format("Automagy.gui.filter.ignoreNBT"));
                    }
                }

                String nameFilter = inv.getNameFilter();
                if (!nameFilter.isEmpty()) {
                    examinedStackResults.add(TextFormatting.AQUA + "\"" + nameFilter + "\"");
                }
            }
        }

        tooltip.addAll(examinedStackResults);
    }

    @Nonnull
    public static InventoryWithFilterOptions getFilterInventory(ItemStack stack) {
        return stackIsFilter(stack) ? new InventoryWithFilterOptions(stack, "Filter Inventory", NUM_SLOTS, STACK_LIMIT) : InventoryWithFilterOptions.NULL_FILTER;
    }

    public static boolean stackIsFilter(ItemStack stack) {
        return stack.getItem() instanceof ItemFilter;
    }

    public static boolean stackIsWhitelist(ItemStack stack) {
        return stack.getItem() instanceof ItemFilter && stack.getItemDamage() == 0;
    }

    public static boolean stackIsBlacklist(ItemStack stack) {
        return stack.getItem() instanceof ItemFilter && stack.getItemDamage() == 1;
    }

    public static boolean isItemPopulatedFilter(ItemStack stack) {
        if (!stack.isEmpty()){
            InventoryWithFilterOptions inv = getFilterInventory(stack);
            if (!inv.isNull()) {
                return !inv.getNameFilter().isEmpty() || !inv.isEmpty();
            }
        }

        return false;
    }
}
