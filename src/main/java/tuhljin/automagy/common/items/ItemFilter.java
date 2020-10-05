package tuhljin.automagy.common.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.lib.inventory.InventoryWithFilterOptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFilter extends ModItem {
    public static final int NUM_SLOTS = 9;
    public static final int STACK_LIMIT = 999;
    protected static ItemStack examinedStack = ItemStack.EMPTY;
    protected static ArrayList<String> examinedStackResults = new ArrayList<>();

    public ItemFilter(String registryName) {
        //super(ImmutableMap.of(0, "white", 1, "black"));
        super(registryName);
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, ITooltipFlag flag) {
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
                        examinedStackResults.add("" + TextFormatting.DARK_GRAY + TextFormatting.ITALIC + I18n.format("automagy.gui.filter.ignoreMetadata"));
                    }

                    if (inv.ignoreNBT) {
                        examinedStackResults.add("" + TextFormatting.DARK_GRAY + TextFormatting.ITALIC + I18n.format("automagy.gui.filter.ignoreNBT"));
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
    public static InventoryWithFilterOptions getFilterInventory(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof ItemFilter ? new InventoryWithFilterOptions(stack, "Filter Inventory", NUM_SLOTS, STACK_LIMIT) : InventoryWithFilterOptions.NULL_FILTER;
    }

    public static boolean isItemPopulatedFilter(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()){
            InventoryWithFilterOptions inv = getFilterInventory(stack);
            if (!inv.isNull()) {
                return !inv.getNameFilter().isEmpty() || !inv.isEmpty();
            }
        }

        return false;
    }
}
